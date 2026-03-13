package com.example.btlmobile.controller.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlmobile.R;
import com.example.btlmobile.adapter.ServiceAdapter;
import com.example.btlmobile.model.Service;
import com.example.btlmobile.repository.ServiceRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/** Displays all services with add/edit/delete actions. */
public class ServiceListActivity extends AppCompatActivity {

    private ServiceRepository serviceRepository;
    private ServiceAdapter adapter;
    private List<Service> serviceList;

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            refreshList();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.title_services));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        serviceRepository = ServiceRepository.getInstance();
        serviceList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ServiceAdapter(new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Service service) {
                Intent intent = new Intent(ServiceListActivity.this, AddEditServiceActivity.class);
                intent.putExtra("service_id", service.getId());
                launcher.launch(intent);
            }

            @Override
            public void onItemLongClick(Service service) {
                showDeleteDialog(service);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddService);
        fab.setOnClickListener(v -> launcher.launch(
                new Intent(ServiceListActivity.this, AddEditServiceActivity.class)));

        refreshList();
    }

    private void refreshList() {
        serviceList.clear();
        serviceList.addAll(serviceRepository.getAll());
        adapter.updateData(new ArrayList<>(serviceList));
    }

    private void showDeleteDialog(Service service) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.delete_service_confirm))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    serviceRepository.delete(service.getId());
                    refreshList();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
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
