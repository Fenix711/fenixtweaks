package fenixtweaks.module.pools.init;

import fenixtweaks.module.pools.capability.IPointPoolPlayerData;
import fenixtweaks.module.pools.capability.PointPoolPlayerData;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class PlayerDataCapabilityInitializer {

  public static void initialize() {

    CapabilityManager.INSTANCE.register(IPointPoolPlayerData.class, new PointPoolPlayerData(), PointPoolPlayerData::new);
  }

  private PlayerDataCapabilityInitializer() {
    //
  }
}
