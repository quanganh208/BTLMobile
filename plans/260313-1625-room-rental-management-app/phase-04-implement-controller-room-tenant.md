# Phase 04: Controller A — Room + Tenant Activities & Adapters

## Overview
- **Priority:** P1
- **Status:** Pending
- **Effort:** 3h
- **Owner:** Member 3
- **Depends on:** Phase 2 (models), Phase 3 (layouts)

Implement all Room and Tenant Activities plus their RecyclerView Adapters.

## Requirements
- RecyclerView Adapters with ViewHolder pattern and click listener interface
- CRUD operations via Repository Singleton
- Form validation on add/edit screens
- Navigation between Activities via Intents
- Pass objects via Intent extras (Serializable)
- ActivityResultLauncher for add/edit → list refresh
- AlertDialog for delete confirmation

## Related Code Files

### Files to Create
- `app/src/main/java/com/example/btlmobile/adapter/RoomAdapter.java`
- `app/src/main/java/com/example/btlmobile/adapter/TenantAdapter.java`
- `app/src/main/java/com/example/btlmobile/controller/room/RoomListActivity.java`
- `app/src/main/java/com/example/btlmobile/controller/room/RoomDetailActivity.java`
- `app/src/main/java/com/example/btlmobile/controller/room/AddEditRoomActivity.java`
- `app/src/main/java/com/example/btlmobile/controller/tenant/TenantListActivity.java`
- `app/src/main/java/com/example/btlmobile/controller/tenant/AddEditTenantActivity.java`

### Files to Modify
- `app/src/main/AndroidManifest.xml` — register all Activities in this phase

## Implementation Steps

### Step 1: RoomAdapter.java

```
Package: com.example.btlmobile.adapter
Extends: RecyclerView.Adapter<RoomAdapter.ViewHolder>

Inner interface: OnItemClickListener
  - void onItemClick(Room room)
  - void onItemLongClick(Room room)  // for delete

Inner class: ViewHolder extends RecyclerView.ViewHolder
  - tvRoomNumber, tvStatus, tvFloor, tvArea, tvPrice

Constructor: RoomAdapter(List<Room> rooms, OnItemClickListener listener)

Methods:
  - onCreateViewHolder: inflate item_room.xml
  - onBindViewHolder: bind room data
    - Set roomNumber, floor, area, price (formatted with NumberFormat)
    - Set status badge text + background color based on RoomStatus:
      AVAILABLE → status_available (green)
      OCCUPIED → status_occupied (red)
      MAINTENANCE → status_maintenance (orange)
    - Set click/longClick listeners
  - getItemCount
  - updateData(List<Room> newRooms): update list + notifyDataSetChanged
```

### Step 2: TenantAdapter.java

```
Same pattern as RoomAdapter.

Inner interface: OnItemClickListener
  - void onItemClick(Tenant tenant)
  - void onItemLongClick(Tenant tenant)

ViewHolder: tvName, tvPhone, tvRoomNumber, tvMoveInDate

onBindViewHolder:
  - Set name, phone, moveInDate
  - Look up room number from RoomRepository.getInstance().getById(tenant.getRoomId())
  - Set click/longClick listeners

updateData(List<Tenant> newTenants)
```

### Step 3: RoomListActivity.java

```
Package: com.example.btlmobile.controller.room
Extends: AppCompatActivity

Layout: activity_room_list.xml

Fields:
  - RecyclerView recyclerView
  - RoomAdapter adapter
  - Spinner spinnerFilter
  - TextView tvEmpty
  - FloatingActionButton fabAdd
  - RoomRepository roomRepository
  - ActivityResultLauncher<Intent> addEditLauncher

onCreate:
  1. setContentView(R.layout.activity_room_list)
  2. Setup toolbar with back navigation
  3. Init roomRepository = RoomRepository.getInstance()
  4. Setup Spinner with string-array room_status_filter
  5. Setup RecyclerView: LinearLayoutManager, adapter
  6. Setup FAB: launch AddEditRoomActivity
  7. Register ActivityResultLauncher: on result, refreshList()
  8. Spinner onItemSelected → filter rooms and update adapter

refreshList():
  - Get rooms from repository (filtered or all based on spinner)
  - adapter.updateData(rooms)
  - Toggle tvEmpty visibility

Adapter click:
  - onItemClick → launch RoomDetailActivity with room.getId() as extra
  - onItemLongClick → show AlertDialog "Xóa phòng?" → delete from repo → refreshList()
```

### Step 4: RoomDetailActivity.java

