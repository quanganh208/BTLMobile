package com.example.btlmobile.controller.tenant;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlmobile.R;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.RoomStatus;
import com.example.btlmobile.model.Tenant;
import com.example.btlmobile.repository.RoomRepository;
import com.example.btlmobile.repository.TenantRepository;

import java.util.ArrayList;
import java.util.List;

/** Form activity for adding a new tenant or editing an existing one. */
public class AddEditTenantActivity extends AppCompatActivity {

    private EditText editName, editPhone, editEmail, editIdCard, editMoveInDate;
    private Spinner spinnerRoom;
    private TenantRepository tenantRepository;
    private RoomRepository roomRepository;
    private String tenantId;
    private boolean isEditMode = false;

    // Tracks the selectable rooms shown in spinner
    private List<Room> availableRooms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_form);

        tenantRepository = TenantRepository.getInstance();
        roomRepository = RoomRepository.getInstance();
        tenantId = getIntent().getStringExtra("tenant_id");
        isEditMode = (tenantId != null);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isEditMode ? R.string.title_edit_tenant : R.string.title_add_tenant);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editIdCard = findViewById(R.id.editIdCard);
        editMoveInDate = findViewById(R.id.editMoveInDate);
        spinnerRoom = findViewById(R.id.spinnerRoom);
        Button btnSave = findViewById(R.id.btnSave);

        populateRoomSpinner();

        if (isEditMode) {
            fillFormFromTenant();
        }

        btnSave.setOnClickListener(v -> saveTenant());
    }

    /** Builds the room spinner with AVAILABLE rooms (+ current room when editing). */
    private void populateRoomSpinner() {
        availableRooms.clear();

        // Include current tenant's room even if OCCUPIED
        String currentRoomId = null;
        if (isEditMode) {
            Tenant tenant = tenantRepository.getById(tenantId);
            if (tenant != null) currentRoomId = tenant.getRoomId();
        }

        for (Room room : roomRepository.getAll()) {
            if (room.getStatus() == RoomStatus.AVAILABLE) {
                availableRooms.add(room);
            } else if (isEditMode && room.getId().equals(currentRoomId)) {
                availableRooms.add(0, room); // prepend current room at top
            }
        }

        List<String> labels = new ArrayList<>();
        for (Room room : availableRooms) {
            labels.add("Phòng " + room.getRoomNumber() + " - Tầng " + room.getFloor());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(adapter);
    }

    private void fillFormFromTenant() {
        Tenant tenant = tenantRepository.getById(tenantId);
        if (tenant == null) { finish(); return; }

        editName.setText(tenant.getName());
        editPhone.setText(tenant.getPhone());
        editEmail.setText(tenant.getEmail());
        editIdCard.setText(tenant.getIdCard());
        editMoveInDate.setText(tenant.getMoveInDate());

        // Select current room in spinner
        for (int i = 0; i < availableRooms.size(); i++) {
            if (availableRooms.get(i).getId().equals(tenant.getRoomId())) {
                spinnerRoom.setSelection(i);
                break;
            }
        }
    }

    private void saveTenant() {
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String idCard = editIdCard.getText().toString().trim();
        String moveInDate = editMoveInDate.getText().toString().trim();

        if (name.isEmpty()) { editName.setError("Vui lòng nhập họ tên"); return; }
        if (phone.isEmpty()) { editPhone.setError("Vui lòng nhập số điện thoại"); return; }
        if (availableRooms.isEmpty()) { return; } // no rooms available

        Room selectedRoom = availableRooms.get(spinnerRoom.getSelectedItemPosition());

        if (isEditMode) {
            Tenant tenant = tenantRepository.getById(tenantId);
            if (tenant == null) return;

            String oldRoomId = tenant.getRoomId();
            String newRoomId = selectedRoom.getId();

            // If room changed: free old room, occupy new room
            if (!oldRoomId.equals(newRoomId)) {
                Room oldRoom = roomRepository.getById(oldRoomId);
                if (oldRoom != null) {
                    oldRoom.setStatus(RoomStatus.AVAILABLE);
                    roomRepository.update(oldRoom);
                }
                selectedRoom.setStatus(RoomStatus.OCCUPIED);
                roomRepository.update(selectedRoom);
            }

            tenant.setName(name);
            tenant.setPhone(phone);
            tenant.setEmail(email);
            tenant.setIdCard(idCard);
            tenant.setRoomId(newRoomId);
            tenant.setMoveInDate(moveInDate);
            tenantRepository.update(tenant);

        } else {
            Tenant tenant = new Tenant(name, phone, email, idCard, selectedRoom.getId(), moveInDate);
            tenantRepository.add(tenant);
            // Mark room as occupied
            selectedRoom.setStatus(RoomStatus.OCCUPIED);
            roomRepository.update(selectedRoom);
        }

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
