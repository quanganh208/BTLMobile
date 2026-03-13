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
