# Phase 01: SplashActivity

## Overview
- **Priority:** P3
- **Status:** Pending
- **Effort:** 20m
- **Owner:** Member 1
- **Depends on:** Existing Plan 1 Phase 1 (project reconfigured to Java+XML)

Full-screen splash screen with app name, displayed 2 seconds before navigating to DashboardActivity.

## Related Code Files

### Files to Create
- `app/src/main/java/com/example/btlmobile/controller/SplashActivity.java`
- `app/src/main/res/layout/activity_splash.xml`

### Files to Modify
- `app/src/main/AndroidManifest.xml` — register SplashActivity as LAUNCHER, remove launcher from DashboardActivity
- `app/src/main/res/values/themes.xml` — add splash theme (fullscreen, no action bar)

## Implementation Steps

### Step 1: Add splash theme in themes.xml

Append inside `<resources>`:
```xml
<!-- Fullscreen splash theme -->
<style name="Theme.BTLMobile.Splash" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <item name="android:windowFullscreen">true</item>
    <item name="android:windowBackground">@color/primary</item>
    <item name="colorPrimary">@color/primary</item>
    <item name="colorOnPrimary">@color/white</item>
</style>
```

### Step 2: Create activity_splash.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/primary">

    <!-- App icon placeholder (use ImageView with ic_launcher or custom icon) -->
    <ImageView
        android:id="@+id/ivLogo"
        android:layout_width="120dp"
        android:layout_height="120dp"
        android:src="@mipmap/ic_launcher"
        android:contentDescription="@string/app_name"
        app:layout_constraintBottom_toTopOf="@id/tvAppName"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_chainStyle="packed" />

    <!-- App name -->
    <TextView
        android:id="@+id/tvAppName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:text="@string/app_name"
        android:textColor="@color/white"
        android:textSize="28sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toTopOf="@id/tvSubtitle"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/ivLogo" />

    <!-- Subtitle -->
    <TextView
        android:id="@+id/tvSubtitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="8dp"
        android:text="Quản lý nhà trọ dễ dàng"
        android:textColor="@color/primary_light"
        android:textSize="16sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/tvAppName" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

### Step 3: Create SplashActivity.java

```java
package com.example.btlmobile.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btlmobile.R;

/**
 * Splash screen displayed for 2 seconds on app launch.
 * Navigates to DashboardActivity automatically.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY_MS = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            finish(); // Remove splash from back stack
        }, SPLASH_DELAY_MS);
    }
}
```

### Step 4: Update AndroidManifest.xml

Move LAUNCHER intent-filter from DashboardActivity to SplashActivity:

```xml
<!-- Splash becomes launcher -->
<activity
    android:name=".controller.SplashActivity"
    android:exported="true"
    android:theme="@style/Theme.BTLMobile.Splash">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<!-- Dashboard is no longer launcher -->
<activity
    android:name=".controller.DashboardActivity"
    android:exported="false"
    android:label="@string/app_name"
    android:theme="@style/Theme.BTLMobile" />
```

### Step 5: Compile check

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Add `Theme.BTLMobile.Splash` style in `themes.xml`
- [ ] Create `activity_splash.xml`
- [ ] Create `SplashActivity.java`
- [ ] Update `AndroidManifest.xml` (launcher change)
- [ ] Run `./gradlew assembleDebug` — must pass

## Success Criteria

- App launches to SplashActivity (fullscreen, blue background)
- After 2 seconds, auto-navigates to DashboardActivity
- Pressing back from Dashboard does NOT return to Splash (finish() called)
- SplashActivity under 200 lines (target: ~25 lines)
