# Phase 01: Utility Classes

## Overview
- **Priority:** P2
- **Status:** Pending
- **Effort:** 1.5h
- **Owner:** Member 1 (Model layer)
- **Depends on:** Existing plan Phase 1 (project reconfiguration)

6 utility classes providing reusable helpers for all team members.

## Related Code Files

### Files to Create
- `app/src/main/java/com/example/btlmobile/util/AppConstants.java`
- `app/src/main/java/com/example/btlmobile/util/IntentKeys.java`
- `app/src/main/java/com/example/btlmobile/util/NumberFormatHelper.java`
- `app/src/main/java/com/example/btlmobile/util/ValidationHelper.java`
- `app/src/main/java/com/example/btlmobile/util/DateHelper.java`
- `app/src/main/java/com/example/btlmobile/util/DialogHelper.java`

## Implementation Steps

### Step 1: AppConstants.java

```java
package com.example.btlmobile.util;

/**
 * App-wide constants: request codes, format patterns, default values.
 * Eliminates magic numbers throughout the codebase.
 */
public final class AppConstants {
    private AppConstants() {} // Prevent instantiation

    // Activity result request codes
    public static final int REQUEST_ADD = 100;
    public static final int REQUEST_EDIT = 101;

    // Number format
    public static final String CURRENCY_SUFFIX = " đ";
    public static final String LOCALE_COUNTRY = "vi";
    public static final String LOCALE_LANGUAGE = "VN";

    // Date format patterns
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String MONTH_FORMAT = "MM/yyyy";

    // Validation
    public static final int PHONE_LENGTH = 10;
    public static final int CCCD_LENGTH = 12;

    // Default values
    public static final double DEFAULT_ELECTRIC_PRICE = 3500.0; // VND/kWh
    public static final double DEFAULT_WATER_PRICE = 15000.0;   // VND/m³
}
```

### Step 2: IntentKeys.java

```java
package com.example.btlmobile.util;

/**
 * All Intent extra key strings used across Activities.
 * Centralizes keys to prevent typos and enable refactoring.
 */
public final class IntentKeys {
    private IntentKeys() {} // Prevent instantiation

    // Room
    public static final String ROOM_ID = "room_id";
    public static final String ROOM = "room";

    // Tenant
    public static final String TENANT_ID = "tenant_id";
    public static final String TENANT = "tenant";

    // Invoice
    public static final String INVOICE_ID = "invoice_id";
    public static final String INVOICE = "invoice";

    // Service
    public static final String SERVICE_ID = "service_id";
    public static final String SERVICE = "service";

    // Mode flag
    public static final String IS_EDIT_MODE = "is_edit_mode";
}
```

### Step 3: NumberFormatHelper.java

```java
package com.example.btlmobile.util;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Format numbers for Vietnamese locale: currency (VND), percentages, plain numbers.
 */
public final class NumberFormatHelper {
    private NumberFormatHelper() {}

    private static final Locale VN_LOCALE = new Locale("vi", "VN");
    private static final NumberFormat currencyFormatter = NumberFormat.getInstance(VN_LOCALE);
    private static final NumberFormat percentFormatter = NumberFormat.getPercentInstance(VN_LOCALE);

    static {
        percentFormatter.setMaximumFractionDigits(1);
    }

    /** Format as VND currency: "3,500,000 đ" */
    public static String formatVND(double amount) {
        return currencyFormatter.format(amount) + AppConstants.CURRENCY_SUFFIX;
    }

    /** Format plain number with grouping: "3,500,000" */
    public static String formatNumber(double number) {
        return currencyFormatter.format(number);
    }

    /** Format as percentage: "85.5%" */
    public static String formatPercent(double ratio) {
        return percentFormatter.format(ratio);
    }

    /** Format area: "25.0 m²" */
    public static String formatArea(double area) {
        return String.format(VN_LOCALE, "%.1f m²", area);
    }

    /** Parse formatted number string back to double, returns 0 on failure */
    public static double parseNumber(String text) {
        if (text == null || text.trim().isEmpty()) return 0;
        try {
            // Remove non-numeric chars except dot and comma
            String cleaned = text.replaceAll("[^\\d.,]", "")
                                 .replace(".", "")
                                 .replace(",", ".");
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
```

### Step 4: ValidationHelper.java

