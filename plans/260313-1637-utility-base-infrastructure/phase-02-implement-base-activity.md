# Phase 02: BaseActivity

## Overview
- **Priority:** P2
- **Status:** Pending
- **Effort:** 0.5h
- **Owner:** Member 1 (Model layer)
- **Depends on:** This plan Phase 1 (utility classes)

Abstract Activity class with common toolbar + back navigation setup. All Phase 4-6 Activities can extend this.

## Related Code Files

### Files to Create
- `app/src/main/java/com/example/btlmobile/controller/BaseActivity.java`

## Implementation Steps

### Step 1: BaseActivity.java

```java
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
```

**Key design decisions:**
- `setupToolbar()` is a method call, NOT forced in `onCreate()` → subclasses have full control
- Looks for `R.id.toolbar` — all XML layouts that use BaseActivity must include a Toolbar with this ID
- Null-safe: if toolbar not found, silently skips (doesn't crash)
- Handles back button via `onOptionsItemSelected` → no need to repeat in every Activity

### Step 2: Compile check

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Create `BaseActivity.java`
- [ ] Run `./gradlew assembleDebug` — must pass

## Success Criteria

- File compiles without errors
- Under 200 lines (target: ~60 lines)
- Subclass pattern works: extend + call `setupToolbar()` after `setContentView()`
- Back button navigates to previous Activity
- Null-safe when toolbar not present in layout

## Integration Notes (for Phase 4-6 team members)

To use BaseActivity in your Activities:
1. Extend `BaseActivity` instead of `AppCompatActivity`
2. Include `<androidx.appcompat.widget.Toolbar android:id="@+id/toolbar" .../>` in your XML layout
3. Call `setupToolbar("Title", true)` after `setContentView()`
4. Back button handling is automatic — no extra code needed
