/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:37
 */
package fr.debris.palatest.common.proxy.items;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.debris.palatest.common.Reference;
import fr.debris.palatest.common.proxy.CommonProxy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;

/**
 * Sur class de ItemHoe pour l'enregistrement
 */
public class ItemHoeProxy extends ItemHoe {

    public ItemHoeProxy(String name, Item.ToolMaterial material) {
        super(material);
        this.setCreativeTab(CommonProxy.palaTab);
        this.setUnlocalizedName(name);

        GameRegistry.registerItem(this, name);
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(String.format(
                "%s:%s",
                Reference.MOD_ID,
                this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1)
        ));
    }
}
