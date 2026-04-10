# Weighted Hotbar Randomizer — Task Checklist

Last Updated: 2026-04-10

## Phase 1: Build Infrastructure [M]

- [x] 1.1 Update `.gitignore` with Gradle, IDE, and run directory patterns
- [x] 1.2 Create `gradle/wrapper/gradle-wrapper.properties` (Gradle 9.4.1)
- [x] 1.3 Generate `gradlew` and `gradlew.bat` wrapper scripts
- [x] 1.4 Create `gradle.properties` (MC 26.1, Loader 0.19.1, Fabric API 0.145.4+26.1.1, mod 0.1.0)
- [x] 1.5 Create `settings.gradle` (Fabric maven, plugin management, project name)
- [x] 1.6 Create `build.gradle` (Loom 1.15, Java 25, snapshot version logic)
- [x] 1.7 Add MIT `LICENSE` file
- [x] 1.8 Verify `./gradlew build` succeeds

## Phase 2: Minimal Mod [S]

_Blocked by: Phase 1_

- [x] 2.1 Create `src/main/resources/fabric.mod.json` (client entrypoint, mod ID `weightedhotbar`)
- [x] 2.2 Create `src/main/resources/weightedhotbar.mixins.json` (mixin config)
- [x] 2.3 Create `WeightedHotbarRandomizer.java` (skeleton `ClientModInitializer`)
- [x] 2.4 Verify mod loads in dev client

## Phase 3: Config System [M]

_Blocked by: Phase 2_

- [x] 3.1 Create `ModConfig.java` (POJO: enabled=false, lowerSlot, upperSlot)
- [x] 3.2 Implement Gson `load()` / `save()` with validation
- [x] 3.3 Wire `ModConfig.load()` into entrypoint
- [ ] 3.4 Verify config file created at `config/weightedhotbar.json`

## Phase 4: Core Selection Logic [S]

_Blocked by: Phase 2_

- [x] 4.1 Create `EligibilityEvaluator.java` (scan hotbar range, filter BlockItem slots)
- [x] 4.2 Create `WeightedSelector.java` (weighted random selection by stack count)
- [x] 4.3 Handle edge cases: empty list → no-op, single candidate → select directly

## Phase 5: Interaction Hook [M]

_Blocked by: Phase 3, Phase 4_

- [x] 5.1 Create `MixinClientPlayerInteractionManager.java` (@Inject at tail of `useItemOn()`)
- [x] 5.2 Create `InteractionHandler.java` (post-placement flow: evaluate → select → switch)
- [x] 5.3 Add guards: disabled check, no candidates, same-slot skip
- [ ] 5.4 Verify slot switching works during block placement

## Phase 6: UX — Commands, Keybind, Feedback [M]

_Blocked by: Phase 3, Phase 5_

- [x] 6.1 Create `FeedbackService.java` (action bar + chat messages)
- [x] 6.2 Create `HotbarRandCommand.java` (toggle, on, off, status, range subcommands)
- [x] 6.3 Register keybind (default: H) via `KeyMappingHelper`
- [x] 6.4 Wire keybind tick handler in entrypoint
- [ ] 6.5 Verify all commands work and provide appropriate feedback
- [ ] 6.6 Verify keybind toggles with action bar message

## Phase 7: CI/CD Workflows [M]

_Blocked by: Phase 1_

- [x] 7.1 Create `.github/workflows/pr-validation.yml` (build on PRs to main)
- [x] 7.2 Create `.github/workflows/main-snapshot.yml` (snapshot on push to main)
- [x] 7.3 Create `.github/workflows/release-tag.yml` (GitHub Release on `v*-mc*` tags)
- [x] 7.4 Implement tag-to-version validation in release workflow
- [ ] 7.5 Verify workflow YAML is valid

## Phase 8: Documentation [S]

_Blocked by: All prior phases_

- [x] 8.1 Update `README.md` (description, features, install, commands, keybinds, config, building)
