package fenixtweaks.module.pools;

import fenixtweaks.ModFenixTweaks;
import net.minecraftforge.common.config.Config;

@Config(modid = ModFenixTweaks.MOD_ID, name = ModFenixTweaks.MOD_ID + "/point-pools")
public class ModulePoolsConfig {

  @Config.Comment({
      "Set to true to enable debug logging to chat.",
      "Default: " + false
  })
  public static boolean CHAT_DEBUG_OUTPUT = false;

  @Config.Comment({
      "Set to true to enable sending a chat message to a player when the player levels up.",
      "Default: " + false
  })
  public static boolean CHAT_LEVEL_MESSAGE = false;

  @Config.Comment({
      "Set to true to enable sending a chat message to a player when the player's points increase.",
      "Default: " + false
  })
  public static boolean CHAT_POINT_MESSAGE = false;

  @Config.Comment({
      "Set to true to enable sending a chat message that contains a player's current points when the player's points increase."
  })
  public static boolean CHAT_POINT_TOTAL_MESSAGE = false;
}
