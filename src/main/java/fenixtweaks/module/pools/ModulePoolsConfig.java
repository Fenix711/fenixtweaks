package fenixtweaks.module.pools;

import fenixtweaks.ModFenixTweaks;
import net.minecraftforge.common.config.Config;

@Config(modid = ModFenixTweaks.MOD_ID, name = ModFenixTweaks.MOD_ID + "/point-pools")
public class ModulePoolsConfig {

  @Config.Comment({
      "Set to true to enable debug logging to chat.",
      "Default: " + false
  })
  public static boolean CHAT_DEBUG_OUTPUT = true;

  @Config.Comment({
      "Set to true to enable sending a chat message to a player when the player levels up.",
      "Default: " + false
  })
  public static boolean CHAT_LEVEL_MESSAGE = true;

  @Config.Comment({
      "Set to true to enable sending a chat message to a player when the player's points increase.",
      "Default: " + false
  })
  public static boolean CHAT_POINT_MESSAGE = true;

  @Config.Comment({
      "Set to true to allow everyone to use the command to display"
  })
  public static boolean COMMAND_ALLOW_EVERYONE_SHOW = false;
}
