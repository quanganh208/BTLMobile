# Phase 03: View Layer (XML Layouts + Resources)

## Overview
- **Priority:** P1
- **Status:** Pending
- **Effort:** 4h
- **Owner:** Member 2
- **Depends on:** Phase 1 (can work in parallel with Phase 2)

Create all 16 XML layout files and update resource files (colors, strings, dimens). Consistent Material Design styling across all screens.

## Requirements
- 12 Activity layouts + 4 RecyclerView item layouts = 16 XML files
- All layouts use Material Components widgets (TextInputLayout, MaterialButton, MaterialCardView, etc.)
- Consistent color scheme, spacing, typography
- Toolbar in all list/detail activities
- FAB in all list activities for "add new"
- Spinner filters where needed

## Related Code Files

### Files to Create
- `app/src/main/res/layout/activity_dashboard.xml`
- `app/src/main/res/layout/activity_room_list.xml`
- `app/src/main/res/layout/activity_room_detail.xml`
- `app/src/main/res/layout/activity_add_edit_room.xml`
- `app/src/main/res/layout/activity_tenant_list.xml`
- `app/src/main/res/layout/activity_add_edit_tenant.xml`
- `app/src/main/res/layout/activity_invoice_list.xml`
- `app/src/main/res/layout/activity_add_edit_invoice.xml`
- `app/src/main/res/layout/activity_service_list.xml`
- `app/src/main/res/layout/activity_add_edit_service.xml`
- `app/src/main/res/layout/activity_search.xml`
- `app/src/main/res/layout/activity_statistics.xml`
- `app/src/main/res/layout/item_room.xml`
- `app/src/main/res/layout/item_tenant.xml`
- `app/src/main/res/layout/item_invoice.xml`
- `app/src/main/res/layout/item_service.xml`
- `app/src/main/res/values/dimens.xml`

### Files to Modify
- `app/src/main/res/values/strings.xml` — add all UI strings
- `app/src/main/res/values/colors.xml` — already updated in Phase 1, verify
- `app/src/main/res/values/themes.xml` — already updated in Phase 1, verify

## Implementation Steps

### Step 1: Create `dimens.xml`

Key dimensions:
```xml
<dimen name="spacing_small">8dp</dimen>
<dimen name="spacing_medium">16dp</dimen>
<dimen name="spacing_large">24dp</dimen>
<dimen name="card_elevation">4dp</dimen>
<dimen name="card_corner_radius">8dp</dimen>
<dimen name="toolbar_height">56dp</dimen>
<dimen name="fab_margin">16dp</dimen>
<dimen name="text_size_title">18sp</dimen>
<dimen name="text_size_body">14sp</dimen>
<dimen name="text_size_caption">12sp</dimen>
<dimen name="status_badge_padding_horizontal">12dp</dimen>
<dimen name="status_badge_padding_vertical">4dp</dimen>
```

### Step 2: Update `strings.xml`

Add all UI strings: titles, labels, hints, button texts, spinner items, etc. Use Vietnamese for user-facing text. Include:
- Dashboard: "Tổng quan", "Phòng trống", "Đã thuê", "Doanh thu tháng", etc.
- Room: "Danh sách phòng", "Thêm phòng", "Sửa phòng", "Số phòng", "Tầng", "Diện tích", "Giá thuê", "Trạng thái", "Mô tả"
- Tenant: "Danh sách người thuê", "Thêm người thuê", "Họ tên", "Số điện thoại", "CCCD", "Email", "Phòng", "Ngày vào ở"
- Invoice: "Danh sách hóa đơn", "Tạo hóa đơn", "Tháng", "Tiền phòng", "Chỉ số điện cũ/mới", "Đơn giá điện", "Chỉ số nước cũ/mới", "Đơn giá nước", "Tổng tiền"
- Service: "Danh sách dịch vụ", "Thêm dịch vụ", "Tên dịch vụ", "Giá", "Đơn vị"
- Search: "Tìm kiếm", "Lọc theo trạng thái"
- Statistics: "Thống kê"
- Common: "Lưu", "Hủy", "Xóa", "Sửa", "Tất cả"
- String-arrays for Spinner: room_status_filter (Tất cả, Trống, Đã thuê, Bảo trì), invoice_status_filter (Tất cả, Chưa thanh toán, Đã thanh toán)

