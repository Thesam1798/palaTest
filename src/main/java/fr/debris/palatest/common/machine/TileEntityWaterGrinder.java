/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 12:06
 */
package fr.debris.palatest.common.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.ForgeEventFactory;

public class TileEntityWaterGrinder extends TileEntity implements ISidedInventory {

    private static final int[] slotsTop = new int[]{0};
    private static final int[] slotBottom = new int[]{2, 1};
    private static final int[] slotSides = new int[]{1};
    protected int burnTime;
    protected int curentBurnTime;
    protected int cookTime;

    // 0 Full, 1 In, 2 Out
    private ItemStack[] itemStacks = new ItemStack[3];
    private String name;

    public static boolean isItemFuel(ItemStack itemStack) {
        return getItemBurnTime(itemStack) > 0;
    }

    private static int getItemBurnTime(ItemStack itemStack) {
        if (itemStack != null) {
            int moddedBurnTime = ForgeEventFactory.getFuelBurnTime(itemStack);
            if (moddedBurnTime >= 0) return moddedBurnTime;

            Item item = itemStack.getItem();

            if (item == Items.diamond) return 100;
        }
        return 0;
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
        return quantity != 0 || position != 1;
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

        this.burnTime = tagCompound.getShort("BurnTime");
        this.curentBurnTime = tagCompound.getShort("CurentBurnTime");
        this.cookTime = getItemBurnTime(this.itemStacks[1]);

        if (tagCompound.hasKey("CustomName", 8)) {
            this.name = tagCompound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setShort("BurnTime", (short) this.burnTime);
        tagCompound.setShort("CurentBurnTime", (short) this.curentBurnTime);

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
    public int getCookTimeScaled(int i1) {
        return this.cookTime * i1 / 200;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeScaled(int i1) {
        if (this.curentBurnTime == 0) {
            this.curentBurnTime = 200;
        }

        return this.burnTime * i1 / this.curentBurnTime;
    }

    public boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void updateEntity() {
        boolean flag = this.burnTime > 0;
        boolean flag1 = false;

        if (this.burnTime > 0) {
            --this.burnTime;
        }

        if (!this.worldObj.isRemote) {
            if (this.burnTime == 0 && this.canSmelt()) {
                this.curentBurnTime = this.burnTime = getItemBurnTime(this.itemStacks[1]);

                if (this.burnTime > 0) {
                    flag1 = true;
                    if (this.itemStacks[1] != null) {
                        --this.itemStacks[1].stackSize;

                        if (this.itemStacks[1].stackSize == 0) {
                            this.itemStacks[1] = this.itemStacks[1].getItem().getContainerItem(this.itemStacks[1]);
                        }
                    }
                }
            }

            if (this.isBurning() && this.canSmelt()) {
                ++this.cookTime;
                if (this.cookTime == 200) {
                    this.cookTime = 0;
                    this.smeltItem();
                    flag1 = true;
                }
            } else {
                this.cookTime = 0;
            }
        }

        if (flag != this.burnTime > 0) {
            flag1 = true;
            WaterGrinder.updateBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.yCoord);
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(this.itemStacks[0]);

            if (this.itemStacks[2] == null) {
                this.itemStacks[2] = itemStack.copy();
            } else if (this.itemStacks[2].getItem() == itemStack.getItem()) {
                this.itemStacks[2].stackSize += itemStack.stackSize;
            }
        }
    }

    private boolean canSmelt() {
        if (this.itemStacks[0] == null) {
            return false;
        } else {
            ItemStack itemStack = FurnaceRecipes.smelting().getSmeltingResult(this.itemStacks[0]);

            if (itemStack == null) return false;
            if (this.itemStacks[2] == null) return true;
            if (!this.itemStacks[2].isItemEqual(itemStack)) return false;
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

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int position, ItemStack itemStack) {
        return position != 2 && (position != 1 || isItemFuel(itemStack));
    }

    public void setName(String displayName) {
        this.name = displayName;
    }
}