```
Package: com.example.btlmobile.controller.room
Extends: AppCompatActivity

Layout: activity_room_detail.xml

onCreate:
  1. Get roomId from Intent extras
  2. Load Room from RoomRepository
  3. Load Tenant from TenantRepository.getByRoomId(roomId)
  4. Load Invoices from InvoiceRepository.getByRoomId(roomId)
  5. Populate room info views
  6. Populate tenant info (or show "Chưa có người thuê")
  7. Setup invoice RecyclerView (reuse InvoiceAdapter or simple list)
  8. Edit button → launch AddEditRoomActivity with room extra
  9. Delete button → AlertDialog → delete room + associated tenant → finish()

onResume:
  - Reload data from repositories (in case edit was made)
```

### Step 5: AddEditRoomActivity.java

```
Package: com.example.btlmobile.controller.room
Extends: AppCompatActivity

Layout: activity_add_edit_room.xml

onCreate:
  1. Check Intent for "room_id" extra → edit mode (load existing) or add mode
  2. If edit: populate fields with existing room data
  3. Setup Spinner with RoomStatus values
  4. Setup Save button click:
     a. Validate: roomNumber not empty, floor > 0, area > 0, price > 0
     b. Show error on TextInputLayout if invalid
     c. Create/update Room object
     d. Call repository.add() or repository.update()
     e. setResult(RESULT_OK) → finish()

Validation rules:
  - roomNumber: required, non-empty
  - floor: required, > 0
  - area: required, > 0
  - price: required, > 0
```

### Step 6: TenantListActivity.java

```
Package: com.example.btlmobile.controller.tenant
Extends: AppCompatActivity

Layout: activity_tenant_list.xml

Same pattern as RoomListActivity but simpler (no Spinner filter):
  - RecyclerView + TenantAdapter
  - FAB → AddEditTenantActivity
  - Click → AddEditTenantActivity (edit mode)
  - LongClick → AlertDialog → delete tenant + set room status to AVAILABLE → refreshList()
  - ActivityResultLauncher for refresh on return
```

### Step 7: AddEditTenantActivity.java

```
Package: com.example.btlmobile.controller.tenant
Extends: AppCompatActivity

Layout: activity_add_edit_tenant.xml

onCreate:
  1. Check Intent for "tenant_id" extra → edit or add mode
  2. Setup Spinner for room selection:
     - Get AVAILABLE rooms from RoomRepository.filterByStatus(AVAILABLE)
     - If editing, also include current room in spinner list
     - Display room numbers in Spinner
  3. If edit: populate fields
  4. Save button:
     a. Validate: name required, phone required, idCard required, room selected
     b. Create/update Tenant
     c. If adding: set old room (if changed) to AVAILABLE, set new room to OCCUPIED
     d. If editing and room changed: update both old and new room statuses
     e. Call repository.add() or repository.update()
     f. setResult(RESULT_OK) → finish()

Key logic:
  - When adding tenant → set selected room status to OCCUPIED
  - When editing tenant and room changed → old room → AVAILABLE, new room → OCCUPIED
  - When deleting tenant (handled in TenantListActivity) → room → AVAILABLE
```

### Step 8: Register Activities in AndroidManifest.xml

Add inside `<application>` tag:
```xml
<activity android:name=".controller.room.RoomListActivity" />
<activity android:name=".controller.room.RoomDetailActivity" />
<activity android:name=".controller.room.AddEditRoomActivity" />
<activity android:name=".controller.tenant.TenantListActivity" />
<activity android:name=".controller.tenant.AddEditTenantActivity" />
```

### Step 9: Compile and test manually

```bash
./gradlew assembleDebug
```

Test navigation: Dashboard → Room List → Add Room → Room Detail → Edit Room, and Dashboard → Tenant List → Add Tenant → Edit Tenant.

## Todo List

- [ ] Create `RoomAdapter.java` with ViewHolder + click interface
- [ ] Create `TenantAdapter.java` with ViewHolder + click interface
- [ ] Create `RoomListActivity.java` with Spinner filter + RecyclerView + FAB
- [ ] Create `RoomDetailActivity.java` with room/tenant/invoice display
- [ ] Create `AddEditRoomActivity.java` with form validation
- [ ] Create `TenantListActivity.java` with RecyclerView + FAB
- [ ] Create `AddEditTenantActivity.java` with room Spinner + room status logic
- [ ] Register all 5 Activities in AndroidManifest.xml
- [ ] Compile check passes
- [ ] Manual test: CRUD Room works end-to-end
- [ ] Manual test: CRUD Tenant works, room status updates correctly

## Success Criteria

- Room list shows filtered results via Spinner
- Add/edit room form validates input and saves to repository
- Room detail shows room info + tenant info + invoices
- Tenant add form only shows AVAILABLE rooms in Spinner
- Adding tenant sets room to OCCUPIED; deleting tenant sets room to AVAILABLE
- Long-press delete shows confirmation dialog
- Back navigation works on all screens
- `./gradlew assembleDebug` passes
