package nl.vpro.io.mediaconnect.spring;

import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@EnableCaching
public class SpringMediaConnectRepositoriesConfiguration extends AbstractSpringMediaConnectRepositoriesConfiguration {


    public SpringMediaConnectRepositoriesConfiguration(String properties) {
        super(properties);
    }


    public SpringMediaConnectRepositoriesConfiguration() {
        super();
    }
}
