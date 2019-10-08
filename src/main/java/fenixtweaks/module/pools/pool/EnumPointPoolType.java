package fenixtweaks.module.pools.pool;

public enum EnumPointPoolType {

  BlockHarvest("block_harvest"),
  MSDamageDealt("ms_damage_dealt");

  private final String name;

  EnumPointPoolType(String name) {

    this.name = name;
  }

  public String getName() {

    return this.name;
  }
}
