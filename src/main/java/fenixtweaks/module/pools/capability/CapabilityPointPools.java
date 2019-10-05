package fenixtweaks.module.pools.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityPointPools {

  @CapabilityInject(IPointPoolPlayerData.class)
  public static Capability<IPointPoolPlayerData> POINT_POOLS = null;

  public static IPointPoolPlayerData get(EntityPlayer player) {

    return player.getCapability(POINT_POOLS, null);
  }
}
