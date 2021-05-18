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
import fr.debris.palatest.common.Reference;
import fr.debris.palatest.common.proxy.CommonProxy;

@Mod(modid = Reference.MOD_ID, version = Reference.MOD_VERSION, name = Reference.MOD_NAME)
public class Main {

    @Mod.Instance(Reference.MOD_ID)
    public static Main instance;

    @SidedProxy(clientSide = Reference.PROXY_CLIENT_CLASS, serverSide = Reference.PROXY_SERVER_CLASS)
    protected static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
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
