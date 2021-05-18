/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 13:39
 */
package fr.debris.palatest.common.machine.watergrinder.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.common.Reference;
import fr.debris.palatest.common.machine.watergrinder.TileEntityWaterGrinder;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiWaterGrinder extends GuiContainer {

    private static final ResourceLocation guiTexture = new ResourceLocation(Reference.MOD_ID, "textures/gui/water_grinder.png");
    private final TileEntityWaterGrinder tileEntity;

    public GuiWaterGrinder(InventoryPlayer inventory, TileEntityWaterGrinder tileEntityWaterGrinder) {
        super(new ContainerWaterGrinder(inventory, tileEntityWaterGrinder));

        this.tileEntity = tileEntityWaterGrinder;
    }

    /**
     * Affichage des texte
     *
     * @param x not used
     * @param y not used
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        String name = I18n.format(String.format("machine.%s", this.tileEntity.getName()));

        if (name.contains("error:")) {
            name = this.tileEntity.getName();
        }

        this.fontRendererObj.drawString(
                name,
                (this.xSize / 2) - (this.fontRendererObj.getStringWidth(name) / 2),
                6,
                4210752
        );

        this.fontRendererObj.drawString(
                I18n.format("container.inventory"),
                8,
                this.ySize - 94,
                4210752
        );
    }

    /**
     * Gestion des progress bar
     *
     * @param f1 not used
     * @param i1 not used
     * @param i2 not used
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float f1, int i1, int i2) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTexture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        // p_73729_1_ : X Destination
        // p_73729_2_ : Y Destination
        // p_73729_3_ : X Source
        // p_73729_4_ : Y Source
        // p_73729_5_ : Y Width
        // p_73729_6_ : Y Height
        int size = this.tileEntity.getDiamondValueScaled(42);
        this.drawTexturedModalRect(k + 9, l + 8, 176, 19, 14, 42 - size);

        if (this.tileEntity.isWorked()) {
            size = this.tileEntity.getSmeltingProgressScaled(25);
            this.drawTexturedModalRect(k + 78, l + 33, 176, 0, size, 19);
        } else {
            this.drawTexturedModalRect(k + 78, l + 33, 176, 0, 25, 19);
        }
    }
}
