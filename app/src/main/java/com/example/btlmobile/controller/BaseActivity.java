package com.example.btlmobile.controller;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.btlmobile.R;

/**
 * Base Activity providing common toolbar setup and back navigation.
 * Subclasses call setupToolbar() after setContentView().
 *
 * Usage:
 *   public class RoomListActivity extends BaseActivity {
 *       @Override
 *       protected void onCreate(Bundle savedInstanceState) {
 *           super.onCreate(savedInstanceState);
 *           setContentView(R.layout.activity_room_list);
 *           setupToolbar("Danh sách phòng", true);
 *       }
 *   }
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Setup toolbar with title and optional back button.
     * @param title Toolbar title text
     * @param showBack Show back/up arrow button
     */
    protected void setupToolbar(String title, boolean showBack) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) return;

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
        }
    }

    /**
     * Setup toolbar with string resource title.
     */
    protected void setupToolbar(@StringRes int titleRes, boolean showBack) {
        setupToolbar(getString(titleRes), showBack);
    }

    /**
     * Handle toolbar back button press.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
