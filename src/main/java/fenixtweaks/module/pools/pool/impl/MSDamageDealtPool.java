package fenixtweaks.module.pools.pool.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.robertx22.api.MineAndSlashEvents;
import fenixtweaks.lib.ItemParser;
import fenixtweaks.module.pools.ModulePoolsConfig;
import fenixtweaks.module.pools.capability.CapabilityPointPools;
import fenixtweaks.module.pools.capability.IPointPoolPlayerData;
import fenixtweaks.module.pools.pool.IPointPoolJsonElementAdapter;
import fenixtweaks.module.pools.pool.PointPoolBase;
import fenixtweaks.module.pools.pool.PointPoolGoal;
import fenixtweaks.module.pools.pool.PointPoolGrowth;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class MSDamageDealtPool
    extends PointPoolBase {

  private final Map<Ingredient, Double> itemDamage;
  private final double defaultPoints;

  public MSDamageDealtPool(String name, PointPoolGrowth growth, List<PointPoolGoal> goals, Map<Ingredient, Double> itemDamage, double defaultPoints) {

    super(name, growth, goals);
    this.itemDamage = itemDamage;
    this.defaultPoints = defaultPoints;
  }

  /**
   * Responsible for listening for M&S damage events and awarding points
   * to the player.
   */
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void on(MineAndSlashEvents.DamageEvent event) {

    EntityLivingBase source = event.getSource();
    EntityLivingBase target = event.getTarget();
    float damage = event.getAdjustedDamage();

    if (!(source instanceof EntityPlayerMP) || target == null) {
      return;
    }

    EntityPlayer player = (EntityPlayer) source;
    float points = damage;

    ItemStack heldItemMainhand = player.getHeldItemMainhand();
    Ingredient toMatch = null;
    boolean matched = false;

    for (Map.Entry<Ingredient, Double> entry : this.itemDamage.entrySet()) {
      double pointMultiplier = entry.getValue();
      toMatch = entry.getKey();

      if (toMatch.apply(heldItemMainhand)) {
        points *= pointMultiplier;
        matched = true;
        break;
      }
    }

    if (matched) {
      this.addPoints(player, points);

    } else if (this.defaultPoints > 0) {
      this.addPoints(player, points * this.defaultPoints);
    }

    if (ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
      IPointPoolPlayerData data = CapabilityPointPools.get(player);

      if (data != null) {
        ResourceLocation resourceLocation = this.getRegistryName();
        double totalPoints = data.getPoints(resourceLocation);
        int levels = data.getLevels(resourceLocation);
        double pointsForLevel = this.getPointsForLevel(levels + 1);

        source.sendMessage(new TextComponentString("[debug] Pool: " + resourceLocation));
        source.sendMessage(new TextComponentString("[debug] Held: " + heldItemMainhand));
        source.sendMessage(new TextComponentString("[debug] Matched: " + (!matched ? "null" : Arrays.toString(toMatch.getMatchingStacks()))));
        source.sendMessage(new TextComponentString("[debug] Points: " + points));
        source.sendMessage(new TextComponentString("[debug] Total Points: " + totalPoints + " / " + pointsForLevel));
      }
    }
  }

  /**
   * Responsible for adapting a {@link JsonElement} into a {@link MSDamageDealtPool}.
   */
  public static class Adapter
      implements IPointPoolJsonElementAdapter<MSDamageDealtPool> {

    private static final Logger LOGGER = LogManager.getLogger(Adapter.class);

    private static final Gson GSON = new Gson();

    @Override
    public MSDamageDealtPool adapt(JsonElement jsonElement) {

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
      Map<Ingredient, Double> itemDamage = entries.stream()
          .map(entry -> {
            ItemParser.ParseResult parseResult = ItemParser.INSTANCE.parse(entry.getKey());
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(parseResult.getDomain(), parseResult.getPath()));

            if (item == null) {
              LOGGER.error("Unable to locate item for " + entry.getKey());
              return null;
            }

            ItemStack itemStack = new ItemStack(item, 1, parseResult.getMeta());
            Ingredient ingredient = Ingredient.fromStacks(itemStack);
            double points = entry.getValue().getAsDouble();
            return new Tuple<>(ingredient, points);
          })
          .filter(Objects::nonNull)
          .collect(Collectors.toMap(Tuple::getFirst, Tuple::getSecond));

      double defaultPoints = 1;

      if (jsonData.has("default")) {
        defaultPoints = jsonData.getAsJsonPrimitive("default").getAsDouble();
      }

      return new MSDamageDealtPool(name, growth, goals, itemDamage, defaultPoints);
    }
  }

}
