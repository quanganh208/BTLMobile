package com.example.btlmobile.repository;

import com.example.btlmobile.model.Service;
import java.util.ArrayList;
import java.util.List;

public class ServiceRepository {
    private static ServiceRepository instance;
    private List<Service> services;

    private ServiceRepository() {
        services = new ArrayList<>();
    }

    public static synchronized ServiceRepository getInstance() {
        if (instance == null) {
            instance = new ServiceRepository();
        }
        return instance;
    }

    public List<Service> getAll() { return new ArrayList<>(services); }
    public Service getById(String id) {
        for (Service service : services) {
            if (service.getId().equals(id)) return service;
        }
        return null;
    }

    public void add(Service service) { services.add(service); }
    public void update(Service service) {
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getId().equals(service.getId())) {
                services.set(i, service);
                return;
            }
        }
    }

    public void delete(String id) {
        services.removeIf(service -> service.getId().equals(id));
    }

    public void clear() { services.clear(); }
}
