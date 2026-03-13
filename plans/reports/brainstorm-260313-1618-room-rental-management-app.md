# Brainstorm: Room Rental Management App

**Date:** 2026-03-13
**Status:** Agreed

---

## Problem Statement

Build Android app for landlords to manage rooms, tenants, invoices, and services.

**Constraints:**
- Java + XML (no Jetpack Compose, no Kotlin)
- MVC architecture
- RecyclerView for lists
- Full CRUD operations
- In-memory List storage only (no SQLite/Room)
- Team project on GitHub/GitLab (4+ members)

---

## Agreed Decisions

| Decision | Choice |
|----------|--------|
| Entities | Room, Tenant, Invoice, Service |
| Room:Tenant | 1:1 |
| Invoice detail | Room price + electricity + water (meter readings) |
| Screens | 7+ (full) |
| Navigation | Activity-based |
| UI Design | Material Design basic |
| Team approach | Layer-based (MVC layers) |
| Sample data | Yes, 5-10 rooms pre-populated |
| Search | Search + filter by room status |

---

## Data Model

### Room
| Field | Type | Description |
|-------|------|-------------|
| id | int | Auto-increment |
| roomNumber | String | e.g. "P101" |
| floor | int | Tầng |
| area | double | Diện tích (m²) |
| price | double | Giá thuê (VND/tháng) |
| status | RoomStatus | AVAILABLE / OCCUPIED / MAINTENANCE |
| description | String | Mô tả thêm |

### Tenant
| Field | Type | Description |
|-------|------|-------------|
| id | int | Auto-increment |
| name | String | Họ tên |
| phone | String | SĐT |
| idCard | String | CCCD |
| email | String | Email |
| roomId | int | Phòng đang thuê |
| moveInDate | String | Ngày vào ở |

### Invoice
| Field | Type | Description |
|-------|------|-------------|
| id | int | Auto-increment |
| roomId | int | Phòng |
| month | String | "03/2026" |
| roomPrice | double | Tiền phòng |
| oldElectric | int | Chỉ số điện cũ |
| newElectric | int | Chỉ số điện mới |
| electricPrice | double | Đơn giá điện/kWh |
| oldWater | int | Chỉ số nước cũ |
| newWater | int | Chỉ số nước mới |
| waterPrice | double | Đơn giá nước/m³ |
| totalAmount | double | Tổng tiền (auto-calculated) |
| status | InvoiceStatus | UNPAID / PAID |
| createdDate | String | Ngày tạo |

### Service
| Field | Type | Description |
|-------|------|-------------|
| id | int | Auto-increment |
| name | String | Tên DV (wifi, giữ xe...) |
| price | double | Giá |
| unit | String | Đơn vị ("tháng", "lần") |
| description | String | Mô tả |

---

## Architecture: MVC

```
com.example.btlmobile/
├── model/                          ← DATA LAYER
│   ├── Room.java                   (POJO + Serializable)
│   ├── Tenant.java
│   ├── Invoice.java
│   ├── Service.java
│   ├── enums/
│   │   ├── RoomStatus.java         (AVAILABLE, OCCUPIED, MAINTENANCE)
│   │   └── InvoiceStatus.java      (UNPAID, PAID)
│   └── repository/                 (Singleton, ArrayList storage)
│       ├── RoomRepository.java
│       ├── TenantRepository.java
│       ├── InvoiceRepository.java
│       └── ServiceRepository.java
│
├── controller/                     ← CONTROLLER LAYER (Activities)
│   ├── DashboardActivity.java
│   ├── room/
│   │   ├── RoomListActivity.java
│   │   ├── RoomDetailActivity.java
│   │   └── AddEditRoomActivity.java
│   ├── tenant/
│   │   ├── TenantListActivity.java
│   │   └── AddEditTenantActivity.java
│   ├── invoice/
│   │   ├── InvoiceListActivity.java
│   │   └── AddEditInvoiceActivity.java
│   ├── service/
│   │   ├── ServiceListActivity.java
│   │   └── AddEditServiceActivity.java
│   ├── SearchActivity.java
│   └── StatisticsActivity.java
│
├── adapter/                        ← ADAPTERS (RecyclerView)
│   ├── RoomAdapter.java
│   ├── TenantAdapter.java
│   ├── InvoiceAdapter.java
│   └── ServiceAdapter.java
│
└── util/
    └── SampleData.java             ← Pre-populated demo data
```

### View Layer (XML Layouts)
```
res/layout/
├── activity_dashboard.xml
├── activity_room_list.xml
├── activity_room_detail.xml
├── activity_add_edit_room.xml
├── activity_tenant_list.xml
├── activity_add_edit_tenant.xml
├── activity_invoice_list.xml
├── activity_add_edit_invoice.xml
├── activity_service_list.xml
├── activity_add_edit_service.xml
├── activity_search.xml
├── activity_statistics.xml
├── item_room.xml
├── item_tenant.xml
├── item_invoice.xml
└── item_service.xml
```

