# Phase 06: Dashboard + Search + Statistics

## Overview
- **Priority:** P1
- **Status:** Pending
- **Effort:** 2h
- **Owner:** Member 4
- **Depends on:** Phase 4, Phase 5 (all Activities and Adapters ready)

Complete DashboardActivity (replace placeholder), SearchActivity, and StatisticsActivity.

## Requirements
- Dashboard: show summary stats + quick navigation to all modules
- Search: real-time search with TextWatcher + filter by room status
- Statistics: aggregate data from all repositories

## Related Code Files

### Files to Modify
- `app/src/main/java/com/example/btlmobile/controller/DashboardActivity.java` — replace placeholder with full implementation

### Files to Create
- `app/src/main/java/com/example/btlmobile/controller/SearchActivity.java`
- `app/src/main/java/com/example/btlmobile/controller/StatisticsActivity.java`

### Files to Modify
- `app/src/main/AndroidManifest.xml` — register SearchActivity, StatisticsActivity

## Implementation Steps

### Step 1: DashboardActivity.java (full implementation)

```
Package: com.example.btlmobile.controller
Extends: AppCompatActivity

Layout: activity_dashboard.xml

onCreate:
  1. setContentView(R.layout.activity_dashboard)
  2. Call SampleData.init() — ensures sample data loaded once
  3. Find views: stat cards (tvTotalRooms, tvOccupiedRooms, tvMonthlyRevenue)
  4. Find quick action cards: cardRooms, cardTenants, cardInvoices, cardServices, cardSearch, cardStatistics
  5. Set click listeners on quick action cards:
     - cardRooms → RoomListActivity
     - cardTenants → TenantListActivity
     - cardInvoices → InvoiceListActivity
     - cardServices → ServiceListActivity
     - cardSearch → SearchActivity
     - cardStatistics → StatisticsActivity

onResume:
  - Refresh stats (data may have changed):
    a. totalRooms = RoomRepository.getInstance().getCount()
    b. occupiedRooms = RoomRepository.getInstance().getCountByStatus(OCCUPIED)
    c. availableRooms = RoomRepository.getInstance().getCountByStatus(AVAILABLE)
    d. monthlyRevenue = InvoiceRepository.getInstance().getTotalRevenue()
    e. Update TextViews with formatted values
```

Key: `SampleData.init()` called in `onCreate` to populate data on first launch. Idempotent — safe on subsequent calls.

### Step 2: SearchActivity.java

```
Package: com.example.btlmobile.controller
Extends: AppCompatActivity

Layout: activity_search.xml

Fields:
  - EditText/SearchView searchInput
  - Spinner spinnerFilter
  - RecyclerView recyclerView
  - RoomAdapter adapter
  - TextView tvEmpty

onCreate:
  1. setContentView, setup toolbar with back
  2. Init repositories
  3. Setup Spinner with room_status_filter array
  4. Setup RecyclerView with RoomAdapter (initial: all rooms)
  5. Setup TextWatcher on searchInput:
     onTextChanged → performSearch()
  6. Spinner onItemSelected → performSearch()

performSearch():
  - Get keyword from searchInput
  - Get selected status filter from Spinner
  - Start with RoomRepository.searchByKeyword(keyword)
  - If status filter != "Tất cả" → further filter by status
  - adapter.updateData(results)
  - Toggle tvEmpty visibility

Search logic:
  - Spinner position 0 ("Tất cả") → no status filter
  - Position 1 ("Trống") → AVAILABLE
  - Position 2 ("Đã thuê") → OCCUPIED
  - Position 3 ("Bảo trì") → MAINTENANCE
  - Keyword matches roomNumber or description (case-insensitive, handled by repository)
```

### Step 3: StatisticsActivity.java

```
Package: com.example.btlmobile.controller
Extends: AppCompatActivity

Layout: activity_statistics.xml

onCreate:
  1. setContentView, setup toolbar with back
  2. Calculate and display all stats:

Room stats:
  - totalRooms = RoomRepository.getCount()
  - available = RoomRepository.getCountByStatus(AVAILABLE)
  - occupied = RoomRepository.getCountByStatus(OCCUPIED)
  - maintenance = RoomRepository.getCountByStatus(MAINTENANCE)
  - occupancyRate = (occupied * 100.0) / totalRooms (handle division by zero)

Revenue stats:
  - totalPaid = InvoiceRepository.getTotalRevenue()
  - totalUnpaid = InvoiceRepository.getTotalUnpaid()
  - totalInvoices = InvoiceRepository.getCount()

Tenant stats:
  - totalTenants = TenantRepository.getCount()

Display:
  - Format numbers with NumberFormat (Vietnamese locale)
  - Show percentages with 1 decimal place
  - Show VND amounts formatted (e.g. "12,500,000 đ")
```

### Step 4: Register Activities in AndroidManifest.xml

Add:
```xml
<activity android:name=".controller.SearchActivity" />
<activity android:name=".controller.StatisticsActivity" />
```

### Step 5: Compile and test

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Implement full `DashboardActivity.java` with stats + navigation + SampleData.init()
- [ ] Create `SearchActivity.java` with TextWatcher + Spinner filter
- [ ] Create `StatisticsActivity.java` with aggregated stats
- [ ] Register SearchActivity and StatisticsActivity in AndroidManifest.xml
- [ ] Compile check passes
- [ ] Manual test: Dashboard shows correct stats, navigation works
- [ ] Manual test: Search filters rooms by keyword + status
- [ ] Manual test: Statistics shows correct aggregated data

## Success Criteria

- App launches to Dashboard with correct summary stats
- All 6 quick action cards navigate to correct Activities
- Stats refresh on `onResume` (reflect changes made in other screens)
- Search filters in real-time as user types
- Search + status Spinner combination works
- Statistics page shows accurate counts, percentages, revenue totals
- `./gradlew assembleDebug` passes
