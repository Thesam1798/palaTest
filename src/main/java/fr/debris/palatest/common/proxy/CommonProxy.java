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
import fr.debris.palatest.common.machine.watergrinder.TileEntityWaterGrinder;
import fr.debris.palatest.common.register.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import static fr.debris.palatest.common.Logger.log;

public class CommonProxy {

    public static final CreativeTabs palaTab = new CreativeTabs("palaTest") {
        public Item getTabIconItem() {
            if (MachineRegister.getWaterGrinder() != null) {
                return Item.getItemFromBlock(MachineRegister.getWaterGrinder());
            }
            return Items.diamond;
        }
    };

    protected static void setupRecipe() {
        log("Setup recipe is empty");
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

        MaterialRegister.setup();
        ItemRegister.setup();
        MachineRegister.setup();
        BlockRegister.setup();
        CraftingRegister.setup();
    }

    public void postInit() {
        log("Post Init is empty");
    }
}
