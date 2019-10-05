package fenixtweaks.module.effects.event;

import fenixtweaks.ModFenixTweaks;
import fenixtweaks.module.effects.EffectUtil;
import fenixtweaks.module.effects.ModuleEffectsConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = ModFenixTweaks.MOD_ID)
public class WorldTickEventHandler {

  private static int TICK_COUNTER = 0;

  @SubscribeEvent
  public static void on(TickEvent.WorldTickEvent event) {

    if (event.side == Side.CLIENT || event.world.isRemote) {
      return;
    }

    TICK_COUNTER += 1;

    if (TICK_COUNTER < 20) {
      return;
    }

    TICK_COUNTER = 0;

    for (EntityPlayer playerEntity : event.world.playerEntities) {
      Collection<PotionEffect> activePotionEffects = playerEntity.getActivePotionEffects();

      for (PotionEffect activePotionEffect : activePotionEffects) {
        Potion potion = activePotionEffect.getPotion();
        ResourceLocation resourceLocation = potion.getRegistryName();

        if (resourceLocation == null) {
          continue;
        }

        String resourceLocationString = resourceLocation.toString();

        for (String effect : ModuleEffectsConfig.EFFECTS) {

          if (resourceLocationString.equals(effect)
              && activePotionEffect.getDuration() < Integer.MAX_VALUE) {
            playerEntity.addPotionEffect(EffectUtil.duplicatePotionEffect(activePotionEffect));
            break;
          }
        }
      }
    }
  }
}
