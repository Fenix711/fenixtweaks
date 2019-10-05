package fenixtweaks.module.pools.pool;

import com.google.gson.JsonElement;

public interface IPointPoolJsonElementAdapter<P extends PointPoolBase> {

  P adapt(JsonElement jsonElement);

}
