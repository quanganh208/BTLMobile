# Phase 02: SortHelper

## Overview
- **Priority:** P3
- **Status:** Pending
- **Effort:** 15m
- **Owner:** Member 1
- **Depends on:** Existing Plan 1 Phase 2 (model classes must exist)

Static Comparator factory methods for sorting Room, Tenant, and Invoice lists.

## Related Code Files

### Files to Create
- `app/src/main/java/com/example/btlmobile/util/SortHelper.java`

## Implementation Steps

### Step 1: SortHelper.java

```java
package com.example.btlmobile.util;

import com.example.btlmobile.model.Invoice;
import com.example.btlmobile.model.Room;
import com.example.btlmobile.model.Tenant;
import java.util.Comparator;

/**
 * Comparator factory for sorting entity lists.
 * Usage: Collections.sort(rooms, SortHelper.roomByPrice());
 */
public final class SortHelper {
    private SortHelper() {}

    // ===== Room Comparators =====

    /** Sort rooms by price ascending */
    public static Comparator<Room> roomByPriceAsc() {
        return (r1, r2) -> Double.compare(r1.getPrice(), r2.getPrice());
    }

    /** Sort rooms by price descending */
    public static Comparator<Room> roomByPriceDesc() {
        return (r1, r2) -> Double.compare(r2.getPrice(), r1.getPrice());
    }

    /** Sort rooms by floor ascending, then room number */
    public static Comparator<Room> roomByFloor() {
        return (r1, r2) -> {
            int cmp = Integer.compare(r1.getFloor(), r2.getFloor());
            if (cmp != 0) return cmp;
            return r1.getRoomNumber().compareTo(r2.getRoomNumber());
        };
    }

    /** Sort rooms by status: AVAILABLE first, then OCCUPIED, then MAINTENANCE */
    public static Comparator<Room> roomByStatus() {
        return (r1, r2) -> r1.getStatus().compareTo(r2.getStatus());
    }

    // ===== Tenant Comparators =====

    /** Sort tenants by name alphabetically */
    public static Comparator<Tenant> tenantByName() {
        return (t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName());
    }

    /** Sort tenants by move-in date (string compare, format dd/MM/yyyy) */
    public static Comparator<Tenant> tenantByMoveInDate() {
        return (t1, t2) -> {
            // Convert dd/MM/yyyy to comparable format yyyyMMdd
            String d1 = reverseDateFormat(t1.getMoveInDate());
            String d2 = reverseDateFormat(t2.getMoveInDate());
            return d1.compareTo(d2);
        };
    }

    // ===== Invoice Comparators =====

    /** Sort invoices by month descending (newest first) */
    public static Comparator<Invoice> invoiceByMonthDesc() {
        return (i1, i2) -> {
            // Format MM/yyyy → yyyyMM for comparison
            String m1 = reverseMonthFormat(i1.getMonth());
            String m2 = reverseMonthFormat(i2.getMonth());
            return m2.compareTo(m1); // descending
        };
    }

    /** Sort invoices by total amount descending */
    public static Comparator<Invoice> invoiceByAmountDesc() {
        return (i1, i2) -> Double.compare(i2.getTotalAmount(), i1.getTotalAmount());
    }

    // ===== Private helpers =====

    /** Convert "dd/MM/yyyy" → "yyyyMMdd" for string comparison */
    private static String reverseDateFormat(String date) {
        if (date == null || date.length() < 10) return "";
        // dd/MM/yyyy → yyyyMMdd
        return date.substring(6) + date.substring(3, 5) + date.substring(0, 2);
    }

    /** Convert "MM/yyyy" → "yyyyMM" for string comparison */
    private static String reverseMonthFormat(String month) {
        if (month == null || month.length() < 7) return "";
        // MM/yyyy → yyyyMM
        return month.substring(3) + month.substring(0, 2);
    }
}
```

### Step 2: Compile check

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Create `SortHelper.java`
- [ ] Run `./gradlew assembleDebug` — must pass

## Success Criteria

- File compiles without errors
- Under 200 lines (target: ~90 lines)
- `Collections.sort(rooms, SortHelper.roomByPriceAsc())` sorts correctly
- Date string comparison produces correct ordering
- Null-safe date helpers (return empty string on null)

## Integration Notes (for Phase 4-6 team members)

To add sort to a ListActivity:
```java
// In RoomListActivity, after getting rooms from repository:
List<Room> rooms = roomRepository.getAll();
Collections.sort(rooms, SortHelper.roomByPriceAsc());
adapter.updateData(rooms);
```

Can combine with existing Spinner filter or add a separate sort Spinner/menu.
