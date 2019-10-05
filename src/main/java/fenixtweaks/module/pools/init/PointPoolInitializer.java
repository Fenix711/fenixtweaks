package fenixtweaks.module.pools.init;

import com.google.gson.JsonElement;
import fenixtweaks.ModFenixTweaks;
import fenixtweaks.module.pools.ModulePools;
import fenixtweaks.module.pools.pool.*;
import fenixtweaks.module.pools.pool.impl.BlockHarvestPool;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PointPoolInitializer {

  public static void initialize(File modConfigurationDirectory, Logger logger) {

    Path modPath = Paths.get(modConfigurationDirectory.toString(), ModFenixTweaks.MOD_ID);
    Path poolsPath = Paths.get(modPath.toString(), "pools");

    // Create the pools path if it doesn't exist
    if (!Files.exists(poolsPath)) {

      try {
        Files.createDirectory(poolsPath);

      } catch (IOException e) {
        logger.error("", e);
        return;
      }
    }

    if (!Files.isDirectory(poolsPath)) {
      return;
    }

    PointPoolLoader loader = new PointPoolLoader();
    PointPoolLocator locator = new PointPoolLocator();

    // Register adapters
    Map<String, IPointPoolJsonElementAdapter<? extends PointPoolBase>> adapterMap = new HashMap<>();
    adapterMap.put(EnumPointPoolType.BlockHarvest.getName(), new BlockHarvestPool.Adapter());
    PointPoolAdapter adapter = new PointPoolAdapter(adapterMap);

    // Locate the json files
    List<Path> paths = locator.locatePointPools(poolsPath);

    for (Path path : paths) {

      try {

        // Load and adapt the file
        JsonElement jsonElement = loader.load(path);
        PointPoolBase pointPool = adapter.adapt(jsonElement);

        // Register the adapted pool
        String name = modPath.relativize(path).toString();
        pointPool.setRegistryName(ModFenixTweaks.MOD_ID, name.substring(0, name.length() - 5));
        ModulePools.Registries.POINT_POOLS.register(pointPool);
        MinecraftForge.EVENT_BUS.register(pointPool);
        logger.info("Registered point pool: " + pointPool.getRegistryName());

      } catch (Exception e) {
        logger.error("Error loading point pool file: " + path, e);
      }
    }
  }

  private PointPoolInitializer() {
    //
  }
}
