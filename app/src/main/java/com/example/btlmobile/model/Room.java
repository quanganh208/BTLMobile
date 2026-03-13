package com.example.btlmobile.model;

import java.util.UUID;

public class Room {
    private String id;
    private String roomNumber;
    private String floor;
    private double price;
    private double area;
    private RoomStatus status;
    private String description;

    public Room(String roomNumber, String floor, double price, double area, String description) {
        this.id = UUID.randomUUID().toString();
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.price = price;
        this.area = area;
        this.status = RoomStatus.AVAILABLE;
        this.description = description;
    }

    public String getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getFloor() { return floor; }
    public void setFloor(String floor) { this.floor = floor; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
