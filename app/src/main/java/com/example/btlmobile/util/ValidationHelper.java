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
