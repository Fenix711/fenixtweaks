package fenixtweaks.module.shared.event;

import fenixtweaks.ModFenixTweaks;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = ModFenixTweaks.MOD_ID)
public class ConfigChangedEventHandler {

  @SubscribeEvent
  public static void on(ConfigChangedEvent.OnConfigChangedEvent event) {

    if (event.getModID().equals(ModFenixTweaks.MOD_ID)) {
      ConfigManager.sync(ModFenixTweaks.MOD_ID, Config.Type.INSTANCE);
    }
  }
}
