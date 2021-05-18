/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 18/05/2021 : 11:31
 */
package fr.debris.palatest.common.machine.watergrinder;

import fr.debris.palatest.common.proxy.CommonProxy;
import fr.debris.palatest.common.proxy.MachineProxy;
import fr.debris.palatest.common.proxy.TileEntityProxy;
import net.minecraft.block.Block;

public class BlockWaterGrinder {

    protected static final String NAME = "water_grinder";
    protected static final String BLOCK_TEXTURE = "casing_grinder";
    protected static final String FRONT_TEXTURE_NAME = "water_grinder";
    protected static final int GUI_INSTANCE = 0;
    protected static final boolean ANIMATE = true;
    protected static final String PARTICULE = "dripWater";
    protected static final boolean TAB = true;
    protected static final TileEntityProxy ENTITY = new TileEntityWaterGrinder();

    protected static final Float HARDNESS = 3.0F;
    protected static final Float RESISTANCE = 5.0F;
    protected static final Block.SoundType STEP_SOUND = Block.soundTypeStone;

    public void register() {
        ENTITY.setName(NAME);

        MachineProxy machine = new MachineProxy(
                NAME,
                BLOCK_TEXTURE,
                FRONT_TEXTURE_NAME,
                GUI_INSTANCE,
                ANIMATE,
                PARTICULE,
                TAB,
                ENTITY
        );

        machine.setHardness(HARDNESS);
        machine.setResistance(RESISTANCE);
        machine.setStepSound(STEP_SOUND);

        CommonProxy.setWaterGrinder(machine);
    }
}
