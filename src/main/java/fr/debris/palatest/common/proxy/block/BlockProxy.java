/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:37
 */
package fr.debris.palatest.common.proxy.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.common.Reference;
import fr.debris.palatest.common.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockProxy extends Block {

    protected final String name;

    public BlockProxy(Material material, String name) {
        super(material);
        this.name = name;

        this.setBlockName(name);
        this.setCreativeTab(CommonProxy.palaTab);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setStepSound(soundTypeStone);


        GameRegistry.registerBlock(this, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(String.format(
                "%s:%s",
                Reference.MOD_ID,
                this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1)
        ));
    }
}
