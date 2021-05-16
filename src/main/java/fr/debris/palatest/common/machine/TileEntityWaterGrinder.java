/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 12:06
 */
package fr.debris.palatest.common.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.common.proxy.CommonProxy;
import fr.debris.palatest.common.proxy.TileEntityProxy;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityWaterGrinder extends TileEntityProxy {

    public static final int[] slotsTop = new int[]{0};
    public static final int[] slotBottom = new int[]{1, 2, 3};
    public static final int[] slotSides = new int[]{1};
    // 0 In, 1 Ful, 2 Out, 3 Model
    public final ItemStack[] itemStacks = new ItemStack[4];
    public int diamondValue = 0;
    public int maxDiamondValue = 100;
    public int smeltingDifficulty = 200;
    public int valueForOneDiamond = 1000;

    public TileEntityWaterGrinder() {
        super();
        super.itemStacks = this.itemStacks;
    }

    public static boolean isItemFuel(ItemStack itemStack1) {
        return itemStack1.getItem().equals(Items.diamond);
    }

    protected int getItemBurnTime(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItem().equals(CommonProxy.diamondBigSwordModel)) return 350;
        }
        return 0;
    }

    @Override
    public boolean canExtractItem(int position, ItemStack itemStack, int quantity) {
        return position == 2;
    }

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

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.diamondValue = tagCompound.getInteger("DiamondValue");
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("DiamondValue", this.diamondValue);
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

    @Override
    public void updateEntity() {
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

        if (update) {
            this.markDirty();
            updateEntity();
            return;
        }

        super.updateEntity();
    }

    @Override
    protected void smeltItem() {
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

    @Override
    protected boolean canSmelt() {
        if (this.itemStacks[0] == null || this.itemStacks[3] == null || this.diamondValue <= 0 || this.inProgress) {
            return false;
        } else {
            ItemStack itemStack = null;

            if (this.itemStacks[3].getItem().equals(CommonProxy.diamondBigSwordModel) && this.itemStacks[0].getItem().equals(CommonProxy.diamondModel)) {
                itemStack = new ItemStack(CommonProxy.diamondBigSword);
            }

            if (itemStack == null) return false;

            // Si le coup est plus élevé que le stockage
            if (this.diamondValue <= getItemBurnCost(itemStack)) return false;

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
    public boolean isItemValidForSlot(int position, ItemStack itemStack) {
        if (position == 1 && itemStack.getItem().equals(Items.diamond)) return true;
        return position == 0 && itemStack.getItem().equals(CommonProxy.diamondBigSwordModel);
    }
}
