package com.example.btlmobile.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.btlmobile.R;
import com.example.btlmobile.controller.invoice.InvoiceListActivity;
import com.example.btlmobile.controller.room.RoomListActivity;
import com.example.btlmobile.controller.service.ServiceListActivity;
import com.example.btlmobile.controller.tenant.TenantListActivity;
import com.example.btlmobile.model.Invoice;
import com.example.btlmobile.model.InvoiceStatus;
import com.example.btlmobile.repository.InvoiceRepository;
import com.example.btlmobile.repository.RoomRepository;
import com.example.btlmobile.repository.ServiceRepository;
import com.example.btlmobile.repository.TenantRepository;
import com.example.btlmobile.util.SampleData;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/** Main dashboard screen. Shows summary stats and quick-action cards. */
public class DashboardActivity extends AppCompatActivity {

    private static boolean sDataInitialized = false;

    private TextView tvTotalRooms, tvTotalTenants, tvTotalInvoices, tvRevenue;

    private RoomRepository roomRepository;
    private TenantRepository tenantRepository;
    private InvoiceRepository invoiceRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        roomRepository = RoomRepository.getInstance();
        tenantRepository = TenantRepository.getInstance();
        invoiceRepository = InvoiceRepository.getInstance();
        // ServiceRepository kept alive via SampleData init
        ServiceRepository.getInstance();

        // Init sample data once per process lifetime
        if (!sDataInitialized) {
            SampleData.initData();
            sDataInitialized = true;
        }

        bindStatViews();
        bindCardActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshStats();
    }

    private void bindStatViews() {
        tvTotalRooms = findViewById(R.id.tvTotalRooms);
        tvTotalTenants = findViewById(R.id.tvTotalTenants);
        tvTotalInvoices = findViewById(R.id.tvTotalInvoices);
        tvRevenue = findViewById(R.id.tvRevenue);
    }

    private void refreshStats() {
        int totalRooms = roomRepository.getAll().size();
        int totalTenants = tenantRepository.getAll().size();
        int totalInvoices = invoiceRepository.getAll().size();

        double revenue = 0;
        List<Invoice> paidInvoices = invoiceRepository.getByStatus(InvoiceStatus.PAID);
        for (Invoice inv : paidInvoices) {
            revenue += inv.getTotalAmount();
        }

        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

        tvTotalRooms.setText(String.valueOf(totalRooms));
        tvTotalTenants.setText(String.valueOf(totalTenants));
        tvTotalInvoices.setText(String.valueOf(totalInvoices));
        tvRevenue.setText(nf.format(revenue) + " đ");
    }

    private void bindCardActions() {
        CardView cardRooms = findViewById(R.id.cardRooms);
        CardView cardTenants = findViewById(R.id.cardTenants);
        CardView cardInvoices = findViewById(R.id.cardInvoices);
        CardView cardServices = findViewById(R.id.cardServices);
        CardView cardSearch = findViewById(R.id.cardSearch);
        CardView cardStatistics = findViewById(R.id.cardStatistics);

        cardRooms.setOnClickListener(v ->
                startActivity(new Intent(this, RoomListActivity.class)));
        cardTenants.setOnClickListener(v ->
                startActivity(new Intent(this, TenantListActivity.class)));
        cardInvoices.setOnClickListener(v ->
                startActivity(new Intent(this, InvoiceListActivity.class)));
        cardServices.setOnClickListener(v ->
                startActivity(new Intent(this, ServiceListActivity.class)));
        cardSearch.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));
        cardStatistics.setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));
    }
}
