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
