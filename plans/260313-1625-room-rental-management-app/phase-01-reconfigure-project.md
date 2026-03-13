# Phase 01: Reconfigure Project (Kotlin+Compose → Java+XML)

## Overview
- **Priority:** P1 (blocker for all other phases)
- **Status:** Pending
- **Effort:** 1h
- **Owner:** Any member (setup task)

Convert the fresh Kotlin+Compose Android project to Java+XML with Material Components.

## Requirements
- Remove all Kotlin and Compose references from build config
- Add Java-compatible dependencies (AppCompat, Material, RecyclerView, CardView, ConstraintLayout)
- Delete Kotlin source files, create Java package structure
- Update AndroidManifest to point to DashboardActivity
- Update theme to Material Components
- Project must compile with `./gradlew assembleDebug`

## Related Code Files

### Files to Modify
- `build.gradle.kts` (root) — remove kotlin-compose plugin
- `app/build.gradle.kts` — full rewrite: remove Compose, add Java deps
- `gradle/libs.versions.toml` — replace Compose/Kotlin entries with Material/RecyclerView/CardView/ConstraintLayout
- `app/src/main/AndroidManifest.xml` — change launcher activity to `.controller.DashboardActivity`
- `app/src/main/res/values/themes.xml` — switch to `Theme.MaterialComponents.DayNight.NoActionBar`
- `app/src/main/res/values/colors.xml` — update with app color palette
- `app/src/main/res/values/strings.xml` — update app name to "Quản Lý Phòng Trọ"

### Files to Delete
- `app/src/main/java/com/example/btlmobile/MainActivity.kt`
- `app/src/main/java/com/example/btlmobile/ui/theme/Color.kt`
- `app/src/main/java/com/example/btlmobile/ui/theme/Theme.kt`
- `app/src/main/java/com/example/btlmobile/ui/theme/Type.kt`
- `app/src/test/java/com/example/btlmobile/ExampleUnitTest.kt`
- `app/src/androidTest/java/com/example/btlmobile/ExampleInstrumentedTest.kt`

### Directories to Create
```
app/src/main/java/com/example/btlmobile/
├── model/
│   ├── enums/
│   └── repository/
├── controller/
│   ├── room/
│   ├── tenant/
│   ├── invoice/
│   └── service/
├── adapter/
└── util/
```

## Implementation Steps

### Step 1: Update `gradle/libs.versions.toml`

Replace entire content with:
```toml
[versions]
agp = "9.1.0"
appcompat = "1.7.0"
material = "1.12.0"
constraintlayout = "2.2.1"
recyclerview = "1.4.0"
cardview = "1.0.0"
activity = "1.10.1"
junit = "4.13.2"
junitVersion = "1.1.5"
espressoCore = "3.5.1"

[libraries]
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
androidx-constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
androidx-recyclerview = { group = "androidx.recyclerview", name = "recyclerview", version.ref = "recyclerview" }
androidx-cardview = { group = "androidx.cardview", name = "cardview", version.ref = "cardview" }
androidx-activity = { group = "androidx.activity", name = "activity", version.ref = "activity" }
junit = { group = "junit", name = "junit", version.ref = "junit" }
androidx-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
androidx-espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
```

### Step 2: Update root `build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
}
```

### Step 3: Rewrite `app/build.gradle.kts`

```kotlin
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.btlmobile"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.btlmobile"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
```

### Step 4: Delete Kotlin files

```bash
rm app/src/main/java/com/example/btlmobile/MainActivity.kt
rm -rf app/src/main/java/com/example/btlmobile/ui/
rm app/src/test/java/com/example/btlmobile/ExampleUnitTest.kt
rm app/src/androidTest/java/com/example/btlmobile/ExampleInstrumentedTest.kt
```

### Step 5: Create Java directory structure

```bash
BASE=app/src/main/java/com/example/btlmobile
mkdir -p $BASE/model/enums
mkdir -p $BASE/model/repository
mkdir -p $BASE/controller/room
mkdir -p $BASE/controller/tenant
mkdir -p $BASE/controller/invoice
mkdir -p $BASE/controller/service
mkdir -p $BASE/adapter
mkdir -p $BASE/util
```

### Step 6: Create placeholder DashboardActivity.java

Create `app/src/main/java/com/example/btlmobile/controller/DashboardActivity.java`:
```java
package com.example.btlmobile.controller;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Layout will be set in Phase 3
    }
}
```

### Step 7: Update AndroidManifest.xml

Change launcher activity from `.MainActivity` to `.controller.DashboardActivity`:
```xml
<activity
    android:name=".controller.DashboardActivity"
    android:exported="true"
    android:label="@string/app_name"
    android:theme="@style/Theme.BTLMobile">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### Step 8: Update `themes.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.BTLMobile" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <item name="colorPrimary">@color/primary</item>
        <item name="colorPrimaryVariant">@color/primary_dark</item>
        <item name="colorOnPrimary">@color/white</item>
        <item name="colorSecondary">@color/accent</item>
        <item name="colorOnSecondary">@color/white</item>
    </style>
</resources>
```

### Step 9: Update `colors.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="primary">#1976D2</color>
    <color name="primary_dark">#1565C0</color>
    <color name="primary_light">#BBDEFB</color>
    <color name="accent">#FF9800</color>
    <color name="white">#FFFFFFFF</color>
    <color name="black">#FF000000</color>
    <color name="text_primary">#212121</color>
    <color name="text_secondary">#757575</color>
    <color name="divider">#BDBDBD</color>
    <color name="background">#F5F5F5</color>
    <color name="status_available">#4CAF50</color>
    <color name="status_occupied">#F44336</color>
    <color name="status_maintenance">#FF9800</color>
    <color name="status_paid">#4CAF50</color>
    <color name="status_unpaid">#F44336</color>
    <color name="card_background">#FFFFFF</color>
</resources>
```

### Step 10: Update `strings.xml`

```xml
<resources>
    <string name="app_name">Quản Lý Phòng Trọ</string>
</resources>
```

### Step 11: Compile and verify

```bash
./gradlew assembleDebug
```

## Todo List

- [ ] Update `gradle/libs.versions.toml`
- [ ] Update root `build.gradle.kts`
- [ ] Rewrite `app/build.gradle.kts`
- [ ] Delete all Kotlin source files
- [ ] Create Java directory structure
- [ ] Create placeholder `DashboardActivity.java`
- [ ] Update `AndroidManifest.xml`
- [ ] Update `themes.xml`
- [ ] Update `colors.xml`
- [ ] Update `strings.xml`
- [ ] Run `./gradlew assembleDebug` — must pass

## Success Criteria

- `./gradlew assembleDebug` completes without errors
- No Kotlin files remain in source tree
- Java package structure created with all required subdirectories
- Theme uses Material Components
- App launches (shows empty DashboardActivity)
