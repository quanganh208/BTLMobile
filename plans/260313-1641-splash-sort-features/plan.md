---
title: "Splash Screen + Sort Helper"
description: "SplashActivity launcher screen and SortHelper utility — zero conflict with main plan"
status: pending
priority: P3
effort: 35m
branch: main
tags: [android, java, ui, util]
created: 2026-03-13
---

# Splash Screen + Sort Helper Plan

## Summary

2 new Java files + 1 XML layout + 1 theme modification. SplashActivity becomes app launcher; SortHelper provides Comparators for list sorting.

**Brainstorm report:** [brainstorm-260313-1641-splash-sort-features.md](../reports/brainstorm-260313-1641-splash-sort-features.md)

## Phases

| # | Phase | Effort | Status | File |
|---|-------|--------|--------|------|
| 1 | SplashActivity | 20m | pending | [phase-01](phase-01-implement-splash-activity.md) |
| 2 | SortHelper | 15m | pending | [phase-02](phase-02-implement-sort-helper.md) |

## Dependencies

```
Existing Plan 1 Phase 1 (Reconfigure) → This Phase 1 (Splash needs Java+XML setup)
Existing Plan 1 Phase 2 (Models) → This Phase 2 (SortHelper uses Room/Tenant/Invoice classes)
```

## File Ownership (zero overlap)

| File | Package | Touches existing? |
|------|---------|-------------------|
| `SplashActivity.java` | `controller/` | NO |
| `activity_splash.xml` | `res/layout/` | NO |
| `SortHelper.java` | `util/` | NO |
| `AndroidManifest.xml` | — | YES (add SplashActivity, change launcher) |
| `themes.xml` | `res/values/` | YES (add splash theme) |

**Note:** Manifest + themes touched by Plan 1 Phase 1 too. Implement AFTER Phase 1 completes to avoid conflict.
