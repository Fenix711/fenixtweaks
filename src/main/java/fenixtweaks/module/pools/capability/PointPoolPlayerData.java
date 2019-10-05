package fenixtweaks.module.pools.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This data is stored on each player.
 *
 * <pre>
 * {
 *    ResourceLocation: {
 *      points,
 *      level
 *    },
 *    ...
 * }
 * </pre>
 */
public class PointPoolPlayerData
    implements IPointPoolPlayerData,
    Capability.IStorage<IPointPoolPlayerData> {

  private Map<ResourceLocation, Entry> entryMap;

  public PointPoolPlayerData() {

    this.entryMap = new HashMap<>();
  }

  @Override
  public double addPoints(ResourceLocation resourceLocation, double points) {

    Entry entry = this.entryMap.computeIfAbsent(resourceLocation, r -> new Entry());
    entry.points = Math.max(0, entry.points + points);
    return entry.points;
  }

  @Override
  public double getPoints(ResourceLocation resourceLocation) {

    Entry entry = this.entryMap.computeIfAbsent(resourceLocation, r -> new Entry());
    return entry.points;
  }

  @Override
  public int addLevels(ResourceLocation resourceLocation, int levels) {

    Entry entry = this.entryMap.computeIfAbsent(resourceLocation, r -> new Entry());
    entry.level = Math.max(0, entry.level + levels);
    return entry.level;
  }

  @Override
  public int getLevels(ResourceLocation resourceLocation) {

    Entry entry = this.entryMap.computeIfAbsent(resourceLocation, r -> new Entry());
    return entry.level;
  }

  // ---------------------------------------------------------------------------
  // - Serialization
  // ---------------------------------------------------------------------------

  @Nullable
  @Override
  public NBTBase writeNBT(Capability<IPointPoolPlayerData> capability, IPointPoolPlayerData instance, EnumFacing side) {

    NBTTagCompound compound = new NBTTagCompound();

    for (Map.Entry<ResourceLocation, Entry> entry : this.entryMap.entrySet()) {
      ResourceLocation resourceLocation = entry.getKey();
      Entry value = entry.getValue();

      NBTTagCompound poolData = new NBTTagCompound();
      poolData.setDouble("points", value.points);
      poolData.setInteger("level", value.level);

      compound.setTag(resourceLocation.toString(), poolData);
    }

    return compound;
  }

  @Override
  public void readNBT(Capability<IPointPoolPlayerData> capability, IPointPoolPlayerData instance, EnumFacing side, NBTBase nbt) {

    if (!(nbt instanceof NBTTagCompound)) {
      throw new IllegalArgumentException("Can't deserialize, incorrect NBT type, expected compound, found: " + nbt.getClass().toString());
    }

    NBTTagCompound compound = (NBTTagCompound) nbt;

    Set<String> keySet = compound.getKeySet();

    for (String resourceLocationString : keySet) {
      NBTTagCompound poolData = compound.getCompoundTag(resourceLocationString);
      Entry entry = new Entry();
      entry.points = poolData.getDouble("points");
      entry.level = poolData.getInteger("level");
      ResourceLocation resourceLocation = new ResourceLocation(resourceLocationString);
      this.entryMap.put(resourceLocation, entry);
    }
  }

  private static class Entry {

    private int level;
    private double points;
  }
}
