/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 19/05/2021 : 20:29
 */
package fr.debris.palatest.client.gui;

import fr.debris.palatest.common.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import static fr.debris.palatest.common.Logger.error;

/**
 * Gère tout les notification du water grinder
 */
public class GrinderNotification {

    private static boolean showDone = false;
    private static boolean showGolem = false;
    private static int taille = 0;
    private static boolean isOpen = false;
    private static boolean closeDirection = false;
    private static boolean asyncTask = false;
    private static boolean animation = true;

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private GrinderNotification() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Permet d'afficher que le grinder a fin
     */
    public static void startGrinderDone() {
        GrinderNotification.showDone = true;
        GrinderNotification.showGolem = false;
        reset();
    }

    /**
     * Reset pour éviter des conflit
     */
    private static void reset() {
        GrinderNotification.taille = 0;
        GrinderNotification.isOpen = false;
        GrinderNotification.closeDirection = false;
        GrinderNotification.asyncTask = false;
        GrinderNotification.animation = true;
    }

    /**
     * Permet de créer la notification "Le water grinder a fini" a chaque frame
     *
     * @param mc Minecraft
     */
    public static void drawGrindDone(Minecraft mc) {
        if (showDone) {
            int posX = 4;
            int posY = 4;

            updateSize();

            if (GrinderNotification.taille > 0) {
                mc.renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/notification.png"));

                mc.ingameGUI.drawTexturedModalRect(posX, posY, 0, 0, GrinderNotification.taille, 32);

                if (GrinderNotification.taille <= 174) {
                    mc.ingameGUI.drawTexturedModalRect(GrinderNotification.taille + posX, posY, 182, 0, 7, 32);
                } else if (GrinderNotification.taille <= 180) {
                    mc.ingameGUI.drawTexturedModalRect(174 + posX, posY, 182, 0, 7, 32);
                }
            }

            autoClose();
        }
    }

    /**
     * Permet de changer de taille a chaque frame
     */
    private static void updateSize() {
        if (GrinderNotification.animation) {
            if (!closeDirection) {
                GrinderNotification.taille += 4;
            } else {
                GrinderNotification.taille -= 4;
            }
        }

        if (GrinderNotification.taille > 181) {
            GrinderNotification.taille = 181;
            GrinderNotification.isOpen = true;
            GrinderNotification.animation = false;
        }

        if (GrinderNotification.taille < 0) {
            GrinderNotification.taille = 0;
            GrinderNotification.isOpen = false;
            GrinderNotification.animation = false;
        }
    }

    /**
     * Permet de créer la notification "Les golem on spawn" a chaque frame
     *
     * @param mc Minecraft
     */
    public static void drawSpawnGolem(Minecraft mc) {
        if (showGolem) {
            int posX = 4;
            int posY = 4;

            updateSize();

            if (GrinderNotification.taille > 0) {
                mc.renderEngine.bindTexture(new ResourceLocation(Reference.MOD_ID, "textures/gui/notification.png"));

                mc.ingameGUI.drawTexturedModalRect(posX, posY, 0, 32, GrinderNotification.taille, 32);

                if (GrinderNotification.taille <= 174) {
                    mc.ingameGUI.drawTexturedModalRect(GrinderNotification.taille + posX, posY, 182, 0, 7, 32);
                } else if (GrinderNotification.taille <= 180) {
                    mc.ingameGUI.drawTexturedModalRect(174 + posX, posY, 182, 0, 7, 32);
                }
            }

            autoClose();
        }
    }

    /**
     * Action pour fermer la notification
     */
    private static void autoClose() {
        if (GrinderNotification.isOpen && !GrinderNotification.animation && !GrinderNotification.asyncTask) {
            GrinderNotification.asyncTask = true;
            GrinderNotification.animation = false;
            Thread t1 = new Thread(new CloseTask());
            t1.start();
        }
    }

    /**
     * Permet d'afficher que les golem on spawn
     */
    public static void startSpawnGolem() {
        GrinderNotification.showDone = false;
        GrinderNotification.showGolem = true;
        reset();
    }

    /**
     * Class pour la fermeture en async
     */
    private static class CloseTask implements Runnable {
        public void run() {
            try {
                Thread.sleep(2000);
                GrinderNotification.isOpen = false;
                GrinderNotification.closeDirection = true;
                GrinderNotification.animation = true;
                GrinderNotification.asyncTask = false;
            } catch (InterruptedException e) {
                error(e.getMessage());
            }
        }
    }
}
