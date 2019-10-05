package fenixtweaks.module.pools.pool;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Map;

/**
 * Uses a map of adapters to create and adapt a point pool from the given
 * {@link com.google.gson.JsonElement}.
 */
public class PointPoolAdapter {

  public final Map<String, IPointPoolJsonElementAdapter<? extends PointPoolBase>> adapterMap;

  public PointPoolAdapter(Map<String, IPointPoolJsonElementAdapter<? extends PointPoolBase>> adapterMap) {

    this.adapterMap = adapterMap;
  }

  public <P extends PointPoolBase> P adapt(JsonElement jsonElement) {

    JsonObject root = jsonElement.getAsJsonObject();
    JsonPrimitive type = root.getAsJsonPrimitive("type");
    String typeAsString = type.getAsString();

    IPointPoolJsonElementAdapter<? extends PointPoolBase> adapter = this.adapterMap.get(typeAsString);

    if (adapter != null) {
      //noinspection unchecked
      return (P) adapter.adapt(jsonElement);
    }

    throw new RuntimeException("No point pool adapter registered for: " + typeAsString);
  }

}
