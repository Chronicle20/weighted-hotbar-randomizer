# Weighted Hotbar Randomizer

A client-side Fabric mod for Minecraft 26.1+ that automatically rotates your selected hotbar slot after placing a block. The next slot is chosen using weighted randomness based on stack size — slots with more items are more likely to be selected.

## Features

- **Weighted random slot selection** after block placement (higher stack count = higher probability)
- **Configurable slot range** (1–9) to limit which slots participate
- **Toggle keybind** (default: H) with action bar feedback
- **Client commands** for full control
- **JSON config** persists across sessions
- **Client-side only** — works on any server

## Installation

1. Install [Fabric Loader](https://fabricmc.net/) 0.19.1+ for Minecraft 26.1
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Drop the mod jar into your `mods/` folder

## Commands

All commands use the `/weightedhotbar` prefix (client-side only):

| Command | Description |
|---------|-------------|
| `/weightedhotbar toggle` | Toggle the mod on/off |
| `/weightedhotbar on` | Enable the mod |
| `/weightedhotbar off` | Disable the mod |
| `/weightedhotbar status` | Show current state and slot range |
| `/weightedhotbar range <lower> <upper>` | Set the active slot range (1–9) |

## Keybinds

| Key | Action |
|-----|--------|
| H | Toggle mod on/off |

Rebindable in Options > Controls.

## Configuration

Config file: `config/weightedhotbar.json`

```json
{
  "enabled": false,
  "lowerSlot": 1,
  "upperSlot": 9
}
```

- **enabled**: Whether the mod is active (default: `false`)
- **lowerSlot / upperSlot**: Hotbar slot range (1–9) that participates in randomization

## How It Works

1. You place a block
2. The mod scans your hotbar (within the configured range) for slots containing block items
3. Each eligible slot is weighted by its stack size
4. A slot is randomly selected (proportional to weight) and becomes your active slot
5. If only one eligible slot exists, it is selected directly (pulls you back into range)

## Building from Source

Requires Java 25.

```sh
git clone https://github.com/your-user/weighted-hotbar-randomizer.git
cd weighted-hotbar-randomizer
./gradlew build
```

The built jar will be at `build/libs/weighted-hotbar-randomizer-<version>+mc.<mc-version>.jar`.

## Versioning

| Type | Format | Example |
|------|--------|---------|
| Release | `<mod>+mc.<mc>` | `0.1.0+mc.26.1` |
| Snapshot | `<mod>-SNAPSHOT+mc.<mc>` | `0.1.0-SNAPSHOT+mc.26.1` |
| Git tag | `v<mod>-mc<mc>` | `v0.1.0-mc26.1` |

## License

[MIT](LICENSE)
