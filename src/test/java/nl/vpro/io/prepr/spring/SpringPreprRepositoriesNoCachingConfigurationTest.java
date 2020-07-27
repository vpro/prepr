package nl.vpro.io.prepr.spring;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nl.vpro.io.prepr.PreprRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
@ExtendWith(SpringExtension.class)
@PropertySource({
  "classpath:test.properties"
})
@ContextConfiguration("classpath:test-context.xml")
@Slf4j
class SpringPreprRepositoriesNoCachingConfigurationTest {

    @Inject
    public SpringPreprRepositories repositories;

    @Test
    public void init() {
        log.info("{}", repositories);
        PreprRepository rad1 = repositories.get("RAD1").get();
        assertThat(rad1.getClient().isLogAsCurl()).isTrue();
        assertThat(rad1.getClient().getDelayAfterToken()).isEqualTo(Duration.ofSeconds(5));
    }
}
