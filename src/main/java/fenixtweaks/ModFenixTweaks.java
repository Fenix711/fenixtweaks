package fenixtweaks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;

@SuppressWarnings("unused")
@Mod(
    modid = ModFenixTweaks.MOD_ID,
    version = ModFenixTweaks.VERSION,
    name = ModFenixTweaks.NAME
    //@@DEPENDENCIES@@
)
public class ModFenixTweaks {

  public static final String MOD_ID = "fenixtweaks";
  public static final String VERSION = "@@VERSION@@";
  public static final String NAME = "FenixTweaks";

  private static final String PROXY_SERVER = "fenixtweaks.proxy.SidedProxy";
  private static final String PROXY_CLIENT = "fenixtweaks.proxy.SidedProxyClient";

  @SuppressWarnings("unused")
  @Mod.Instance
  public static ModFenixTweaks INSTANCE;

  @net.minecraftforge.fml.common.SidedProxy(
      modId = MOD_ID,
      serverSide = PROXY_SERVER,
      clientSide = PROXY_CLIENT
  )
  public static fenixtweaks.proxy.SidedProxy PROXY;

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
