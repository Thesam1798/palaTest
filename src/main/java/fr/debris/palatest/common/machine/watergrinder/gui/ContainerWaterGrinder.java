/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 16/05/2021 : 13:39
 */
package fr.debris.palatest.common.machine.watergrinder.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.debris.palatest.common.machine.watergrinder.TileEntityWaterGrinder;
import fr.debris.palatest.common.proxy.TileEntityProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerWaterGrinder extends Container {

    private final TileEntityWaterGrinder tileEntity;
    private int lastCookTime;
    private int lastBurnTime;

    /**
     * Création du container
     *
     * @param inventory              inventaire du joueur
     * @param tileEntityWaterGrinder la Tile
     */
    public ContainerWaterGrinder(InventoryPlayer inventory, TileEntityWaterGrinder tileEntityWaterGrinder) {
        this.tileEntity = tileEntityWaterGrinder;

        // Positionnement des slot
        // p_i1824_2_ : Slot Id
        // p_i1824_3_ : X in gui
        // p_i1824_4_ : Y in gui
        this.addSlotToContainer(newSlot(tileEntityWaterGrinder, 0, 116, 53));
        this.addSlotToContainer(newSlot(tileEntityWaterGrinder, 1, 8, 53));
        this.addSlotToContainer(new SlotFurnace(inventory.player, tileEntityWaterGrinder, 2, 53, 26));
        this.addSlotToContainer(newSlot(tileEntityWaterGrinder, 3, 116, 25));

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

    /**
     * Permet de créer un slot avec la vérification via l'inventaire
     *
     * @param entityProxy TileEntityProxy
     * @param slotId      id du slot
     * @param x           x
     * @param y           y
     * @return boolean
     */
    private Slot newSlot(final TileEntityProxy entityProxy, int slotId, int x, int y) {
        return new Slot(entityProxy, slotId, x, y) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return this.inventory.isItemValidForSlot(this.getSlotIndex(), stack);
            }

            @Override
            public int getSlotStackLimit() {
                return ((TileEntityProxy) (this.inventory)).getInventoryStackLimit(this.getSlotIndex());
            }
        };
    }

    /**
     * Ajout d'une GUI et update des progress bar
     *
     * @param craft ICrafting
     */
    @Override
    public void addCraftingToCrafters(ICrafting craft) {
        super.addCraftingToCrafters(craft);

        craft.sendProgressBarUpdate(this, 0, this.tileEntity.getDiamondValue());
        craft.sendProgressBarUpdate(this, 1, this.tileEntity.getProgressValue());
    }

    /**
     * Si un changent est survenue update de tout les progress bar
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (Object crafter : this.crafters) {
            ICrafting craft = (ICrafting) crafter;

            if (this.lastCookTime != this.tileEntity.getDiamondValue()) {
                craft.sendProgressBarUpdate(this, 0, this.tileEntity.getDiamondValue());
            }

            if (this.lastBurnTime != this.tileEntity.getProgressValue()) {
                craft.sendProgressBarUpdate(this, 1, this.tileEntity.getProgressValue());
            }

            this.lastBurnTime = this.tileEntity.getProgressValue();
            this.lastCookTime = this.tileEntity.getDiamondValue();
        }
    }

    /**
     * Permet de set les valeur des progress bar
     *
     * @param type  la progress bar
     * @param value la valeur
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int type, int value) {
        if (type == 0) {
            this.tileEntity.setDiamondValue(value);
        }

        if (type == 1) {
            this.tileEntity.setProgressValue(value);
        }
    }

    /**
     * Vérifit si le joueur en param peut utiliser le container
     *
     * @param player joueur
     * @return boolean
     */
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
        ItemStack itemStack;
        Slot slot = (Slot) this.inventorySlots.get(position);

        if (slot == null || !slot.getHasStack())
            return null;

        ItemStack itemStack1 = slot.getStack();
        itemStack = itemStack1.copy();

        // provenance de l'inventaire >= 4
        if (position >= 4) {

            // Récupération du slot de destination
            int validSlot = this.tileEntity.getSlotItemValid(itemStack1);

            // Si le slot est invalid return null
            if (validSlot == -1) return null;

            // Récupération du maximum d'item et division
            int limit = this.tileEntity.getInventoryStackLimit(validSlot);
            ItemStack itemStack2 = itemStack1;

            if (limit < itemStack1.stackSize)
                itemStack2 = itemStack1.splitStack(limit);

            // Si il n'a pas été possible de mouve l'item return null
            if (!this.mergeItemStack(itemStack2, validSlot, validSlot + 1, true)) return null;

            // Update des slot
            slot.onSlotChange(itemStack2, itemStack1);
            slot.onSlotChange(itemStack2, itemStack);
        } else {
            // Si il n'a pas été possible de mouve l'item return null
            if (!this.mergeItemStack(itemStack1, 4, this.inventorySlots.size(), true)) return null;

            // Update des slot
            slot.onSlotChange(itemStack1, itemStack);
        }

        // Si les stack on la meme taille, pas d'update
        if (itemStack1.stackSize == itemStack.stackSize)
            return null;

        // Suppression du slot si la taille est a 0
        if (itemStack1.stackSize == 0) slot.putStack(null);

        // Mark slot is Dirty
        slot.onSlotChanged();
        slot.onPickupFromSlot(player, itemStack1);

        return itemStack;
    }
}
