package nl.vpro.io.mediaconnect.spring;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import nl.vpro.io.mediaconnect.*;

/**
 * This is used to instantiate all classes by spring. The advantage is that spring than also will proxy them (e.g. for the @CacheResult annotation)
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
@Configuration
@EnableCaching
public class SpringMediaConnectRepositoriesConfiguration implements BeanDefinitionRegistryPostProcessor {

    private final static String PREF = "mediaconnect";
    private final static String CPREF = "mediaconnectrepository";
    private final static String CLIENT_PREF = CPREF + ".client";

    private final String properties;

    public SpringMediaConnectRepositoriesConfiguration(String properties) {
        this.properties = properties;
    }


    public SpringMediaConnectRepositoriesConfiguration() {
        this("mediaconnect.properties");
    }

    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Properties properties = new Properties();
        properties.load(new ClassPathResource(this.properties).getInputStream());
        String prefix = PREF + ".clientId.";
        List<String> channels = properties.entrySet().stream()
            .map(e -> new AbstractMap.SimpleEntry<>(e.getKey().toString(), e.getValue().toString()))
            .filter((e) -> StringUtils.isNotBlank(e.getValue()))
            .map(Map.Entry::getKey)
            .filter((k) -> k.startsWith(prefix))
            .map((k) -> k.substring(prefix.length()))
            .collect(Collectors.toList());

        for (String channel : channels) {
            String secret = (String) properties.get(PREF + ".clientSecret." + channel);
            if (secret == null) {
                log.info("Skipped creating bean for {}, because no client secret configured", channel);
                continue;
            }
            beanDefinitionRegistry.registerBeanDefinition(CLIENT_PREF + "." + channel,
                BeanDefinitionBuilder
                    .genericBeanDefinition(MediaConnectRepositoryClient.class)
                    .addConstructorArgValue(properties.get(PREF + ".api"))
                    .addConstructorArgValue(channel)
                    .addConstructorArgValue(properties.get(PREF + ".clientId." + channel))
                    .addConstructorArgValue(properties.get(PREF + ".clientSecret." + channel))
                    .addConstructorArgValue(properties.get(PREF + ".guideId." + channel))
                    .addConstructorArgValue(properties.get(PREF + ".scopes"))
                    .addConstructorArgValue(properties.get(PREF + ".logascurl"))
                    .getBeanDefinition());

            define(beanDefinitionRegistry, "prepr", MediaConnectPreprImpl.class, channel);
            define(beanDefinitionRegistry, "guides", MediaConnectGuidesImpl.class, channel);
            define(beanDefinitionRegistry, "webhooks", MediaConnectWebhooksImpl.class, channel);
            define(beanDefinitionRegistry, "assets", MediaConnectAssetsImpl.class, channel);
            define(beanDefinitionRegistry, "content", MediaConnectContentImpl.class, channel);
            define(beanDefinitionRegistry, "tags", MediaConnectTagsImpl.class, channel);
            define(beanDefinitionRegistry, "containers", MediaConnectContainersImpl.class, channel);


            beanDefinitionRegistry.registerBeanDefinition(CPREF + "." + channel,
                BeanDefinitionBuilder
                    .genericBeanDefinition(MediaConnectRepositoryImpl.class)
                    .addConstructorArgReference(CLIENT_PREF + "." + channel)
                    .addConstructorArgReference(CPREF + ".prepr." + channel)
                    .addConstructorArgReference(CPREF + ".guides." + channel)
                    .addConstructorArgReference(CPREF + ".webhooks." + channel)
                    .addConstructorArgReference(CPREF + ".assets." + channel)
                    .addConstructorArgReference(CPREF + ".content." + channel)
                    .addConstructorArgReference(CPREF + ".tags." + channel)
                    .addConstructorArgReference(CPREF + ".containers." + channel)
                    .getBeanDefinition()
            );

        }
        beanDefinitionRegistry.registerBeanDefinition("mediaconnectrepositories",
            BeanDefinitionBuilder.genericBeanDefinition(SpringMediaConnectRepositories.class).getBeanDefinition());

    }

    void define(BeanDefinitionRegistry beanDefinitionRegistry, String name, Class<?> clazz, String channel) {
        beanDefinitionRegistry
            .registerBeanDefinition(CPREF + "." + name + "." + channel,
                BeanDefinitionBuilder
                    .genericBeanDefinition(clazz)
                    .setLazyInit(true)
                    .addConstructorArgReference(CLIENT_PREF + "." + channel)
                    .getBeanDefinition()
            );
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {


    }
}
