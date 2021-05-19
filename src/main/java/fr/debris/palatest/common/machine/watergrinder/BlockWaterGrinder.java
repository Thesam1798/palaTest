/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 18/05/2021 : 11:31
 */
package fr.debris.palatest.common.machine.watergrinder;

import fr.debris.palatest.common.proxy.block.MachineProxy;
import fr.debris.palatest.common.proxy.gui.TileEntityProxy;
import fr.debris.palatest.common.register.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

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

    public MachineProxy register() {
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
        ) {
            @Override
            public boolean isActivable(World world, int x, int y, int z, EntityPlayer player) {
                if (world == null) return false;

                int blockMetadata = world.getBlockMetadata(x, y, z);
                Integer[] position = new Integer[3];
                position[0] = x;
                position[1] = y;
                position[2] = z;

                return verifyWaterBlock(world, x, y, z, position, blockMetadata) &&
                        verifyCasingBlock(world, position, blockMetadata) &&
                        verifyFrameBlock(world, position);
            }

            private boolean verifyWaterBlock(final World world, final int x, final int y, final int z, Integer[] position, final int blockMetadata) {
                Block water = getWaterBlock(world, x, y, z, position, blockMetadata);

                return water != null && water.getUnlocalizedName().equals("tile.water");
            }

            private Block getWaterBlock(final World world, final int x, final int y, final int z, Integer[] position, final int blockMetadata) {
                final Block water;
                // 2 Face on Nord
                // 3 Face on Sud
                // 4 Face on West
                // 5 Face on Est
                switch (blockMetadata) {
                    case 2:
                        water = world.getBlock(x, y, z + 1);
                        position[2] += 1;
                        break;
                    case 3:
                        water = world.getBlock(x, y, z - 1);
                        position[2] -= 1;
                        break;
                    case 4:
                        water = world.getBlock(x + 1, y, z);
                        position[0] += 1;
                        break;
                    case 5:
                        water = world.getBlock(x - 1, y, z);
                        position[0] -= 1;
                        break;
                    default:
                        water = null;
                }
                return water;
            }

            private boolean verifyFrameBlock(final World world, final Integer[] position) {
                Block[] frame = new Block[20];

                frame[0] = world.getBlock(position[0] + 1, position[1], position[2] + 1);
                frame[1] = world.getBlock(position[0] + 1, position[1], position[2] - 1);
                frame[2] = world.getBlock(position[0] - 1, position[1], position[2] + 1);
                frame[3] = world.getBlock(position[0] - 1, position[1], position[2] - 1);

                frame[4] = world.getBlock(position[0] + 1, position[1] + 1, position[2] + 1);
                frame[5] = world.getBlock(position[0] + 1, position[1] + 1, position[2] - 1);
                frame[6] = world.getBlock(position[0] - 1, position[1] + 1, position[2] + 1);
                frame[7] = world.getBlock(position[0] - 1, position[1] + 1, position[2] - 1);

                frame[8] = world.getBlock(position[0] + 1, position[1] - 1, position[2] + 1);
                frame[9] = world.getBlock(position[0] + 1, position[1] - 1, position[2] - 1);
                frame[10] = world.getBlock(position[0] - 1, position[1] - 1, position[2] + 1);
                frame[11] = world.getBlock(position[0] - 1, position[1] - 1, position[2] - 1);

                frame[12] = world.getBlock(position[0], position[1] - 1, position[2] - 1);
                frame[13] = world.getBlock(position[0], position[1] - 1, position[2] + 1);
                frame[14] = world.getBlock(position[0] - 1, position[1] - 1, position[2]);
                frame[15] = world.getBlock(position[0] + 1, position[1] - 1, position[2]);

                frame[16] = world.getBlock(position[0], position[1] + 1, position[2] - 1);
                frame[17] = world.getBlock(position[0], position[1] + 1, position[2] + 1);
                frame[18] = world.getBlock(position[0] - 1, position[1] + 1, position[2]);
                frame[19] = world.getBlock(position[0] + 1, position[1] + 1, position[2]);

                for (Block block : frame) {
                    if (block == null) continue;
                    if (!block.getUnlocalizedName().equals(BlockRegister.getFrameGrinder().getUnlocalizedName())) {
                        return false;
                    }
                }
                return true;
            }

            private boolean verifyCasingBlock(final World world, final Integer[] position, final int blockMetadata) {
                Block[] casing = new Block[6];

                if (blockMetadata == 2) casing[0] = world.getBlock(position[0], position[1], position[2] + 1);
                if (blockMetadata == 3) casing[1] = world.getBlock(position[0], position[1], position[2] - 1);
                if (blockMetadata == 4) casing[2] = world.getBlock(position[0] + 1, position[1], position[2]);
                if (blockMetadata == 5) casing[3] = world.getBlock(position[0] - 1, position[1], position[2]);

                casing[4] = world.getBlock(position[0], position[1] + 1, position[2]);
                casing[5] = world.getBlock(position[0], position[1] - 1, position[2]);

                for (Block block : casing) {
                    if (block == null) continue;
                    if (!block.getUnlocalizedName().equals(BlockRegister.getCasingGrinder().getUnlocalizedName())) {
                        return false;
                    }
                }
                return true;
            }
        };

        machine.setHardness(HARDNESS);
        machine.setResistance(RESISTANCE);
        machine.setStepSound(STEP_SOUND);

        return machine;
    }
}
