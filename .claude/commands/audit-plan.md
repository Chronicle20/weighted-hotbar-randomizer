---
description: Audit an implementation plan to verify all tasks were completed, nothing was skipped/deferred without approval, and the implementation adheres to project conventions
argument-hint: Path to the plan tasks file (e.g., "docs/tasks/[task-name]/tasks.md")
---

You are an implementation plan auditor for the Weighted Hotbar Randomizer mod. Your job is to verify that the implementation described in a plan was faithfully executed and nothing was silently skipped or deferred.

Audit the plan at: $ARGUMENTS

## Instructions

### Step 1: Load the Plan

1. Treat `$ARGUMENTS` as a path relative to the project root.
2. Read the tasks file. If a corresponding `plan.md` and `context.md` exist in the same directory, read those too.
3. Parse every task item (lines matching `- [ ]` or `- [x]`). Record total count, completed count, and incomplete count.

### Step 2: Determine Scope

1. From the plan and context files, identify:
   - Which source files/packages were expected to be modified.
   - Which files were expected to be created or changed.
2. Use `git log` and `git diff main...HEAD` (or the appropriate base branch) to identify what was actually changed on the current branch.

### Step 3: Task Completion Audit

For each task in the plan:

1. **Check if the task was implemented.** Look for evidence in the git diff, file system, or build artifacts.
2. **Classify each task** as one of:
   - `DONE` — Evidence found that the task was completed.
   - `PARTIAL` — Some but not all acceptance criteria met.
   - `SKIPPED` — No evidence of implementation found and task is unchecked.
   - `DEFERRED` — Explicitly marked as deferred in plan or conversation.
   - `NOT_APPLICABLE` — Task is no longer relevant (explain why).
3. For `PARTIAL` or `SKIPPED` tasks, note what specifically is missing.

### Step 4: Code Quality Check

Check the changed code for:
- Mixin best practices (narrow targets, minimal injection scope)
- Client-side only enforcement (no server-side code leaking in)
- Proper use of Fabric API patterns
- Configuration handling correctness
- No hardcoded values that should be configurable

### Step 5: Build Verification

1. Run `./gradlew build` from the project root.
2. Record pass/fail status.

### Step 6: Produce Audit Report

Create the audit report at `docs/tasks/[task-name]/audit.md` where `[task-name]` is derived from the tasks file path.

The report must include:

```markdown
# Plan Audit — [task-name]

**Plan Path:** [path to tasks file]
**Audit Date:** YYYY-MM-DD
**Branch:** [current branch]
**Base Branch:** main

## Executive Summary

[2-4 sentences: overall completion rate, any critical gaps, code quality status]

## Task Completion

| # | Task | Status | Evidence / Notes |
|---|------|--------|-----------------|
| 1.1 | Description | DONE/PARTIAL/SKIPPED | file:line or commit ref |
| ... | ... | ... | ... |

**Completion Rate:** X/Y tasks (Z%)
**Skipped without approval:** [count]
**Partial implementations:** [count]

## Skipped / Deferred Tasks

[For each SKIPPED or PARTIAL task, explain what is missing and the potential impact]

## Code Quality

### Passes
[List checks that pass with brief evidence]

### Issues
[For each issue:]
- **Rule:** [which convention]
- **File:** [path:line]
- **Issue:** [what's wrong]
- **Severity:** high/medium/low
- **Fix:** [recommended action]

## Build Results

| Check | Status | Notes |
|-------|--------|-------|
| `./gradlew build` | PASS/FAIL | error details if any |

## Overall Assessment

- **Plan Adherence:** [FULL / MOSTLY_COMPLETE / INCOMPLETE]
- **Code Quality:** [GOOD / MINOR_ISSUES / MAJOR_ISSUES]
- **Recommendation:** [READY_TO_MERGE / NEEDS_FIXES / NEEDS_REVIEW]

## Action Items

[Numbered list of required fixes before the plan can be considered complete]
```

## Important Rules

- Do NOT make any code changes. This is a read-only audit.
- Every finding must include evidence (file path, line number, git commit, or specific code reference).
- If a task's completion status is ambiguous, mark it `PARTIAL` and explain what you found vs. what was expected.
- Be thorough but fair — if a task was completed in a slightly different way than described but achieves the same goal, mark it `DONE` with a note.
- Focus on NEW or MODIFIED code only, not pre-existing code.
