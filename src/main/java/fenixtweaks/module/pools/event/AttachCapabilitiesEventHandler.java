package fenixtweaks.module.pools.event;

import fenixtweaks.ModFenixTweaks;
import fenixtweaks.module.pools.capability.CapabilityPointPools;
import fenixtweaks.module.pools.capability.PointPoolPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = ModFenixTweaks.MOD_ID)
public class AttachCapabilitiesEventHandler {

  @SubscribeEvent
  public static void on(AttachCapabilitiesEvent<Entity> event) {

    Entity entity = event.getObject();

    if (entity instanceof EntityPlayer && !(entity instanceof FakePlayer)) {
      event.addCapability(new ResourceLocation(ModFenixTweaks.MOD_ID, "point_pools"), new Provider());
    }
  }

  private static class Provider
      implements ICapabilitySerializable<NBTTagCompound> {

    private PointPoolPlayerData data;

    /* package */ Provider() {

      this.data = new PointPoolPlayerData();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {

      return (capability == CapabilityPointPools.POINT_POOLS);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

      if (capability == CapabilityPointPools.POINT_POOLS) {
        //noinspection unchecked
        return (T) this.data;
      }

      return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {

      return (NBTTagCompound) this.data.writeNBT(CapabilityPointPools.POINT_POOLS, this.data, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {

      this.data.readNBT(CapabilityPointPools.POINT_POOLS, this.data, null, nbt);
    }
  }
}
