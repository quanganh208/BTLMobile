package com.example.btlmobile.repository;

import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.RoomStatus;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    private static RoomRepository instance;
    private List<Room> rooms;

    private RoomRepository() {
        rooms = new ArrayList<>();
    }

    public static synchronized RoomRepository getInstance() {
        if (instance == null) {
            instance = new RoomRepository();
        }
        return instance;
    }

    public List<Room> getAll() { return new ArrayList<>(rooms); }
    public Room getById(String id) {
        for (Room room : rooms) {
            if (room.getId().equals(id)) return room;
        }
        return null;
    }

    public void add(Room room) { rooms.add(room); }
    public void update(Room room) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId().equals(room.getId())) {
                rooms.set(i, room);
                return;
            }
        }
    }

    public void delete(String id) {
        rooms.removeIf(room -> room.getId().equals(id));
    }

    public List<Room> getByStatus(RoomStatus status) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getStatus() == status) result.add(room);
        }
        return result;
    }

    public void clear() { rooms.clear(); }
}
