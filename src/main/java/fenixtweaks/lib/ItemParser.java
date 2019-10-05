package fenixtweaks.lib;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemParser {

  public static final ItemParser INSTANCE = new ItemParser();

  @Nonnull
  public ParseResult parse(@Nullable String data) throws RuntimeException {

    if (data == null || "null".equals(data.trim())) {
      return ParseResult.NULL;
    }

    ParseResult result = new ParseResult();
    result.setMeta(0);
    result.setQuantity(1);

    String[] split = data.split(":");

    if (split.length < 2 || split.length > 3) {
      throw new RuntimeException(String.format(
          "[PARSE] Too many segments in [%s], must be two or three segments: <domain:path> or <domain:path:meta>",
          data
      ));
    }

    result.setDomain(split[0].trim());

    String[] pathSplit = split[1].split("\\*");

    result.setPath(pathSplit[0].trim());

    if (pathSplit.length > 1) {

      try {
        result.setQuantity(Integer.parseInt(pathSplit[1].trim()));

      } catch (NumberFormatException e) {
        throw new RuntimeException(String.format("[PARSE] Expected integer, got [%s]", split[1].trim()));
      }
    }

    if (split.length == 3) {
      String meta = split[2].trim();

      if ("*".equals(meta.substring(0, 1))) {
        result.setMeta(OreDictionary.WILDCARD_VALUE);
        String quantity = meta.replace("*", "").trim();

        if (!quantity.isEmpty()) {
          try {
            result.setQuantity(Integer.parseInt(quantity));

          } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("[PARSE] Expected integer, got [%s]", quantity));
          }
        }

      } else {
        String[] metaSplit = meta.split("\\*");

        try {
          result.setMeta(Integer.parseInt(metaSplit[0].trim()));

        } catch (NumberFormatException e) {
          throw new RuntimeException(String.format("[PARSE] Expected integer, got [%s]", metaSplit[1].trim()));
        }

        if (metaSplit.length > 1) {

          try {
            result.setQuantity(Integer.parseInt(metaSplit[1].trim()));

          } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("[PARSE] Expected integer, got [%s]", metaSplit[1].trim()));
          }
        }
      }
    }

    return result;
  }

  public static class ParseResult {

    public static final ParseResult NULL = new ParseResult();

    private String domain;
    private String path;
    private int meta;
    private int quantity;

    public ParseResult() {
      //
    }

    public ParseResult(String domain, String path, int meta) {

      this(domain, path, meta, 1);
    }

    public ParseResult(String domain, String path, int meta, int quantity) {

      this.domain = domain;
      this.path = path;
      this.meta = meta;
      this.quantity = quantity;
    }

    public void setDomain(String domain) {

      this.domain = domain;
    }

    public void setPath(String path) {

      this.path = path;
    }

    public void setMeta(int meta) {

      this.meta = meta;
    }

    public void setQuantity(int quantity) {

      this.quantity = quantity;
    }

    public String getDomain() {

      return domain;
    }

    public String getPath() {

      return path;
    }

    public int getMeta() {

      return meta;
    }

    public int getQuantity() {

      return quantity;
    }

    @Override
    public String toString() {

      if (this == ParseResult.NULL) {
        return "null";
      }

      return this.domain + ":" + this.path + (("ore".equals(this.domain)) ? "" : ":" + this.meta + ((this.quantity != 1) ? " * " + this.quantity : ""));
    }

    public boolean matches(ItemStack itemStack, boolean ignoreQuantity) {

      Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(this.getDomain(), this.getPath()));

      if (itemStack.getItem() != item) {
        return false;
      }

      if (this.getMeta() != OreDictionary.WILDCARD_VALUE
          && this.getMeta() != itemStack.getMetadata()) {
        return false;
      }

      return (ignoreQuantity) || (this.getQuantity() == itemStack.getCount());
    }
  }
}
