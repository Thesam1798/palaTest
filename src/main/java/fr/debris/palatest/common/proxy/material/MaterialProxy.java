/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 18/05/2021 : 13:40
 */
package fr.debris.palatest.common.proxy.material;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class MaterialProxy {

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private MaterialProxy() {
        throw new IllegalStateException("Utility class");
    }

    public static Item.ToolMaterial newMaterial(String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability) {
        return EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damage, enchantability);
    }

}
