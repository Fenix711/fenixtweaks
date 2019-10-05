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
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandPoints
    extends CommandBase {

  @Nonnull
  @Override
  public String getName() {

    return "ftpoints";
  }

  @Override
  public int getRequiredPermissionLevel() {

    return 2;
  }

  @Nonnull
  @Override
  public String getUsage(@Nonnull ICommandSender sender) {

    return "commands.fenixtweaks.ftpoints.usage";
  }

  @Override
  public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {

    if (args.length < 2 || args.length > 3) {
      throw new WrongUsageException("commands.fenixtweaks.ftpoints.usage");
    }

    String argPool = args[0];
    PointPoolBase pool = ModulePools.Registries.POINT_POOLS.getValue(new ResourceLocation(argPool));

    if (pool == null) {
      throw new WrongUsageException("commands.fenixtweaks.fail.pool", argPool);
    }

    double amount;
    boolean levels = false;
    String argAmount = args[1];

    try {

      if (argAmount.endsWith("L")) {
        levels = true;
        argAmount = argAmount.substring(0, argAmount.length() - 1);
      }

      amount = Double.parseDouble(argAmount);

    } catch (Exception e) {
      throw new WrongUsageException("commands.fenixtweaks.ftpoints.fail.amount", argAmount);
    }

    EntityPlayer player = (args.length == 3) ? getPlayer(server, sender, args[2]) : getCommandSenderAsPlayer(sender);

    if (levels) {

      if (amount < 0) {
        double abs = Math.abs(amount);
        pool.removeLevels(player, (int) abs);
        sender.sendMessage(new TextComponentTranslation("commands.fenixtweaks.ftpoints.message.levels.removed", (int) abs, player.getName()));

      } else if (amount > 0) {
        pool.addLevels(player, (int) amount);
        sender.sendMessage(new TextComponentTranslation("commands.fenixtweaks.ftpoints.message.levels.added", (int) amount, player.getName()));
      }

    } else {

      if (amount < 0) {
        double abs = Math.abs(amount);
        pool.removePoints(player, abs);
        sender.sendMessage(new TextComponentTranslation("commands.fenixtweaks.ftpoints.message.points.removed", abs, player.getName()));

      } else if (amount > 0) {
        pool.addPoints(player, amount);
        sender.sendMessage(new TextComponentTranslation("commands.fenixtweaks.ftpoints.message.points.added", amount, player.getName()));
      }
    }
  }

  @Nonnull
  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

    if (args.length == 1) {
      return getListOfStringsMatchingLastWord(
          args,
          ModulePools.Registries.POINT_POOLS.getKeys()
              .stream()
              .map(ResourceLocation::toString)
              .collect(Collectors.toList())
      );

    } else if (args.length == 3) {
      return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }

    return Collections.emptyList();
  }
}
