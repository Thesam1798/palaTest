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

/**
 * Class de base pour les different init
 */
public class CommonProxy {

    /**
     * Tab dans le creative
     */
    public static final CreativeTabs palaTab = new CreativeTabs("palaTest") {
        public Item getTabIconItem() {
            if (MachineRegister.getWaterGrinder() != null) {
                return Item.getItemFromBlock(MachineRegister.getWaterGrinder());
            }
            return Items.diamond;
        }
    };

    /**
     * Init du mod ajout des recette et des GUI/Network
     */
    public void init() {
        CraftingRegister.setup();

        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
        GameRegistry.registerTileEntity(TileEntityWaterGrinder.class, Reference.MOD_ID + "TileEntityWaterGrinder");
    }

    /**
     * Enregistrement de tout les block item material....
     */
    public void preInit() {
        new EntityProxy(EntityGolem.class, "golem", 0xEC4545, 0x001EFF);

        MaterialRegister.setup();
        ItemRegister.setup();
        MachineRegister.setup();
        BlockRegister.setup();
    }

    /**
     * Interaction avec d'autre mod
     */
    public void postInit() {
        log("Post Init is empty");
    }
}
