package nl.vpro.io.prepr.rs;

/**
 * Some constants that can be used as header keys for incoming webhooks (e.g. when passing them to JMS)
 */
public class Constants {

    private Constants() {

    }
    public static final String REMOTE = "remote";
    public static final String USER_AGENT = "userAgent";
    public static final String CHANNEL = "channel";
    public static final String RECEIVAL = "receival";
    public static final String VERSION = "version";
    public static final String SEQUENCE = "sequence";
}
