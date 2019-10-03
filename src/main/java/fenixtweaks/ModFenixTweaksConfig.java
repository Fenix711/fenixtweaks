package fenixtweaks;

import net.minecraftforge.common.config.Config;

@Config(modid = ModFenixTweaks.MOD_ID, name = ModFenixTweaks.MOD_ID + "/persistent-effects")
public class ModFenixTweaksConfig {

  @Config.Comment({
      "Effects added to this list will never expire from a player and persist ",
      "through player death.",
      "",
      "The expected format is: (domain):(path)",
      "For example: minecraft:regeneration"
  })
  public static String[] EFFECTS = new String[]{"minecraft:regeneration"};

}
