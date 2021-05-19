/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 15:06
 */
package fr.debris.palatest.common.register;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CraftingRegister {

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private CraftingRegister() {
        throw new IllegalStateException("Utility class");
    }

    public static void setup() {

        if (MaterialRegister.getGrinderMaterial() == null) {
            throw new IllegalStateException("GrinderMaterial not set");
        }

        setupCraftingItems();
        setupCraftingBlocks();
    }

    private static void setupCraftingBlocks() {
        GameRegistry.addRecipe(
                new ItemStack(BlockRegister.getFrameGrinder(), 1),
                "###",
                "   ",
                "###",
                '#',
                Items.diamond
        );

        GameRegistry.addRecipe(
                new ItemStack(BlockRegister.getCasingGrinder(), 1),
                "# #",
                "# #",
                "# #",
                '#',
                Items.diamond
        );

        GameRegistry.addRecipe(
                new ItemStack(MachineRegister.getWaterGrinder(), 1),
                "###",
                "#F#",
                "###",
                '#',
                Items.diamond,
                'F',
                Blocks.furnace
        );
    }

    private static void setupCraftingItems() {
        GameRegistry.addRecipe(
                new ItemStack(ItemRegister.getDiamondBigSwordModel(), 1),
                "###",
                "# #",
                "###",
                '#',
                Items.diamond
        );

        GameRegistry.addRecipe(
                new ItemStack(ItemRegister.getDiamondPlate(), 1),
                "###",
                "#M#",
                "###",
                '#',
                Items.diamond,
                'M',
                ItemRegister.getDiamondBigSwordModel()
        );
    }
}
