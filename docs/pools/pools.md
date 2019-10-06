### What are point pools?

Point pools allow storing point and level values on players and awarding points via different criteria depending on the type of the point pool. When a player acquired a defined number of points in a point pool, the player's level in that pool goes up. When a player levels up, goals can be triggered that execute commands.
 
### Where are the files?

Point pools are defined by `.json` files located in `config/fenixtweaks/pools`. Each file will create a unique point pool and register that pool with a `ResourceLocation` of `fenixtweaks:<name>`, where `<name>` is the file's path relative to the mod's config folder sans the `.json` suffix.

For example, a file placed in the following location `config/fenixtweaks/pools/mining.json` will be registered with the `ResourceLocation` of `fenixtweaks:pools/mining`.