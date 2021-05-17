/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 22:59
 */
package fr.debris.palatest.common.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.Main;
import fr.debris.palatest.common.Reference;
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

public class MachineProxy extends BlockContainer {

    private final String blockTexture;
    private final String frontTextureName;
    private final int guiInstance;
    private final Block blockOnDrop;
    private final boolean animate;
    private final TileEntityProxy entity;
    private final Random random = new Random();
    @SideOnly(Side.CLIENT)
    private IIcon front;

    public MachineProxy(
            String name,
            String blockTexture,
            String frontTextureName,
            int guiInstance,
            Block blockOnDrop,
            boolean animate,
            SoundType sound,
            boolean tab,
            TileEntityProxy entity
    ) {
        super(Material.iron);
        this.blockTexture = blockTexture;
        this.frontTextureName = frontTextureName;
        this.guiInstance = guiInstance;
        this.blockOnDrop = blockOnDrop;
        this.animate = animate;
        this.entity = entity;

        this.setBlockName(name);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setStepSound(sound);

        if (tab) this.setCreativeTab(CommonProxy.palaTab);

        GameRegistry.registerBlock(this, name);
    }

    /**
     * A la destruction du block
     *
     * @param world le monde
     * @param x     X
     * @param y     Y
     * @param z     Z
     * @param block le block
     * @param meta  les meta du block
     */
    @Override
    // SonarLint : java:S3776 : Cognitive Complexity of methods should not be too hig
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (this.entity != null) {
            TileEntityProxy tileEntity = (TileEntityProxy) world.getTileEntity(x, y, z);
            if (tileEntity != null) {
                for (int i = 0; i < tileEntity.getSizeInventory(); ++i) {
                    ItemStack itemStack = tileEntity.getStackInSlot(i);

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

    /**
     * A un tick random création d'un effet
     *
     * @param world  le monde
     * @param x      X
     * @param y      Y
     * @param z      Z
     * @param random
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        if (this.animate && this.entity != null) {
            TileEntityProxy tileEntity = (TileEntityProxy) world.getTileEntity(x, y, z);
            if (tileEntity != null && tileEntity.isWorked()) {
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
    }

    /**
     * Enregistrement des icon pour le block
     *
     * @param iconRegister IIconRegister
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(String.format(
                "%s:%s",
                Reference.MOD_ID, this.blockTexture)
        );
        this.front = iconRegister.registerIcon(String.format(
                "%s:%s",
                Reference.MOD_ID, this.frontTextureName)
        );
    }

    /**
     * Récupération des icon en fonction de la face
     *
     * @param side face
     * @param meta meta de la face
     * @return IIcon
     */
    @Override
    public IIcon getIcon(int side, int meta) {
        if (meta == side) {
            return this.front;
        } else {
            return this.blockIcon;
        }
    }

    /**
     * Au clique sur le block
     *
     * @param world  le monde
     * @param x      X
     * @param y      Y
     * @param z      Z
     * @param player le joueur
     * @param i1     not used
     * @param f1     not used
     * @param f2     not used
     * @param f3     not used
     * @return si il y a une action
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i1, float f1, float f2, float f3) {
        player.openGui(Main.getInstance(), this.guiInstance, world, x, y, z);
        return true;
    }

    /**
     * Quand on drop l'item
     *
     * @param i1     not used
     * @param random not used
     * @param i2     not used
     * @return Item
     */
    @Override
    public Item getItemDropped(int i1, Random random, int i2) {
        // Code coverage 17/05/2021 non utiliser ??
        return Item.getItemFromBlock(this.blockOnDrop);
    }

    /**
     * A la récupération du block
     *
     * @param world le monde
     * @param x     X
     * @param y     Y
     * @param z     Z
     * @return Item
     */
    @Override
    public Item getItem(World world, int x, int y, int z) {
        // Code coverage 17/05/2021 non utiliser ??
        return Item.getItemFromBlock(this.blockOnDrop);
    }

    /**
     * Quand le bloc est ajouter au monde
     *
     * @param world le monde
     * @param x     X
     * @param y     Y
     * @param z     Z
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void onBlockAdded(World world, int x, int y, int z) {
        // Code coverage 17/05/2021 non utiliser ??
        super.onBlockAdded(world, x, y, z);
        this.direction(world, x, y, z);
    }

    /**
     * Définition de la direction pour les faces
     *
     * @param world le monde
     * @param x     X
     * @param y     Y
     * @param z     Z
     */
    private void direction(World world, int x, int y, int z) {
        // Code coverage 17/05/2021 non utiliser ??
        if (!world.isRemote) {
            Block block = world.getBlock(x, y, z - 1);
            Block block1 = world.getBlock(x, y, z + 1);
            Block block2 = world.getBlock(x - 1, y, z);
            Block block3 = world.getBlock(x + 1, y, z);
            byte b0 = 3;

            if (block1.func_149730_j() && !block.func_149730_j()) {
                b0 = 2;
            }

            if (block.func_149730_j() && !block1.func_149730_j()) {
                b0 = 3;
            }

            if (block3.func_149730_j() && !block2.func_149730_j()) {
                b0 = 4;
            }

            if (block2.func_149730_j() && !block3.func_149730_j()) {
                b0 = 5;
            }

            world.setBlockMetadataWithNotify(x, y, z, b0, 2);
        }
    }

    /**
     * Quand le block est placer par une entity
     *
     * @param world     le monde
     * @param x         X
     * @param y         Y
     * @param z         Z
     * @param entity    le joueur a 95%
     * @param itemStack le stack de l'entité
     */
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
            ((TileEntityProxy) world.getTileEntity(x, y, z)).setName(itemStack.getDisplayName());
        }
    }

    /**
     * Création de la TileEntity a la pose du block
     *
     * @param world world
     * @param i1    not used
     * @return TileEntity
     */
    @Override
    public TileEntity createNewTileEntity(World world, int i1) {
        TileEntityProxy o = null;
        try {
            o = (TileEntityProxy) Class.forName(this.entity.getClass().getName()).newInstance();
            o.setName(this.entity.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return o;
    }
}
