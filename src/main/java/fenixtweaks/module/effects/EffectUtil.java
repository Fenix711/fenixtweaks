package fenixtweaks.module.effects;

import net.minecraft.potion.PotionEffect;

public final class EffectUtil {

  public static PotionEffect duplicatePotionEffect(PotionEffect activePotionEffect) {

    return new PotionEffect(
        activePotionEffect.getPotion(),
        Integer.MAX_VALUE,
        activePotionEffect.getAmplifier(),
        activePotionEffect.getIsAmbient(),
        activePotionEffect.doesShowParticles()
    );
  }

  private EffectUtil() {
    //
  }
}
