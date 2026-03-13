package com.example.btlmobile.controller.room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btlmobile.R;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.Tenant;
import com.example.btlmobile.repository.RoomRepository;
import com.example.btlmobile.repository.TenantRepository;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/** Shows full details of a single room and its current tenant. */
public class RoomDetailActivity extends AppCompatActivity {

    private String roomId;
    private RoomRepository roomRepository;
    private TenantRepository tenantRepository;
    private final NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        roomRepository = RoomRepository.getInstance();
        tenantRepository = TenantRepository.getInstance();
        roomId = getIntent().getStringExtra("room_id");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_room_detail);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button btnEdit = findViewById(R.id.btnEditRoom);
        Button btnDelete = findViewById(R.id.btnDeleteRoom);

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditRoomActivity.class);
            intent.putExtra("room_id", roomId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> showDeleteDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        Room room = roomRepository.getById(roomId);
        if (room == null) {
            finish();
            return;
        }

        TextView tvRoomNumber = findViewById(R.id.tvDetailRoomNumber);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);
        TextView tvFloor = findViewById(R.id.tvDetailFloor);
        TextView tvArea = findViewById(R.id.tvDetailArea);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvDescription = findViewById(R.id.tvDetailDescription);

        tvRoomNumber.setText("Phòng " + room.getRoomNumber());
        tvFloor.setText(room.getFloor());
        tvArea.setText(room.getArea() + " m²");
        tvPrice.setText(currencyFormat.format(room.getPrice()) + " đ/tháng");
        tvDescription.setText(room.getDescription());

        switch (room.getStatus()) {
            case AVAILABLE:
                tvStatus.setText("Còn trống");
                tvStatus.setTextColor(getResources().getColor(R.color.status_available, null));
                break;
            case OCCUPIED:
                tvStatus.setText("Đã thuê");
                tvStatus.setTextColor(getResources().getColor(R.color.status_occupied, null));
                break;
            default:
                tvStatus.setText("Bảo trì");
                tvStatus.setTextColor(getResources().getColor(R.color.status_maintenance, null));
        }

        loadTenantData(room.getId());
    }

    private void loadTenantData(String rId) {
        TextView tvNoTenant = findViewById(R.id.tvNoTenant);
        TextView tvName = findViewById(R.id.tvDetailTenantName);
        TextView tvPhone = findViewById(R.id.tvDetailTenantPhone);
        TextView tvEmail = findViewById(R.id.tvDetailTenantEmail);
        TextView tvIdCard = findViewById(R.id.tvDetailTenantIdCard);
        TextView tvMoveIn = findViewById(R.id.tvDetailMoveInDate);

        List<Tenant> tenants = tenantRepository.getByRoomId(rId);
        if (tenants == null || tenants.isEmpty()) {
            tvNoTenant.setVisibility(View.VISIBLE);
            tvName.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
            tvEmail.setVisibility(View.GONE);
            tvIdCard.setVisibility(View.GONE);
            tvMoveIn.setVisibility(View.GONE);
        } else {
            Tenant tenant = tenants.get(0);
            tvNoTenant.setVisibility(View.GONE);
            tvName.setVisibility(View.VISIBLE);
            tvPhone.setVisibility(View.VISIBLE);
            tvEmail.setVisibility(View.VISIBLE);
            tvIdCard.setVisibility(View.VISIBLE);
            tvMoveIn.setVisibility(View.VISIBLE);

            tvName.setText(tenant.getName());
            tvPhone.setText(tenant.getPhone());
            tvEmail.setText(tenant.getEmail());
            tvIdCard.setText("CMND/CCCD: " + tenant.getIdCard());
            tvMoveIn.setText("Ngày vào: " + tenant.getMoveInDate());
        }
    }

    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.delete_room_confirm)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    roomRepository.delete(roomId);
                    finish();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
