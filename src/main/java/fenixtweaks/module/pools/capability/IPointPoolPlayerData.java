package fenixtweaks.module.pools.capability;

import net.minecraft.util.ResourceLocation;

public interface IPointPoolPlayerData {

  /**
   * Add the given points to the pool with the given {@link ResourceLocation}.
   *
   * @param resourceLocation the location of the pool
   * @param points           the points to add
   * @return the new point total for the pool
   */
  double addPoints(ResourceLocation resourceLocation, double points);

  /**
   * Returns the point total for the given pool.
   *
   * @param resourceLocation the location of the pool
   * @return the point total for the given pool
   */
  double getPoints(ResourceLocation resourceLocation);

  /**
   * Add the given levels to the pool with the given {@link ResourceLocation}.
   *
   * @param resourceLocation the location of the pool
   * @param levels           the levels to add
   * @return the new level total for the pool
   */
  int addLevels(ResourceLocation resourceLocation, int levels);

  /**
   * Returns the level total for the given pool.
   *
   * @param resourceLocation the location of the pool
   * @return the level total for the given pool
   */
  int getLevels(ResourceLocation resourceLocation);

}
