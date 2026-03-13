package com.example.btlmobile.controller.invoice;

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
import com.example.btlmobile.adapter.InvoiceAdapter;
import com.example.btlmobile.model.Invoice;
import com.example.btlmobile.repository.InvoiceRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

/** Displays the full list of invoices with add/edit/delete actions. */
public class InvoiceListActivity extends AppCompatActivity {

    private InvoiceRepository invoiceRepository;
    private InvoiceAdapter adapter;
    private List<Invoice> invoiceList;

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
        setContentView(R.layout.activity_invoice_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Danh sách hóa đơn");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        invoiceRepository = InvoiceRepository.getInstance();
        invoiceList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewInvoices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new InvoiceAdapter(invoiceList, new InvoiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Invoice invoice) {
                Intent intent = new Intent(InvoiceListActivity.this, AddEditInvoiceActivity.class);
                intent.putExtra("invoice_id", invoice.getId());
                launcher.launch(intent);
            }

            @Override
            public void onItemLongClick(Invoice invoice) {
                showDeleteDialog(invoice);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddInvoice);
        fab.setOnClickListener(v -> launcher.launch(
                new Intent(InvoiceListActivity.this, AddEditInvoiceActivity.class)));

        refreshList();
    }

    private void refreshList() {
        invoiceList.clear();
        invoiceList.addAll(invoiceRepository.getAll());
        adapter.updateData(new ArrayList<>(invoiceList));
    }

    private void showDeleteDialog(Invoice invoice) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.delete_invoice_confirm))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    invoiceRepository.delete(invoice.getId());
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
