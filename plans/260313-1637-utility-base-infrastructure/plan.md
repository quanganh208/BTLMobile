---
title: "Utility & Base Infrastructure"
description: "Implement utility classes and BaseActivity — zero conflict with main plan"
status: pending
priority: P2
effort: 2h
branch: main
tags: [android, java, util, infrastructure]
created: 2026-03-13
---

# Utility & Base Infrastructure Plan

## Summary

7 new Java files (6 util + 1 BaseActivity) providing reusable infrastructure for all team members. Zero conflict with existing plan `260313-1625-room-rental-management-app`.

**Brainstorm report:** [brainstorm-260313-1637-utility-base-infrastructure.md](../reports/brainstorm-260313-1637-utility-base-infrastructure.md)

## Phases

| # | Phase | Effort | Status | File |
|---|-------|--------|--------|------|
| 1 | Utility Classes (6 files) | 1.5h | pending | [phase-01](phase-01-implement-utility-classes.md) |
| 2 | BaseActivity | 0.5h | pending | [phase-02](phase-02-implement-base-activity.md) |

## Dependencies

```
Existing Phase 1 (Reconfigure Project) → This Phase 1 → This Phase 2
```

**IMPORTANT:** Must wait for existing plan Phase 1 (Kotlin→Java reconfiguration) to complete first.

## File Ownership

All files are NEW. No overlap with existing plan:

| File | Package |
|------|---------|
| `AppConstants.java` | `util/` |
| `IntentKeys.java` | `util/` |
| `NumberFormatHelper.java` | `util/` |
| `ValidationHelper.java` | `util/` |
| `DateHelper.java` | `util/` |
| `DialogHelper.java` | `util/` |
| `BaseActivity.java` | `controller/` |
