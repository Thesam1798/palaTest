/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 12:06
 */
package fr.debris.palatest.common.machine.watergrinder;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.common.entity.EntityGolem;
import fr.debris.palatest.common.proxy.CommonProxy;
import fr.debris.palatest.common.proxy.gui.TileEntityProxy;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;

public class TileEntityWaterGrinder extends TileEntityProxy {

    protected int[] slotsTop = new int[]{0};
    protected int[] slotBottom = new int[]{1, 2, 3};
    protected int[] slotSides = new int[]{1};
    protected int diamondValue = 0;
    protected int maxDiamondValue = 100;
    protected int smeltingDifficulty = 1;
    protected int valueForOneDiamond = 1000;
    // 0 Plate, 1 Fuel, 2 Out, 3 Model
    protected ItemStack[] itemStacks = new ItemStack[4];

    protected EntityGolem[] golems = new EntityGolem[5];
    protected boolean spawnGolems = false;
    protected boolean isGolemsValid = false;

    public TileEntityWaterGrinder() {
        super();
        super.slotsTop = this.slotsTop;
        super.slotBottom = this.slotBottom;
        super.slotSides = this.slotSides;
        super.itemStacks = this.itemStacks;
    }

    /**
     * Permet de récupérer la plate en slot 0
     *
     * @return ItemStack
     */
    private ItemStack getPlate() {
        if (this.itemStacks[0] != null && this.itemStacks[0].stackSize > 0) {
            return this.itemStacks[0];
        } else {
            return null;
        }
    }

    /**
     * Permet de récupérer le fuel en slot 1
     *
     * @return ItemStack
     */
    private ItemStack getFuel() {
        if (this.itemStacks[1] != null && this.itemStacks[1].stackSize > 0) {
            return this.itemStacks[1];
        } else {
            return null;
        }
    }

    /**
     * Permet de récupérer la sortie en slot 2
     *
     * @return ItemStack
     */
    private ItemStack getOutput() {
        if (this.itemStacks[2] != null && this.itemStacks[2].stackSize > 0) {
            return this.itemStacks[2];
        } else {
            return null;
        }
    }

    /**
     * Permet de récupérer le model en slot 3
     *
     * @return ItemStack
     */
    private ItemStack getModel() {
        if (this.itemStacks[3] != null && this.itemStacks[3].stackSize > 0) {
            return this.itemStacks[3];
        } else {
            return null;
        }
    }

    /**
     * Permet de savoir combien de temps (tiks) il faut pour smelt l'item
     *
     * @param stack le stack d'items
     * @return int
     */
    protected int getItemBurnTime(ItemStack stack) {
        if (stack != null && stack.getItem().equals(CommonProxy.getDiamondBigSwordModel())) return 350;
        return 0;
    }

    /**
     * Utilise pas les hopper
     *
     * @param position  emplacement
     * @param itemStack élément
     * @param side      côté
     * @return side int
     */
    @Override
    public boolean canExtractItem(int position, ItemStack itemStack, int side) {
        return position == 2;
    }

