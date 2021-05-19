/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 15:06
 */
package fr.debris.palatest.common.register;

import fr.debris.palatest.common.proxy.items.ItemProxy;
import fr.debris.palatest.common.proxy.items.ItemSwordProxy;

public class ItemRegister {

    protected static ItemSwordProxy diamondBigSword;
    protected static ItemProxy diamondBigSwordModel;
    protected static ItemProxy diamondPlate;

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private ItemRegister() {
        throw new IllegalStateException("Utility class");
    }

    public static void setup() {

        if (MaterialRegister.getGrinderMaterial() == null) {
            throw new IllegalStateException("GrinderMaterial not set");
        }

        diamondBigSword = new ItemSwordProxy("diamond_big_sword", MaterialRegister.getGrinderMaterial());
        diamondBigSwordModel = new ItemProxy("modl_sword");
        diamondPlate = new ItemProxy("modl_diamond");
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
}
