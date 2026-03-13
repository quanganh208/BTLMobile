package com.example.btlmobile.model;

import java.util.UUID;

public class Tenant {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String idCard;
    private String roomId;
    private String moveInDate;

    public Tenant(String name, String phone, String email, String idCard, String roomId, String moveInDate) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.idCard = idCard;
        this.roomId = roomId;
        this.moveInDate = moveInDate;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getMoveInDate() { return moveInDate; }
    public void setMoveInDate(String moveInDate) { this.moveInDate = moveInDate; }
}
