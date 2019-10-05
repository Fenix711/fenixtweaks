package fenixtweaks.module.pools.pool;

import fenixtweaks.module.pools.ModulePoolsConfig;
import fenixtweaks.module.pools.capability.CapabilityPointPools;
import fenixtweaks.module.pools.capability.IPointPoolPlayerData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class PointPoolBase
    extends IForgeRegistryEntry.Impl<PointPoolBase> {

  private final String name;
  private final PointPoolGrowth growth;
  private final List<PointPoolGoal> goals;

  public PointPoolBase(String name, PointPoolGrowth growth, List<PointPoolGoal> goals) {

    this.name = name;
    this.growth = growth;
    this.goals = goals;
  }

  public String getName() {

    return this.name;
  }

  /**
   * Returns the levels for the given player or -1 if the player entity doesn't
   * have the correct capability.
   *
   * @param player the player
   * @return the levels for the given player
   */
  public int getLevels(EntityPlayer player) {

    IPointPoolPlayerData capability = CapabilityPointPools.get(player);

    if (capability == null) {
      return -1;
    }

    ResourceLocation resourceLocation = this.getRegistryName();
    return capability.getLevels(resourceLocation);
  }

  /**
   * Returns the points for the given player or -1 if the player entity doesn't
   * have the correct capability.
   *
   * @param player the player
   * @return the points for the given player
   */
  public double getPoints(EntityPlayer player) {

    IPointPoolPlayerData capability = CapabilityPointPools.get(player);

    if (capability == null) {
      return -1;
    }

    ResourceLocation resourceLocation = this.getRegistryName();
    return capability.getPoints(resourceLocation);
  }

  /**
   * Remove levels from the given player.
   * <p>
   * Does not trigger goals.
   * <p>
   * Do not use this method with negative numbers.
   *
   * @param player the player
   * @param levels the levels to remove (positive number)
   * @return the new level total
   */
  public int removeLevels(EntityPlayer player, int levels) {

    if (levels < 0) {
      throw new IllegalArgumentException("This method doesn't support a negative value for levels");
    }

    IPointPoolPlayerData capability = CapabilityPointPools.get(player);

    if (capability == null) {
      return levels;
    }

    ResourceLocation resourceLocation = this.getRegistryName();
    int result = capability.addLevels(resourceLocation, -levels);

    if (ModulePoolsConfig.CHAT_POINT_MESSAGE || ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
      player.sendMessage(new TextComponentTranslation("message.fenixtweaks.levels.lost", result, this.name));
    }

    return result;
  }

  /**
   * Add levels to the player.
   * <p>
   * Does not trigger goals.
   * <p>
   * Do not use this method with negative numbers.
   *
   * @param player the player
   * @param levels the levels to add
   * @return the new level total
   */
  public int addLevels(EntityPlayer player, int levels) {

    if (levels < 0) {
      throw new IllegalArgumentException("This method doesn't support a negative value for levels");
    }

    IPointPoolPlayerData capability = CapabilityPointPools.get(player);

    if (capability == null) {
      return levels;
    }

    ResourceLocation resourceLocation = this.getRegistryName();
    int result = capability.addLevels(resourceLocation, levels);

    if (ModulePoolsConfig.CHAT_POINT_MESSAGE || ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
      player.sendMessage(new TextComponentTranslation("message.fenixtweaks.levels.gained", result, this.name));
    }

    return result;
  }

  /**
   * Removes points from the player.
   * <p>
   * Do not use this method with negative numbers.
   *
   * @param player the player
   * @param points the points to remove (positive number)
   * @return the new point total
   */
  public double removePoints(EntityPlayer player, double points) {

    if (points < 0) {
      throw new IllegalArgumentException("This method doesn't support a negative value for points");
    }

    IPointPoolPlayerData capability = CapabilityPointPools.get(player);

    if (capability == null) {
      return points;
    }

    ResourceLocation resourceLocation = this.getRegistryName();
    double result = capability.addPoints(resourceLocation, -points);

    if (ModulePoolsConfig.CHAT_POINT_MESSAGE || ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
      player.sendMessage(new TextComponentTranslation("message.fenixtweaks.points.lost", points, this.name));
    }

    return result;
  }

  /**
   * Awards points to the player for this point pool and checks for level up.
   * If the player has leveled up, the goals are triggered.
   * <p>
   * Do not use this method with negative numbers.
   *
   * @param player the player
   * @param points the points to add
   * @return the new point total
   */
  public double addPoints(EntityPlayer player, double points) {

    if (points < 0) {
      throw new IllegalArgumentException("This method doesn't support a negative value for points");
    }

    IPointPoolPlayerData capability = CapabilityPointPools.get(player);

    if (capability == null) {
      return points;
    }

    ResourceLocation resourceLocation = this.getRegistryName();
    double pointTotal = capability.addPoints(resourceLocation, points);

    if (ModulePoolsConfig.CHAT_POINT_MESSAGE || ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
      player.sendMessage(new TextComponentTranslation("message.fenixtweaks.points.gained", points, this.name));
    }

    int levels = capability.getLevels(resourceLocation);
    double pointsForNextLevel = this.getPointsForLevel(levels + 1);
    int sanity = 100;

    if (pointTotal >= pointsForNextLevel) {

      while (--sanity > 0 && pointTotal >= pointsForNextLevel) {
        pointTotal = capability.addPoints(resourceLocation, -pointsForNextLevel);
        levels = capability.addLevels(resourceLocation, 1);

        this.triggerGoals(player, levels);
      }

      if (ModulePoolsConfig.CHAT_LEVEL_MESSAGE || ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
        player.sendMessage(new TextComponentTranslation("message.fenixtweaks.levels.gained", capability.getLevels(resourceLocation), this.name));
      }
    }

    return pointTotal;
  }

  /**
   * Returns the points needed for the given level.
   *
   * @param level the level
   * @return the points needed for the given level
   */
  public double getPointsForLevel(int level) {

    double slope = this.growth.getSlope();
    double exponent = this.growth.getExponent();
    double intercept = this.growth.getIntercept();
    return slope * Math.pow(level, exponent) + intercept;
  }

  /**
   * Iterate through the pool's goals and if the goal level matches the given
   * level, the goal's command is executed.
   *
   * @param player the player that just leveled up
   * @param level  the player's level after leveling up
   */
  private void triggerGoals(EntityPlayer player, int level) {

    MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

    for (PointPoolGoal goal : this.goals) {

      if (goal.getLevel() == level) {
        ResourceLocation resourceLocation = this.getRegistryName();

        if (resourceLocation == null) {
          continue;
        }

        String name = resourceLocation.toString();
        CommandSender sender = new CommandSender(name, player.world, server, player);
        server.commandManager.executeCommand(sender, goal.getCommand());

        if (ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
          player.sendMessage(new TextComponentString("[debug] Triggered: " + goal.getCommand()));
        }
      }
    }
  }

  /**
   * A basic {@link ICommandSender} implementation.
   * <p>
   * This allows us to execute commands on the player's behalf that the
   * server would otherwise not allow.
   */
  private static class CommandSender
      implements ICommandSender {

    private final String name;
    private final World world;
    private final MinecraftServer server;
    private final EntityPlayer player;

    private CommandSender(String name, World world, MinecraftServer server, EntityPlayer player) {

      this.name = name;
      this.world = world;
      this.server = server;
      this.player = player;
    }

    @Nonnull
    @Override
    public String getName() {

      return this.name;
    }

    @Override
    public boolean canUseCommand(int permLevel, @Nonnull String commandName) {

      return true;
    }

    @Nonnull
    @Override
    public World getEntityWorld() {

      return this.world;
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {

      return this.server;
    }

    @Nullable
    @Override
    public Entity getCommandSenderEntity() {

      return this.player;
    }
  }
}
