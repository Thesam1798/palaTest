/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 21:43
 */
package fr.debris.palatest.common.proxy;

import fr.debris.palatest.common.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiProxy extends GuiScreen {
    protected final int guiWidth;
    protected final int guiHeight;
    protected final String name;
    protected final Minecraft minecraft = Minecraft.getMinecraft();

    public GuiProxy(int width, int height, String name) {
        this.guiWidth = width;
        this.guiHeight = height;
        this.name = name;
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        int guiX = (width - this.guiWidth) / 2;
        int guiY = (height - this.guiHeight) / 2;

        GL11.glColor4f(1, 1, 1, 1);

        drawDefaultBackground();

        this.minecraft.renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID, String.format("textures/gui/%s.png", name)));

        drawTexturedModalRect(guiX, guiY, 0, 0, this.guiWidth, this.guiHeight);

        super.drawScreen(x, y, ticks);
    }
}
