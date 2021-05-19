/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 15:06
 */
package fr.debris.palatest.common.register;

import fr.debris.palatest.common.machine.watergrinder.BlockWaterGrinder;
import fr.debris.palatest.common.proxy.block.MachineProxy;

public class MachineRegister {
    protected static MachineProxy waterGrinder;

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private MachineRegister() {
        throw new IllegalStateException("Utility class");
    }

    public static void setup() {
        waterGrinder = new BlockWaterGrinder().register();
    }

    public static MachineProxy getWaterGrinder() {
        return waterGrinder;
    }
}
