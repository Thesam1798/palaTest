/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 20:43
 */
package fr.debris.palatest.common.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityProxy extends TileEntity implements ISidedInventory {

    // SonarLint Correction : java:S1192 : String literals should not be duplicated
    public static final String NBT_CUSTOM_NAME = "CustomName";
    protected static final int[] slotsTop = new int[]{0};
    protected static final int[] slotBottom = new int[]{2};
    protected static final int[] slotSides = new int[]{1};
    // SonarLint : java:S1104 : Class variable fields should not have public accessibility
    public int progressValue;
    protected boolean inProgress = false;

    protected ItemStack[] itemStacks;
    protected String name;

    protected abstract int getItemBurnTime(ItemStack itemStack);

    /**
     * Renvoie un tableau contenant les indices des emplacements accessibles par automatisation du côté donné de ce bloc.
     *
     * @param side side
     * @return int[]
     */
    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        // Code coverage 17/05/2021 non utiliser ??
        if (side == 0) return slotBottom;
        return side == 1 ? slotsTop : slotSides;
    }

    /**
     * Renvoie true si l'automatisation peut insérer l'élément donné dans l'emplacement donné depuis le côté donné.
     *
     * @param position  emplacement
     * @param itemStack élément
     * @param side      côté
     * @return boolean
     */
    @Override
    public boolean canInsertItem(int position, ItemStack itemStack, int side) {
        // Code coverage 17/05/2021 non utiliser ??
        return this.isItemValidForSlot(position, itemStack);
    }

    /**
     * Renvoie true si l'automatisation peut extraire l'élément donné dans l'emplacement donné du côté donné
     *
     * @param position  emplacement
     * @param itemStack élément
     * @param side      côté
     * @return boolean
     */
    @Override
    public boolean canExtractItem(int position, ItemStack itemStack, int side) {
        // Code coverage 17/05/2021 non utiliser | 100% de surcharge
        return position == 2;
    }

    /**
     * Retourne la taille de la machine
     *
     * @return int
     */
    @Override
    public int getSizeInventory() {
        // Code coverage 17/05/2021 non utiliser | 100% de surcharge
        return 3;
    }

    /**
     * Renvoie le stack dans l'emplacement X
     *
     * @param position position
     * @return ItemStack
     */
    @Override
    public ItemStack getStackInSlot(int position) {
        return this.itemStacks[position];
    }

    /**
     * Supprime d'un emplacement d'inventaire d'un nombre spécifié d'éléments
     *
     * @param position position
     * @param quantity quantity
     * @return ItemStack
     */
    @Override
    public ItemStack decrStackSize(int position, int quantity) {
        if (this.itemStacks[position] != null) {
            ItemStack itemStack;
            if (this.itemStacks[position].stackSize <= quantity) {
                itemStack = this.itemStacks[position];
                this.itemStacks[position] = null;
            } else {
                itemStack = this.itemStacks[position].splitStack(quantity);

                if (this.itemStacks[position].stackSize == 0) {
                    this.itemStacks[position] = null;
                }
            }
            return itemStack;
        } else {
            return null;
        }
    }

    /**
     * Lorsque certains conteneurs sont fermés, ils appellent cela sur chaque emplacement, puis déposent tout ce qu'ils retournent en tant qu'entité
     * Item - comme lorsque vous fermez une interface graphique de Workbench.
     *
     * @param position position
     * @return ItemStack
     */
    @Override
    public ItemStack getStackInSlotOnClosing(int position) {
        // Code coverage 17/05/2021 non utiliser ??
        if (this.itemStacks[position] != null) {
            ItemStack itemStack = this.itemStacks[position];
            this.itemStacks[position] = null;

            return itemStack;
        } else {
            return null;
        }
    }

    /**
     * Définit la pile d'objets donnée sur l'emplacement spécifié dans l'inventaire.
     */
    @Override
    public void setInventorySlotContents(int position, ItemStack itemStack) {
        // Code coverage 17/05/2021 non utiliser | 100% de surcharge
        this.itemStacks[position] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    /**
     * Permet de récupérer le nom de la machine pour l'ui
     *
     * @return String
     */
    @Override
    public String getInventoryName() {
        // Code coverage 17/05/2021 non utiliser ??
        return this.hasCustomInventoryName() ? this.name : "Undefined Name";
    }

    /**
     * Permet de savoir si la machine a un nom custom
     *
     * @return boolean
     */
    @Override
    public boolean hasCustomInventoryName() {
        return this.name != null && this.name.length() > 0;
    }

    /**
     * Renvoie la taille de maximale d'inventaire
     *
     * @return int
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Permet de set les info depuis les NBT
     *
     * @param tagCompound Object a update
     */
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        NBTTagList tagList = tagCompound.getTagList("Items", 10);
        this.itemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound1 = tagList.getCompoundTagAt(i);
            byte byte0 = tagCompound1.getByte("Slot");

            if (byte0 >= 0 && byte0 < this.itemStacks.length) {
                this.itemStacks[byte0] = ItemStack.loadItemStackFromNBT(tagCompound1);
            }
        }

        this.progressValue = tagCompound.getInteger("progress");

        if (tagCompound.hasKey(NBT_CUSTOM_NAME, 8)) {
            this.name = tagCompound.getString(NBT_CUSTOM_NAME);
        }
    }

    /**
     * Permet de set les NBT depuis les info
     *
     * @param tagCompound Object a update
     */
    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("progress", this.progressValue);

        if (this.hasCustomInventoryName()) {
            tagCompound.setString(NBT_CUSTOM_NAME, this.name);
        }

        NBTTagList tagList = new NBTTagList();

        for (int i = 0; i < this.itemStacks.length; i++) {
            if (this.itemStacks[i] != null) {
                NBTTagCompound tagCompound1 = new NBTTagCompound();
                tagCompound1.setByte("Slot", (byte) i);
                this.itemStacks[i].writeToNBT(tagCompound1);
                tagList.appendTag(tagCompound1);
            }
        }

        tagCompound.setTag("Items", tagList);
    }

    /**
     * Update a chaque tick
     */
    @Override
    public void updateEntity() {
        boolean update = clearItemStacks();

        if (this.progressValue > 0) {
            --this.progressValue;
        }

        if (!this.worldObj.isRemote) {
            if (this.progressValue == 0 && this.canSmelt() && getItemBurnTime(this.itemStacks[3]) > 0) {
                this.progressValue = getItemBurnTime(this.itemStacks[3]);
                this.inProgress = true;
                update = true;
            } else if (this.inProgress && this.progressValue == 0) {
                this.inProgress = false;
                update = true;
                smeltItem();
            }
        }

        if (update) {
            this.markDirty();
            updateEntity();
        }
    }

    /**
     * Cela permet de process l'item
     */
    protected abstract void smeltItem();

    /**
     * Permet de savoir si tout est pres pour le process
     *
     * @return boolean
     */
    protected abstract boolean canSmelt();

    /**
     * Permet de vérifier si le joueur a la possibilité d'ouvrir l'interface
     *
     * @param player joueur
     * @return boolean
     */
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) return false;
        return player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) < 64.0D;
    }

    /**
     * A l'ouverture de l'inventaire
     */
    @Override
    public void openInventory() {
        // Code coverage 17/05/2021 non utiliser ??
        updateEntity();
    }

    /**
     * A la fermeture de l'inventaire
     */
    @Override
    public void closeInventory() {
        // Code coverage 17/05/2021 non utiliser ??
        updateEntity();
    }

    /**
     * Clear les ItemStacks a 0
     *
     * @return si un changement effectué
     */
    protected boolean clearItemStacks() {
        boolean clear = false;

        for (int i = 0; i < this.itemStacks.length; i++) {
            if (this.itemStacks[i] != null && this.itemStacks[i].stackSize == 0) {
                this.itemStacks[i] = null;
                clear = true;
            }
        }

        return clear;
    }

    /**
     * Récupère le nom de la machine
     *
     * @return nom
     */
    public String getName() {
        return this.name;
    }

    /**
     * Définit le nom de la machine
     *
     * @param displayName nom
     */
    public void setName(String displayName) {
        this.name = displayName;
    }

    /**
     * Renvoie si la machine est en fonctionnement
     * @return boolean
     */
    public boolean isWorked() {
        return this.progressValue > 0;
    }
}
