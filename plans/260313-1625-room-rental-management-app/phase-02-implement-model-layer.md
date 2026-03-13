# Phase 02: Model Layer (POJOs, Enums, Repositories, SampleData)

## Overview
- **Priority:** P1
- **Status:** Pending
- **Effort:** 2h
- **Owner:** Member 1
- **Depends on:** Phase 1

Implement all data model classes, enum types, Singleton repositories with ArrayList storage, and sample data initialization.

## Key Insights
- All models implement `Serializable` for Intent passing between Activities
- Repositories use Singleton pattern — single source of truth shared across Activities
- `getAll()` returns defensive copy (`new ArrayList<>(list)`) to prevent external mutation
- Auto-increment IDs managed by repository, not by model
- Invoice `totalAmount` auto-calculated: `roomPrice + (newElectric - oldElectric) * electricPrice + (newWater - oldWater) * waterPrice`

## Requirements
- 4 POJO classes with getters/setters
- 2 enum types
- 4 Singleton repository classes with CRUD + search/filter
- 1 SampleData utility with static `init()` method

## Related Code Files

### Files to Create
- `app/src/main/java/com/example/btlmobile/model/enums/RoomStatus.java`
- `app/src/main/java/com/example/btlmobile/model/enums/InvoiceStatus.java`
- `app/src/main/java/com/example/btlmobile/model/Room.java`
- `app/src/main/java/com/example/btlmobile/model/Tenant.java`
- `app/src/main/java/com/example/btlmobile/model/Invoice.java`
- `app/src/main/java/com/example/btlmobile/model/Service.java`
- `app/src/main/java/com/example/btlmobile/model/repository/RoomRepository.java`
- `app/src/main/java/com/example/btlmobile/model/repository/TenantRepository.java`
- `app/src/main/java/com/example/btlmobile/model/repository/InvoiceRepository.java`
- `app/src/main/java/com/example/btlmobile/model/repository/ServiceRepository.java`
- `app/src/main/java/com/example/btlmobile/util/SampleData.java`

## Implementation Steps

### Step 1: Enums

**RoomStatus.java:**
```java
package com.example.btlmobile.model.enums;

public enum RoomStatus {
    AVAILABLE("Trống"),
    OCCUPIED("Đã thuê"),
    MAINTENANCE("Bảo trì");

    private final String displayName;
    RoomStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
```

**InvoiceStatus.java:**
```java
package com.example.btlmobile.model.enums;

public enum InvoiceStatus {
    UNPAID("Chưa thanh toán"),
    PAID("Đã thanh toán");

    private final String displayName;
    InvoiceStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
```

### Step 2: Room.java

Fields:
| Field | Type |
|-------|------|
| id | int |
| roomNumber | String |
| floor | int |
| area | double |
| price | double |
| status | RoomStatus |
| description | String |

- Implements `Serializable`
- Default constructor + parameterized constructor (without id)
- Getters and setters for all fields

### Step 3: Tenant.java

Fields:
| Field | Type |
|-------|------|
| id | int |
| name | String |
| phone | String |
| idCard | String |
| email | String |
| roomId | int |
| moveInDate | String |

- Implements `Serializable`
- Default constructor + parameterized constructor (without id)
- Getters and setters for all fields

### Step 4: Invoice.java

Fields:
| Field | Type |
|-------|------|
| id | int |
| roomId | int |
| month | String |
| roomPrice | double |
| oldElectric | int |
| newElectric | int |
| electricPrice | double |
| oldWater | int |
| newWater | int |
| waterPrice | double |
| totalAmount | double |
| status | InvoiceStatus |
| createdDate | String |

- Implements `Serializable`
- `calculateTotal()` method: `roomPrice + (newElectric - oldElectric) * electricPrice + (newWater - oldWater) * waterPrice`
- Call `calculateTotal()` from setter methods that affect total (or require explicit call before save)
- Getters and setters for all fields

### Step 5: Service.java

Fields:
| Field | Type |
|-------|------|
| id | int |
| name | String |
| price | double |
| unit | String |
| description | String |

- Implements `Serializable`
- Default constructor + parameterized constructor (without id)
- Getters and setters for all fields

### Step 6: RoomRepository.java

```
Pattern: Singleton + ArrayList<Room>
Fields: instance (static), rooms (List<Room>), nextId (int, starts at 1)
Methods:
  - getInstance(): RoomRepository (lazy init)
  - getAll(): List<Room> (defensive copy)
  - getById(int id): Room (or null)
  - add(Room room): void (sets id = nextId++)
  - update(Room room): void (find by id, replace fields)
  - delete(int id): void (remove by id)
  - searchByKeyword(String keyword): List<Room> (match roomNumber or description, case-insensitive)
  - filterByStatus(RoomStatus status): List<Room>
  - getCount(): int
  - getCountByStatus(RoomStatus status): int
```

### Step 7: TenantRepository.java

```
Same Singleton pattern.
Methods:
  - getInstance(), getAll(), getById(), add(), update(), delete()
  - getByRoomId(int roomId): Tenant (or null)
  - searchByKeyword(String keyword): List<Tenant> (match name, phone, or idCard)
  - getCount(): int
```

### Step 8: InvoiceRepository.java

```
Same Singleton pattern.
Methods:
  - getInstance(), getAll(), getById(), add(), update(), delete()
  - getByRoomId(int roomId): List<Invoice>
  - filterByStatus(InvoiceStatus status): List<Invoice>
  - getTotalRevenue(): double (sum totalAmount where status == PAID)
  - getTotalUnpaid(): double (sum totalAmount where status == UNPAID)
  - getCount(): int
```

### Step 9: ServiceRepository.java

```
Same Singleton pattern.
Methods:
  - getInstance(), getAll(), getById(), add(), update(), delete()
  - searchByKeyword(String keyword): List<Service> (match name)
  - getCount(): int
```

### Step 10: SampleData.java

```java
package com.example.btlmobile.util;

public class SampleData {
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;
        // Populate repositories with sample data
    }
}
```

Sample data contents:
- **Rooms (8):** P101-P104 (floor 1), P201-P204 (floor 2). Mix of AVAILABLE, OCCUPIED, MAINTENANCE. Prices 2,000,000-4,500,000 VND. Areas 18-30m².
- **Tenants (5):** Vietnamese names, phones (09x), CCCD numbers, assigned to OCCUPIED rooms.
- **Services (4):** WiFi (100,000/tháng), Giữ xe (50,000/tháng), Giặt ủi (30,000/lần), Dọn phòng (50,000/lần).
- **Invoices (5):** For occupied rooms, months 01/2026-03/2026, mix PAID/UNPAID. Realistic meter readings.
- After adding tenants to rooms, set those rooms' status to OCCUPIED.

### Step 11: Compile check

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Create `RoomStatus.java`
- [ ] Create `InvoiceStatus.java`
- [ ] Create `Room.java`
- [ ] Create `Tenant.java`
- [ ] Create `Invoice.java`
- [ ] Create `Service.java`
- [ ] Create `RoomRepository.java`
- [ ] Create `TenantRepository.java`
- [ ] Create `InvoiceRepository.java`
- [ ] Create `ServiceRepository.java`
- [ ] Create `SampleData.java` with 8 rooms, 5 tenants, 4 services, 5 invoices
- [ ] Run `./gradlew assembleDebug` — must pass

## Success Criteria

- All 11 Java files compile without errors
- Enum `getDisplayName()` returns Vietnamese text
- Repository Singleton pattern works (same instance across calls)
- `getAll()` returns defensive copies
- `Invoice.calculateTotal()` produces correct formula
- `SampleData.init()` is idempotent (safe to call multiple times)
