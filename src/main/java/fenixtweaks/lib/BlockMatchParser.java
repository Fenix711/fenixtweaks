package fenixtweaks.lib;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockMatchParser {

  public static final BlockMatchParser INSTANCE = new BlockMatchParser(ItemParser.INSTANCE);

  private static final Logger LOGGER = LogManager.getLogger(BlockMatchParser.class);

  private final ItemParser parser;

  public BlockMatchParser(ItemParser parser) {

    this.parser = parser;
  }

  public BlockMatchEntry parse(String string) {

    String[] split = string.split(",");

    ItemParser.ParseResult parse;

    try {
      parse = parser.parse(split[0]);

    } catch (Exception e) {
      LOGGER.error("Unable to parse block <" + split[0] + ">", e);
      return null;
    }

    Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parse.getDomain(), parse.getPath()));

    if (block == null) {
      LOGGER.error("Unable to find registered block: " + parse.toString());
      return null;
    }

    int meta = parse.getMeta();
    int[] metas = new int[Math.max(split.length - 1, 0)];

    for (int i = 1; i < split.length; i++) {

      if ("*".equals(split[i].trim())) {
        meta = OreDictionary.WILDCARD_VALUE;
        metas = new int[0];
        break;
      }

      try {
        metas[i - 1] = Integer.parseInt(split[i].trim());

      } catch (Exception e) {
        LOGGER.error("[PARSE] Unable to parse extra meta for <" + string + ">", e);
      }
    }

    BlockMatchEntry blockMatchEntry = new BlockMatchEntry(parse.getDomain(), parse.getPath(), meta, metas);
    LOGGER.debug("Added block matcher: " + blockMatchEntry);

    return blockMatchEntry;
  }

  public static class BlockMatchEntry {

    private String domain;
    private String path;
    private int meta;
    private int[] metas;

    public BlockMatchEntry(String domain, String path, int meta, int[] metas) {

      this.domain = domain;
      this.path = path;
      this.meta = meta;
      this.metas = metas;
    }

    public boolean matches(IBlockState blockState) {

      ResourceLocation registryName = blockState.getBlock().getRegistryName();

      if (registryName == null) {
        return false;
      }

      if (!registryName.getResourceDomain().equals(this.domain)) {
        return false;
      }

      if (!registryName.getResourcePath().equals(this.path)) {
        return false;
      }

      int metaFromState = blockState.getBlock().getMetaFromState(blockState);

      if (this.meta == OreDictionary.WILDCARD_VALUE
          || this.meta == metaFromState) {
        return true;
      }

      for (int meta : this.metas) {

        if (meta == OreDictionary.WILDCARD_VALUE
            || meta == metaFromState) {
          return true;
        }
      }

      return false;
    }
  }
}

