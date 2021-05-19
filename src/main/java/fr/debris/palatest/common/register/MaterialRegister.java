/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 15:06
 */
package fr.debris.palatest.common.register;

import fr.debris.palatest.common.proxy.material.MaterialProxy;
import net.minecraft.item.Item;

public class MaterialRegister {
    protected static Item.ToolMaterial grinderMaterial;

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private MaterialRegister() {
        throw new IllegalStateException("Utility class");
    }

    public static void setup() {
        grinderMaterial = MaterialProxy.newMaterial("Grinder Material", 3, 250, 10F, 5F, 0);
    }

    public static Item.ToolMaterial getGrinderMaterial() {
        return grinderMaterial;
    }
}
