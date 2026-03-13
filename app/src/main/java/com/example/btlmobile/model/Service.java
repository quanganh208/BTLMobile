package com.example.btlmobile.model;

import java.util.UUID;

public class Service {
    private String id;
    private String name;
    private String unit;
    private double pricePerUnit;
    private String description;

    public Service(String name, String unit, double pricePerUnit, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public double getPricePerUnit() { return pricePerUnit; }
    public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
