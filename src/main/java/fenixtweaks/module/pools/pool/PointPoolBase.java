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

  /**
   * Awards points to the player for this point pool and checks for level up.
   * If the player has leveled up, the goals are triggered.
   * <p>
   * Do not use this method to remove points.
   *
   * @param player the player
   * @param points the points to add
   */
  protected void addPoints(EntityPlayer player, double points) {

    IPointPoolPlayerData capability = CapabilityPointPools.get(player);

    if (capability == null) {
      return;
    }

    ResourceLocation resourceLocation = this.getRegistryName();
    double pointTotal = capability.addPoints(resourceLocation, points);

    if (ModulePoolsConfig.CHAT_POINT_MESSAGE || ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
      player.sendMessage(new TextComponentTranslation("message.fenixtweaks.points", points, this.name));
    }

    int levels = capability.getLevels(resourceLocation);
    double pointsForNextLevel = this.getPointsForLevel(levels + 1);
    int sanity = 100;

    if (pointTotal >= pointsForNextLevel) {

      while (--sanity > 0 && pointTotal >= pointsForNextLevel) {
        pointTotal = capability.addPoints(resourceLocation, -pointsForNextLevel);
        levels = capability.addLevels(resourceLocation, 1);

        if (ModulePoolsConfig.CHAT_LEVEL_MESSAGE || ModulePoolsConfig.CHAT_DEBUG_OUTPUT) {
          player.sendMessage(new TextComponentTranslation("message.fenixtweaks.level.up", capability.getLevels(resourceLocation), this.name));
        }

        this.triggerGoals(player, levels);
      }
    }
  }

  /**
   * Returns the points needed for the given level.
   *
   * @param level the level
   * @return the points needed for the given level
   */
  protected double getPointsForLevel(int level) {

    // TODO: growth
    return level * this.growth.getBase();
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
