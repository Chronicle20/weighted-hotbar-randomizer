---
description: Review codebase for TODOs, missing implementations, and unimplemented functions, then update docs/TODO.md
---

You are a codebase analyst performing a comprehensive review of the Weighted Hotbar Randomizer mod to identify incomplete work.

## Instructions

### Phase 1: Discovery (Run in Parallel)

Launch three parallel exploration tasks:

1. **Find all TODO markers**
   - Search the entire codebase for: TODO, FIXME, XXX, HACK, and similar markers
   - For each finding, note: file path, line number, content, and surrounding context
   - Check all file types: Java, JSON, YAML, properties, etc.

2. **Find unimplemented/stub code**
   - Search for patterns indicating incomplete implementations:
     - Methods with empty bodies or placeholder implementations
     - "not implemented" or "NotImplemented" strings/exceptions
     - Methods that throw UnsupportedOperationException
     - Methods that only log warnings about missing implementation
     - Commented-out code blocks that might indicate incomplete work
   - Focus on Java files
   - Note the file, method name, and what appears to be missing

3. **Analyze project structure**
   - Identify the different packages and modules in the codebase
   - Understand the project structure and component purposes
   - This enables organizing findings by area

### Phase 2: Analysis

After discovery completes:

1. **Categorize findings by area**
   - Group all TODOs and incomplete implementations by package/component
   - Identify cross-cutting concerns

2. **Prioritize findings**
   - **Critical**: Core mod functionality broken or missing
   - **High Priority**: Features incomplete but not blocking basic functionality
   - **Medium Priority**: Quality/polish issues, performance optimizations
   - **Low Priority**: Minor cosmetic issues, documentation

3. **Identify patterns**
   - Note areas with concentrated incomplete work
   - Identify systemic issues vs one-off TODOs

### Phase 3: Update TODO.md

Read the existing `docs/TODO.md` file, then update it with the comprehensive findings.

**Structure for docs/TODO.md:**

```markdown
# Weighted Hotbar Randomizer — TODO

This document tracks planned features and improvements for the mod.

---

## Priority Summary

### Critical (Core Functionality)
- [ ] **Item** - Brief description

### High Priority (Feature Incomplete)
- [ ] **Item** - Brief description

---

## By Component

### Component Name
- [ ] Description of TODO (`file/path.java:123`)
- [ ] Description of TODO (`file/path.java:456`)

[Continue for all components]

---

## Notes

- Summary statistics
- Important context
```

**Guidelines for updating:**
- Preserve any manually-curated items that aren't from inline TODOs
- Include file path and line number for each inline TODO
- Use bold for critical/blocking items
- Group related items under subsections when a component has many items
- Keep descriptions concise but informative
- Add summary statistics at the end (total TODOs, most concentrated areas)

### Output

After updating the file, provide a brief summary:
- Total number of TODOs/incomplete items found
- Top 3-5 most critical items
- Components with the most incomplete work
- Any new items added since the last review (if determinable)
