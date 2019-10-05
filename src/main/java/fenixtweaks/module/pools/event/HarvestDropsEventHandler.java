package fenixtweaks.module.pools.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class HarvestDropsEventHandler {

  @SubscribeEvent
  public static void on(BlockEvent.HarvestDropsEvent event) {

    EntityPlayer harvester = event.getHarvester();

    if (harvester == null) {
      return;
    }

    if (harvester.world.isRemote) {
      return;
    }

    // TODO:
    // get all block harvest listeners
    // process
  }

}
