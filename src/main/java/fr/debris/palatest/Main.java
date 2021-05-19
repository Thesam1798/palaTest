/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:00
 */
package fr.debris.palatest;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fr.debris.palatest.common.Reference;
import fr.debris.palatest.common.network.GrinderNotificationNetwork;
import fr.debris.palatest.common.network.GrinderTileEntityNetwork;
import fr.debris.palatest.common.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Reference.MOD_ID, version = Reference.MOD_VERSION, name = Reference.MOD_NAME)
public class Main {

    @Mod.Instance(Reference.MOD_ID)
    public static Main instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT_CLASS, serverSide = Reference.PROXY_SERVER_CLASS)
    protected static CommonProxy proxy;

    private static SimpleNetworkWrapper networkWrapper;

    public static SimpleNetworkWrapper getNetworkWrapper() {
        return networkWrapper;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Main.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);

        Main.networkWrapper.registerMessage(GrinderNotificationNetwork.Handler.class, GrinderNotificationNetwork.class, 0, Side.CLIENT);
        Main.networkWrapper.registerMessage(GrinderTileEntityNetwork.Handler.class, GrinderTileEntityNetwork.class, 1, Side.SERVER);

        MinecraftForge.EVENT_BUS.register(new Event());
        proxy.preInit();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}
