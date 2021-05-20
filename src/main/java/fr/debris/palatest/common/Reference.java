/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:16
 */
package fr.debris.palatest.common;

/**
 * Class de tout les référence que le mod pourrait avoir besoin
 */
public class Reference {
    public static final String MOD_NAME = "Pala Test";
    public static final String MOD_ID = "palatest";
    public static final String MOD_VERSION = "2021.5.1";

    public static final String PROXY_CLIENT_CLASS = "fr.debris.palatest.client.proxy.ClientProxy";
    public static final String PROXY_SERVER_CLASS = "fr.debris.palatest.common.proxy.CommonProxy";

    // SonarLint : java:S1118 : Utility classes should not have public constructors
    private Reference() {
        throw new IllegalStateException("Utility class");
    }

    public static class MYSQL {
        public static final String USERNAME = "test";
        public static final String PASSWORD = "RL1]!z70eYVzFjhe";
        public static final String HOST = "mysql.eclosia.life";
        public static final String PORT = "3306";
        public static final String DATABASE = "test";

        // SonarLint : java:S1118 : Utility classes should not have public constructors
        private MYSQL() {
            throw new IllegalStateException("Utility class");
        }
    }
}
