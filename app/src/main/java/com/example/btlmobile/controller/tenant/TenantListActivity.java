package com.example.btlmobile.controller.tenant;

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
import com.example.btlmobile.adapter.TenantAdapter;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.RoomStatus;
import com.example.btlmobile.model.Tenant;
import com.example.btlmobile.repository.RoomRepository;
import com.example.btlmobile.repository.TenantRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/** Displays the full list of tenants with add/edit/delete actions. */
public class TenantListActivity extends AppCompatActivity {

    private TenantAdapter adapter;
    private TenantRepository tenantRepository;
    private RoomRepository roomRepository;

    private final ActivityResultLauncher<Intent> addEditLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshList();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_list);

        tenantRepository = TenantRepository.getInstance();
        roomRepository = RoomRepository.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_tenants);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTenants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TenantAdapter(new TenantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tenant tenant) {
                // Open edit form on click
                Intent intent = new Intent(TenantListActivity.this, AddEditTenantActivity.class);
                intent.putExtra("tenant_id", tenant.getId());
                addEditLauncher.launch(intent);
            }

            @Override
            public void onItemLongClick(Tenant tenant) {
                showDeleteDialog(tenant);
            }
        });

        recyclerView.setAdapter(adapter);
        refreshList();

        FloatingActionButton fab = findViewById(R.id.fabAddTenant);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditTenantActivity.class);
            addEditLauncher.launch(intent);
        });
    }

    private void refreshList() {
        adapter.updateData(tenantRepository.getAll());
    }

    private void showDeleteDialog(Tenant tenant) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete)
                .setMessage(R.string.delete_tenant_confirm)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    // Free the room when tenant is deleted
                    Room room = roomRepository.getById(tenant.getRoomId());
                    if (room != null) {
                        room.setStatus(RoomStatus.AVAILABLE);
                        roomRepository.update(room);
                    }
                    tenantRepository.delete(tenant.getId());
                    refreshList();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
    protected void onResume() {
        super.onResume();
        refreshList();
    }
}
