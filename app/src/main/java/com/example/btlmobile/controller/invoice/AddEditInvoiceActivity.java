package com.example.btlmobile.controller.invoice;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btlmobile.R;
import com.example.btlmobile.model.Invoice;
import com.example.btlmobile.model.Tenant;
import com.example.btlmobile.repository.InvoiceRepository;
import com.example.btlmobile.repository.RoomRepository;
import com.example.btlmobile.repository.TenantRepository;
import java.util.ArrayList;
import java.util.List;

/** Add or edit an invoice. Receives optional "invoice_id" extra for edit mode. */
public class AddEditInvoiceActivity extends AppCompatActivity {

    private Spinner spinnerTenant;
    private EditText editRoomFee, editServiceFee, editElectricFee, editWaterFee, editDueDate;
    private Button btnSave;

    private TenantRepository tenantRepository;
    private InvoiceRepository invoiceRepository;
    private RoomRepository roomRepository;

    private List<Tenant> tenantList;
    private Invoice existingInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_form);

        tenantRepository = TenantRepository.getInstance();
        invoiceRepository = InvoiceRepository.getInstance();
        roomRepository = RoomRepository.getInstance();
        tenantList = tenantRepository.getAll();

        bindViews();
        setupSpinner();

        String invoiceId = getIntent().getStringExtra("invoice_id");
        if (invoiceId != null) {
            existingInvoice = invoiceRepository.getById(invoiceId);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(getString(R.string.title_edit_invoice));
            fillFields();
        } else {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(getString(R.string.title_add_invoice));
        }

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addFeeWatchers();
        btnSave.setOnClickListener(v -> saveInvoice());
    }

    private void bindViews() {
        spinnerTenant = findViewById(R.id.spinnerTenant);
        editRoomFee = findViewById(R.id.editRoomFee);
        editServiceFee = findViewById(R.id.editServiceFee);
        editElectricFee = findViewById(R.id.editElectricFee);
        editWaterFee = findViewById(R.id.editWaterFee);
        editDueDate = findViewById(R.id.editDueDate);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupSpinner() {
        List<String> names = new ArrayList<>();
        for (Tenant t : tenantList) {
            String label = t.getName();
            com.example.btlmobile.model.Room room = roomRepository.getById(t.getRoomId());
            if (room != null) label += " (P." + room.getRoomNumber() + ")";
            names.add(label);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTenant.setAdapter(adapter);
    }

    private void fillFields() {
        if (existingInvoice == null) return;
        for (int i = 0; i < tenantList.size(); i++) {
            if (tenantList.get(i).getId().equals(existingInvoice.getTenantId())) {
                spinnerTenant.setSelection(i);
                break;
            }
        }
        editRoomFee.setText(String.valueOf(existingInvoice.getRoomFee()));
        editServiceFee.setText(String.valueOf(existingInvoice.getServiceFee()));
        editElectricFee.setText(String.valueOf(existingInvoice.getElectricFee()));
        editWaterFee.setText(String.valueOf(existingInvoice.getWaterFee()));
        editDueDate.setText(existingInvoice.getDueDate());
    }

    private void addFeeWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { updateTotalInTitle(); }
        };
        editRoomFee.addTextChangedListener(watcher);
        editServiceFee.addTextChangedListener(watcher);
        editElectricFee.addTextChangedListener(watcher);
        editWaterFee.addTextChangedListener(watcher);
    }

    private void updateTotalInTitle() {
        double total = parseFee(editRoomFee) + parseFee(editServiceFee)
                + parseFee(editElectricFee) + parseFee(editWaterFee);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Tổng: " + (long) total + " đ");
        }
    }

    private double parseFee(EditText et) {
        try { return Double.parseDouble(et.getText().toString().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private void saveInvoice() {
        if (tenantList.isEmpty()) {
            Toast.makeText(this, "Không có khách thuê nào", Toast.LENGTH_SHORT).show();
            return;
        }
        String dueDate = editDueDate.getText().toString().trim();
        if (dueDate.isEmpty()) {
            editDueDate.setError("Vui lòng nhập ngày đến hạn");
            return;
        }
        int idx = spinnerTenant.getSelectedItemPosition();
        Tenant tenant = tenantList.get(idx);
        double roomFee = parseFee(editRoomFee);
        double serviceFee = parseFee(editServiceFee);
        double electricFee = parseFee(editElectricFee);
        double waterFee = parseFee(editWaterFee);
        double total = roomFee + serviceFee + electricFee + waterFee;

        if (existingInvoice == null) {
            Invoice invoice = new Invoice(tenant.getId(), tenant.getRoomId(),
                    roomFee, serviceFee, electricFee, waterFee, dueDate);
            invoice.setTotalAmount(total);
            invoiceRepository.add(invoice);
        } else {
            existingInvoice.setTenantId(tenant.getId());
            existingInvoice.setRoomId(tenant.getRoomId());
            existingInvoice.setRoomFee(roomFee);
            existingInvoice.setServiceFee(serviceFee);
            existingInvoice.setElectricFee(electricFee);
            existingInvoice.setWaterFee(waterFee);
            existingInvoice.setDueDate(dueDate);
            existingInvoice.setTotalAmount(total);
            invoiceRepository.update(existingInvoice);
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
