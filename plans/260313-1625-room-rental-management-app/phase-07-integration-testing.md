# Phase 07: Integration & Testing

## Overview
- **Priority:** P1
- **Status:** Pending
- **Effort:** 1h
- **Owner:** All members
- **Depends on:** Phase 6 (all features complete)

Final integration: ensure all Activities registered, navigation flows work end-to-end, CRUD operations are correct, and APK builds successfully.

## Requirements
- All 12 Activities registered in AndroidManifest.xml
- Complete navigation graph works (Dashboard → every screen and back)
- CRUD for all 4 entities works without crashes
- Search and filter features work
- Invoice calculation is correct
- Sample data loads on first launch
- APK builds successfully

## Related Code Files

### Files to Verify
- `app/src/main/AndroidManifest.xml` — all 12 Activities registered

### Full Activity Registration (AndroidManifest.xml)

All activities that must be inside `<application>`:
```xml
<!-- Dashboard (launcher) -->
<activity android:name=".controller.DashboardActivity" android:exported="true" ...>
    <intent-filter> ... MAIN + LAUNCHER ... </intent-filter>
</activity>

<!-- Room -->
<activity android:name=".controller.room.RoomListActivity" />
<activity android:name=".controller.room.RoomDetailActivity" />
<activity android:name=".controller.room.AddEditRoomActivity" />

<!-- Tenant -->
<activity android:name=".controller.tenant.TenantListActivity" />
<activity android:name=".controller.tenant.AddEditTenantActivity" />

<!-- Invoice -->
<activity android:name=".controller.invoice.InvoiceListActivity" />
<activity android:name=".controller.invoice.AddEditInvoiceActivity" />

<!-- Service -->
<activity android:name=".controller.service.ServiceListActivity" />
<activity android:name=".controller.service.AddEditServiceActivity" />

<!-- Search & Statistics -->
<activity android:name=".controller.SearchActivity" />
<activity android:name=".controller.StatisticsActivity" />
```

## Implementation Steps

### Step 1: Verify AndroidManifest.xml

Confirm all 12 Activities are registered. Missing registration = crash on navigation.

### Step 2: Build APK

```bash
./gradlew clean assembleDebug
```

Must complete without errors.

### Step 3: Test Navigation Flows

Test each flow manually on emulator/device:

**Flow 1: Room CRUD**
1. Dashboard → "Phòng" card → RoomListActivity
2. Verify sample rooms displayed
3. Spinner filter: select "Trống" → only AVAILABLE rooms shown
4. FAB → AddEditRoomActivity → fill form → Save → back to list (new room visible)
5. Click room → RoomDetailActivity → verify room info displayed
6. Edit button → AddEditRoomActivity (pre-filled) → modify → Save
7. Long-press room → Delete dialog → Confirm → room removed

**Flow 2: Tenant CRUD**
1. Dashboard → "Người thuê" → TenantListActivity
2. Verify sample tenants displayed
3. FAB → AddEditTenantActivity → room Spinner shows only AVAILABLE rooms
4. Select room, fill form → Save → verify room status changed to OCCUPIED
5. Click tenant → edit → change room → verify old room AVAILABLE, new room OCCUPIED
6. Long-press tenant → Delete → verify room status back to AVAILABLE

**Flow 3: Invoice CRUD**
1. Dashboard → "Hóa đơn" → InvoiceListActivity
2. Spinner filter: UNPAID only → verify correct invoices
3. FAB → AddEditInvoiceActivity
4. Select room → roomPrice auto-fills
5. Enter meter readings → total auto-calculates in real-time
6. Save → back to list → new invoice visible
7. Click invoice → edit → change status to PAID → Save

**Flow 4: Service CRUD**
1. Dashboard → "Dịch vụ" → ServiceListActivity
2. FAB → AddEditServiceActivity → fill → Save
3. Click service → edit → Save
4. Long-press → Delete

**Flow 5: Search**
1. Dashboard → "Tìm kiếm" → SearchActivity
2. Type "P1" → rooms matching P1xx shown
3. Change Spinner to "Đã thuê" → only OCCUPIED rooms matching "P1"
4. Clear search → all rooms of selected status shown

**Flow 6: Statistics**
1. Dashboard → "Thống kê" → StatisticsActivity
2. Verify total rooms matches RoomRepository count
3. Verify occupancy % is correct
4. Verify revenue totals match invoices

**Flow 7: Dashboard Stats**
1. Return to Dashboard from any screen
2. Verify stats update to reflect changes made

### Step 4: Edge Case Testing

- Add room with empty fields → validation error shown
- Add tenant when no rooms available → Spinner empty, cannot save
- Invoice with newElectric < oldElectric → total still calculates (negative usage = user error, acceptable for MVP)
- Delete room that has a tenant → should also handle tenant cleanup (or prevent deletion)
- Rapid navigation back/forward → no crashes

### Step 5: Final Build

```bash
./gradlew assembleDebug
```

Locate APK: `app/build/outputs/apk/debug/app-debug.apk`

## Todo List

- [ ] Verify all 12 Activities in AndroidManifest.xml
- [ ] `./gradlew clean assembleDebug` passes
- [ ] Test Flow 1: Room CRUD (list, add, detail, edit, delete, filter)
- [ ] Test Flow 2: Tenant CRUD (list, add, edit, delete, room status sync)
- [ ] Test Flow 3: Invoice CRUD (list, add, edit, filter, auto-calculate)
- [ ] Test Flow 4: Service CRUD (list, add, edit, delete)
- [ ] Test Flow 5: Search (keyword + status filter)
- [ ] Test Flow 6: Statistics (correct aggregations)
- [ ] Test Flow 7: Dashboard stats refresh
- [ ] Edge case: form validation prevents bad data
- [ ] No crashes during normal usage
- [ ] APK built successfully

## Success Criteria

- `./gradlew assembleDebug` succeeds
- All 7 navigation flows work without crashes
- CRUD for all 4 entities works correctly
- Room↔Tenant status sync works (add tenant → OCCUPIED, delete tenant → AVAILABLE)
- Invoice total auto-calculates correctly
- Search + filter produces correct results
- Statistics shows accurate aggregated data
- Dashboard stats refresh after data changes
- Sample data loads on first launch
- APK file generated at `app/build/outputs/apk/debug/app-debug.apk`
