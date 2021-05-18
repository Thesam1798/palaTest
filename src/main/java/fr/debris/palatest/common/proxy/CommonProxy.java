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
import fr.debris.palatest.common.entity.EntityGolem;
import fr.debris.palatest.common.machine.watergrinder.BlockWaterGrinder;
import fr.debris.palatest.common.machine.watergrinder.TileEntityWaterGrinder;
import fr.debris.palatest.common.proxy.block.BlockProxy;
import fr.debris.palatest.common.proxy.block.MachineProxy;
import fr.debris.palatest.common.proxy.items.ItemProxy;
import fr.debris.palatest.common.proxy.items.ItemSwordProxy;
import fr.debris.palatest.common.proxy.material.MaterialProxy;
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

    protected static Item.ToolMaterial grinderMaterial;

    protected static BlockProxy frameGrinder;
    protected static BlockProxy casingGrinder;

    protected static MachineProxy waterGrinder;

    protected static ItemSwordProxy diamondBigSword;

    protected static ItemProxy diamondBigSwordModel;
    protected static ItemProxy diamondPlate;

    protected static void setupRecipe() {
        log("Setup recipe is empty");
    }

    protected static void setupItems() {
        diamondBigSword = new ItemSwordProxy("diamond_big_sword", grinderMaterial);
        diamondBigSwordModel = new ItemProxy("modl_sword");
        diamondPlate = new ItemProxy("modl_diamond");
    }

    protected static void setupMaterial() {
        grinderMaterial = MaterialProxy.newMaterial("Grinder Material", 3, 250, 10F, 5F, 0);
    }

    protected static void setupBlocks() {
        casingGrinder = new BlockProxy(Material.ground, "casing_grinder");
        frameGrinder = new BlockProxy(Material.ground, "frame_grinder");
    }

    protected static void setupMachine() {
        new BlockWaterGrinder().register();
    }

    public static BlockProxy getFrameGrinder() {
        return frameGrinder;
    }

    public static BlockProxy getCasingGrinder() {
        return casingGrinder;
    }

    public static MachineProxy getWaterGrinder() {
        return waterGrinder;
    }

    public static void setWaterGrinder(MachineProxy waterGrinder) {
        CommonProxy.waterGrinder = waterGrinder;
    }

    public static ItemSwordProxy getDiamondBigSword() {
        return diamondBigSword;
    }

    public static ItemProxy getDiamondBigSwordModel() {
        return diamondBigSwordModel;
    }

    public static ItemProxy getDiamondPlate() {
        return diamondPlate;
    }

    public static Item.ToolMaterial getGrinderMaterial() {
        return grinderMaterial;
    }

    public void init() {
        setupRecipe();
        setupNetworkGui();
    }

    public void setupNetworkGui() {
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
        GameRegistry.registerTileEntity(TileEntityWaterGrinder.class, Reference.MOD_ID + "TileEntityWaterGrinder");
    }

    public void preInit() {
        new EntityProxy(EntityGolem.class, "golem", 0xEC4545, 0x001EFF);

        setupMaterial();
        setupItems();
        setupMachine();
        setupBlocks();
    }

    public void postInit() {
        log("Post Init is empty");
    }
}
