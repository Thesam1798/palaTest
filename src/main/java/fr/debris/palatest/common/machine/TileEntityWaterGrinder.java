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

    protected static final int[] slotsTop = new int[]{0};
    protected static final int[] slotBottom = new int[]{1, 2, 3};
    protected static final int[] slotSides = new int[]{1};
    protected int diamondValue = 0;
    protected int maxDiamondValue = 100;
    protected int smeltingDifficulty = 200;
    protected int valueForOneDiamond = 1000;
    // 0 Plate, 1 Full, 2 Out, 3 Model
    protected ItemStack[] itemStacks = new ItemStack[4];

    public TileEntityWaterGrinder() {
        super();
        super.itemStacks = this.itemStacks;
    }

    public static boolean isItemFuel(ItemStack itemStack1) {
        return itemStack1.getItem().equals(Items.diamond);
    }

    private ItemStack getFull() {
        if (this.itemStacks[1] != null && this.itemStacks[1].stackSize > 0) {
            return this.itemStacks[1];
        } else {
            return null;
        }
    }

    private ItemStack getPlate() {
        if (this.itemStacks[0] != null && this.itemStacks[0].stackSize > 0) {
            return this.itemStacks[0];
        } else {
            return null;
        }
    }

    private ItemStack getModel() {
        if (this.itemStacks[3] != null && this.itemStacks[3].stackSize > 0) {
            return this.itemStacks[3];
        } else {
            return null;
        }
    }

    private ItemStack getOutput() {
        if (this.itemStacks[2] != null && this.itemStacks[2].stackSize > 0) {
            return this.itemStacks[2];
        } else {
            return null;
        }
    }

    protected int getItemBurnTime(ItemStack itemStack) {
        if (itemStack != null) {
            if (itemStack.getItem().equals(CommonProxy.getDiamondBigSwordModel())) return 350;
        }
        return 0;
    }

    @Override
    public boolean canExtractItem(int position, ItemStack itemStack, int quantity) {
        // Code coverage 17/05/2021 non utiliser ??
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
        this.itemStacks = super.itemStacks;
        this.diamondValue = tagCompound.getInteger("DiamondValue");
    }

    @Override
    public int getSizeInventory() {
        return 4;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.itemStacks = this.itemStacks;
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("DiamondValue", this.diamondValue);
    }

    @SideOnly(Side.CLIENT)
    public int getSmeltingProgressScaled(int i1) {
        if (this.getModel() == null) return 0;
        return this.progressValue * i1 / getItemBurnTime(this.getModel());
    }

    @SideOnly(Side.CLIENT)
    public int getDiamondValueScaled(int i1) {
        return (this.diamondValue * this.valueForOneDiamond) * i1 / (this.maxDiamondValue * this.valueForOneDiamond);
    }

    @Override
    public void updateEntity() {
        super.itemStacks = this.itemStacks;

        boolean update = clearItemStacks();

        if (this.getModel() == null || this.getPlate() == null) {
            this.inProgress = false;
            this.progressValue = 0;
        }

        ItemStack full = this.getFull();
        if (full != null && Items.diamond.equals(full.getItem()) && this.diamondValue < this.maxDiamondValue) {
            if (full.stackSize + this.diamondValue <= this.maxDiamondValue) {
                this.diamondValue = (this.itemStacks[1].stackSize + this.diamondValue);
                full.stackSize = -1;
                this.itemStacks[1] = null;
                update = true;
            } else if ((full.stackSize + this.diamondValue) - this.maxDiamondValue > 0) {
                int add = (full.stackSize + this.diamondValue) - this.maxDiamondValue;
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

    @Override
    protected void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemStack = null;

            ItemStack model = this.getModel();
            ItemStack plate = this.getPlate();

            if (model == null || plate == null) return;

            if (CommonProxy.getDiamondBigSwordModel().equals(model.getItem()) && CommonProxy.getDiamondModel().equals(plate.getItem())) {
                itemStack = new ItemStack(CommonProxy.getDiamondBigSword());
            }

            if (itemStack == null) return;

            ItemStack output = this.getOutput();
            if (output == null) {
                this.itemStacks[2] = itemStack.copy();
            } else if (output.getItem() == itemStack.getItem()) {
                output.stackSize += itemStack.stackSize;
            }

            this.diamondValue -= getItemBurnCost(model);
            this.inProgress = false;
        }
    }

    private int getItemBurnCost(ItemStack itemStack) {
        if (itemStack != null) {
            if (CommonProxy.getDiamondBigSwordModel().equals(itemStack.getItem())) return 5;
        }
        return 0;
    }

    @Override
    // SonarLint : java:S3776 : Cognitive Complexity of methods should not be too high
    protected boolean canSmelt() {
        if (this.getModel() == null || this.getPlate() == null || this.diamondValue <= 0 || this.inProgress) {
            return false;
        } else {
            ItemStack itemStack = null;

            if (this.getModel().getItem().equals(CommonProxy.getDiamondBigSwordModel()) && this.getPlate().getItem().equals(CommonProxy.getDiamondModel())) {
                itemStack = new ItemStack(CommonProxy.getDiamondBigSword());
            }

            if (itemStack == null) return false;

            ItemStack output = this.getOutput();

            // Si le coup est plus élevé que le stockage
            if (this.diamondValue <= getItemBurnCost(itemStack)) return false;

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
    }

    @Override
    public boolean isItemValidForSlot(int position, ItemStack itemStack) {
        // Code coverage 17/05/2021 non utiliser ??
        if (position == 1 && itemStack.getItem().equals(Items.diamond)) return true;
        return position == 0 && itemStack.getItem().equals(CommonProxy.getDiamondBigSwordModel());
    }
}
