package fenixtweaks.module.pools.pool;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Responsible for returning a list of .json files in the given path.
 */
public class PointPoolLocator {

  public List<Path> locatePointPools(Path path) {

    try (Stream<Path> paths = Files.walk(path)) {
      return paths
          .filter(Files::isRegularFile)
          .filter(p -> p.toString().endsWith(".json"))
          .collect(Collectors.toList());

    } catch (Exception e) {
      e.printStackTrace();
    }

    return Collections.emptyList();
  }
}
