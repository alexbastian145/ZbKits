# zbKits

A modern kits plugin for Crystal PvP servers on Paper. Players save up to a configurable number of kits, load them with one command, and optionally get automatically re-kitted after a kill.

- **Server:** Paper or Folia, Minecraft 1.21 - 26.x (Java 21+)
- **Soft dependency:** WorldGuard (blacklisted regions)

## Features

- Player kits (`/kit`, `/kit1` ... `/kitN`) with a kit editor and a per-kit ender chest editor
- Virtual kit room with categories, edited by admins
- One premade kit, saved by admins and loadable by everyone
- Auto rekit: reloads your chosen kit after you kill someone (also works for crystal and respawn anchor kills)
- Admin tools to view or clear any player's kits
- WorldGuard blacklisted regions where kit commands are blocked
- Every player-facing message lives in `config.yml`; menus are fully configurable in `menus/*.yml`
- Developer API with kit save/load and kit room events

## Commands

| Command | Description | Permission |
|---|---|---|
| `/kit` (`/k`) | Open the kits menu | |
| `/kit1` ... `/kitN` (`/k1` ...) | Load a kit directly | `zbkits.kitN` (default: everyone) |
| `/premadekit` | Load the premade kit | |
| `/premadekit save` | Save your inventory as the premade kit | `zbkits.admin` |
| `/autorekit` (`/autokit`) | Open the auto rekit menu | `zbkits.autorekit` |
| `/kitroomadmin <add/clear> <category>` | Edit or clear a kit room category | `zbkits.admin` |
| `/kitsadmin view <player>` | View a player's kits | `zbkits.admin` |
| `/kitsadmin clear <player> <kit name>` | Clear a player's kit | `zbkits.admin` |
| `/zbkits reload` (`/zbk reload`) | Reload `config.yml` and the menu files | `zbkits.admin` |

Permissions `zbkits.kit1` ... `zbkits.kitN` are registered automatically and default to true. Take one away with a permission plugin (for example LuckPerms) to lock that kit; this applies to the commands, the kits menu and auto rekit.

The number of kit commands (`kits` in `config.yml`) is read at startup, so changing it needs a server restart. Everything else reloads with `/zbkits reload`.

## Configuration

- `config.yml`: kit count, ender chest handling, broadcasts, all messages, blacklisted WorldGuard regions
- `menus/*.yml`: titles, sizes and items for every menu
- `data/players/<uuid>.yml`: one file per player with their kits, kit ender chests and auto rekit settings (generated, do not edit while the server is running). Online players are kept in memory and only the file of the player that changed is written.
- `data/kitroom.yml`, `data/premadekit.yml`: the kit room and the premade kit

Servers that used an older version are migrated automatically on the first start: the old `data/kits.yml`, `data/enderchest.yml` and `data/autorekit.yml` are split into player files and renamed to `*.migrated`.

## Building

```
mvn clean package
```

The jar is written to `target/`. Requires JDK 21. The plugin is compiled against the Paper 1.21.11 API and loads on 1.21 and newer.

## Folia

zbKits declares `folia-supported: true`. It never uses the Bukkit scheduler: delayed work for a player runs on that player's own scheduler, and a kit is only ever loaded by the thread that owns the player. On Paper the same calls run on the main thread, so behaviour is unchanged.

## Support

https://discord.gg/dvRN4FabDq
