# CLAUDE.md — DragonBot

## Project Overview

DragonBot is an automation bot for the MMORPG Tibia. It automates hunting routines (movement, combat, looting, healing, eating) via scripted waypoint loops, and includes market-trading and item-optimization utilities.

- **Language**: Java 8 (JDK 1.8)
- **Build**: Maven 3.x
- **Platform**: Windows-only (uses AWT Robot, JNA for Win32 API, global keyboard/mouse hooks)
- **OCR**: Tesseract via tess4j (expects `C:/Tesseract`)
- **Database**: H2 embedded
- **Target resolution**: 1920x1080
- **UI language**: Portuguese (BR) for dialogs/logs; code in English

## Build & Run

```bash
mvn clean package
java -jar target/dragonbot-2.0.0-jar-with-dependencies.jar
```

Entry point: `com.dhuy.dragonbot.App.main()` → `AppInitialization.execute()`

## Configuration

- `config.xml` — characterName, spellCasterHotkey, spellCasterInterval, spellCasterEnableCavebot
- Tibia screenshots: `%USERPROFILE%\AppData\Local\Tibia\packages\Tibia\screenshots`
- Scripts (H2 DB): `scripts/*.mv.db`
- Item lists: `xml/` folder (`ALL/` and `ENHANCED/` subfolders)

## Project Structure

```
src/main/java/com/dhuy/dragonbot/
├── App.java                    # main(), mode selection dialog
├── AppInitialization.java      # wires everything, starts threads & hunting loop
├── global/
│   ├── Store.java              # singleton — central state hub (all coords, flags, constants)
│   ├── Database.java           # singleton — H2 operations (create/insert/select waypoints)
│   ├── DBConnection.java       # singleton — JDBC connection
│   ├── KeyboardHook.java       # singleton — global keyboard listener (system-hook)
│   ├── MouseHook.java          # singleton — global mouse listener
│   └── Log.java                # singleton — logging
├── model/
│   ├── Waypoint.java           # data: id, type, direction, baseImage, goalImage, phrase
│   └── Item.java               # data: name, buy/sell prices, NPCs
├── modules/
│   ├── Hunting.java            # main hunting loop (walk → attack → loot cycle)
│   ├── Cavebot.java            # waypoint execution engine
│   ├── CavebotActions.java     # per-waypoint-type actions (rope, shovel, ladder, talk, etc.)
│   ├── Looting.java            # shift+right-click in 8 directions after kill
│   ├── MovementDetector.java   # minimap pixel-diff to detect walk completion/stuck
│   ├── Screenshot.java         # F12 → read PNG → delete → return BufferedImage
│   ├── Healing.java            # background thread — HP monitoring & heal
│   ├── Food.java               # background thread — periodic eating
│   ├── SpellCaster.java        # background thread — periodic spell
│   ├── AntiLogout.java         # background thread — prevent AFK kick
│   ├── MarketMoneyMaker.java   # automated market buy/sell with OCR
│   ├── CollectItemsToSell.java # gather items from depot to sell
│   ├── OptimizeItemList.java   # analyze market prices
│   ├── Setup.java              # initial calibration
│   └── Waypoint.java           # waypoint recording (mode 0)
├── templates/
│   └── PausableRunnable.java   # base for background threads (pause/resume)
└── util/
    ├── ImageProcessor.java     # brute-force pixel comparison, crop, diff%
    ├── Keyboard.java           # AWT Robot key press/release helpers
    ├── Mouse.java              # AWT Robot mouse move/click helpers
    ├── Character.java          # character state queries
    ├── FileSystem.java         # file I/O helpers
    ├── OCRHelper.java          # Tesseract wrapper
    ├── ApplicationWindow.java  # window focus/positioning via JNA
    ├── AreaSelector.java       # on-screen rectangle selection
    └── MouseCoordinates.java   # coordinate helpers
```

### Resources

- `images/` — minimapCross_4x.png, battleListCrop.png
- `scripts/` — H2 database files (waypoint scripts)
- `xml/` — item XML files for market operations

## Architecture Patterns

