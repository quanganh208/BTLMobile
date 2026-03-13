package com.example.btlmobile.util;

import com.example.btlmobile.model.*;
import com.example.btlmobile.repository.*;

public class SampleData {
    public static void initData() {
        RoomRepository roomRepo = RoomRepository.getInstance();
        TenantRepository tenantRepo = TenantRepository.getInstance();
        InvoiceRepository invoiceRepo = InvoiceRepository.getInstance();
        ServiceRepository serviceRepo = ServiceRepository.getInstance();

        // Create rooms
        Room room1 = new Room("101", "1", 3000000, 25, "Phòng có cửa sổ, WC riêng");
        Room room2 = new Room("102", "1", 3500000, 30, "Phòng rộng, có ban công");
        Room room3 = new Room("103", "1", 2800000, 20, "Phòng tiết kiệm");
        Room room4 = new Room("201", "2", 4000000, 35, "Phòng VIP, đầy đủ tiện nghi");
        Room room5 = new Room("202", "2", 3200000, 28, "Phòng tiêu chuẩn");

        room1.setStatus(RoomStatus.OCCUPIED);
        room4.setStatus(RoomStatus.OCCUPIED);
        room5.setStatus(RoomStatus.MAINTENANCE);

        roomRepo.add(room1);
        roomRepo.add(room2);
        roomRepo.add(room3);
        roomRepo.add(room4);
        roomRepo.add(room5);

        // Create tenants
        Tenant tenant1 = new Tenant("Nguyen Van A", "0912345678", "vana@email.com", "012345678901", room1.getId(), "2024-01-15");
        Tenant tenant2 = new Tenant("Tran Thi B", "0987654321", "thib@email.com", "012345678902", room4.getId(), "2024-02-01");

        tenantRepo.add(tenant1);
        tenantRepo.add(tenant2);

        // Create services
        Service internet = new Service("Internet", "tháng", 150000, "Wifi tốc độ cao");
        Service cleaning = new Service("Ve sinh", "tháng", 100000, "Dọn dẹp hàng tuần");
        Service parking = new Service("Gui xe", "tháng", 50000, "Bãi xe có bảo vệ");
        Service laundry = new Service("Giat la", "kg", 30000, "Giặt ủi");

        serviceRepo.add(internet);
        serviceRepo.add(cleaning);
        serviceRepo.add(parking);
        serviceRepo.add(laundry);

        // Create invoices
        Invoice invoice1 = new Invoice(tenant1.getId(), room1.getId(), 3000000, 150000, 200000, 150000, "2024-03-05");
        invoice1.setStatus(InvoiceStatus.PAID);
        invoice1.setPaidDate("2024-03-03");

        Invoice invoice2 = new Invoice(tenant1.getId(), room1.getId(), 3000000, 150000, 250000, 180000, "2024-04-05");
        invoice2.setStatus(InvoiceStatus.PENDING);

        Invoice invoice3 = new Invoice(tenant2.getId(), room4.getId(), 4000000, 150000, 180000, 120000, "2024-04-05");
        invoice3.setStatus(InvoiceStatus.PENDING);

        invoiceRepo.add(invoice1);
        invoiceRepo.add(invoice2);
        invoiceRepo.add(invoice3);
    }
}
