---
title: "Room Rental Management Android App"
description: "Java+XML MVC app for landlords to manage rooms, tenants, invoices, and services with in-memory storage"
status: pending
priority: P1
effort: 16h
branch: main
tags: [android, java, xml, mvc, crud, recycler-view]
created: 2026-03-13
---

# Room Rental Management App - Implementation Plan

## Summary

Android app (Java + XML, MVC) with 4 entities (Room, Tenant, Invoice, Service), 12 Activities, 16 XML layouts, in-memory ArrayList storage via Singleton repositories. Sample data pre-populated on launch.

**Brainstorm report:** [brainstorm-260313-1618-room-rental-management-app.md](../reports/brainstorm-260313-1618-room-rental-management-app.md)

## Phases

| # | Phase | Effort | Status | Owner | File |
|---|-------|--------|--------|-------|------|
| 1 | Reconfigure Project (Kotlin+Compose → Java+XML) | 1h | pending | All | [phase-01](phase-01-reconfigure-project.md) |
| 2 | Model Layer (POJOs, Enums, Repositories, SampleData) | 2h | pending | Member 1 | [phase-02](phase-02-implement-model-layer.md) |
| 3 | View Layer (16 XML layouts + resources) | 4h | pending | Member 2 | [phase-03](phase-03-implement-view-layer.md) |
| 4 | Controller A: Room + Tenant Activities & Adapters | 3h | pending | Member 3 | [phase-04](phase-04-implement-controller-room-tenant.md) |
| 5 | Controller B: Invoice + Service Activities & Adapters | 3h | pending | Member 4 | [phase-05](phase-05-implement-controller-invoice-service.md) |
| 6 | Dashboard + Search + Statistics | 2h | pending | Member 4 | [phase-06](phase-06-implement-dashboard-search-statistics.md) |
| 7 | Integration & Testing | 1h | pending | All | [phase-07](phase-07-integration-testing.md) |

## Dependencies

```
Phase 1 → Phase 2 + Phase 3 (parallel)
Phase 2 → Phase 4 + Phase 5 (parallel)
Phase 3 → Phase 4 + Phase 5 (parallel)
Phase 4 + Phase 5 → Phase 6
Phase 6 → Phase 7
```

## Team Division

| Member | Layer | Scope |
|--------|-------|-------|
| Member 1 | Model | `model/**`, `util/SampleData.java` |
| Member 2 | View | `res/layout/**`, `res/values/**` |
| Member 3 | Controller A | Room + Tenant Activities + Adapters |
| Member 4 | Controller B | Invoice + Service + Dashboard + Search + Statistics + Adapters |

## Key Constraints

- Java + XML only (NO Kotlin, NO Compose)
- In-memory ArrayList storage (data resets on app restart)
- MVC architecture with Activity-based navigation
- RecyclerView with ViewHolder pattern for all lists
- Material Components design system
