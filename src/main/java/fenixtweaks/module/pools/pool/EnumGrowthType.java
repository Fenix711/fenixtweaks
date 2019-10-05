package fenixtweaks.module.pools.pool;

public enum EnumGrowthType {

  MULTIPLY("multiply");

  private final String name;

  EnumGrowthType(String name) {

    this.name = name;
  }

  public String getName() {

    return this.name;
  }
}
