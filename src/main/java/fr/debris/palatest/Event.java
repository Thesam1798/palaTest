/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 18:16
 */
package fr.debris.palatest;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.client.gui.GrinderNotification;
import fr.debris.palatest.server.Mysql;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;

public class Event {

    private final boolean mysqlLoaded = false;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (!event.isCancelable() && event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            Minecraft mc = Minecraft.getMinecraft();

            GrinderNotification.drawGrindDone(mc);
            GrinderNotification.drawSpawnGolem(mc);
        }
    }

    @SubscribeEvent()
    @SideOnly(Side.SERVER)
    public void onDimensionLoad(WorldEvent.Load event) {
        if (!mysqlLoaded && MinecraftServer.getServer().isDedicatedServer()) {
            try {
                Mysql mysql = new Mysql();
                mysql.recreateDatabase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
