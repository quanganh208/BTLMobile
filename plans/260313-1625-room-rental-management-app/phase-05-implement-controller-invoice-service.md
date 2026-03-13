# Phase 05: Controller B — Invoice + Service Activities & Adapters

## Overview
- **Priority:** P1
- **Status:** Pending
- **Effort:** 3h
- **Owner:** Member 4
- **Depends on:** Phase 2 (models), Phase 3 (layouts)

Implement all Invoice and Service Activities plus their RecyclerView Adapters.

## Requirements
- RecyclerView Adapters with ViewHolder pattern and click listener interface
- Invoice form auto-calculates total in real-time via TextWatcher
- Invoice status filter (PAID / UNPAID / ALL) via Spinner
- CRUD operations via Repository Singleton
- Form validation on add/edit screens

## Related Code Files

### Files to Create
- `app/src/main/java/com/example/btlmobile/adapter/InvoiceAdapter.java`
- `app/src/main/java/com/example/btlmobile/adapter/ServiceAdapter.java`
- `app/src/main/java/com/example/btlmobile/controller/invoice/InvoiceListActivity.java`
- `app/src/main/java/com/example/btlmobile/controller/invoice/AddEditInvoiceActivity.java`
- `app/src/main/java/com/example/btlmobile/controller/service/ServiceListActivity.java`
- `app/src/main/java/com/example/btlmobile/controller/service/AddEditServiceActivity.java`

### Files to Modify
- `app/src/main/AndroidManifest.xml` — register all Activities in this phase

## Implementation Steps

### Step 1: InvoiceAdapter.java

```
Package: com.example.btlmobile.adapter
Extends: RecyclerView.Adapter<InvoiceAdapter.ViewHolder>

Inner interface: OnItemClickListener
  - void onItemClick(Invoice invoice)
  - void onItemLongClick(Invoice invoice)

Inner class: ViewHolder
  - tvMonth, tvRoomNumber, tvTotal, tvStatus

Constructor: InvoiceAdapter(List<Invoice> invoices, OnItemClickListener listener)

onBindViewHolder:
  - Set month text
  - Look up room number from RoomRepository.getInstance().getById(invoice.getRoomId())
  - Format totalAmount with NumberFormat (Vietnamese locale, VND)
  - Set status badge:
    PAID → text "Đã thanh toán", background color status_paid (green)
    UNPAID → text "Chưa thanh toán", background color status_unpaid (red)
  - Set click/longClick listeners

updateData(List<Invoice> newInvoices)
```

### Step 2: ServiceAdapter.java

```
Package: com.example.btlmobile.adapter

Inner interface: OnItemClickListener
  - void onItemClick(Service service)
  - void onItemLongClick(Service service)

ViewHolder: tvName, tvPrice, tvDescription

onBindViewHolder:
  - Set name
  - Format price + "/" + unit (e.g. "100,000 đ/tháng")
  - Set description (or hide if empty)
  - Set click/longClick listeners

updateData(List<Service> newServices)
```

### Step 3: InvoiceListActivity.java

```
Package: com.example.btlmobile.controller.invoice
Extends: AppCompatActivity

Layout: activity_invoice_list.xml

Fields:
  - RecyclerView, InvoiceAdapter, Spinner spinnerFilter, TextView tvEmpty, FAB
  - InvoiceRepository invoiceRepository
  - ActivityResultLauncher<Intent> addEditLauncher

onCreate:
  1. setContentView, setup toolbar with back
  2. Init repository
  3. Setup Spinner with invoice_status_filter array ("Tất cả", "Chưa thanh toán", "Đã thanh toán")
  4. Setup RecyclerView: LinearLayoutManager, adapter
  5. FAB → AddEditInvoiceActivity
  6. Register ActivityResultLauncher → refreshList()
  7. Spinner onItemSelected → filter and update

refreshList():
  - Position 0 ("Tất cả") → getAll()
  - Position 1 ("Chưa thanh toán") → filterByStatus(UNPAID)
  - Position 2 ("Đã thanh toán") → filterByStatus(PAID)
  - adapter.updateData(filtered)
  - Toggle tvEmpty

Adapter click:
  - onItemClick → AddEditInvoiceActivity with "invoice_id" extra (edit mode)
  - onItemLongClick → AlertDialog → delete → refreshList()
```

