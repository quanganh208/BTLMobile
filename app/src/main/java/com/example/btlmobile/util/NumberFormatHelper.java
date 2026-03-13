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
