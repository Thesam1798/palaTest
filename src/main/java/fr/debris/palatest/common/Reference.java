/*
 * @author Alexandre Debris <alexandre@debris.ovh>
 * @date 15/05/2021 : 20:16
 */
package fr.debris.palatest.common;

public class Reference {
    public static final String MOD_NAME = "Pala Test";
    public static final String MOD_ID = "palatest";
    public static final String MOD_VERSION = "2021.5.1";

    public static final String PROXY_CLIENT_CLASS = "fr.debris.palatest.client.proxy.ClientProxy";
    public static final String PROXY_SERVER_CLASS = "fr.debris.palatest.common.proxy.CommonProxy";

    // SonarLint Rule : java:S1118
    private Reference() {
        throw new IllegalStateException("Utility class");
    }
}
