/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:01
 */
package fr.debris.palatest.client.proxy;

import fr.debris.palatest.common.proxy.CommonProxy;
import fr.debris.palatest.common.proxy.GuiProxy;

import static fr.debris.palatest.common.Logger.log;

public class ClientProxy extends CommonProxy {

    protected static void initGui() {
        log("Init Gui : Start");
        waterGrinderGui = new GuiProxy(175, 165, "water_grinder");
        log("Init Gui : End");
    }

    @Override
    public void register() {
        log("Client Proxy register");
        initGui();

        super.register();
    }
}
