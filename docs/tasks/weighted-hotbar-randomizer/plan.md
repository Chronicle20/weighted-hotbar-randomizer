# Weighted Hotbar Randomizer — Implementation Plan

Last Updated: 2026-04-10

## Executive Summary

Build a Fabric client mod for Minecraft 26.1 that automatically rotates the selected hotbar slot after block placement using weighted randomness based on stack size. The mod includes keybind toggle, client commands, JSON config persistence, and full CI/CD infrastructure (PR validation, snapshot builds, tagged releases).

## Current State Analysis

- Empty repository with only `.gitignore` and `README.md`
- No Gradle project structure, no source code, no CI/CD
- Target: Minecraft 26.1 (unobfuscated — no mappings needed)
- Toolchain: Java 25, Gradle 9.4.0, Fabric Loom 1.15, Fabric Loader 0.19.1, Fabric API 0.145.4+26.1.1

## Proposed Future State

A fully functional Fabric client mod with:
- Weighted random hotbar slot selection after block placement
- Configurable slot range (1–9), toggle keybind, client commands
- JSON config persistence across sessions (default: disabled)
- Action bar and chat feedback
- Three GitHub Actions workflows for PR validation, snapshots, and tagged releases
- Self-describing artifact naming: `weighted-hotbar-randomizer-<version>+mc.<mc-version>.jar`

## Implementation Phases

### Phase 1: Build Infrastructure (Effort: M)

Set up the Gradle project with Fabric Loom 1.15 targeting MC 26.1.

**Tasks:**
1. Update `.gitignore` with Gradle, IDE, and Minecraft run directory patterns
2. Create `gradle/wrapper/gradle-wrapper.properties` for Gradle 9.4.0
3. Generate `gradlew` and `gradlew.bat` wrapper scripts
4. Create `gradle.properties` with all version constants
5. Create `settings.gradle` with Fabric maven and plugin management
6. Create `build.gradle` with Loom 1.15, Java 25 target, snapshot version logic
7. Add MIT `LICENSE` file

**Acceptance Criteria:**
- `./gradlew build` succeeds (empty mod, no entrypoint yet)
- Version string follows `<mod-version>+mc.<mc-version>` format
- Snapshot builds append `-SNAPSHOT` when `SNAPSHOT=true` env var is set

**Key decisions:**
- No mappings dependency (MC 26.1 is unobfuscated)
- Use `implementation` instead of `modImplementation` (Loom 1.15 change)
- No `remapJar` — standard `jar` task produces the final artifact
- `rootProject.name = 'weighted-hotbar-randomizer'` controls jar base name

### Phase 2: Minimal Mod (Effort: S)

Create the mod entrypoint and metadata so it loads under Fabric.

**Tasks:**
1. Create `src/main/resources/fabric.mod.json` with client entrypoint, mod ID `weightedhotbar`
2. Create `src/main/resources/weightedhotbar.mixins.json` with mixin config
3. Create `WeightedHotbarRandomizer.java` — skeleton `ClientModInitializer` with logger

**Acceptance Criteria:**
- `./gradlew build` produces a loadable mod jar
- Mod loads without error in `./gradlew runClient`

**Dependencies:** Phase 1

### Phase 3: Config System (Effort: M)

JSON config persistence for enabled state and slot range.

**Tasks:**
1. Create `ModConfig.java` — POJO with `enabled` (default: false), `lowerSlot` (1–9), `upperSlot` (1–9)
2. Implement Gson-based `load()` and `save()` methods
3. Config path: `FabricLoader.getInstance().getConfigDir().resolve("weightedhotbar.json")`
4. Validation: clamp slots to 1–9, ensure lower <= upper
5. Wire `ModConfig.load()` into entrypoint's `onInitializeClient()`

**Acceptance Criteria:**
- Config file created at `config/weightedhotbar.json` on first run
- Default state is disabled
- Persists across restarts
- Invalid values are clamped/corrected silently

**Dependencies:** Phase 2

### Phase 4: Core Selection Logic (Effort: S)

Pure logic classes for candidate evaluation and weighted selection.

**Tasks:**
1. Create `EligibilityEvaluator.java`
   - `evaluate(Inventory, lowerSlot, upperSlot)` returns `List<Candidate>`
   - Filters: non-empty slots containing `BlockItem` within range
   - Converts 1-indexed config to 0-indexed inventory slots
   - Inner record: `Candidate(int slot, int stackSize)`
2. Create `WeightedSelector.java`
   - `select(List<Candidate>, RandomSource)` returns `OptionalInt`
   - Weight = stack size; probability proportional to weight
   - Empty list returns `OptionalInt.empty()`
   - Single candidate returns that candidate's slot directly

**Acceptance Criteria:**
- Correct weighted distribution (higher stack count = higher selection probability)
- Empty candidate list → `OptionalInt.empty()`
- Single candidate → returns that slot (enables pulling player back into palette range)
- No Minecraft runtime dependencies beyond type references (testable in isolation)

**Dependencies:** Phase 2

### Phase 5: Interaction Hook (Effort: M)

Post-placement slot switching via Mixin on `ClientPlayerInteractionManager`.

**Tasks:**
1. Create `MixinClientPlayerInteractionManager.java`
   - `@Inject` at tail of `interactBlock()` method
   - Check return value indicates successful placement
   - Delegate to `InteractionHandler.onBlockPlaced()`
