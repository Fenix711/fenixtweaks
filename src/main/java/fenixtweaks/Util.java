package fenixtweaks;

import net.minecraft.potion.PotionEffect;

public final class Util {

  public static PotionEffect duplicatePotionEffect(PotionEffect activePotionEffect) {

    return new PotionEffect(
        activePotionEffect.getPotion(),
        Integer.MAX_VALUE,
        activePotionEffect.getAmplifier(),
        activePotionEffect.getIsAmbient(),
        activePotionEffect.doesShowParticles()
    );
  }

  private Util() {
    //
  }
}