### Step 4: AddEditInvoiceActivity.java

```
Package: com.example.btlmobile.controller.invoice
Extends: AppCompatActivity

Layout: activity_add_edit_invoice.xml

Key feature: AUTO-CALCULATE total in real-time via TextWatcher

Fields:
  - Spinner spinnerRoom, spinnerStatus
  - TextInputEditText: etMonth, etRoomPrice, etOldElectric, etNewElectric, etElectricPrice, etOldWater, etNewWater, etWaterPrice
  - TextView tvTotal
  - InvoiceRepository, RoomRepository

onCreate:
  1. Check Intent for "invoice_id" → edit or add mode
  2. Setup room Spinner: populate with all OCCUPIED rooms (display room number + tenant name)
  3. Setup status Spinner: UNPAID, PAID
  4. If edit: populate all fields from existing invoice
  5. Add TextWatcher to all numeric fields that affect total:
     etRoomPrice, etOldElectric, etNewElectric, etElectricPrice, etOldWater, etNewWater, etWaterPrice
  6. When room selected → auto-fill roomPrice from Room.getPrice()
  7. Save button:
     a. Validate: room selected, month not empty, all meter readings filled
     b. Calculate total: roomPrice + (newElectric - oldElectric) * electricPrice + (newWater - oldWater) * waterPrice
     c. Create/update Invoice with calculateTotal()
     d. repository.add() or repository.update()
     e. setResult(RESULT_OK) → finish()

calculateAndDisplayTotal():
  - Parse all fields (default 0 if empty)
  - total = roomPrice + (newElec - oldElec) * elecPrice + (newWater - oldWater) * waterPrice
  - Display formatted total in tvTotal

TextWatcher implementation:
  - Create single TextWatcher that calls calculateAndDisplayTotal()
  - Attach to all 7 numeric fields
```

### Step 5: ServiceListActivity.java

```
Package: com.example.btlmobile.controller.service
Extends: AppCompatActivity

Layout: activity_service_list.xml

Simple list pattern (no filter Spinner):
  - RecyclerView + ServiceAdapter
  - FAB → AddEditServiceActivity
  - Click → AddEditServiceActivity (edit mode)
  - LongClick → AlertDialog → delete → refreshList()
  - ActivityResultLauncher for refresh
```

### Step 6: AddEditServiceActivity.java

```
Package: com.example.btlmobile.controller.service
Extends: AppCompatActivity

Layout: activity_add_edit_service.xml

onCreate:
  1. Check Intent for "service_id" → edit or add mode
  2. If edit: populate fields
  3. Save button:
     a. Validate: name required, price > 0, unit required
     b. Create/update Service
     c. repository.add() or repository.update()
     d. setResult(RESULT_OK) → finish()
```

### Step 7: Register Activities in AndroidManifest.xml

Add inside `<application>` tag:
```xml
<activity android:name=".controller.invoice.InvoiceListActivity" />
<activity android:name=".controller.invoice.AddEditInvoiceActivity" />
<activity android:name=".controller.service.ServiceListActivity" />
<activity android:name=".controller.service.AddEditServiceActivity" />
```

### Step 8: Compile and test

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Create `InvoiceAdapter.java` with status badge coloring
- [ ] Create `ServiceAdapter.java`
- [ ] Create `InvoiceListActivity.java` with Spinner filter
- [ ] Create `AddEditInvoiceActivity.java` with auto-calculate total (TextWatcher)
- [ ] Create `ServiceListActivity.java`
- [ ] Create `AddEditServiceActivity.java` with validation
- [ ] Register all 4 Activities in AndroidManifest.xml
- [ ] Compile check passes
- [ ] Manual test: Invoice total auto-calculates correctly
- [ ] Manual test: Invoice filter by PAID/UNPAID works
- [ ] Manual test: CRUD Service works end-to-end

## Success Criteria

- Invoice total auto-calculates as user types (real-time via TextWatcher)
- Invoice list filters by PAID/UNPAID/ALL via Spinner
- Room selection in invoice form shows only OCCUPIED rooms
- Selecting room auto-fills room price
- Invoice status badge shows correct color (green=paid, red=unpaid)
- Service CRUD works with form validation
- `./gradlew assembleDebug` passes
