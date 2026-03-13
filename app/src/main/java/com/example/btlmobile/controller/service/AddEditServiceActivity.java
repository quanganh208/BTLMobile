package com.example.btlmobile.controller.service;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btlmobile.R;
import com.example.btlmobile.model.Service;
import com.example.btlmobile.repository.ServiceRepository;

/** Add or edit a service. Receives optional "service_id" extra for edit mode. */
public class AddEditServiceActivity extends AppCompatActivity {

    private EditText editName, editUnit, editPricePerUnit, editDescription;
    private Button btnSave;

    private ServiceRepository serviceRepository;
    private Service existingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_form);

        serviceRepository = ServiceRepository.getInstance();

        editName = findViewById(R.id.editName);
        editUnit = findViewById(R.id.editUnit);
        editPricePerUnit = findViewById(R.id.editPricePerUnit);
        editDescription = findViewById(R.id.editDescription);
        btnSave = findViewById(R.id.btnSave);

        String serviceId = getIntent().getStringExtra("service_id");
        if (serviceId != null) {
            existingService = serviceRepository.getById(serviceId);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(getString(R.string.title_edit_service));
            fillFields();
        } else {
            if (getSupportActionBar() != null) getSupportActionBar().setTitle(getString(R.string.title_add_service));
        }

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSave.setOnClickListener(v -> saveService());
    }

    private void fillFields() {
        if (existingService == null) return;
        editName.setText(existingService.getName());
        editUnit.setText(existingService.getUnit());
        editPricePerUnit.setText(String.valueOf(existingService.getPricePerUnit()));
        editDescription.setText(existingService.getDescription());
    }

    private void saveService() {
        String name = editName.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();
        String priceStr = editPricePerUnit.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError(getString(R.string.name) + " " + "không được để trống");
            return;
        }
        if (unit.isEmpty()) {
            editUnit.setError(getString(R.string.unit) + " " + "không được để trống");
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            editPricePerUnit.setError("Giá phải là số hợp lệ");
            return;
        }
        if (price <= 0) {
            editPricePerUnit.setError("Giá phải lớn hơn 0");
            return;
        }

        if (existingService == null) {
            Service service = new Service(name, unit, price, description);
            serviceRepository.add(service);
        } else {
            existingService.setName(name);
            existingService.setUnit(unit);
            existingService.setPricePerUnit(price);
            existingService.setDescription(description);
            serviceRepository.update(existingService);
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
