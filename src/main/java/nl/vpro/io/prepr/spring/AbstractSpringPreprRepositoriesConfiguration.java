package nl.vpro.io.prepr.spring;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Provider;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import nl.vpro.io.prepr.*;

/**
 * This is used to instantiate all classes by spring. The advantage is that spring than also will proxy them (e.g. for the @CacheResult annotation)
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
@Configuration
public abstract class AbstractSpringPreprRepositoriesConfiguration implements BeanDefinitionRegistryPostProcessor {

    private final static String PREF = "prepr";
    private final static String CPREF = "preprrepository";
    private final static String CLIENT_PREF = CPREF + ".client";

    private final String[] propertiesResources;
    private Provider<Map<String, String>> moreProperties = HashMap::new;

    public AbstractSpringPreprRepositoriesConfiguration(String... propertiesResources) {
        this.propertiesResources = propertiesResources;
    }


    public AbstractSpringPreprRepositoriesConfiguration() {
        this("prepr.properties");
    }


    public void setMoreProperties(Provider<Map<String, String>> m) {
        this.moreProperties = m;
    }

    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Properties properties = new Properties();
        for (String prop : this.propertiesResources) {
            properties.load(new ClassPathResource(prop).getInputStream());
        }
        properties.putAll(moreProperties.get());

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
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(PreprRepositoryClient.class)
                .addConstructorArgValue(get(properties, "api"))
                .addConstructorArgValue(channel)
                .addConstructorArgValue(get(properties, "clientId", channel))
                .addConstructorArgValue(get(properties, "clientSecret", channel))
                .addConstructorArgValue(get(properties, "guideId", channel))
                .addConstructorArgValue(getWithDefault(properties, "scopes", channel))
                .addConstructorArgValue(get(properties, "description", channel))
                .addConstructorArgValue(getWithDefault(properties, "logascurl", channel))
                .getBeanDefinition();
            beanDefinitionRegistry.registerBeanDefinition(CLIENT_PREF + "." + channel,
                beanDefinition);

            define(beanDefinitionRegistry, "prepr", PreprPreprImpl.class, channel);
            define(beanDefinitionRegistry, "guides", PreprGuidesImpl.class, channel);
            define(beanDefinitionRegistry, "webhooks", PreprWebhooksImpl.class, channel);
            define(beanDefinitionRegistry, "assets", PreprAssetsImpl.class, channel);
            define(beanDefinitionRegistry, "content", PreprContentImpl.class, channel);
            define(beanDefinitionRegistry, "tags", PreprTagsImpl.class, channel);
            define(beanDefinitionRegistry, "containers", PreprContainersImpl.class, channel);
            define(beanDefinitionRegistry, "persons", PreprPersonsImpl.class, channel);


            String baseUrl = get(properties, "baseUrl", channel);

            beanDefinitionRegistry.registerBeanDefinition(CPREF + "." + channel,
                BeanDefinitionBuilder
                    .genericBeanDefinition(PreprRepositoryImpl.class)
                    .addConstructorArgReference(CLIENT_PREF + "." + channel)
                    .addConstructorArgReference(CPREF + ".prepr." + channel)
                    .addConstructorArgReference(CPREF + ".guides." + channel)
                    .addConstructorArgReference(CPREF + ".webhooks." + channel)
                    .addConstructorArgReference(CPREF + ".assets." + channel)
                    .addConstructorArgReference(CPREF + ".content." + channel)
                    .addConstructorArgReference(CPREF + ".tags." + channel)
                    .addConstructorArgReference(CPREF + ".containers." + channel)
                    .addConstructorArgReference(CPREF + ".persons." + channel)
                    .addConstructorArgValue(baseUrl == null ? null : URI.create(baseUrl))
                    .getBeanDefinition()
            );

        }
        beanDefinitionRegistry.registerBeanDefinition("preprrepositories",
            BeanDefinitionBuilder.genericBeanDefinition(SpringPreprRepositories.class).getBeanDefinition());

    }

    protected String getWithDefault(Properties properties, String prop, String channel) {
        return (String) properties.getOrDefault(PREF + "." + prop + "." + channel, properties.get(PREF + "." + prop));
    }


    protected String get(Properties properties, String prop) {
        return (String) properties.get(PREF + "." + prop);
    }
    protected String get(Properties properties, String prop, String channel) {
        return (String) properties.get(PREF + "." + prop + "." + channel);
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
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