    /**
     * Définit la pile d'objets donnée sur l'emplacement spécifié dans l'inventaire.
     */
    @Override
    public void setInventorySlotContents(int position, ItemStack itemStack) {
        this.itemStacks[position] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();

            if (position == 1 && itemStack.getItem().equals(Items.diamond)) {
                this.diamondValue += itemStack.stackSize;
                itemStack.stackSize = 0;
                this.itemStacks[position] = null;
            }
        }
    }

    /**
     * Permet de set les info depuis les NBT
     *
     * @param tagCompound Object a update
     */
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.itemStacks = super.itemStacks;
        this.diamondValue = tagCompound.getInteger("DiamondValue");
        this.smeltingDifficulty = tagCompound.getInteger("SmeltingDifficulty");
        this.spawnGolems = tagCompound.getBoolean("SpawnGolems");

        NBTTagList tagList = tagCompound.getTagList("Golems", 10);
        this.golems = new EntityGolem[this.golems.length];

        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
            byte byte0 = tagCompound1.getByte("Id");

            if (byte0 >= 0 && byte0 < this.golems.length) {
                this.golems[byte0] = new EntityGolem(this.worldObj);
                this.golems[byte0].getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(this.golems[byte0].getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue() * smeltingDifficulty);
                this.golems[byte0].getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.golems[byte0].getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue() * smeltingDifficulty);
                this.golems[byte0].setPosition(this.xCoord, this.yCoord + 1.5, this.zCoord);
                this.golems[byte0].onSpawnWithEgg(null);
                this.golems[byte0].readFromNBT(tagCompound1);
            }
        }
    }

    /**
     * Retourne la taille de la machine
     *
     * @return int
     */
    @Override
    public int getSizeInventory() {
        return 4;
    }

    /**
     * Permet de set les NBT depuis les info
     *
     * @param tagCompound Object a update
     */
    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.itemStacks = this.itemStacks;
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("DiamondValue", this.diamondValue);
        tagCompound.setInteger("SmeltingDifficulty", this.smeltingDifficulty);
        tagCompound.setBoolean("SpawnGolems", this.spawnGolems);

        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.golems.length; i++) {
            if (this.golems[i] != null) {
                NBTTagCompound tagCompound1 = new NBTTagCompound();
                tagCompound1.setByte("Id", (byte) i);
                this.golems[i].writeToNBT(tagCompound1);
                tagList.appendTag(tagCompound1);
            }
        }

        tagCompound.setTag("Golems", tagList);
    }

    /**
     * Permet de convertir la progression en "pixel"
     *
     * @param size 100% = size
     * @return int
     */
    @SideOnly(Side.CLIENT)
    public int getSmeltingProgressScaled(int size) {
        if (this.getModel() == null) return 0;
        return this.progressValue * size / getItemBurnTime(this.getModel());
    }

    /**
     * Permet de convertir la progression en "pixel"
     *
     * @param size 100% = size
     * @return int
     */
    @SideOnly(Side.CLIENT)
    public int getDiamondValueScaled(int size) {
        return (this.diamondValue * this.valueForOneDiamond) * size / (this.maxDiamondValue * this.valueForOneDiamond);
    }

    /**
     * Action a chaque update
     */
    @Override
    public void updateEntity() {
        super.itemStacks = this.itemStacks;

        boolean update = clearItemStacks();

        if (!this.isGolemsValid && this.golems.length > 0) updateLocalGolemFromWorld();

        if (this.getModel() == null || this.getPlate() == null) {
            this.inProgress = false;
            this.progressValue = 0;
        }

        ItemStack full = this.getFuel();
        if (full != null && Items.diamond.equals(full.getItem()) && this.diamondValue < this.maxDiamondValue) {
            if (full.stackSize + this.diamondValue < this.maxDiamondValue) {
                this.diamondValue += this.itemStacks[1].stackSize;
                full.stackSize = -1;
                this.itemStacks[1] = null;
                update = true;
            } else if (this.maxDiamondValue - this.diamondValue > 0) {
                int add = (this.maxDiamondValue - this.diamondValue);
                this.diamondValue += add;
                full.stackSize -= add;
                update = true;
            }
        }

        if (update) {
            this.markDirty();
            updateEntity();
            return;
        }

        super.updateEntity();
    }

    /**
     * Permet de férifier si toute les condition sont valid pour smelt
     *
     * @return boolean
     */
    @Override
    protected boolean smeltValid() {
        if (this.spawnGolems) {
            boolean valid = true;
            for (int i = 0; i < 5; i++) {
                if (this.golems[i] != null && !this.golems[i].isEntityAlive()) {
                    this.golems[i] = null;
                } else if (this.golems[i] != null && this.golems[i].isEntityAlive()) {
                    valid = false;
                }
            }

            if (!valid) return false;

            // TODO : SHOW NOTIF

            this.spawnGolems = false;
            return true;
        } else if (!this.worldObj.isRemote) {
            spawnGolem();
        }

        return false;
    }

    private void spawnGolem() {
        this.spawnGolems = true;
        for (int i = 0; i < 5; i++) {
            if (this.golems[i] == null) {
                this.golems[i] = new EntityGolem(this.worldObj);
                this.golems[i].setPosition(this.xCoord, this.yCoord + 1.5, this.zCoord);
                this.golems[i].getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(this.golems[i].getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue() * smeltingDifficulty);
                this.golems[i].getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.golems[i].getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue() * smeltingDifficulty);
                this.golems[i].onSpawnWithEgg(null);

                this.worldObj.spawnEntityInWorld(this.golems[i]);
            }
        }

        smeltingDifficulty *= 4;
    }

    /**
     * Permet d'update les golem en local depuis les NBT en vrais entity
     * /!\ NON OPTI /!\
     * TODO : A Revoir
     */
    private void updateLocalGolemFromWorld() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            HashMap<String, Integer> uuid = new HashMap<String, Integer>();

            for (int i = 0; i < this.golems.length; i++) {
                if (this.golems[i] != null) {
                    uuid.put(this.golems[i].getUniqueID().toString(), i);
                }
            }

            for (int i = 0; i < this.worldObj.loadedEntityList.size(); i++) {
                Object entity = this.worldObj.loadedEntityList.get(i);

                if (entity != null && entity.getClass().equals(EntityGolem.class)) {
                    String entityUuid = ((EntityGolem) entity).getUniqueID().toString();

                    if (uuid.containsKey(entityUuid)) {
                        int id = uuid.get(entityUuid);
                        this.golems[id] = (EntityGolem) entity;
                    }
                }
            }

            this.isGolemsValid = true;
        }
    }

    /**
     * Cela permet de process l'item
     */
    @Override
    protected void smeltItem() {
        if (!this.canSmelt()) return;

        ItemStack itemStack = null;

        ItemStack model = this.getModel();
        ItemStack plate = this.getPlate();

        if (model == null || plate == null) return;

        if (CommonProxy.getDiamondBigSwordModel().equals(model.getItem()) && CommonProxy.getDiamondPlate().equals(plate.getItem())) {
            itemStack = new ItemStack(CommonProxy.getDiamondBigSword());
        }

        if (itemStack == null) return;

        ItemStack output = this.getOutput();
        if (output == null) {
            this.itemStacks[2] = itemStack.copy();
        } else if (output.getItem() == itemStack.getItem()) {
            output.stackSize += itemStack.stackSize;
        }

        this.diamondValue -= getItemBurnCost(itemStack);
        this.inProgress = false;
    }

    /**
     * Permet de savoir combien de consommation prend l'item
     *
     * @param stack le stack
     * @return int
     */
    private int getItemBurnCost(ItemStack stack) {
        if (stack != null && CommonProxy.getDiamondBigSword().equals(stack.getItem())) return 5;
        return 0;
    }

    /**
     * Permet de savoir si tout est pres pour le process
     *
     * @return boolean
     */
    @Override
    protected boolean canSmelt() {
        if (this.getModel() == null || this.getPlate() == null || this.diamondValue <= 0 || this.inProgress) {
            return false;
        }

        ItemStack itemStack = null;

        if (this.getModel().getItem().equals(CommonProxy.getDiamondBigSwordModel()) && this.getPlate().getItem().equals(CommonProxy.getDiamondPlate())) {
            itemStack = new ItemStack(CommonProxy.getDiamondBigSword());
        }

        if (itemStack == null) return false;

        ItemStack output = this.getOutput();

        // Si le coup est plus élevé que le stockage
        if (this.diamondValue < getItemBurnCost(itemStack)) return false;

        // Si output est vide
        if (output == null) return true;

        // Si l'output n'est pas du meme type
        if (!output.isItemEqual(itemStack)) return false;

        // Si aucun diamond est en stockage
        if (this.diamondValue <= 0) return false;

        // Calcule du nombres apres smelting
        int result = output.stackSize + itemStack.stackSize;

        return result <= getInventoryStackLimit() && result <= output.getMaxStackSize();
    }

    /**
     * Permet de savoir le nombres d'items posible dans un slot
     *
     * @param position le slot
     * @return le nombres d'items
     */
    @Override
    public int getInventoryStackLimit(int position) {
        return position == 1 ? 64 : 1;
    }

    /**
     * Retourne si l'item peut entrée dans le slot
     *
     * @param position la position
     * @param stack    le stack
     * @return boolean
     */
    @Override
    public boolean isItemValidForSlot(int position, ItemStack stack) {
        if (position == 1 && stack.getItem().equals(Items.diamond)) return true;
        if (position == 0 && stack.getItem().equals(CommonProxy.getDiamondPlate()) && (getPlate() == null || getPlate().stackSize == 0))
            return true;
        return position == 3 && stack.getItem().equals(CommonProxy.getDiamondBigSwordModel()) && (getModel() == null || getModel().stackSize == 0);
    }

    /**
     * Retourne la position ou l'item peut entrer
     *
     * @param stack le stack
     * @return le slot
     */
    public int getSlotItemValid(ItemStack stack) {
        for (int i = 0; i < this.getSizeInventory(); i++) {
            if (this.isItemValidForSlot(i, stack)) {
                return i;
            }
        }

        return -1;
    }

    public int getDiamondValue() {
        return diamondValue;
    }

    public void setDiamondValue(int diamondValue) {
        this.diamondValue = diamondValue;
    }
}
