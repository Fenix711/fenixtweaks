package fenixtweaks.module.pools.pool;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Responsible for loading a {@link JsonElement} from the given path.
 */
public class PointPoolLoader {

  private final JsonParser jsonParser;

  public PointPoolLoader() {

    this.jsonParser = new JsonParser();
  }

  public JsonElement load(Path path) throws IOException {

    String data = new String(Files.readAllBytes(path));
    return this.jsonParser.parse(data);
  }
}
