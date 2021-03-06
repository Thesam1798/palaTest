/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 20:48
 */
package fr.debris.palatest.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.debris.palatest.client.gui.GrinderNotification;
import io.netty.buffer.ByteBuf;

/**
 * Gestion des notification via le réseaux
 */
public class GrinderNotificationNetwork implements IMessage {

    private boolean grinderDone;
    private boolean spawnGolem;

    public GrinderNotificationNetwork(boolean grinderDone, boolean spawnGolem) {
        this.grinderDone = grinderDone;
        this.spawnGolem = spawnGolem;
    }

    public GrinderNotificationNetwork() {
        this.grinderDone = false;
        this.spawnGolem = false;
    }

    /**
     * Lecture du flux
     *
     * @param buf flux
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        this.grinderDone = buf.readBoolean();
        this.spawnGolem = buf.readBoolean();
    }

    /**
     * Écriture du flux
     *
     * @param buf flux
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.grinderDone);
        buf.writeBoolean(this.spawnGolem);
    }

    /**
     * Class a la reception d'un packet apres lecture du flux
     */
    public static class Handler implements IMessageHandler<GrinderNotificationNetwork, IMessage> {
        @Override
        public IMessage onMessage(GrinderNotificationNetwork message, MessageContext ctx) {
            if (message.grinderDone) {
                GrinderNotification.startGrinderDone();
            }
            if (message.spawnGolem) {
                GrinderNotification.startSpawnGolem();
            }
            return null;
        }
    }
}
