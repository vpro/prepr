package nl.vpro.io.mediaconnect.spring;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class SpringMediaConnectRepositoriesNoCachingConfiguration extends AbstractSpringMediaConnectRepositoriesConfiguration {

    public SpringMediaConnectRepositoriesNoCachingConfiguration(String properties) {
        super(properties);
    }


    public SpringMediaConnectRepositoriesNoCachingConfiguration() {
        super("mediaconnect.properties");
    }
}
