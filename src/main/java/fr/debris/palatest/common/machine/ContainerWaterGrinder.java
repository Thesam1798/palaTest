/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 13:39
 */
package fr.debris.palatest.common.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ContainerWaterGrinder extends Container {

    private final TileEntityWaterGrinder tileEntity;
    private int lastCookTime;
    private int lastBurnTime;

    public ContainerWaterGrinder(InventoryPlayer inventory, TileEntityWaterGrinder tileEntityWaterGrinder) {
        this.tileEntity = tileEntityWaterGrinder;

        // Positionnement des slot
        // p_i1824_2_ : Slot Id
        // p_i1824_3_ : X in gui
        // p_i1824_4_ : Y in gui
        this.addSlotToContainer(new Slot(tileEntityWaterGrinder, 0, 116, 53));
        this.addSlotToContainer(new Slot(tileEntityWaterGrinder, 1, 8, 53));
        this.addSlotToContainer(new Slot(tileEntityWaterGrinder, 3, 116, 25));
        this.addSlotToContainer(new SlotFurnace(inventory.player, tileEntityWaterGrinder, 2, 53, 26));

        int i;

        // Slot de l'inventaire
        for (i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Slot de la hot bar
        for (i = 0; i < 9; i++) {
            this.addSlotToContainer(new Slot(inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void addCraftingToCrafters(ICrafting craft) {
        super.addCraftingToCrafters(craft);

        craft.sendProgressBarUpdate(this, 0, this.tileEntity.diamondValue);
        craft.sendProgressBarUpdate(this, 1, this.tileEntity.progressValue);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting craft = (ICrafting) crafter;

            if (this.lastCookTime != this.tileEntity.diamondValue) {
                craft.sendProgressBarUpdate(this, 0, this.tileEntity.diamondValue);
            }

            if (this.lastBurnTime != this.tileEntity.progressValue) {
                craft.sendProgressBarUpdate(this, 1, this.tileEntity.progressValue);
            }

            this.lastBurnTime = this.tileEntity.progressValue;
            this.lastCookTime = this.tileEntity.diamondValue;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int type, int value) {
        if (type == 0) {
            this.tileEntity.diamondValue = value;
        }

        if (type == 1) {
            this.tileEntity.progressValue = value;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return this.tileEntity.isUseableByPlayer(player);
    }

    /**
     * Envoie avec shift dans un slot
     *
     * @param player   le joueur
     * @param position la position du slot
     * @return un itemStack
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int position) {
        ItemStack itemStack = null;
        Slot slot = (Slot) this.inventorySlots.get(position);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemStack = itemStack1.copy();

            if (position == 2) {
                if (!this.mergeItemStack(itemStack1, 3, 39, true)) {
                    return null;
                }

                slot.onSlotChange(itemStack1, itemStack);
            } else if (position != 1 && position != 0) {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemStack1) != null) {
                    if (!this.mergeItemStack(itemStack1, 0, 1, false)) {
                        return null;
                    }
                } else if (TileEntityWaterGrinder.isItemFuel(itemStack1)) {
                    if (!this.mergeItemStack(itemStack1, 1, 2, false)) {
                        return null;
                    }
                } else if (position < 30) {
                    if (!this.mergeItemStack(itemStack1, 30, 29, false)) {
                        return null;
                    }
                } else if (position < 39 && !this.mergeItemStack(itemStack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemStack1, 3, 39, false)) {
                return null;
            }

            if (itemStack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemStack1.stackSize == itemStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, itemStack1);
        }

        return itemStack;
    }
}
