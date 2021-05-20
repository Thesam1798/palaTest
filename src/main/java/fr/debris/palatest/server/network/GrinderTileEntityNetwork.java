/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 20:48
 */
package fr.debris.palatest.server.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.debris.palatest.common.machine.watergrinder.TileEntityWaterGrinder;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

/**
 * Permet d'update le dernier joueur a avoir ouvert le Water grinder
 */
public class GrinderTileEntityNetwork implements IMessage {


    private int x;
    private int y;
    private int z;
    private UUID uuid;

    public GrinderTileEntityNetwork() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.uuid = null;
    }

    public GrinderTileEntityNetwork(int x, int y, int z, UUID uuid) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.uuid = uuid;
    }

    /**
     * Lecture du flux
     *
     * @param buf flux
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        ByteBuf temp = buf.readBytes(36);
        this.uuid = UUID.fromString(new String(temp.array()));
    }

    /**
     * Écriture du flux
     *
     * @param buf flux
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeBytes(this.uuid.toString().getBytes());
    }

    /**
     * Class a la reception d'un packet apres lecture du flux
     */
    public static class Handler implements IMessageHandler<GrinderTileEntityNetwork, IMessage> {

        @Override
        public IMessage onMessage(GrinderTileEntityNetwork message, MessageContext ctx) {
            if (MinecraftServer.getServer() != null) {
                ((TileEntityWaterGrinder) MinecraftServer.getServer().worldServers[0].getTileEntity(message.x, message.y, message.z))
                        .setLastPlayerOpen(
                                MinecraftServer.getServer().getEntityWorld().func_152378_a(message.uuid)
                        );
            }

            return null;
        }
    }
}
