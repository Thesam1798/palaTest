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

    public static final int[] slotsTop = new int[]{0};
    public static final int[] slotBottom = new int[]{2};
    public static final int[] slotSides = new int[]{1};
    public int progressValue;
    public boolean inProgress = false;

    public ItemStack[] itemStacks;
    public String name;

    protected abstract int getItemBurnTime(ItemStack itemStack);

    @Override
    public int[] getAccessibleSlotsFromSide(int position) {
        if (position == 0) return slotBottom;
        return position == 1 ? slotsTop : slotSides;
    }

    @Override
    public boolean canInsertItem(int position, ItemStack itemStack, int quantity) {
        return this.isItemValidForSlot(position, itemStack);
    }

    @Override
    public boolean canExtractItem(int position, ItemStack itemStack, int quantity) {
        return position == 2;
    }

    @Override
    public int getSizeInventory() {
        return 3;
    }

    @Override
    public ItemStack getStackInSlot(int position) {
        return this.itemStacks[position];
    }

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

    @Override
    public ItemStack getStackInSlotOnClosing(int position) {
        if (this.itemStacks[position] != null) {
            ItemStack itemStack = this.itemStacks[position];
            this.itemStacks[position] = null;

            return itemStack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int position, ItemStack itemStack) {
        this.itemStacks[position] = itemStack;

        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.name : "Undefined Name";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return this.name != null && this.name.length() > 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

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

        if (tagCompound.hasKey("CustomName", 8)) {
            this.name = tagCompound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("progress", this.progressValue);

        if (this.hasCustomInventoryName()) {
            tagCompound.setString("CustomName", this.name);
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

    protected abstract void smeltItem();

    protected abstract boolean canSmelt();

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) return false;
        return player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) < 64.0D;
    }

    @Override
    public void openInventory() {
        updateEntity();
    }

    @Override
    public void closeInventory() {
        updateEntity();
    }

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

    public String getName() {
        return this.name;
    }

    public void setName(String displayName) {
        this.name = displayName;
    }

    public boolean isWorked() {
        return this.progressValue > 0;
    }
}
