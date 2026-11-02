# zbKits

A modern kits plugin for Crystal PvP servers on Paper. Players save up to a configurable number of kits, load them with one command, and optionally get automatically re-kitted after a kill.

- **Server:** Paper, Minecraft 1.21 - 26.x (Java 21+)
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
| `/kit1` ... `/kitN` (`/k1` ...) | Load a kit directly | `zbkits.kitN` |
| `/premadekit` | Load the premade kit | |
| `/premadekit save` | Save your inventory as the premade kit | `zbkits.admin` |
| `/autorekit` (`/autokit`) | Open the auto rekit menu | `zbkits.autorekit` |
| `/kitroomadmin <add/clear> <category>` | Edit or clear a kit room category | `zbkits.admin` |
| `/kitsadmin view <player>` | View a player's kits | `zbkits.admin` |
| `/kitsadmin clear <player> <kit name>` | Clear a player's kit | `zbkits.admin` |
| `/zbkits reload` (`/zbk reload`) | Reload `config.yml` and the menu files | `zbkits.admin` |

The number of kit commands (`kits` in `config.yml`) is read at startup, so changing it needs a server restart. Everything else reloads with `/zbkits reload`.

## Configuration

- `config.yml`: kit count, broadcasts, all messages, blacklisted WorldGuard regions
- `menus/*.yml`: titles, sizes and items for every menu
- `data/*.yml`: saved kits, ender chests, kit room, premade kit and auto rekit settings (generated, do not edit while the server is running)

## Building

```
mvn clean package
```

The jar is written to `target/`. Requires JDK 21.

## Support

https://discord.gg/dvRN4FabDq