---

## Screens Overview (12 screens)

1. **DashboardActivity** — Tổng quan: số phòng trống/đã thuê, doanh thu tháng, quick actions
2. **RoomListActivity** — RecyclerView danh sách phòng + FAB thêm phòng + filter status
3. **RoomDetailActivity** — Chi tiết phòng + thông tin người thuê + lịch sử hóa đơn
4. **AddEditRoomActivity** — Form thêm/sửa phòng
5. **TenantListActivity** — RecyclerView danh sách người thuê
6. **AddEditTenantActivity** — Form thêm/sửa người thuê + chọn phòng
7. **InvoiceListActivity** — RecyclerView danh sách hóa đơn + filter PAID/UNPAID
8. **AddEditInvoiceActivity** — Form tạo/sửa hóa đơn + auto-calculate total
9. **ServiceListActivity** — RecyclerView danh sách dịch vụ
10. **AddEditServiceActivity** — Form thêm/sửa dịch vụ
11. **SearchActivity** — Search rooms/tenants + filter by status
12. **StatisticsActivity** — Doanh thu, tỷ lệ lấp đầy, biểu đồ đơn giản

---

## Repository Pattern (Singleton + ArrayList)

```java
// Example: RoomRepository.java
public class RoomRepository {
    private static RoomRepository instance;
    private final List<Room> rooms = new ArrayList<>();
    private int nextId = 1;

    private RoomRepository() {}

    public static RoomRepository getInstance() {
        if (instance == null) instance = new RoomRepository();
        return instance;
    }

    public List<Room> getAll() { return new ArrayList<>(rooms); }
    public Room getById(int id) { ... }
    public void add(Room room) { room.setId(nextId++); rooms.add(room); }
    public void update(Room room) { ... }
    public void delete(int id) { ... }
    public List<Room> searchByName(String keyword) { ... }
    public List<Room> filterByStatus(RoomStatus status) { ... }
}
```

---

## Team Division (Layer-based, 4+ members)

| Member | Responsibility | Files |
|--------|---------------|-------|
| **Member 1: Model** | All POJOs, enums, repositories, SampleData | `model/**`, `util/SampleData.java` |
| **Member 2: View** | All XML layouts, styles, colors, drawables | `res/layout/**`, `res/values/**`, `res/drawable/**` |
| **Member 3: Controller A** | Room + Tenant Activities + Adapters | `controller/room/**`, `controller/tenant/**`, `adapter/Room*.java`, `adapter/Tenant*.java` |
| **Member 4: Controller B** | Invoice + Service + Dashboard + Search + Statistics + Adapters | `controller/invoice/**`, `controller/service/**`, `controller/Dashboard*.java`, `controller/Search*.java`, `controller/Statistics*.java`, `adapter/Invoice*.java`, `adapter/Service*.java` |

### Workflow Order
1. **Member 1** starts first → define models + repositories + sample data
2. **Member 2** starts in parallel → create XML layouts (can use placeholder data)
3. **Members 3 & 4** start after Member 1 commits models → implement Activities using models + layouts

---

## Key Technical Patterns

- **Singleton** cho Repository (share data across Activities)
- **Serializable** trên Model classes (pass via Intent extras)
- **ViewHolder pattern** trong RecyclerView Adapters
- **Interface callback** cho adapter click events
- **AlertDialog** cho delete confirmation
- **TextWatcher** cho real-time search
- **Spinner** cho filter dropdown (room status)
- **startActivityForResult / ActivityResultLauncher** cho add/edit flows

---

## Risks & Mitigations

| Risk | Mitigation |
|------|-----------|
| Data lost on app restart | Accepted (requirement). SampleData re-populates on launch |
| Merge conflicts (layer-based) | Define clear package boundaries, commit frequently |
| Member 1 bottleneck | Member 2 (View) works in parallel; models are simple POJOs |
| Invoice calculation errors | Unit test the total calculation formula |
| Inconsistent UI | Member 2 owns all XML → consistent design |

---

## Success Criteria

- [ ] Full CRUD for 4 entities (Room, Tenant, Invoice, Service)
- [ ] RecyclerView displays lists correctly with ViewHolder
- [ ] MVC architecture clearly separated
- [ ] Dashboard shows summary statistics
- [ ] Search + filter works
- [ ] Sample data loads on app start
- [ ] App compiles & runs without crashes
- [ ] Clean Git history with contributions from all members

---

## Current Project Issue

**IMPORTANT:** Current `build.gradle.kts` uses Kotlin + Jetpack Compose. Must reconfigure to Java + XML:
- Remove `kotlin-compose` plugin
- Remove Compose dependencies
- Add `java` source sets
- Add RecyclerView, Material Components, CardView dependencies
- Remove Kotlin source files, create Java structure

---

## Next Steps

1. Reconfigure project from Kotlin+Compose → Java+XML
2. Create implementation plan with phases
3. Member 1 implements Model layer first
4. Members 2-4 implement in parallel after models are ready