### Step 3: `activity_dashboard.xml`

Structure: `ConstraintLayout` root
- **Toolbar** (MaterialToolbar) at top with app title
- **ScrollView** wrapping content:
  - Stats row: 3 `MaterialCardView` side-by-side (GridLayout or horizontal LinearLayout)
    - Card 1: Total rooms count + icon
    - Card 2: Occupied rooms count + percentage
    - Card 3: Monthly revenue
  - Quick Actions section: Label "Quản lý" + GridLayout (2 columns) of `MaterialCardView` buttons:
    - Phòng (Room icon)
    - Người thuê (Tenant icon)
    - Hóa đơn (Invoice icon)
    - Dịch vụ (Service icon)
    - Tìm kiếm (Search icon)
    - Thống kê (Statistics icon)
- Each quick action card: icon (ImageView) + label (TextView), clickable

### Step 4: `activity_room_list.xml`

Structure: `CoordinatorLayout` root
- `MaterialToolbar` with title "Danh sách phòng" + back arrow
- `Spinner` (id: `spinnerFilter`) below toolbar for room status filter
- `RecyclerView` (id: `recyclerView`) below spinner, `match_parent`
- `TextView` (id: `tvEmpty`) centered, "Không có phòng nào", visibility=gone
- `FloatingActionButton` (id: `fabAdd`) bottom-right, add icon

### Step 5: `activity_room_detail.xml`

Structure: `ConstraintLayout` root
- `MaterialToolbar` with title "Chi tiết phòng" + back arrow + edit/delete menu
- `ScrollView`:
  - **Room info section** (MaterialCardView):
    - Room number (large), status badge (colored TextView)
    - Floor, area, price rows (label: value)
    - Description
  - **Tenant info section** (MaterialCardView):
    - Title "Người thuê"
    - Name, phone, ID card, email, move-in date (or "Chưa có người thuê")
  - **Invoice section**:
    - Title "Hóa đơn" + count
    - RecyclerView (id: `recyclerViewInvoices`) for room's invoices

### Step 6: `activity_add_edit_room.xml`

Structure: `ConstraintLayout` root
- `MaterialToolbar` with title "Thêm phòng" / "Sửa phòng" + back arrow
- `ScrollView`:
  - `TextInputLayout` + `TextInputEditText` for: roomNumber, floor (inputType=number), area (inputType=numberDecimal), price (inputType=numberDecimal), description
  - `Spinner` (id: `spinnerStatus`) for RoomStatus
  - `MaterialButton` "Lưu" (id: `btnSave`) at bottom, full-width

### Step 7: `activity_tenant_list.xml`

Same pattern as `activity_room_list.xml`:
- Toolbar "Danh sách người thuê" + back
- RecyclerView, empty text, FAB
- No Spinner filter (simpler)

### Step 8: `activity_add_edit_tenant.xml`

Structure: `ConstraintLayout` root
- Toolbar "Thêm người thuê" / "Sửa người thuê"
- ScrollView:
  - TextInputLayouts for: name, phone (inputType=phone), idCard (inputType=number), email (inputType=textEmailAddress), moveInDate
  - Spinner (id: `spinnerRoom`) for available rooms
  - MaterialButton "Lưu"

### Step 9: `activity_invoice_list.xml`

Same pattern as `activity_room_list.xml`:
- Toolbar "Danh sách hóa đơn" + back
- Spinner filter for InvoiceStatus (Tất cả / Chưa thanh toán / Đã thanh toán)
- RecyclerView, empty text, FAB

### Step 10: `activity_add_edit_invoice.xml`

