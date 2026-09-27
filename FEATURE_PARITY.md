# NEF Fabric Port — Feature Parity Ledger

This file tracks the modern rewrite against the upstream 1.8.9 feature surface.

## Source inventory

The upstream source tree currently contains **185 feature-side Java classes** under `features/`. Every one is included in `notenoughfakepixel-features.json` and loaded into the modern feature registry at runtime.

## Modern implementations

The following are implemented as native Fabric 1.21.11 code rather than legacy Forge hooks:

- Always Sprint
- Fullbright, with gamma restoration
- No Hurt Camera
- Persistent Waypoints + distance HUD + add/remove/list commands
- Client feature toggles and persistent feature state
- Chat/game-message filters
- Fishing, Diana, Crimson, and Slayer notification handlers
- Safe feature inventory/registry for the complete upstream feature tree

## Compatibility rule

Complex 1.8.9 systems that depended on old Forge events, 1.8.9 container internals, or old OpenGL/render hooks are not copied as broken legacy classes. They are represented by a stable modern feature ID and must be implemented through 1.21.11 Fabric events/mixins/render APIs. This prevents an old hook from crashing the client.

## Upstream user-facing feature headings

### Quality of life
- General
  - Alerts
  - Aliases
  - Disable block breaking particles
  - Always sprint
  - Copy chat message
  - Full block lever
  - Block placing items
  - Show unclaimed Jacob rewards
  - Fairy soul waypoints
  - Spider's den relic waypoints
  - Etherwarp overlay
  - Etherwarp sounds
  - Hide flaming fists
  - Hide player armor
  - Hide dead mobs
  - Hide falling blocks
  - Item animations
  - Smol people
  - Reforge helper
  - Disable Hyperion Explosions
  - Disable Thunderlord enchantment bolts
  - No Hurt camera animation
  - Golden enchants
  - Fullbright
  - Disable Potion Effects in inventory
  - Show current pet in pet menu
  - Storage overlay
  - Equipment overlay
  - Slot locking & binding
  - Middle click on Menus
  - Scrollable tooltips
  - Disable sounds from Jerry-chine gun and AOTE
  - Midas Staff minimal animation and sounds
  - Wardrobe key shortcut
  - Equipment key shortcut
  - Hide players close to NPCs **(NEW)**
  - Missing Accessories GUI **(NEW)**
  - Show Ender Nodes **(NEW)**
- Chat
  - Disable Friend > joined messages
  - Disable Info & Watchdog messages
  - Disable selling ranks messages
### Farming
- 1.12 Crop height
### Fishing
- Fishing countdown
- Fishing notifier for Legendary Sea Creatures
- Notifier for Trophy Fishing
### Dungeons
- General
  - Custom Leap GUI
  - Custom Terminal GUI
  - Blood ready title
  - Boulder Solver
  - Silverfish Solver
  - Wither doors highlight
  - Teleport maze Solver
  - Terminal waypoints
  - Custom S+ message
  - Spirit bow tracer
  - Dungeon Map
  - Three Weirdos Solver
  - Auto Ready
  - Auto close secret chests
  - "Unable to locate sign" hidden
  - S+ reminder
  - Score overlay
  - Secret overlay
- Starred mobs
  - Starred mob display
  - Fel position display
  - Bat position display
- Floor 7
  - Terminal solvers
  - Terminal tracker
  - Box withers
- Master Mode 7
  - M7 dragon highlight
  - M7 relic waypoints
### Mining
- General
  - Drill Animation Reset FIX
  - Mining Ability ready notifier
  - Dwarven waypoints
- Mining Overlay
  - Drill Fuel Overlay
  - Mining Ability cooldown Overlay
  - Commissions Overlay
  - Mithril Powder Overlay
  - Puzzler NPC Solver
- Crystal Hollows **(NEW)**
  - Crystal Hollows Map  **(NEW)**
  - Crystal Hollows Waypoints  **(NEW)**
  - FullBlock glass panes **(NEW)**
  - Treasure Found notifier  **(NEW)**
  - Precursor City Overlay **(NEW)**
  - Automaton Overlay **(NEW)**
  - Metal Detector Guesser **(NEW)**
  - Worm Notifier **(NEW)**
  - Mines of Divan Overlay **(NEW)**
### Experimentation Table
- Chronomatron solver
- Ultrasequencer solver
- Prevent missclicks
### Chocolate Factory
- Show best rabbit in Chocolate Factory
- Hoppity's hunt eggs waypoints
- New Egg notifier **(NEW)**
- New Egg timer **(NEW)**
### Crimson Isle
- Bosses notifier
- Ashfang helper & overlay
### Diana
- Burrow guess
- Warp keybind
- Burrow waypoints
- Gaia construct helper
- Syamese Lynx helper
- Minos Inquisitor party chat & waypoint
- Inquisitor Outline
- Some QOL features
### Slayers
- Voidgloom Seraph beacon waypoint BETA
- Miniboss spawn alert
- Slayer health display
- Slayer boss time
- Blaze pillar title
- Blaze attunements

## Build target

- Minecraft 1.21.11
- Fabric Loader 0.19.3+
- Fabric API 0.141.6+1.21.11
- Java 21

