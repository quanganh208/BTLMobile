package com.example.btlmobile.repository;

import com.example.btlmobile.model.Tenant;
import java.util.ArrayList;
import java.util.List;

public class TenantRepository {
    private static TenantRepository instance;
    private List<Tenant> tenants;

    private TenantRepository() {
        tenants = new ArrayList<>();
    }

    public static synchronized TenantRepository getInstance() {
        if (instance == null) {
            instance = new TenantRepository();
        }
        return instance;
    }

    public List<Tenant> getAll() { return new ArrayList<>(tenants); }
    public Tenant getById(String id) {
        for (Tenant tenant : tenants) {
            if (tenant.getId().equals(id)) return tenant;
        }
        return null;
    }

    public void add(Tenant tenant) { tenants.add(tenant); }
    public void update(Tenant tenant) {
        for (int i = 0; i < tenants.size(); i++) {
            if (tenants.get(i).getId().equals(tenant.getId())) {
                tenants.set(i, tenant);
                return;
            }
        }
    }

    public void delete(String id) {
        tenants.removeIf(tenant -> tenant.getId().equals(id));
    }

    public List<Tenant> getByRoomId(String roomId) {
        List<Tenant> result = new ArrayList<>();
        for (Tenant tenant : tenants) {
            if (tenant.getRoomId().equals(roomId)) result.add(tenant);
        }
        return result;
    }

    public void clear() { tenants.clear(); }
}
