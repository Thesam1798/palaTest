/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:01
 */
package fr.debris.palatest.client.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import fr.debris.palatest.client.model.ModelGolem;
import fr.debris.palatest.client.model.RenderProxy;
import fr.debris.palatest.common.Reference;
import fr.debris.palatest.common.entity.EntityGolem;
import fr.debris.palatest.common.proxy.CommonProxy;
import org.lwjgl.opengl.Display;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        Display.setTitle(String.format("%s - %s", Reference.MOD_NAME, Reference.MOD_VERSION));

        super.preInit();

        RenderingRegistry.registerEntityRenderingHandler(EntityGolem.class, new RenderProxy(new ModelGolem(), -1F));
    }
}
