package com.example.btlmobile.controller;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btlmobile.R;
import com.example.btlmobile.model.Invoice;
import com.example.btlmobile.model.InvoiceStatus;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.RoomStatus;
import com.example.btlmobile.repository.InvoiceRepository;
import com.example.btlmobile.repository.RoomRepository;
import com.example.btlmobile.repository.TenantRepository;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/** Displays occupancy and financial statistics. */
public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_statistics));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadStatistics();
    }

    private void loadStatistics() {
        RoomRepository roomRepo = RoomRepository.getInstance();
        TenantRepository tenantRepo = TenantRepository.getInstance();
        InvoiceRepository invoiceRepo = InvoiceRepository.getInstance();

        List<Room> allRooms = roomRepo.getAll();
        int totalRooms = allRooms.size();
        int available = 0, occupied = 0, maintenance = 0;
        for (Room room : allRooms) {
            if (room.getStatus() == RoomStatus.AVAILABLE) available++;
            else if (room.getStatus() == RoomStatus.OCCUPIED) occupied++;
            else if (room.getStatus() == RoomStatus.MAINTENANCE) maintenance++;
        }

        double occupancyRate = totalRooms > 0 ? (occupied * 100.0) / totalRooms : 0.0;

        double totalRevenue = 0;
        for (Invoice inv : invoiceRepo.getByStatus(InvoiceStatus.PAID)) {
            totalRevenue += inv.getTotalAmount();
        }
        double totalUnpaid = 0;
        for (Invoice inv : invoiceRepo.getByStatus(InvoiceStatus.PENDING)) {
            totalUnpaid += inv.getTotalAmount();
        }

        int totalTenants = tenantRepo.getAll().size();

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        setText(R.id.tvStatTotalRooms, String.valueOf(totalRooms));
        setText(R.id.tvStatAvailable, String.valueOf(available));
        setText(R.id.tvStatOccupied, String.valueOf(occupied));
        setText(R.id.tvStatMaintenance, String.valueOf(maintenance));
        setText(R.id.tvStatOccupancyRate, String.format(Locale.US, "%.1f%%", occupancyRate));
        setText(R.id.tvStatTotalRevenue, nf.format(totalRevenue) + " đ");
        setText(R.id.tvStatUnpaid, nf.format(totalUnpaid) + " đ");
        setText(R.id.tvStatTotalTenants, String.valueOf(totalTenants));
    }

    private void setText(int viewId, String text) {
        TextView tv = findViewById(viewId);
        if (tv != null) tv.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
