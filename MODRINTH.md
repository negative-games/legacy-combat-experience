# Legacy Combat Experience

Bring back the fast, familiar feel of 1.8 PvP while keeping your server on modern Minecraft.

**Legacy Combat Experience (LCE)** restores the parts of old combat that players notice immediately: cleaner hit feedback, familiar knockback, useful projectiles, bow boosting, sword blocking, and movement that feels closer to classic PvP. It is a good fit for practice servers, duel arenas, minigames, and any Paper server that wants modern Minecraft without modern combat getting in the way.

LCE is currently a **beta plugin**. It is ready for testing on modern Paper servers, but you should try updates on a staging server before rolling them out to live PvP arenas. The project is open source, and feedback, issue reports, and contributions are welcome.

## What It Does

- Makes PvP feel closer to Minecraft 1.8 on modern Paper servers
- Removes distracting modern combat particles and sounds
- Lets staff tune knockback without replacing the whole combat system
- Brings back useful projectile interactions for snowballs, eggs, ender pearls, and fishing rods
- Adds optional bow boosting for classic movement plays
- Supports sword blocking behavior for legacy-style weapons
- Lets you limit legacy combat to specific worlds or WorldGuard regions
- Keeps the plugin lightweight and focused on combat feel

## Cleaner Combat Feedback

Modern combat adds extra sounds and particles that can make PvP feel noisy or unfamiliar. LCE can remove those effects so fights look and sound cleaner for players who prefer classic PvP.

Players get fewer distracting hit indicators, no unwanted sweep attack clutter, and a combat experience that feels closer to the servers they remember.

<details>
<summary>Admin details</summary>

LCE can block or remap combat sounds and particles through configuration.

Examples include:

- Sweep attack particles
- Damage indicator particles
- Modern attack sounds
- Fishing rod sound behavior

</details>

## Knockback That Feels Right

Knockback is one of the biggest reasons old PvP feels different from modern combat. LCE gives server owners a simple way to adjust the feel of hits so duels, arenas, and practice fights play the way your community expects.

Staff can check and update the main knockback values in-game, making it easier to test changes with players instead of editing files and restarting repeatedly.

<details>
<summary>Knockback commands</summary>

```txt
/legacycombatexperience knockback
/legacycombatexperience knockback <x> <y> <z>
/lce knockback
/lce knockback <x> <y> <z>
```

LCE also supports the shorter knockback alias through the command system:

```txt
/kb
```

</details>

<details>
<summary>Supported knockback causes</summary>

LCE can store knockback values for Paper knockback causes such as:

- `DAMAGE`
- `ENTITY_ATTACK`
- `EXPLOSION`
- `SHIELD_BLOCK`
- `SWEEP_ATTACK`
- `PUSH`
- `UNKNOWN`

</details>

## Projectiles Matter Again

Snowballs, eggs, ender pearls, and fishing rods are a big part of legacy PvP. LCE can make these interactions useful again so players can pressure, combo, and move with tools that feel familiar.

This helps practice arenas and PvP modes feel less like modern survival combat and more like classic competitive play.

<details>
<summary>Projectile details</summary>

Default knockback projectile types include:

- Snowballs
- Eggs
- Ender pearls
- Fishing bobbers

Staff can change projectile damage, knockback strength, and which projectile types apply knockback.

</details>

## Bow Boosting and Movement

Bow boosting gives skilled players another way to move, escape, chase, and make plays. LCE includes optional bow boosting so servers can bring back that old movement style without forcing it on every setup.

LCE also includes optional bridging and potion physics adjustments for servers that want more of the classic movement feel.

<details>
<summary>Movement options</summary>

Configurable movement and physics options include:

- Bow boosting
- Bow boost force threshold
- Horizontal and vertical bow boost speed
- Fishing rod velocity
- Legacy potion velocity reduction
- Optional shift-backward bridging speed behavior

</details>

## Sword Blocking and Legacy Damage

LCE can support sword blocking behavior and legacy-style damage tuning so fights feel less like modern cooldown combat. Server owners can control how much blocking reduces damage and whether older axe, sharpness, and critical hit behavior should be used.

<details>
<summary>Combat options</summary>

Configurable combat behavior includes:

- Damage reduction while blocking with a sword
- Old axe damage behavior
- Critical hit modifier
- Whether sprinting cancels critical hits
- Old sharpness damage behavior
- Offhand disabling

</details>

## Use It Only Where You Need It

Not every world needs legacy combat. LCE can be limited to specific worlds, and with WorldGuard installed, server owners can control legacy combat behavior by region.

That makes it easier to run legacy PvP arenas alongside normal survival worlds on the same network.

<details>
<summary>World and region support</summary>

LCE includes world-based legacy combat checks.

With WorldGuard installed, LCE can also use region flag support for area-specific behavior.

</details>

## Commands and Permissions

<details>
<summary>Commands</summary>

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

</details>

<details>
<summary>Permissions</summary>

```txt
lce.command
combat.command.giveblockhittingsword
```

</details>

## Dependencies

<details>
<summary>Required and optional plugins</summary>

Required:

- **[PacketEvents](https://modrinth.com/plugin/packetevents)** - used for packet-based combat behavior

Optional:

- **[PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)**
- **[WorldGuard](https://dev.bukkit.org/projects/worldguard)** - region support

</details>

## Roadmap

LCE will continue to grow around the same goal: modern servers with old-school PvP feel.

Planned areas include more legacy combat polish, easier admin controls, better documentation, more test coverage, and improvements based on real server feedback. If your community depends on a specific 1.8 combat behavior, open an issue and describe what you expect players to experience.

## Source and Support

LCE is open source. Use GitHub to follow development, report issues, request changes, or contribute improvements:

**GitHub:** [moonrise-studios/legacy-combat-experience](https://github.com/moonrise-studios/legacy-combat-experience)
