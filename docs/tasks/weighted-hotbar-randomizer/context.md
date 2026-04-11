# Weighted Hotbar Randomizer — Context

Last Updated: 2026-04-10

## Key Files

### Build Configuration
| File | Purpose |
|------|---------|
| `build.gradle` | Loom 1.15 plugin, dependencies, Java 25, jar config, snapshot version logic |
| `settings.gradle` | Plugin management repos, root project name |
| `gradle.properties` | Version constants: MC 26.1, Loader 0.19.1, Fabric API 0.145.4+26.1.1, mod 0.1.0 |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle 9.4.0 distribution URL |
| `LICENSE` | MIT license |

### Source Code
| File | Purpose |
|------|---------|
| `src/main/java/com/weightedhotbar/WeightedHotbarRandomizer.java` | `ClientModInitializer` entrypoint — wires config, keybind, commands, hook |
| `src/main/java/com/weightedhotbar/config/ModConfig.java` | Config POJO + Gson JSON persistence (default: disabled) |
| `src/main/java/com/weightedhotbar/selection/EligibilityEvaluator.java` | Scans hotbar range for BlockItem candidates |
| `src/main/java/com/weightedhotbar/selection/WeightedSelector.java` | Weighted random slot selection by stack size |
| `src/main/java/com/weightedhotbar/hook/InteractionHandler.java` | Block placement handler, triggers slot switch |
| `src/main/java/com/weightedhotbar/mixin/MixinClientPlayerInteractionManager.java` | Mixin into `MultiPlayerGameMode.useItemOn()` to detect placement |
| `src/main/java/com/weightedhotbar/command/HotbarRandCommand.java` | Client `/weightedhotbar` commands via Brigadier |
| `src/main/java/com/weightedhotbar/feedback/FeedbackService.java` | Action bar + chat message display |

### Resources
| File | Purpose |
|------|---------|
| `src/main/resources/fabric.mod.json` | Mod metadata, client entrypoint, dependencies |
| `src/main/resources/weightedhotbar.mixins.json` | Mixin configuration pointing to mixin classes |

### CI/CD
| File | Purpose |
|------|---------|
| `.github/workflows/pr-validation.yml` | Build + test on PRs to main |
| `.github/workflows/main-snapshot.yml` | Snapshot artifact on push to main |
| `.github/workflows/release-tag.yml` | GitHub Release on `v*-mc*` tags |

## Key Decisions

### MC 26.1 is unobfuscated — no mappings needed
Minecraft 26.1 ships with unobfuscated code. This means:
- No `mappings` dependency in `build.gradle`
- Use real Minecraft class/method names directly (e.g., `net.minecraft.world.item.BlockItem`)
- No `remapJar` task — standard `jar` produces the final artifact
- Use `implementation` instead of `modImplementation`

### One Mixin for placement detection + Fabric API events for everything else
Fabric API does not provide a client-side block placement event (confirmed: no `ClientBlockPlaceEvent` exists). The mod uses:
- **Mixin**: `@Inject` at tail of `MultiPlayerGameMode.useItemOn()` to detect successful block placement
- **Fabric API**: `ClientTickEvents.END_CLIENT_TICK` for keybind polling, `ClientCommandRegistrationCallback` for commands, `KeyBindingHelper` for keybind registration

The mixin is minimal — a single `@Inject` that delegates to `InteractionHandler`.

### Post-placement slot switching
The slot switch happens AFTER a successful block placement, not before. Rationale:
- More stable — no timing issues with pre-placement switching
- The placed block is always what the player had selected
- Slot switch prepares the next item for the next interaction

### Single candidate selection
When only one eligible slot exists in the configured range, the mod selects it directly. This enables "pull-back" behavior: if the player manually switches away from their palette range, the next placement brings them back to the only eligible slot.

### Default state is disabled
The mod starts disabled on first install. Player must explicitly enable via keybind (H) or command (`/weightedhotbar on`).

### Config uses Gson (bundled with Minecraft)
No additional dependencies needed. Gson is on the classpath via the Minecraft dependency.

### Client-side only
`fabric.mod.json` declares `"environment": "client"`. No server-side code. Slot switching uses `inventory.selected` field assignment; the client automatically syncs to the server via `ServerboundSetCarriedItemPacket`.

## Dependencies Between Components

```
WeightedHotbarRandomizer (entrypoint)
  ├── ModConfig (load/save config)
  ├── HotbarRandCommand (registers commands, uses ModConfig + FeedbackService)
  ├── MixinClientPlayerInteractionManager (detects placement)
  │     └── InteractionHandler.onBlockPlaced() (static callback)
  │           ├── ModConfig (check enabled, get range)
  │           ├── EligibilityEvaluator (scan candidates)
  │           ├── WeightedSelector (pick slot)
  │           └── FeedbackService (show action bar)
  └── KeyBind tick handler (uses ModConfig + FeedbackService)
```

## Versioning Scheme

| Artifact Type | Version Format | Example |
|---------------|---------------|---------|
| Release | `<mod>+mc.<mc>` | `0.1.0+mc.26.1` |
| Snapshot | `<mod>-SNAPSHOT+mc.<mc>` | `0.1.0-SNAPSHOT+mc.26.1` |
| Git tag | `v<mod>-mc<mc>` | `v0.1.0-mc26.1` |
| Release jar | `weighted-hotbar-randomizer-<version>.jar` | `weighted-hotbar-randomizer-0.1.0+mc.26.1.jar` |

## Minecraft API Surface Used

| Class | Usage |
|-------|-------|
| `net.minecraft.client.multiplayer.MultiPlayerGameMode` | Mixin target: `useItemOn()` for placement detection |
| `net.minecraft.world.entity.player.Inventory` | `getItem(slot)`, `selected` field |
| `net.minecraft.world.item.ItemStack` | `isEmpty()`, `getCount()`, `getItem()` |
| `net.minecraft.world.item.BlockItem` | `instanceof` check for placement eligibility |
| `net.minecraft.client.player.LocalPlayer` | `getInventory()`, `getRandom()`, `displayClientMessage()` |
| `net.minecraft.network.chat.Component` | `literal()` for text messages |
| `net.minecraft.ChatFormatting` | Color constants for feedback |
| `net.minecraft.world.InteractionResult` | Check placement success in mixin |