```java
package com.example.btlmobile.util;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Input validation for forms: required fields, phone, CCCD, email, numeric ranges.
 * Returns error message String or null if valid.
 */
public final class ValidationHelper {
    private ValidationHelper() {}

    /** Check required field not empty */
    public static String validateRequired(String value, String fieldName) {
        if (TextUtils.isEmpty(value) || value.trim().isEmpty()) {
            return fieldName + " không được để trống";
        }
        return null;
    }

    /** Validate Vietnamese phone: starts with 0, 10 digits */
    public static String validatePhone(String phone) {
        String req = validateRequired(phone, "Số điện thoại");
        if (req != null) return req;
        phone = phone.trim();
        if (!phone.matches("^0\\d{9}$")) {
            return "Số điện thoại phải gồm 10 chữ số, bắt đầu bằng 0";
        }
        return null;
    }

    /** Validate CCCD: exactly 12 digits */
    public static String validateCCCD(String cccd) {
        String req = validateRequired(cccd, "Số CCCD");
        if (req != null) return req;
        cccd = cccd.trim();
        if (!cccd.matches("^\\d{12}$")) {
            return "Số CCCD phải gồm 12 chữ số";
        }
        return null;
    }

    /** Validate email format (optional - returns null if empty) */
    public static String validateEmail(String email) {
        if (TextUtils.isEmpty(email) || email.trim().isEmpty()) {
            return null; // Email is optional
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            return "Email không đúng định dạng";
        }
        return null;
    }

    /** Validate positive number */
    public static String validatePositive(String value, String fieldName) {
        String req = validateRequired(value, fieldName);
        if (req != null) return req;
        try {
            double num = Double.parseDouble(value.trim().replace(",", "."));
            if (num <= 0) {
                return fieldName + " phải lớn hơn 0";
            }
        } catch (NumberFormatException e) {
            return fieldName + " phải là số hợp lệ";
        }
        return null;
    }

    /** Validate non-negative integer (for meter readings) */
    public static String validateNonNegativeInt(String value, String fieldName) {
        String req = validateRequired(value, fieldName);
        if (req != null) return req;
        try {
            int num = Integer.parseInt(value.trim());
            if (num < 0) {
                return fieldName + " không được âm";
            }
        } catch (NumberFormatException e) {
            return fieldName + " phải là số nguyên hợp lệ";
        }
        return null;
    }
}
```

### Step 5: DateHelper.java

```java
package com.example.btlmobile.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date formatting utilities for Vietnamese locale.
 */
public final class DateHelper {
    private DateHelper() {}

    private static final Locale VN_LOCALE = new Locale("vi", "VN");

    /** Get current date as "dd/MM/yyyy" */
    public static String getCurrentDate() {
        return new SimpleDateFormat(AppConstants.DATE_FORMAT, VN_LOCALE).format(new Date());
    }

    /** Get current month as "MM/yyyy" */
    public static String getCurrentMonth() {
        return new SimpleDateFormat(AppConstants.MONTH_FORMAT, VN_LOCALE).format(new Date());
    }

    /** Format Date object to "dd/MM/yyyy" */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(AppConstants.DATE_FORMAT, VN_LOCALE).format(date);
    }

    /** Format Date object to custom pattern */
    public static String formatDate(Date date, String pattern) {
        if (date == null) return "";
        return new SimpleDateFormat(pattern, VN_LOCALE).format(date);
    }
}
```

### Step 6: DialogHelper.java

```java
package com.example.btlmobile.util;

import android.content.Context;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Reusable dialog builders: delete confirmation, info messages.
 * Uses Material Design dialogs.
 */
public final class DialogHelper {
    private DialogHelper() {}

    /**
     * Show delete confirmation dialog.
     * @param context Activity context
     * @param itemName Name of item being deleted (e.g. "Phòng P101")
     * @param onConfirm Callback when user confirms deletion
     */
    public static void showDeleteConfirm(Context context, String itemName, Runnable onConfirm) {
        new MaterialAlertDialogBuilder(context)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa " + itemName + "?")
            .setPositiveButton("Xóa", (dialog, which) -> onConfirm.run())
            .setNegativeButton("Hủy", null)
            .show();
    }

    /**
     * Show simple info dialog.
     * @param context Activity context
     * @param title Dialog title
     * @param message Dialog message
     */
    public static void showInfo(Context context, String title, String message) {
        new MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show();
    }
}
```

### Step 7: Compile check

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Create `AppConstants.java`
- [ ] Create `IntentKeys.java`
- [ ] Create `NumberFormatHelper.java`
- [ ] Create `ValidationHelper.java`
- [ ] Create `DateHelper.java`
- [ ] Create `DialogHelper.java`
- [ ] Run `./gradlew assembleDebug` — must pass

## Success Criteria

- All 6 files compile without errors
- `NumberFormatHelper.formatVND(3500000)` → `"3.500.000 đ"`
- `ValidationHelper.validatePhone("0912345678")` → `null` (valid)
- `ValidationHelper.validatePhone("123")` → error message
- `DateHelper.getCurrentDate()` → `"13/03/2026"` format
- `DialogHelper.showDeleteConfirm()` shows Material dialog
- Each file under 200 lines
