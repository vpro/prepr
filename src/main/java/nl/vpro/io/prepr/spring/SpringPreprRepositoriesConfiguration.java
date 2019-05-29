package nl.vpro.io.prepr.spring;

import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@EnableCaching
public class SpringPreprRepositoriesConfiguration extends AbstractSpringPreprRepositoriesConfiguration {


    public SpringPreprRepositoriesConfiguration(String... properties) {
        super(properties);
    }


    public SpringPreprRepositoriesConfiguration() {
        super();
    }
}
