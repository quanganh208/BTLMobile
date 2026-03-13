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
