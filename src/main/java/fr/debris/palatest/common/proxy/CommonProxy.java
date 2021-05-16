/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:00
 */
package fr.debris.palatest.common.proxy;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.debris.palatest.Main;
import fr.debris.palatest.common.GuiHandler;
import fr.debris.palatest.common.Reference;
import fr.debris.palatest.common.machine.TileEntityWaterGrinder;
import fr.debris.palatest.common.machine.WaterGrinder;
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

    public static BlockProxy frameGrinder;
    public static BlockProxy casingGrinder;

    public static WaterGrinder waterGrinder;
    public static WaterGrinder waterGrinderActive;

    public static ItemProxy diamondBigSword;
    public static ItemProxy diamondBigSwordModel;
    public static ItemProxy diamondModel;

    public static GuiProxy waterGrinderGui;


    protected static void initRecipe() {
        log("Init Recipe : Start");
        log("Init Recipe : End");
    }

    protected static void initItems() {
        log("Init Items : Start");
        diamondBigSword = new ItemProxy("diamond_big_sword");
        diamondBigSwordModel = new ItemProxy("modl_sword");
        diamondModel = new ItemProxy("modl_diamond");
        log("Init Items : End");
    }

    protected static void initBlocks() {
        log("Init BLock : Start");
        casingGrinder = new BlockProxy(Material.ground, "casing_grinder");
        frameGrinder = new BlockProxy(Material.ground, "frame_grinder");
        // waterGrinder = new BlockProxy(Material.ground, "water_grinder");
        // waterGrinderActive = new BlockProxy(Material.ground, "water_grinder");

        waterGrinder = new WaterGrinder(false, "water_grinder", true);
        waterGrinderActive = new WaterGrinder(true, "water_grinder_active");
        log("Init BLock : End");
    }

    public void register() {
        log("Common Proxy register");

        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());

        GameRegistry.registerTileEntity(TileEntityWaterGrinder.class, Reference.MOD_ID + "TileEntityWaterGrinder");

        initRecipe();
        initItems();
        initBlocks();
    }
}
