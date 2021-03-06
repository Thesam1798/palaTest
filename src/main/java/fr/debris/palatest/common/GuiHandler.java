/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 13:35
 */
package fr.debris.palatest.common;

import cpw.mods.fml.common.network.IGuiHandler;
import fr.debris.palatest.common.machine.watergrinder.TileEntityWaterGrinder;
import fr.debris.palatest.common.machine.watergrinder.gui.ContainerWaterGrinder;
import fr.debris.palatest.common.machine.watergrinder.gui.GuiWaterGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Class de gestion de l'affichage des Gui en fonction de leur Id
 */
public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            TileEntityWaterGrinder tileEntityWaterGrinder = (TileEntityWaterGrinder) world.getTileEntity(x, y, z);
            return new ContainerWaterGrinder(player.inventory, tileEntityWaterGrinder);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            TileEntityWaterGrinder tileEntityWaterGrinder = (TileEntityWaterGrinder) world.getTileEntity(x, y, z);
            return new GuiWaterGrinder(player.inventory, tileEntityWaterGrinder);
        }

        if (id == 1) {
            TileEntityWaterGrinder tileEntityWaterGrinder = (TileEntityWaterGrinder) world.getTileEntity(x, y, z);
            return new GuiWaterGrinder(player.inventory, tileEntityWaterGrinder);
        }
        return null;
    }
}
