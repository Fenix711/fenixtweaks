package fenixtweaks.module.pools.pool.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import fenixtweaks.lib.BlockMatchParser;
import fenixtweaks.module.pools.ModulePoolsConfig;
import fenixtweaks.module.pools.capability.CapabilityPointPools;
import fenixtweaks.module.pools.capability.IPointPoolPlayerData;
import fenixtweaks.module.pools.pool.IPointPoolJsonElementAdapter;
import fenixtweaks.module.pools.pool.PointPoolBase;
import fenixtweaks.module.pools.pool.PointPoolGoal;
import fenixtweaks.module.pools.pool.PointPoolGrowth;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockHarvestPool
    extends PointPoolBase {

  private final Map<BlockMatchParser.BlockMatchEntry, Double> blockMatchEntries;

  public BlockHarvestPool(String name, PointPoolGrowth growth, List<PointPoolGoal> goals, Map<BlockMatchParser.BlockMatchEntry, Double> blockMatchEntries) {

    super(name, growth, goals);
    this.blockMatchEntries = blockMatchEntries;
  }

  /**
   * Responsible for listening for block harvest events and awarding points
   * to the player.
   */
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void on(BlockEvent.HarvestDropsEvent event) {

    if (event.isCanceled()) {
      return;
    }

    EntityPlayer harvester = event.getHarvester();

    if (harvester == null) {
      return;
    }

    IBlockState blockState = event.getState();

    for (Map.Entry<BlockMatchParser.BlockMatchEntry, Double> entry : this.blockMatchEntries.entrySet()) {

      BlockMatchParser.BlockMatchEntry blockMatchEntry = entry.getKey();

      if (blockMatchEntry.matches(blockState)) {
        this.addPoints(harvester, entry.getValue());

        if (ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
          IPointPoolPlayerData data = CapabilityPointPools.get(harvester);

          if (data != null) {
            ResourceLocation resourceLocation = this.getRegistryName();
            double points = data.getPoints(resourceLocation);
            int levels = data.getLevels(resourceLocation);
            double pointsForLevel = this.getPointsForLevel(levels + 1);

            harvester.sendMessage(new TextComponentString("[debug] Pool: " + resourceLocation));
            harvester.sendMessage(new TextComponentString("[debug] Matched: " + blockState));
            harvester.sendMessage(new TextComponentString("[debug] Points: " + entry.getValue()));
            harvester.sendMessage(new TextComponentString("[debug] Total Points: " + points + " / " + pointsForLevel));
          }
        }
      }
    }
  }

  /**
   * Responsible for adapting a {@link JsonElement} into a {@link BlockHarvestPool}.
   */
  public static class Adapter
      implements IPointPoolJsonElementAdapter<BlockHarvestPool> {

    private static final Gson GSON = new Gson();

    @Override
    public BlockHarvestPool adapt(JsonElement jsonElement) {

      JsonObject json = jsonElement.getAsJsonObject();

      String name = json.getAsJsonPrimitive("name").getAsString();

      PointPoolGrowth growth = GSON.fromJson(json.getAsJsonObject("growth"), PointPoolGrowth.class);

      Type type = new TypeToken<List<PointPoolGoal>>() {
        //
      }.getType();

      List<PointPoolGoal> goals = GSON.fromJson(json.getAsJsonArray("goals"), type);

      JsonObject jsonData = json.getAsJsonObject("data");
      JsonObject jsonPoints = jsonData.getAsJsonObject("points");
      Set<Map.Entry<String, JsonElement>> entries = jsonPoints.entrySet();

      // Convert data to list of block match entries
      Map<BlockMatchParser.BlockMatchEntry, Double> blockMatchEntries = entries.stream()
          .map(entry -> {
            BlockMatchParser.BlockMatchEntry blockMatchEntry = BlockMatchParser.INSTANCE.parse(entry.getKey());
            double points = entry.getValue().getAsDouble();
            return blockMatchEntry != null ? new Tuple<>(blockMatchEntry, points) : null;
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));

      return new BlockHarvestPool(name, growth, goals, blockMatchEntries);
    }
  }

}
