package nl.vpro.io.mediaconnect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
@Configuration
public class SpringMediaConnectRepositories implements BeanDefinitionRegistryPostProcessor {

    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Properties properties = new Properties();
        properties.load(new ClassPathResource("mediaconnect.properties").getInputStream());
        //MediaConnectRepositories repositories = beanDefinitionRegistry.c
        MediaConnectRepositoryImpl.Creator creator = new MediaConnectRepositoryImpl.Creator() {
            @Override
            public <T> T apply(Class<T> objectClass) {
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(objectClass).setLazyInit(true);
                return null;

            }
        };
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MediaConnectRepository.class).setLazyInit(true);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        //configurableListableBeanFactory.
        log.info("--");

    }
}
