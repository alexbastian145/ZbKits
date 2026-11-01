# zbKits 1.0.4 – changes vs 1.0.3

- Removed Skript support: `hooks/skript/*`, the `SKRIPT` entry in `SupportedPlugins`, and the `Skript` softdepend.
- Targets Paper 26.1 – 26.2 (`api-version: '26.1'`, Paper API `26.2.build.132-stable`, Java 25).
- `Attribute.GENERIC_MAX_HEALTH`, `Enchantment.LUCK`, `Enchantment.getByKey`, `EntityType.ENDER_CRYSTAL`
  replaced with version-safe lookups (`dev.viper.utils.compat.Compat`, `EnderCrystal` instanceof).
- `Versions` now parses `26.x` / pre-release strings; `ReflectionUtil.minecraftVersion()` understands the new numbering.
- Command map is read via `Bukkit.getCommandMap()` instead of reflecting on CraftServer.
- Metrics uses `getPluginMeta()`.
- Cleaned decompiler artefacts (`$values()` methods, broken anonymous class in `KitUtil`) so the sources recompile.
- Dropped the shaded Lombok / Brigadier / Commodore copies from the sources; Paper supplies Brigadier.
