package fenixtweaks.module.pools.event;

import fenixtweaks.ModFenixTweaks;
import fenixtweaks.module.pools.InjectorUtil;
import fenixtweaks.module.pools.ModulePools;
import fenixtweaks.module.pools.pool.PointPoolBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber
public class NewRegistryEventHandler {

  @SubscribeEvent
  public static void on(RegistryEvent.NewRegistry event) {

    new RegistryBuilder<PointPoolBase>()
        .setName(new ResourceLocation(ModFenixTweaks.MOD_ID, "point_pools"))
        .setType(PointPoolBase.class)
        .allowModification()
        .create();

    InjectorUtil.inject(
        ModulePools.Registries.class,
        "POINT_POOLS",
        GameRegistry.findRegistry(PointPoolBase.class)
    );
  }
}