- **Singletons**: Store, Database, DBConnection, KeyboardHook, MouseHook, Log — all via `getInstance()`
- **Store.java** is the central state hub — all modules read/write coordinates, flags, and constants through it
- **Thread model**: background threads (Healing, Food, SpellCaster, AntiLogout) extend `PausableRunnable`; the main hunting loop runs `while(true)` on the main thread
- **Screenshot pipeline**: press F12 → wait for PNG in Tibia's screenshot folder → read it → delete the file → return `BufferedImage`. This is expensive; reuse the image when possible instead of taking multiple screenshots
- **Image matching**: brute-force pixel-by-pixel comparison in `ImageProcessor`; returns a diff percentage

## Bot Modes

Selected at startup via dialog (0–5):

| Mode | Name | Description |
|------|------|-------------|
| 0 | Create Cavebot Script | Record waypoints via hotkeys |
| 1 | Run Cavebot | Execute waypoint loop with hunting/healing/food |
| 2 | Run Spell Caster | Periodic spell casting, optional cavebot |
| 3 | Market Money Maker | Automated market trading with OCR |
| 4 | Sell Items | Collect items and sell to NPCs |
| 5 | Optimize Item List | Analyze market prices |

## Core Systems

### Screenshot

Press F12 → Tibia saves a PNG to its screenshot folder → bot reads & deletes it → returns `BufferedImage`. The path is derived from `%USERPROFILE%`.

### Waypoints

7 types: `WALK`, `ROPE`, `SHOVEL`, `LADDER`, `HOLE`, `TALK`, `SEQ_CLICKS`
4 directions: N, E, S, W

WALK waypoints store a 16x16 minimap tile. Arrival is detected when the current minimap region has <=1% pixel diff from the stored goal image.

### Movement Detection

Compares a 20x20 sample from the minimap between frames.

- `MOVEMENT_THRESHOLD` = 0.005 (0.5% diff means still moving)
- `STOPPED_TIMEOUT_MS` = 1000 (must be stopped for 1s to confirm arrival)
- Call `resetForNewWalk()` when clicking the minimap or starting combat

### Battle Detection

Reads pixel color in the battle window area:

- `#444444` = no monster present
- `#FF0000` / `#FF8080` = monster is attacking / being attacked
- Stuck monster timeout: 30 seconds

### Looting

After a monster dies: Shift + right-click in 8 directions around the character, 40ms between clicks.

## Keyboard Hotkeys

### Bot Control
- **F5**: Exit bot

### Script Creation (Mode 0)
- **7/8/9/0**: Direction — North / East / South / West
- **Delete**: Walk waypoint
- **Page Up**: Rope waypoint
- **Page Down**: Shovel waypoint
- **Home**: Ladder waypoint
- **End**: Hole/Ramp waypoint
- **1**: Talk waypoint
- **2**: Sequential clicks waypoint

### In-Game (configured in Tibia)
- **F8**: Healing spell
- **F9**: Food
- **F10**: Shovel
- **F11**: Rope
- **F12**: Screenshot

## Hardcoded UI Coordinates (1920x1080)

- Battle window: `Rectangle(1737, 370, 37, 305)`
- Character position: `Rectangle(866, 462, 1, 1)`
- Minimap: dynamically calculated from screen width

## Key Constants (Store.java)

```
WAYPOINT_BLOCK_SIZE    = 16
MAP_INNER_WIDTH        = 106
MAP_INNER_HEIGHT       = 109
AMOUNT_MONSTERS_VISIBLE_IN_BATTLE = 13
MOVEMENT_SAMPLE_SIZE   = 20
MOVEMENT_SAMPLE_X      = 43
MOVEMENT_SAMPLE_Y      = 44
```

## Database

H2 embedded database.

- JDBC URL: `jdbc:h2:./scripts/<scriptName>`
- User: `dragonbot`, no password
- **Waypoints table**: `id` (int), `type` (varchar), `direction` (varchar), `base_image` (blob), `goal_image` (blob), `phrase` (varchar)

## Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| system-hook | 3.7 | Global keyboard/mouse hooks |
| H2 | 1.4.200 | Embedded database |
| JNA | 5.5.0 | Windows API access |
| tess4j | 3.4.2 | Tesseract OCR (requires C:/Tesseract) |
| thumbnailator | 0.4.8 | Image resizing |
| dorkbox-Notify | 3.7 | Desktop notifications |
| commons-io | 2.6 | File utilities |
| JUnit | 4.12 | Testing |
