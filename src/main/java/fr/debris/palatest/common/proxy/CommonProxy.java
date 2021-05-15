/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:00
 */
package fr.debris.palatest.common.proxy;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import static fr.debris.palatest.common.Logger.log;

public class CommonProxy {

    public static final CreativeTabs palaTab = new CreativeTabs("palaTest") {
        public Item getTabIconItem() {
            return Items.arrow;
        }
    };

    protected static BlockProxy frameGrinder;
    protected static BlockProxy casingGrinder;
    protected static BlockProxy waterGrinder;
    protected static ItemProxy diamondBigSword;
    protected static ItemProxy diamondBigSwordModel;
    protected static GuiProxy waterGrinderGui;

    protected static void initRecipe() {
        log("Init Recipe : Start");
        log("Init Recipe : End");
    }

    protected static void initItems() {
        log("Init Items : Start");
        diamondBigSword = new ItemProxy("diamond_big_sword");
        diamondBigSwordModel = new ItemProxy("modl_sword");
        log("Init Items : End");
    }

    protected static void initBlocks() {
        log("Init BLock : Start");
        casingGrinder = new BlockProxy(Material.ground, "casing_grinder");
        frameGrinder = new BlockProxy(Material.ground, "frame_grinder");
        waterGrinder = new BlockProxy(Material.ground, "water_grinder");
        log("Init BLock : End");
    }

    public void register() {
        log("Common Proxy register");
        initRecipe();
        initItems();
        initBlocks();
    }
}
