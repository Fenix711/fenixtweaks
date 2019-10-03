package template;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

@SuppressWarnings("unused")
@Mod(
    modid = ModTemplate.MOD_ID,
    version = ModTemplate.VERSION,
    name = ModTemplate.NAME
    //@@DEPENDENCIES@@
)
public class ModTemplate {

  public static final String MOD_ID = "template";
  public static final String VERSION = "@@VERSION@@";
  public static final String NAME = "Template";

  private static final String PROXY_SERVER = "template.proxy.SidedProxy";
  private static final String PROXY_CLIENT = "template.proxy.SidedProxyClient";

  @SuppressWarnings("unused")
  @Mod.Instance
  public static ModTemplate INSTANCE;

  @net.minecraftforge.fml.common.SidedProxy(
      modId = MOD_ID,
      serverSide = PROXY_SERVER,
      clientSide = PROXY_CLIENT
  )
  public static template.proxy.SidedProxy PROXY;

  public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID) {

    @Override
    public ItemStack getTabIconItem() {

      return new ItemStack(Items.STICK);
    }
  };

  @Mod.EventHandler
  public void onConstructionEvent(FMLConstructionEvent event) {
    //
  }

  @Mod.EventHandler
  public void onPreInitializationEvent(FMLPreInitializationEvent event) {
    //
  }

  @Mod.EventHandler
  public void onInitializationEvent(FMLInitializationEvent event) {
    //
  }

  @Mod.EventHandler
  public void onPostInitializationEvent(FMLPostInitializationEvent event) {
    //
  }

  @Mod.EventHandler
  public void onLoadCompleteEvent(FMLLoadCompleteEvent event) {
    //
  }

  @Mod.EventHandler
  public void onServerAboutToStartEvent(FMLServerAboutToStartEvent event) {
    //
  }

  @Mod.EventHandler
  public void onServerStartingEvent(FMLServerStartingEvent event) {
    //
  }

  @Mod.EventHandler
  public void onServerStartedEvent(FMLServerStartedEvent event) {
    //
  }

  @Mod.EventHandler
  public void onServerStoppingEvent(FMLServerStoppingEvent event) {
    //
  }

  @Mod.EventHandler
  public void onServerStoppedEvent(FMLServerStoppedEvent event) {
    //
  }
}
