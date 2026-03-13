package com.example.btlmobile.model;

import java.util.UUID;

public class Invoice {
    private String id;
    private String tenantId;
    private String roomId;
    private double roomFee;
    private double serviceFee;
    private double electricFee;
    private double waterFee;
    private double totalAmount;
    private String dueDate;
    private String paidDate;
    private InvoiceStatus status;

    public Invoice(String tenantId, String roomId, double roomFee, double serviceFee,
                  double electricFee, double waterFee, String dueDate) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.roomId = roomId;
        this.roomFee = roomFee;
        this.serviceFee = serviceFee;
        this.electricFee = electricFee;
        this.waterFee = waterFee;
        this.totalAmount = roomFee + serviceFee + electricFee + waterFee;
        this.dueDate = dueDate;
        this.paidDate = null;
        this.status = InvoiceStatus.PENDING;
    }

    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public double getRoomFee() { return roomFee; }
    public void setRoomFee(double roomFee) { this.roomFee = roomFee; }
    public double getServiceFee() { return serviceFee; }
    public void setServiceFee(double serviceFee) { this.serviceFee = serviceFee; }
    public double getElectricFee() { return electricFee; }
    public void setElectricFee(double electricFee) { this.electricFee = electricFee; }
    public double getWaterFee() { return waterFee; }
    public void setWaterFee(double waterFee) { this.waterFee = waterFee; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getPaidDate() { return paidDate; }
    public void setPaidDate(String paidDate) { this.paidDate = paidDate; }
    public InvoiceStatus getStatus() { return status; }
    public void setStatus(InvoiceStatus status) { this.status = status; }
}
