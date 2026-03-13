# Brainstorm: Utility & Base Infrastructure

**Date:** 2026-03-13
**Status:** Agreed
**Member:** Member 1 (Model layer owner)

---

## Problem Statement

Implement foundational utility classes and BaseActivity that DON'T conflict with existing plan `260313-1625-room-rental-management-app`. These provide reusable infrastructure for all team members.

**Constraints:**
- Java + XML only
- Must not touch files owned by Phase 2-7
- Must be usable by Phase 4, 5, 6 Activities and Adapters
- Follow MVC architecture

---

## Agreed Scope

### A. Utility Classes (in `util/` package — Member 1 ownership)

| Class | Purpose |
|-------|---------|
| `AppConstants.java` | Request codes, format patterns, app-wide constants |
| `IntentKeys.java` | All Intent extra key strings — eliminates magic strings |
| `NumberFormatHelper.java` | Format VND currency, percentages, numbers |
| `ValidationHelper.java` | Validate phone, CCCD, email, required fields, numeric ranges |
| `DateHelper.java` | Format dates, get current date string, parse date strings |
| `DialogHelper.java` | Reusable AlertDialog for delete confirmation, info dialogs |

### B. BaseActivity (in `controller/` package — shared infrastructure)

| Class | Purpose |
|-------|---------|
| `BaseActivity.java` | Abstract Activity: setup toolbar + back navigation + common init |

---

## Architecture Fit

```
com.example.btlmobile/
├── util/
│   ├── AppConstants.java        ← NEW
│   ├── IntentKeys.java          ← NEW
│   ├── NumberFormatHelper.java  ← NEW
│   ├── ValidationHelper.java   ← NEW
│   ├── DateHelper.java          ← NEW
│   ├── DialogHelper.java        ← NEW
│   └── SampleData.java          (existing in plan Phase 2)
├── controller/
│   ├── BaseActivity.java        ← NEW
│   └── ... (Phase 4-6 Activities extend BaseActivity)
```

---

## Conflict Analysis

| Existing Plan Phase | Conflict? | Notes |
|---------------------|-----------|-------|
| Phase 1 (Reconfigure) | NO | Only touches build.gradle, project config |
| Phase 2 (Model) | NO | Models + repos + SampleData — different files |
| Phase 3 (View) | NO | XML layouts only |
| Phase 4 (Room+Tenant) | NO | Activities can optionally extend BaseActivity |
| Phase 5 (Invoice+Service) | NO | Activities can optionally extend BaseActivity |
| Phase 6 (Dashboard+Search+Stats) | NO | Activities can optionally extend BaseActivity |
| Phase 7 (Testing) | NO | Testing phase |

**Zero conflict confirmed.** All files are NEW and don't touch any planned files.

---

## Benefits for Team

- **Phase 4-6 owners** use `IntentKeys.ROOM_ID` instead of `"room_id"` magic strings
- **Phase 4-6 owners** call `DialogHelper.showDeleteConfirm()` instead of duplicating AlertDialog code
- **Phase 4-6 owners** call `NumberFormatHelper.formatVND(price)` instead of inline NumberFormat
- **Phase 4-6 owners** call `ValidationHelper.isPhoneValid(phone)` for consistent validation
- **Phase 4-6 owners** extend `BaseActivity` → toolbar + back nav setup in 1 line

---

## Risks

| Risk | Mitigation |
|------|-----------|
| BaseActivity too opinionated | Keep minimal: only toolbar + back. No forced patterns |
| Util methods unused if team ignores | Share with team early. Methods are optional |
| Depends on Phase 1 (Java+XML setup) | Must wait for Phase 1 completion first |

---

## Success Criteria

- [ ] All 7 Java files compile successfully
- [ ] No conflict with existing plan files
- [ ] Team members can import and use utilities
- [ ] BaseActivity reduces boilerplate in Activity subclasses
