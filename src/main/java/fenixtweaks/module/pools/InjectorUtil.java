package fenixtweaks.module.pools;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class InjectorUtil {

  public static void inject(Class<?> apiClass, String fieldName, Object value) {

    try {
      Field field = apiClass.getDeclaredField(fieldName);
      field.setAccessible(true);

      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

      field.set(null, value);

    } catch (Exception e) {
      throw new RuntimeException(String.format("Unable to inject [%s] into [%s]", fieldName, apiClass), e);
    }
  }

  private InjectorUtil() {
    //
  }
}
