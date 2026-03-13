package com.example.btlmobile.controller.room;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btlmobile.R;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.RoomStatus;
import com.example.btlmobile.repository.RoomRepository;

/** Form activity for adding a new room or editing an existing one. */
public class AddEditRoomActivity extends AppCompatActivity {

    private EditText editRoomNumber, editFloor, editPrice, editArea, editDescription;
    private Spinner spinnerStatus;
    private RoomRepository roomRepository;
    private String roomId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_form);

        roomRepository = RoomRepository.getInstance();
        roomId = getIntent().getStringExtra("room_id");
        isEditMode = (roomId != null);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(isEditMode ? R.string.title_edit_room : R.string.title_add_room);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editRoomNumber = findViewById(R.id.editRoomNumber);
        editFloor = findViewById(R.id.editFloor);
        editPrice = findViewById(R.id.editPrice);
        editArea = findViewById(R.id.editArea);
        editDescription = findViewById(R.id.editDescription);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        Button btnSave = findViewById(R.id.btnSave);

        // Populate spinner with RoomStatus values
        String[] statusLabels = {"Còn trống", "Đã thuê", "Bảo trì"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, statusLabels);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        if (isEditMode) {
            fillFormFromRoom();
        }

        btnSave.setOnClickListener(v -> saveRoom());
    }

    private void fillFormFromRoom() {
        Room room = roomRepository.getById(roomId);
        if (room == null) {
            finish();
            return;
        }
        editRoomNumber.setText(room.getRoomNumber());
        editFloor.setText(room.getFloor());
        editPrice.setText(String.valueOf(room.getPrice()));
        editArea.setText(String.valueOf(room.getArea()));
        editDescription.setText(room.getDescription());

        // Map RoomStatus enum to spinner position
        switch (room.getStatus()) {
            case AVAILABLE: spinnerStatus.setSelection(0); break;
            case OCCUPIED:  spinnerStatus.setSelection(1); break;
            default:        spinnerStatus.setSelection(2); break;
        }
    }

    private void saveRoom() {
        String roomNumber = editRoomNumber.getText().toString().trim();
        String floor = editFloor.getText().toString().trim();
        String priceStr = editPrice.getText().toString().trim();
        String areaStr = editArea.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (roomNumber.isEmpty()) {
            editRoomNumber.setError("Vui lòng nhập số phòng");
            return;
        }
        if (floor.isEmpty()) {
            editFloor.setError("Vui lòng nhập tầng");
            return;
        }

        double price = 0, area = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            editPrice.setError("Giá không hợp lệ");
            return;
        }
        try {
            area = Double.parseDouble(areaStr);
        } catch (NumberFormatException e) {
            editArea.setError("Diện tích không hợp lệ");
            return;
        }

        if (price <= 0) { editPrice.setError("Giá phải lớn hơn 0"); return; }
        if (area <= 0)  { editArea.setError("Diện tích phải lớn hơn 0"); return; }

        RoomStatus[] statuses = {RoomStatus.AVAILABLE, RoomStatus.OCCUPIED, RoomStatus.MAINTENANCE};
        RoomStatus selectedStatus = statuses[spinnerStatus.getSelectedItemPosition()];

        if (isEditMode) {
            Room room = roomRepository.getById(roomId);
            if (room != null) {
                room.setRoomNumber(roomNumber);
                room.setFloor(floor);
                room.setPrice(price);
                room.setArea(area);
                room.setDescription(description);
                room.setStatus(selectedStatus);
                roomRepository.update(room);
            }
        } else {
            Room room = new Room(roomNumber, floor, price, area, description);
            room.setStatus(selectedStatus);
            roomRepository.add(room);
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
