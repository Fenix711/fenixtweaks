package fenixtweaks.module.pools.command;

import fenixtweaks.module.pools.ModulePools;
import fenixtweaks.module.pools.pool.PointPoolBase;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandShow
    extends CommandBase {

  @Nonnull
  @Override
  public String getName() {

    return "ftshow";
  }

  @Override
  public int getRequiredPermissionLevel() {

    return 2;
  }

  @Nonnull
  @Override
  public String getUsage(@Nonnull ICommandSender sender) {

    return "commands.fenixtweaks.ftshow.usage";
  }

  @Override
  public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {

    if (args.length < 1 || args.length > 2) {
      throw new WrongUsageException("commands.fenixtweaks.ftshow.usage");
    }

    String argPool = args[0];

    if ("all".equals(args[0].toLowerCase())) {

      EntityPlayer player = (args.length == 2) ? getPlayer(server, sender, args[1]) : getCommandSenderAsPlayer(sender);

      String messageString = ModulePools.Registries.POINT_POOLS.getValuesCollection().stream()
          .map(pool -> this.getMessageStringFromPool(pool, player))
          .collect(Collectors.joining("\n"));

      TextComponentString message = new TextComponentString(messageString);
      sender.sendMessage(message);

    } else {

      PointPoolBase pool = ModulePools.Registries.POINT_POOLS.getValue(new ResourceLocation(argPool));

      if (pool == null) {
        throw new WrongUsageException("commands.fenixtweaks.fail.pool", argPool);
      }

      EntityPlayer player = (args.length == 2) ? getPlayer(server, sender, args[1]) : getCommandSenderAsPlayer(sender);

      String messageString = this.getMessageStringFromPool(pool, player);
      TextComponentString message = new TextComponentString(messageString);
      sender.sendMessage(message);
    }
  }

  private String getMessageStringFromPool(PointPoolBase pool, EntityPlayer player) {

    double points = pool.getPoints(player);
    int levels = pool.getLevels(player);
    double nextPoints = pool.getPointsForLevel(levels + 1);
    String poolName = pool.getName();

    return this.getMessageString(points, levels, nextPoints, poolName);
  }

  private String getMessageString(double points, int levels, double nextPoints, String poolName) {

    return TextFormatting.GOLD + poolName +
        TextFormatting.WHITE + "[" +
        TextFormatting.GREEN + levels +
        TextFormatting.WHITE + "]: " +
        TextFormatting.GREEN + points +
        TextFormatting.WHITE + " / " + nextPoints;
  }

  @Nonnull
  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

    if (args.length == 1) {
      return getListOfStringsMatchingLastWord(
          args,
          Stream.concat(
              Stream.of("all"),
              ModulePools.Registries.POINT_POOLS.getKeys()
                  .stream()
                  .map(ResourceLocation::toString)
          ).collect(Collectors.toList())
      );

    } else if (args.length == 2) {
      return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }

    return Collections.emptyList();
  }
}
