package com.example.btlmobile.repository;

import com.example.btlmobile.model.Invoice;
import com.example.btlmobile.model.InvoiceStatus;
import java.util.ArrayList;
import java.util.List;

public class InvoiceRepository {
    private static InvoiceRepository instance;
    private List<Invoice> invoices;

    private InvoiceRepository() {
        invoices = new ArrayList<>();
    }

    public static synchronized InvoiceRepository getInstance() {
        if (instance == null) {
            instance = new InvoiceRepository();
        }
        return instance;
    }

    public List<Invoice> getAll() { return new ArrayList<>(invoices); }
    public Invoice getById(String id) {
        for (Invoice invoice : invoices) {
            if (invoice.getId().equals(id)) return invoice;
        }
        return null;
    }

    public void add(Invoice invoice) { invoices.add(invoice); }
    public void update(Invoice invoice) {
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getId().equals(invoice.getId())) {
                invoices.set(i, invoice);
                return;
            }
        }
    }

    public void delete(String id) {
        invoices.removeIf(invoice -> invoice.getId().equals(id));
    }

    public List<Invoice> getByTenantId(String tenantId) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getTenantId().equals(tenantId)) result.add(invoice);
        }
        return result;
    }

    public List<Invoice> getByStatus(InvoiceStatus status) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getStatus() == status) result.add(invoice);
        }
        return result;
    }

    public void clear() { invoices.clear(); }
}
