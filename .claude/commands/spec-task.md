---
description: Turn a backlog idea into a structured feature spec with a PRD and supporting materials
argument-hint: Brief description of the feature idea (e.g., "weight persistence", "hotbar preset profiles")
---

You are a product-minded engineer helping turn a rough backlog idea into a well-structured feature spec for a Minecraft Fabric mod. The idea is: **$ARGUMENTS**

## Process

### Step 1 — Determine Task Number

Look at existing folders in `docs/tasks/` to find the next available task number. Task folders use the format `task-NNN-slug` (e.g., `task-001-init`, `task-002-config-screen`). Derive the slug from the idea name (lowercase, hyphenated, max 3-4 words).

### Step 2 — Understand Context

Before writing anything, gather context:

1. Read project docs in `docs/` and `CLAUDE.md` to understand what the mod looks like today
2. Scan the codebase to understand what already exists and what's relevant to this feature
3. Check `docs/TODO.md` (if it exists) for any related items
4. List existing task folders in `docs/tasks/` and scan their PRD titles/overviews for overlap — only read a full task PRD if it looks directly related to this feature
5. Identify which packages/components this feature would touch

### Step 3 — Collaborate on the Spec

Do NOT immediately generate all files. Instead, present the user with:

1. **Scope summary** — Your understanding of the feature in 2-3 sentences
2. **Key questions** — Anything ambiguous or requiring a decision (list 3-7 questions)
3. **Proposed boundaries** — What's in scope vs explicitly out of scope
4. **Affected components** — Which packages/classes need changes and why

Wait for the user to answer questions and confirm scope before proceeding.

### Step 4 — Generate the Task Folder

Once scope is confirmed, create the task folder at `docs/tasks/task-NNN-slug/` with these files:

#### `prd.md` — Product Requirements Document

Use this structure:

```markdown
# [Feature Name] — Product Requirements Document

Version: v1
Status: Draft
Created: YYYY-MM-DD
---

## 1. Overview

[What this feature is and why it matters — 2-3 paragraphs]

## 2. Goals

Primary goals:
- [list]

Non-goals:
- [list]

## 3. User Stories

- As a player, I want to [action] so that [outcome]
- [repeat]

## 4. Functional Requirements

[Organized by capability area. Be specific and testable.]

## 5. User Interface

[Any GUI screens, HUD elements, keybindings, or config options]

## 6. Data / Persistence

[Config files, saved state, NBT data, or any data that needs to persist]

## 7. Compatibility

[Minecraft version targets, Fabric API dependencies, mod compatibility considerations]

## 8. Non-Functional Requirements

[Performance impact, client-side enforcement, memory usage considerations]

## 9. Open Questions

[Anything still unresolved after the conversation]

## 10. Acceptance Criteria

[Concrete checklist of what "done" looks like]
```

#### Supporting Materials (create as needed)

Depending on the feature, also create relevant supporting files:

- `ui-mockup.md` — If the feature involves GUI screens or HUD elements
- `config-schema.md` — If configuration options are non-trivial
- `risks.md` — If there are significant technical risks worth documenting separately

Only create supporting files that add value beyond what's in the PRD. Don't create empty or repetitive files.

### Step 5 — Summary

After generating files, present:

1. List of files created with brief description of each
2. Suggested next step (e.g., "Run `/dev-docs task-NNN-slug` to create an implementation plan")

## Quality Standards

- Requirements must be specific and testable — avoid vague language like "should be fast"
- Respect existing code patterns and project conventions
- Consider Minecraft version compatibility and Fabric API requirements
- Keep the PRD self-contained — a developer should be able to implement from it without needing to ask clarifying questions
- Remember this is client-side only — no server-side requirements