Structure: `ConstraintLayout` root
- Toolbar "Tạo hóa đơn" / "Sửa hóa đơn"
- ScrollView:
  - Spinner (id: `spinnerRoom`) for room selection
  - TextInputLayout for month (hint "MM/YYYY")
  - TextInputLayout for roomPrice (inputType=numberDecimal)
  - **Electricity section** (labeled): oldElectric, newElectric (inputType=number), electricPrice (inputType=numberDecimal)
  - **Water section** (labeled): oldWater, newWater (inputType=number), waterPrice (inputType=numberDecimal)
  - Divider
  - **Total display** (id: `tvTotal`) — large text showing calculated total
  - Spinner (id: `spinnerStatus`) for InvoiceStatus
  - MaterialButton "Lưu"

### Step 11: `activity_service_list.xml`

Same pattern:
- Toolbar "Danh sách dịch vụ" + back
- RecyclerView, empty text, FAB
- No filter Spinner

### Step 12: `activity_add_edit_service.xml`

Structure: `ConstraintLayout` root
- Toolbar "Thêm dịch vụ" / "Sửa dịch vụ"
- ScrollView:
  - TextInputLayouts for: name, price (inputType=numberDecimal), unit, description
  - MaterialButton "Lưu"

### Step 13: `activity_search.xml`

Structure: `ConstraintLayout` root
- Toolbar "Tìm kiếm" + back
- `SearchView` or `TextInputLayout` (id: `searchView`) for keyword input
- `Spinner` (id: `spinnerFilter`) for room status filter
- `RecyclerView` (id: `recyclerView`) for results
- `TextView` (id: `tvEmpty`) "Không tìm thấy kết quả"

### Step 14: `activity_statistics.xml`

Structure: `ConstraintLayout` root
- Toolbar "Thống kê" + back
- ScrollView:
  - **MaterialCardView** — Total rooms + breakdown (Available / Occupied / Maintenance) with counts and percentages
  - **MaterialCardView** — Occupancy rate with large percentage text
  - **MaterialCardView** — Revenue: total paid, total unpaid, this month summary
  - **MaterialCardView** — Tenants: total count

### Step 15: RecyclerView Item Layouts

**item_room.xml** — `MaterialCardView` wrapping `ConstraintLayout`:
- `TextView` roomNumber (bold, large)
- `TextView` status badge (colored background, rounded, small text)
- `TextView` floor info
- `TextView` area (e.g. "25 m²")
- `TextView` price (e.g. "3,000,000 đ/tháng")

**item_tenant.xml** — `MaterialCardView`:
- `TextView` name (bold)
- `TextView` phone
- `TextView` room number
- `TextView` move-in date

**item_invoice.xml** — `MaterialCardView`:
- `TextView` month (bold)
- `TextView` room number
- `TextView` totalAmount
- `TextView` status badge (green=paid, red=unpaid)

**item_service.xml** — `MaterialCardView`:
- `TextView` name (bold)
- `TextView` price + unit (e.g. "100,000 đ/tháng")
- `TextView` description

### Step 16: Compile check

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Create `dimens.xml`
- [ ] Update `strings.xml` with all UI strings
- [ ] Create `activity_dashboard.xml`
- [ ] Create `activity_room_list.xml`
- [ ] Create `activity_room_detail.xml`
- [ ] Create `activity_add_edit_room.xml`
- [ ] Create `activity_tenant_list.xml`
- [ ] Create `activity_add_edit_tenant.xml`
- [ ] Create `activity_invoice_list.xml`
- [ ] Create `activity_add_edit_invoice.xml`
- [ ] Create `activity_service_list.xml`
- [ ] Create `activity_add_edit_service.xml`
- [ ] Create `activity_search.xml`
- [ ] Create `activity_statistics.xml`
- [ ] Create `item_room.xml`
- [ ] Create `item_tenant.xml`
- [ ] Create `item_invoice.xml`
- [ ] Create `item_service.xml`
- [ ] Compile check passes

## Success Criteria

- All 16 layout XML files are valid and parse without errors
- Consistent spacing using `dimens.xml` values
- All user-facing text uses `@string/` references (no hardcoded strings)
- Colors reference `@color/` values
- Material Components used (TextInputLayout, MaterialCardView, MaterialToolbar, FloatingActionButton, MaterialButton)
- `./gradlew assembleDebug` passes
