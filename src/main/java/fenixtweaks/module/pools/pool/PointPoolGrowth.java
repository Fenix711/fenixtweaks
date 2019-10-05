package fenixtweaks.module.pools.pool;

public class PointPoolGrowth {

  private double slope;
  private double exponent;
  private double intercept;

  public PointPoolGrowth() {

    this.slope = 1;
    this.exponent = 1;
    this.intercept = 0;
  }

  public double getSlope() {

    return this.slope;
  }

  public double getExponent() {

    return this.exponent;
  }

  public double getIntercept() {

    return this.intercept;
  }
}
