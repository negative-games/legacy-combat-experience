# Legacy Combat Experience

**Legacy Combat Experience (LCE)** brings classic Minecraft 1.8-style PvP behavior to modern Paper servers.

LCE is built for PvP, practice, minigame, and competitive servers that want the speed and feel of legacy combat while staying on current Minecraft versions. It focuses on configurable combat physics, reduced modern combat noise, projectile behavior, knockback tuning, and optional world or region scoping.

## At a Glance

- Disable modern attack sounds and particles such as sweep attacks and damage indicators
- Tune knockback values by knockback cause
- Restore legacy-style projectile interactions for snowballs, eggs, ender pearls, fishing rods, and more
- Optional bow boosting and legacy potion physics
- Optional bridging speed behavior for shift-backward movement
- Configurable sword blocking damage reduction and old sharpness/axe damage behavior
- WorldGuard integration for region-based legacy combat flags

## Dependencies

- **[PacketEvents](https://modrinth.com/plugin/packetevents)** - required for packet-based sound, particle, and blocking behavior

## Optional Dependencies

- **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)** - optional placeholder support
- **[WorldGuard](https://dev.bukkit.org/projects/worldguard)** - optional region flag support

## Main Features

### Legacy Sounds and Particles

LCE can block or remap modern combat feedback so PvP feels closer to Minecraft 1.8. This includes disabling sounds such as modern player attack feedback and particles such as sweep attacks or damage indicators.

<details>
<summary>Sound and Particle Controls</summary>

Sounds and particles are configured through the generated configuration files. You can disable unwanted modern feedback or remap specific sounds to legacy-style alternatives.

Examples of behavior LCE can remove or remap:

- Sweep attack particles
- Damage indicator particles
- Modern attack sounds
- Fishing rod throw/retrieve sound behavior

</details>

### Knockback Tuning

Tune knockback profiles for supported knockback causes. This lets server owners adjust PvP feel without replacing the rest of the combat system.

<details>
<summary>Knockback Configuration</summary>

LCE supports knockback values for Paper's entity knockback causes, including:

- `DAMAGE`
- `ENTITY_ATTACK`
- `EXPLOSION`
- `SHIELD_BLOCK`
- `SWEEP_ATTACK`
- `PUSH`
- `UNKNOWN`

The in-game knockback command can be used to inspect and update the main entity attack profile:

```txt
/knockback
/knockback <x> <y> <z>
```

Alias:

```txt
/kb
```

</details>

### Projectile Physics

LCE can make projectiles feel closer to legacy PvP by applying knockback to configured projectile types and adjusting related combat behavior.

<details>
<summary>Projectile Behavior</summary>

Default knockback projectile types include:

- Snowballs
- Eggs
- Ender pearls
- Fishing bobbers

You can configure projectile damage, knockback strength, and the list of projectile entity types that should apply knockback.

</details>

### Bow Boosting

Bow boosting can be enabled for servers that want classic movement tech. When enabled, LCE applies configurable horizontal and vertical boost values for qualifying bow shots.

<details>
<summary>Bow Boost Settings</summary>

Bow boosting is controlled by configuration values for:

- Whether bow boosting is enabled
- The force threshold for detecting a bow boost
- Horizontal and vertical boost speed

</details>

### Legacy Combat Physics

LCE includes several optional physics and damage adjustments for recreating older combat behavior while keeping server owners in control.

<details>
<summary>Combat Physics Options</summary>

Configurable behavior includes:

- Fishing rod velocity
- Damage reduction while blocking with a sword
- Old axe damage behavior
- Critical hit modifier
- Whether sprinting cancels critical hits
- Old sharpness damage behavior
- Legacy potion velocity reduction
- Offhand disabling
- Optional bridging speed behavior

</details>

### World and Region Control

LCE can be scoped so legacy combat behavior applies only where you want it.

<details>
<summary>WorldGuard and World Support</summary>

Use the world configuration to define legacy combat worlds. If WorldGuard is installed, LCE can also use region flag support so specific areas can opt into or out of legacy combat behavior.

This is useful for networks where only PvP arenas, practice areas, or minigame worlds should use legacy combat mechanics.

</details>

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

## Recommended For

- PvP practice servers
- Minigame servers
- Competitive survival servers
- Arena and duel servers
- Networks that want legacy combat in specific worlds or regions

## Notes

LCE is designed for Paper servers and requires PacketEvents. For the most reliable experience, test combat changes in a staging environment before applying them to a live PvP server.
