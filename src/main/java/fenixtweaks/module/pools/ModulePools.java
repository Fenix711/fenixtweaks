package fenixtweaks.module.pools;

import fenixtweaks.module.pools.command.CommandPoints;
import fenixtweaks.module.pools.command.CommandShow;
import fenixtweaks.module.pools.init.PlayerDataCapabilityInitializer;
import fenixtweaks.module.pools.init.PointPoolInitializer;
import fenixtweaks.module.pools.pool.PointPoolBase;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class ModulePools {

  private static final Logger LOGGER = LogManager.getLogger(ModulePools.class);
  private static File modConfigurationDirectory;

  public static void onPreInitialization(FMLPreInitializationEvent event) {

    PlayerDataCapabilityInitializer.initialize();
    modConfigurationDirectory = event.getModConfigurationDirectory();
  }

  public static void onInitialization(FMLInitializationEvent event) {

    PointPoolInitializer.initialize(modConfigurationDirectory, LOGGER);
  }

  public static void onServerStarting(FMLServerStartingEvent event) {

    event.registerServerCommand(new CommandPoints());
    event.registerServerCommand(new CommandShow());
  }

  public static class Registries {

    public static final IForgeRegistryModifiable<PointPoolBase> POINT_POOLS;

    static {
      POINT_POOLS = null;
    }
  }
}