2. Create `InteractionHandler.java`
   - On placement: check enabled → evaluate candidates → select → switch slot
   - Guard: skip if no candidates
   - Guard: skip if selected slot equals current slot
   - Slot switch: set `inventory.selected = newSlot` (client syncs to server next tick)

**Acceptance Criteria:**
- Slot changes after placing a block
- No switching when mod is disabled
- No switching when no eligible slots exist
- Single eligible slot: switches to it (pulls player back into range)
- Works in both singleplayer and multiplayer
- No crash on empty inventory or non-block items

**Key design notes:**
- Post-placement strategy: switch AFTER placement, not before
- Mixin required because Fabric API has no `ClientBlockPlaceEvent`
- All code runs on client main thread — no synchronization needed
- Mixin target: `net.minecraft.client.multiplayer.MultiPlayerGameMode.useItemOn()` (unobfuscated name)

**Dependencies:** Phase 3, Phase 4

### Phase 6: UX — Commands, Keybind, Feedback (Effort: M)

User-facing interaction layer.

**Tasks:**
1. Create `FeedbackService.java`
   - `showSlotSwitch(LocalPlayer, int slot)` — action bar message
   - `showToggle(LocalPlayer, boolean enabled)` — action bar message
   - `showStatus(FabricClientCommandSource, ...)` — chat message
   - `showRangeSet(FabricClientCommandSource, ...)` — chat message
2. Create `HotbarRandCommand.java`
   - Register via `ClientCommandRegistrationCallback`
   - Subcommands: `toggle`, `on`, `off`, `status`, `range <lower> <upper>`
   - Range validation: lower <= upper, values 1–9
3. Wire keybind in entrypoint
   - Register via `KeyBindingHelper` (default: H key)
   - Poll `wasPressed()` in `ClientTickEvents.END_CLIENT_TICK`
   - Toggle mod and show feedback

**Acceptance Criteria:**
- `/weightedhotbar toggle` toggles and shows feedback
- `/weightedhotbar status` shows enabled state and range
- `/weightedhotbar range 2 7` updates config and persists
- `/weightedhotbar range 5 3` is rejected with error message
- Keybind H toggles with action bar feedback
- Slot switch shows brief action bar message (not chat spam)

**Dependencies:** Phase 3, Phase 5

### Phase 7: CI/CD Workflows (Effort: M)

Three GitHub Actions workflows for the full delivery pipeline.

**Tasks:**
1. Create `.github/workflows/pr-validation.yml`
   - Trigger: `pull_request` on `main`
   - Steps: checkout, setup Java 25 (temurin), setup Gradle, `./gradlew build`
   - Upload jar as temporary artifact
2. Create `.github/workflows/main-snapshot.yml`
   - Trigger: `push` to `main`
   - Steps: same build + set `SNAPSHOT=true` env var
   - Upload jar with commit SHA in artifact name
3. Create `.github/workflows/release-tag.yml`
   - Trigger: `push` tags matching `v*-mc*`
   - Steps: build, validate tag-to-version match, create GitHub Release with jar attached
   - Use `softprops/action-gh-release@v2`
   - Fail if tag version doesn't match `gradle.properties`

**Acceptance Criteria:**
- PR builds validate cleanly
- Snapshot artifacts are clearly marked as non-release
- Tagged release creates GitHub Release with attached jar
- Release workflow rejects mismatched tag/version combinations
- Artifact names include both mod version and MC version

**Dependencies:** Phase 1

### Phase 8: Documentation (Effort: S)

Update README with usage, commands, building instructions.

**Tasks:**
1. Update `README.md` with: description, features, installation, commands, keybinds, config, building from source, versioning

**Acceptance Criteria:**
- README covers all user-facing features
- Build instructions are accurate

**Dependencies:** All prior phases

## Risk Assessment and Mitigation

| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|------------|
| Mixin target method signature differs in MC 26.1 | Medium | High | Verify exact method signature via Loom decompiled sources before writing mixin |
| Fabric Loom 1.15 API differences from research | Medium | Medium | Reference fabric-example-mod 26.1 branch; adapt build.gradle as needed |
| Java 25 not available in GitHub Actions temurin | Low | High | Use `actions/setup-java@v4` with early-access distribution or Oracle JDK |
| Slot switching causes client/server desync | Low | Medium | Only set `inventory.selected`; client handles sync packet automatically |
| Rapid placement causes performance issues | Low | Low | Evaluation is O(9) worst case — negligible cost |

## Success Metrics

- `./gradlew build` succeeds and produces correctly named jar
- All three CI workflows pass on their respective triggers
- Mod loads in dev client without errors
- Block placement triggers weighted slot switching
- Config persists across game restarts
- Commands and keybind work as specified
- No crashes on empty inventory, world changes, or reconnects

## Required Resources and Dependencies

| Resource | Version | Source |
|----------|---------|--------|
| Minecraft | 26.1 | Mojang |
| Java | 25 | Temurin |
| Gradle | 9.4.0 | Gradle |
| Fabric Loom | 1.15 | maven.fabricmc.net |
| Fabric Loader | 0.19.1 | maven.fabricmc.net |
| Fabric API | 0.145.4+26.1.1 | maven.fabricmc.net |
| Gson | (bundled with MC) | — |
| GitHub Actions | — | GitHub |
