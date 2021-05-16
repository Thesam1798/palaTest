/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 10:59
 */
package fr.debris.palatest.common.machine;


import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.Main;
import fr.debris.palatest.common.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class WaterGrinder extends BlockContainer {

    private static boolean isBurning;
    private final boolean isBurning2;
    private final Random random = new Random();
    @SideOnly(Side.CLIENT)
    private IIcon top;
    @SideOnly(Side.CLIENT)
    private IIcon front;

    public WaterGrinder(boolean isActive, String name, boolean tab) {
        super(Material.iron);
        isBurning2 = isActive;

        this.setBlockName(name);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setStepSound(soundTypeStone);

        if (tab) this.setCreativeTab(CommonProxy.palaTab);

        GameRegistry.registerBlock(this, name);
    }

    public WaterGrinder(boolean isActive, String name) {
        this(isActive, name, false);
    }

    public static void updateBlockState(boolean burning, World world, int x, int z, int y) {
        int direction = world.getBlockMetadata(x, y, z);

        TileEntity tileEntity = world.getTileEntity(x, y, z);

        isBurning = true;

        if (burning) {
            world.setBlock(x, y, z, CommonProxy.waterGrinderActive);
        } else {
            world.setBlock(x, y, z, CommonProxy.waterGrinder);
        }

        isBurning = false;

        world.setBlockMetadataWithNotify(x, y, z, direction, 2);

        if (tileEntity != null) {
            tileEntity.validate();
            world.setTileEntity(x, y, z, tileEntity);
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!isBurning) {
            TileEntityWaterGrinder tileEntityWaterGrinder = (TileEntityWaterGrinder) world.getTileEntity(x, y, z);
            if (tileEntityWaterGrinder != null) {
                for (int i = 0; i < tileEntityWaterGrinder.getSizeInventory(); ++i) {
                    ItemStack itemStack = tileEntityWaterGrinder.getStackInSlot(i);

                    if (itemStack != null) {
                        float f1 = this.random.nextFloat() * 0.6F + 0.1F;
                        float f2 = this.random.nextFloat() * 0.6F + 0.1F;
                        float f3 = this.random.nextFloat() * 0.6F + 0.1F;

                        while (itemStack.stackSize > 0) {
                            int j = this.random.nextInt(21) + 10;

                            if (j > itemStack.stackSize) {
                                j = itemStack.stackSize;
                            }

                            itemStack.stackSize -= j;
                            EntityItem entityItem = new EntityItem(world, x + f1, y + f2, z + f3, new ItemStack(itemStack.getItem(), j, itemStack.getItemDamage()));

                            if (itemStack.hasTagCompound()) {
                                entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
                            }

                            float f4 = 0.025F;
                            entityItem.motionX = this.random.nextGaussian() * f4;
                            entityItem.motionY = this.random.nextGaussian() * f4 + 0.1F;
                            entityItem.motionZ = this.random.nextGaussian() * f4;

                            world.spawnEntityInWorld(entityItem);
                        }
                    }
                    world.func_147453_f(x, y, z, block);
                }
            }
            super.breakBlock(world, x, y, z, block, meta);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (this.isBurning2) {
            int direction = world.getBlockMetadata(x, y, z);
            float x1 = (float) x + 0.5F;
            float y1 = (float) y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
            float z1 = (float) z + 0.5F;
            float mouve = 0.52F;
            float x2 = random.nextFloat() * 0.6F - 0.3F;

            String smoke = "smoke";
            String flame = "flame";
            if (direction == 4) {
                world.spawnParticle(smoke, x1 - mouve, y1, z1 + x2, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(flame, x1 - mouve, y1, z1 + x2, 0.0D, 0.0D, 0.0D);
            } else if (direction == 5) {
                world.spawnParticle(smoke, x1 + mouve, y1, z1 + x2, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(flame, x1 + mouve, y1, z1 + x2, 0.0D, 0.0D, 0.0D);
            } else if (direction == 2) {
                world.spawnParticle(smoke, x1 + x2, y1, z1 - mouve, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(flame, x1 + x2, y1, z1 - mouve, 0.0D, 0.0D, 0.0D);
            } else if (direction == 3) {
                world.spawnParticle(smoke, x1 + x2, y1, z1 + mouve, 0.0D, 0.0D, 0.0D);
                world.spawnParticle(flame, x1 + x2, y1, z1 + mouve, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon("furnace_side");
        this.front = iconRegister.registerIcon(this.isBurning2 ? "furnace_front_on" : "furnace_front_off");
        this.top = iconRegister.registerIcon("furnace_top");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return this.top;
        } else if (side == 3) {
            return this.front;
        } else {
            return this.blockIcon;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i1, float f1, float f2, float f3) {
        player.openGui(Main.instance, 0, world, x, y, z);
        return true;
    }

    @Override
    public Item getItemDropped(int i1, Random random, int i2) {
        return Item.getItemFromBlock(CommonProxy.waterGrinder);
    }

    @Override
    public Item getItem(World world, int i1, int i2, int i3) {
        return Item.getItemFromBlock(CommonProxy.waterGrinder);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        this.direction(world, x, y, z);
    }

    private void direction(World world, int x, int y, int z) {
        if (!world.isRemote) {
            Block leftBlock = world.getBlock(x, y, z - 1);
            Block rightBlock = world.getBlock(x, y, z + 1);
            Block frontBlock = world.getBlock(x - 1, y, z);
            Block backBlock = world.getBlock(x + 1, y, z);

            byte b0 = 3;

            if (rightBlock.func_149730_j() && !leftBlock.func_149730_j()) {
                b0 = 2;
            }

            if (frontBlock.func_149730_j() && !backBlock.func_149730_j()) {
                b0 = 5;
            }

            if (backBlock.func_149730_j() && !frontBlock.func_149730_j()) {
                b0 = 4;
            }

            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (l == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (l == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (l == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }

        if (itemStack.hasDisplayName()) {
            ((TileEntityWaterGrinder) world.getTileEntity(x, y, z)).setName(itemStack.getDisplayName());
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i1) {
        TileEntityWaterGrinder tileEntityWaterGrinder = new TileEntityWaterGrinder();
        tileEntityWaterGrinder.setName(this.getUnlocalizedName());
        return tileEntityWaterGrinder;
    }
}
