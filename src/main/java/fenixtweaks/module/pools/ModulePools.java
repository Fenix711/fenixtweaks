package fenixtweaks.module.pools;

import fenixtweaks.module.pools.init.PlayerDataCapabilityInitializer;
import fenixtweaks.module.pools.init.PointPoolInitializer;
import fenixtweaks.module.pools.pool.PointPoolBase;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModulePools {

  private static final Logger LOGGER = LogManager.getLogger(ModulePools.class);

  public static void onPreInitialization(FMLPreInitializationEvent event) {

    PlayerDataCapabilityInitializer.initialize();
    PointPoolInitializer.initialize(event.getModConfigurationDirectory(), LOGGER);
  }

  public static class Registries {

    public static final IForgeRegistryModifiable<PointPoolBase> POINT_POOLS;

    static {
      POINT_POOLS = null;
    }
  }
}
