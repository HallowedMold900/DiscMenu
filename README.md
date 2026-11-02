# DiscMenu

A Paper plugin (1.20.x, Java 17+) that gives players custom music discs for free once per hour through a GUI menu. (Use [CustomDiscs SVC](https://modrinth.com/project/1yowwDpk))

## Features

- `/discsmenu` — opens a menu with all added discs. Clicking a disc gives it to the player if the cooldown (default 1 hour) has expired.
- `/discsadmin add` — adds the disc currently held in the main hand to the menu.
- `/discsadmin remove <number>` — removes a disc from the menu by its number.
- `/discsadmin list` — lists all discs with their numbers.
- `/discsadmin reload` — reloads config and discs without restarting the server.
- If there are more than 45 discs, the menu is automatically split into pages with Prev/Next buttons.

## Permissions

- `discs.use` (default: everyone) — access to `/discsmenu`.
- `discs.admin` (default: op) — access to `/discsadmin`.

## How to add a disc

1. Create an item with the desired sound/name — easiest via a custom items plugin, a command block `/give` with NBT (`minecraft:custom_data`/`resolvable_profile` etc., depending on version), or a resource pack with a custom model on a regular music disc.
2. Hold this item in your main hand.
3. Run `/discsadmin add`.
4. The disc will appear in `/discsmenu` for all players.