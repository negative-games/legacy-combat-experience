# Legacy Combat Experience

[![Modrinth](https://img.shields.io/modrinth/v/lce?logo=modrinth&label=Modrinth&color=00AF5C)](https://modrinth.com/plugin/lce)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/lce?logo=modrinth&label=Downloads&color=00AF5C)](https://modrinth.com/plugin/lce)

**Legacy Combat Experience (LCE)** brings classic Minecraft 1.8-style PvP behavior to modern Paper servers.

LCE is built for PvP, practice, minigame, and competitive servers that want the speed and feel of legacy combat while staying on current Minecraft versions. The project is open source so server owners, developers, and community members can report issues, suggest improvements, and contribute fixes.

## Links

- Stable releases: [Modrinth](https://modrinth.com/plugin/lce)
- Dev builds: [Moonrise CI](https://ci.moonrise.gg/job/legacy-combat-experience/)
- Issues and feature requests: use the GitHub issue templates
- Contributing: see [CONTRIBUTING.md](CONTRIBUTING.md)

## Supported Platform

- Loader: Paper
- Minecraft version: currently built against Paper `1.21.4`
- Java: 21

PacketEvents is required. PlaceholderAPI and WorldGuard are optional integrations.

## Features

- Block or remap modern combat sounds and particles
- Tune knockback values by knockback cause
- Restore legacy-style projectile interactions for snowballs, eggs, ender pearls, fishing rods, and more
- Optional bow boosting and legacy potion physics
- Optional bridging speed behavior for shift-backward movement
- Configurable sword blocking damage reduction and old sharpness/axe damage behavior
- World and WorldGuard region support for scoping legacy combat behavior

## Dependencies

### Required

- [PacketEvents](https://modrinth.com/plugin/packetevents)

### Optional

- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- [WorldGuard](https://dev.bukkit.org/projects/worldguard)

## Building

Use Java 21.

```bash
./gradlew build
```

The shaded plugin jar is written to:

```txt
build/libs/
```

If the Gradle wrapper is not executable on your machine:

```bash
chmod +x ./gradlew
```

## Testing

Run the unit test suite with:

```bash
./gradlew test
```

Pull requests should include tests for behavior changes when the code can be tested without a live server.

## Commands

```txt
/legacycombatexperience
/legacycombatexperience reload
/legacycombatexperience sound <sound> [volume] [pitch]
/legacycombatexperience knockback
/legacycombatexperience knockback <x> <y> <z>
/lce
/lce reload
/lce sound <sound> [volume] [pitch]
/lce knockback
/lce knockback <x> <y> <z>
/giveblockhittingsword
```

## Permissions

```txt
lce.command
combat.command.giveblockhittingsword
```

## Contributing

Community contributions are welcome. The default branch is protected, so all changes should go through a pull request.

Before opening a pull request:

1. Create a branch from the latest default branch.
2. Keep the change focused.
3. Run `./gradlew test`.
4. Update documentation when behavior, commands, permissions, or configuration changes.
5. Fill out the pull request description with what changed and how it was tested.

For more detail, see [CONTRIBUTING.md](CONTRIBUTING.md).
