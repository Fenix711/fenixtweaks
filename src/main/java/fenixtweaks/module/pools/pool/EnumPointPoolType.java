package fenixtweaks.module.pools.pool;

public enum EnumPointPoolType {

  BlockHarvest("block_harvest");

  private final String name;

  EnumPointPoolType(String name) {

    this.name = name;
  }

  public String getName() {

    return this.name;
  }
}
