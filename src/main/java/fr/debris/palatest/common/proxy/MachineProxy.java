/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 22:59
 */
package fr.debris.palatest.common.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.common.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MachineProxy extends BlockContainer {

    protected final boolean active;
    protected final String name;

    public MachineProxy(boolean active, String name, Material material) {
        super(material);
        this.active = active;
        this.name = name;

        this.setBlockName(name);
        this.setCreativeTab(CommonProxy.palaTab);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
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

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return null;
    }
}
