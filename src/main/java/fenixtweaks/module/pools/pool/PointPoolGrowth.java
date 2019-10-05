package fenixtweaks.module.pools.pool;

public class PointPoolGrowth {

  private final double base;
  private final EnumGrowthType type;

  public PointPoolGrowth(double base, EnumGrowthType type) {

    this.base = base;
    this.type = type;
  }

  public double getBase() {

    return this.base;
  }

  public EnumGrowthType getType() {

    return this.type;
  }
}
