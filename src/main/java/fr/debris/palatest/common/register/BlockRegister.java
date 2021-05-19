/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 15:05
 */
package fr.debris.palatest.common.register;

import fr.debris.palatest.common.proxy.block.BlockProxy;
import net.minecraft.block.material.Material;

public class BlockRegister {

    protected static BlockProxy frameGrinder;
    protected static BlockProxy casingGrinder;

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private BlockRegister() {
        throw new IllegalStateException("Utility class");
    }

    public static void setup() {
        casingGrinder = new BlockProxy(Material.ground, "casing_grinder");
        frameGrinder = new BlockProxy(Material.ground, "frame_grinder");
    }

    public static BlockProxy getFrameGrinder() {
        return frameGrinder;
    }

    public static BlockProxy getCasingGrinder() {
        return casingGrinder;
    }
}
