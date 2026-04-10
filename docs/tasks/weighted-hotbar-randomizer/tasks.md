# Weighted Hotbar Randomizer — Task Checklist

Last Updated: 2026-04-10

## Phase 1: Build Infrastructure [M]

- [ ] 1.1 Update `.gitignore` with Gradle, IDE, and run directory patterns
- [ ] 1.2 Create `gradle/wrapper/gradle-wrapper.properties` (Gradle 9.4.0)
- [ ] 1.3 Generate `gradlew` and `gradlew.bat` wrapper scripts
- [ ] 1.4 Create `gradle.properties` (MC 26.1, Loader 0.19.1, Fabric API 0.145.4+26.1.1, mod 0.1.0)
- [ ] 1.5 Create `settings.gradle` (Fabric maven, plugin management, project name)
- [ ] 1.6 Create `build.gradle` (Loom 1.15, Java 25, snapshot version logic)
- [ ] 1.7 Add MIT `LICENSE` file
- [ ] 1.8 Verify `./gradlew build` succeeds

## Phase 2: Minimal Mod [S]

_Blocked by: Phase 1_

- [ ] 2.1 Create `src/main/resources/fabric.mod.json` (client entrypoint, mod ID `weightedhotbar`)
- [ ] 2.2 Create `src/main/resources/weightedhotbar.mixins.json` (mixin config)
- [ ] 2.3 Create `WeightedHotbarRandomizer.java` (skeleton `ClientModInitializer`)
- [ ] 2.4 Verify mod loads in dev client

## Phase 3: Config System [M]

_Blocked by: Phase 2_

- [ ] 3.1 Create `ModConfig.java` (POJO: enabled=false, lowerSlot, upperSlot)
- [ ] 3.2 Implement Gson `load()` / `save()` with validation
- [ ] 3.3 Wire `ModConfig.load()` into entrypoint
- [ ] 3.4 Verify config file created at `config/weightedhotbar.json`

## Phase 4: Core Selection Logic [S]

_Blocked by: Phase 2_

- [ ] 4.1 Create `EligibilityEvaluator.java` (scan hotbar range, filter BlockItem slots)
- [ ] 4.2 Create `WeightedSelector.java` (weighted random selection by stack count)
- [ ] 4.3 Handle edge cases: empty list → no-op, single candidate → select directly

## Phase 5: Interaction Hook [M]

_Blocked by: Phase 3, Phase 4_

- [ ] 5.1 Create `MixinClientPlayerInteractionManager.java` (@Inject at tail of `useItemOn()`)
- [ ] 5.2 Create `InteractionHandler.java` (post-placement flow: evaluate → select → switch)
- [ ] 5.3 Add guards: disabled check, no candidates, same-slot skip
- [ ] 5.4 Verify slot switching works during block placement

## Phase 6: UX — Commands, Keybind, Feedback [M]

_Blocked by: Phase 3, Phase 5_

- [ ] 6.1 Create `FeedbackService.java` (action bar + chat messages)
- [ ] 6.2 Create `HotbarRandCommand.java` (toggle, on, off, status, range subcommands)
- [ ] 6.3 Register keybind (default: H) via `KeyBindingHelper`
- [ ] 6.4 Wire keybind tick handler in entrypoint
- [ ] 6.5 Verify all commands work and provide appropriate feedback
- [ ] 6.6 Verify keybind toggles with action bar message

## Phase 7: CI/CD Workflows [M]

_Blocked by: Phase 1_

- [ ] 7.1 Create `.github/workflows/pr-validation.yml` (build on PRs to main)
- [ ] 7.2 Create `.github/workflows/main-snapshot.yml` (snapshot on push to main)
- [ ] 7.3 Create `.github/workflows/release-tag.yml` (GitHub Release on `v*-mc*` tags)
- [ ] 7.4 Implement tag-to-version validation in release workflow
- [ ] 7.5 Verify workflow YAML is valid

## Phase 8: Documentation [S]

_Blocked by: All prior phases_

- [ ] 8.1 Update `README.md` (description, features, install, commands, keybinds, config, building)
