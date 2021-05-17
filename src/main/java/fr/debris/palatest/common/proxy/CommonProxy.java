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
import net.minecraft.block.Block;
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

    protected static MachineProxy waterGrinder;

    protected static ItemProxy diamondBigSword;
    protected static ItemProxy diamondBigSwordModel;
    protected static ItemProxy diamondPlate;

    public static BlockProxy getFrameGrinder() {
        return frameGrinder;
    }

    public static BlockProxy getCasingGrinder() {
        return casingGrinder;
    }

    public static MachineProxy getWaterGrinder() {
        return waterGrinder;
    }

    public static ItemProxy getDiamondBigSword() {
        return diamondBigSword;
    }

    public static ItemProxy getDiamondBigSwordModel() {
        return diamondBigSwordModel;
    }

    public static ItemProxy getDiamondPlate() {
        return diamondPlate;
    }

    protected static void setupRecipe() {
        log("Setup recipe is empty");
    }

    protected static void setupItems() {
        diamondBigSword = new ItemProxy("diamond_big_sword");
        diamondBigSwordModel = new ItemProxy("modl_sword");
        diamondPlate = new ItemProxy("modl_diamond");
    }

    protected static void setupBlocks() {
        casingGrinder = new BlockProxy(Material.ground, "casing_grinder");
        frameGrinder = new BlockProxy(Material.ground, "frame_grinder");
    }

    protected static void setupMachine() {
        String waterGrinderName = "water_grinder";
        TileEntityProxy waterEntity = new TileEntityWaterGrinder();
        waterEntity.setName(waterGrinderName);

        waterGrinder = new MachineProxy(
                waterGrinderName,
                "casing_grinder",
                waterGrinderName,
                0,
                waterGrinder,
                true,
                Block.soundTypeMetal,
                true,
                waterEntity
        );
    }

    public void init() {
        setupRecipe();
        setupNetworkGui();
    }

    public void setupNetworkGui() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.getInstance(), new GuiHandler());
        GameRegistry.registerTileEntity(TileEntityWaterGrinder.class, Reference.MOD_ID + "TileEntityWaterGrinder");
    }

    public void preInit() {
        setupItems();
        setupMachine();
        setupBlocks();
    }

    public void postInit() {
        log("Post Init is empty");
    }
}
