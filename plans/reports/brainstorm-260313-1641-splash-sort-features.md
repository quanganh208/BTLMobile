# Brainstorm: Splash + Sort Features

**Date:** 2026-03-13
**Status:** Agreed
**Member:** Member 1 (Model layer)

---

## Problem Statement

Additional features that don't conflict with:
- Plan 1: `260313-1625-room-rental-management-app` (main CRUD app)
- Plan 2: `260313-1637-utility-base-infrastructure` (utilities + BaseActivity)

**Time budget:** 1-2 hours

---

## Agreed Scope

| Feature | Effort | Files |
|---------|--------|-------|
| SplashActivity | 20 min | `SplashActivity.java` + `activity_splash.xml` |
| SortHelper | 15 min | `SortHelper.java` |

**Dropped:** Contract entity (3-5h, doesn't fit time budget)

---

## Conflict Analysis

| Existing Plan | Conflict? |
|--------------|-----------|
| Plan 1 Phase 1 (Reconfigure) | NO — new files only |
| Plan 1 Phase 2 (Model) | NO — SortHelper uses models but doesn't modify them |
| Plan 1 Phase 3 (View) | NO — splash XML is new layout, not in Plan 1 |
| Plan 1 Phase 4-6 (Controllers) | NO — SplashActivity is independent |
| Plan 2 (Utilities) | NO — SortHelper in same util/ package but different file |

**Manifest note:** SplashActivity needs to be registered as LAUNCHER and DashboardActivity changed to non-launcher. This touches `AndroidManifest.xml` which Plan 1 Phase 1 also touches. **Solution:** implement AFTER Phase 1 completes.

---

## Technical Decisions

### SplashActivity
- 2-second delay with `Handler.postDelayed()` → navigate to DashboardActivity
- Full-screen, no toolbar, custom theme with no action bar
- Shows app logo (can use text/icon placeholder) + app name
- Becomes the new LAUNCHER activity in AndroidManifest

### SortHelper
- Static Comparator factory methods for Room, Tenant, Invoice
- Used by Phase 4-6 ListActivities to add sort functionality
- Returns `Comparator<T>` — caller applies to list before updating adapter

---

## Success Criteria

- [ ] App launches to SplashActivity → auto-navigates to Dashboard after 2s
- [ ] SortHelper provides sort-by methods for all 3 main entities
- [ ] Both files compile, app doesn't crash
- [ ] Zero conflict with Plan 1 and Plan 2 files
