/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 12:06
 */
package fr.debris.palatest.common.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.common.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWaterGrinder extends TileEntity implements ISidedInventory {

    private static final int[] slotsTop = new int[]{0};
    private static final int[] slotBottom = new int[]{1, 2, 3};
    private static final int[] slotSides = new int[]{1};
    protected int diamondValue = 0;
    protected int maxDiamondValue = 100;
    protected int smeltingDifficulty = 200;
    protected int valueForOneDiamond = 1000;
    protected int progressValue;
    protected boolean inProgress = false;

    // 0 In, 1 Ful, 2 Out, 3 Model
    private ItemStack[] itemStacks = new ItemStack[4];
    private String name;

    private static int getItemBurnTime(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItem().equals(CommonProxy.diamondBigSwordModel)) return 350;
        }
        return 0;
    }

    public static boolean isItemFuel(ItemStack itemStack1) {
        return itemStack1.getItem().equals(Items.diamond);
    }


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
        return this.itemStacks.length;
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

            if (position == 1 && itemStack.getItem().equals(Items.diamond)) {
                this.diamondValue += itemStack.stackSize;
            }
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

        this.diamondValue = tagCompound.getInteger("DiamondValue");

        if (tagCompound.hasKey("CustomName", 8)) {
            this.name = tagCompound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("DiamondValue", this.diamondValue);

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

    @SideOnly(Side.CLIENT)
    public int getSmeltingProgressScaled(int i1) {
        if (this.itemStacks[3] == null) return 0;
        return this.progressValue * i1 / getItemBurnTime(this.itemStacks[3]);
    }

    @SideOnly(Side.CLIENT)
    public int getDiamondValueScaled(int i1) {
        return (this.diamondValue * this.valueForOneDiamond) * i1 / (this.maxDiamondValue * this.valueForOneDiamond);
    }

    public boolean isBurning() {
        return this.progressValue > 0;
    }

    /**
     * Update a chaque tick
     */
    @Override
    public void updateEntity() {
        boolean inWork = this.progressValue > 0;
        boolean update = clearItemStacks();

        if (this.itemStacks[1] != null && this.itemStacks[1].stackSize > 0 && this.itemStacks[1].getItem().equals(Items.diamond) && this.diamondValue < this.maxDiamondValue) {
            if (this.itemStacks[1].stackSize + this.diamondValue <= this.maxDiamondValue) {
                this.diamondValue = (this.itemStacks[1].stackSize + this.diamondValue);
                this.itemStacks[1].stackSize = -1;
                this.itemStacks[1] = null;
                update = true;
            } else if ((this.itemStacks[1].stackSize + this.diamondValue) - this.maxDiamondValue > 0) {
                int add = (this.itemStacks[1].stackSize + this.diamondValue) - this.maxDiamondValue;
                this.diamondValue += add;
                this.itemStacks[1].stackSize -= add;
                update = true;
            }
        }

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

        if (inWork != this.progressValue > 0) {
            update = true;
            WaterGrinder.updateBlockState(this.progressValue > 0, this.worldObj, this.xCoord, this.yCoord, this.yCoord);
        }

        if (update) {
            this.markDirty();
            updateEntity();
        }
    }

    private void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemStack = null;

            if (this.itemStacks[3].getItem().equals(CommonProxy.diamondBigSwordModel) && this.itemStacks[0].getItem().equals(CommonProxy.diamondModel)) {
                itemStack = new ItemStack(CommonProxy.diamondBigSword);
            }

            if (itemStack == null) return;

            if (this.itemStacks[2] == null) {
                this.itemStacks[2] = itemStack.copy();
            } else if (this.itemStacks[2].getItem() == itemStack.getItem()) {
                this.itemStacks[2].stackSize += itemStack.stackSize;
            }

            this.diamondValue -= getItemBurnCost(this.itemStacks[3]);
            this.inProgress = false;
        }
    }

    private int getItemBurnCost(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItem().equals(CommonProxy.diamondBigSwordModel)) return 5;
        }
        return 0;
    }

    private boolean canSmelt() {
        if (this.itemStacks[0] == null || this.itemStacks[3] == null || this.diamondValue <= 0 || this.inProgress) {
            return false;
        } else {
            ItemStack itemStack = null;

            if (this.itemStacks[3].getItem().equals(CommonProxy.diamondBigSwordModel) && this.itemStacks[0].getItem().equals(CommonProxy.diamondModel)) {
                itemStack = new ItemStack(CommonProxy.diamondBigSword);
            }

            if (itemStack == null) return false;

            // Si output est vide
            if (this.itemStacks[2] == null) return true;

            // Si l'output n'est pas du meme type
            if (!this.itemStacks[2].isItemEqual(itemStack)) return false;

            // Si aucun diamond est en stockage
            if (this.diamondValue <= 0) return false;

            // Calcule du nombres apres smelting
            int result = this.itemStacks[2].stackSize + itemStack.stackSize;

            return result <= getInventoryStackLimit() && result <= this.itemStacks[2].getMaxStackSize();
        }
    }

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

    private boolean clearItemStacks() {
        boolean clear = false;

        if (this.itemStacks[0] != null && this.itemStacks[0].stackSize == 0) {
            this.itemStacks[0] = null;
            clear = true;
        }

        if (this.itemStacks[1] != null && this.itemStacks[1].stackSize == 0) {
            this.itemStacks[1] = null;
            clear = true;
        }

        if (this.itemStacks[2] != null && this.itemStacks[2].stackSize == 0) {
            this.itemStacks[2] = null;
            clear = true;
        }

        if (this.itemStacks[3] != null && this.itemStacks[3].stackSize == 0) {
            this.itemStacks[3] = null;
            clear = true;
        }

        if (this.diamondValue > this.maxDiamondValue) {
            this.diamondValue = this.maxDiamondValue;
        }

        return clear;
    }

    @Override
    public boolean isItemValidForSlot(int position, ItemStack itemStack) {
        if (position == 1 && itemStack.getItem().equals(Items.diamond)) return true;
        return position == 0 && itemStack.getItem().equals(CommonProxy.diamondBigSwordModel);
    }

    public void setName(String displayName) {
        this.name = displayName;
    }
}
