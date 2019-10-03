package fenixtweaks.event;

import fenixtweaks.ModFenixTweaksConfig;
import fenixtweaks.Util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

@Mod.EventBusSubscriber
public class PlayerCloneEventHandler {

  /**
   * If the player dies, copy all the existing effects that should be persisted
   * to the new entity.
   */
  @SubscribeEvent
  public static void on(PlayerEvent.Clone event) {

    if (event.isWasDeath()
        && event.getEntity() instanceof EntityPlayer) {

      EntityPlayer originalEntity = event.getOriginal();
      EntityPlayer newEntity = event.getEntityPlayer();
      Collection<PotionEffect> activePotionEffects = originalEntity.getActivePotionEffects();

      for (PotionEffect activePotionEffect : activePotionEffects) {
        Potion potion = activePotionEffect.getPotion();
        ResourceLocation resourceLocation = potion.getRegistryName();

        if (resourceLocation != null) {
          String resourceLocationString = resourceLocation.toString();

          for (String effect : ModFenixTweaksConfig.EFFECTS) {

            if (resourceLocationString.equals(effect)) {
              newEntity.addPotionEffect(Util.duplicatePotionEffect(activePotionEffect));
              break;
            }
          }
        }
      }
    }
  }
}