package nl.vpro.io.prepr.spring;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import nl.vpro.io.prepr.*;

import static nl.vpro.io.prepr.PreprRepositoryClient.Version.v5;

/**
 * This is used to instantiate all classes by spring. The advantage is that spring than also will proxy them (e.g. for the @CacheResult annotation)
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
@Configuration
public abstract class AbstractSpringPreprRepositoriesConfiguration implements InitializingBean, BeanDefinitionRegistryPostProcessor  {

    private final static String PREF = "prepr";
    private final static String CPREF = "preprrepository";
    private final static String CLIENT_PREF = CPREF + ".client";

    private final String[] propertiesResources;
    private Map<String, String> moreProperties;


    @Autowired
    @Named("properties")
    Map<String, String> properties;


    public AbstractSpringPreprRepositoriesConfiguration(String... propertiesResources) {
        this.propertiesResources = propertiesResources;
    }


    public AbstractSpringPreprRepositoriesConfiguration() {
        this("prepr.properties");
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        registerBeans(beanDefinitionRegistry);;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        log.info("{}", configurableListableBeanFactory);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @SneakyThrows
    protected void registerBeans(BeanDefinitionRegistry beanDefinitionRegistry) {
        Properties properties = new Properties();
        for (String prop : this.propertiesResources) {
            properties.load(new ClassPathResource(prop).getInputStream());
        }


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
            AbstractBeanDefinition clientV5Definition = BeanDefinitionBuilder
                .genericBeanDefinition(PreprRepositoryClient.class)
                .addConstructorArgValue(get(properties, "api"))
                .addConstructorArgValue(channel)
                .addConstructorArgValue(get(properties, "clientId", channel))
                .addConstructorArgValue(get(properties, "clientSecret", channel))
                .addConstructorArgValue(get(properties, "clientToken", channel))
                .addConstructorArgValue(get(properties, "guideId", channel))
                .addConstructorArgValue(getWithDefault(properties, "scopes", channel))
                .addConstructorArgValue(get(properties, "description", channel))
                .addConstructorArgValue(getWithDefault(properties, "logascurl", channel))
                .addConstructorArgValue(null)
                .addConstructorArgValue(null)
                .addConstructorArgValue(v5)
                .getBeanDefinition();

            beanDefinitionRegistry.registerBeanDefinition(CLIENT_PREF + "." + channel + "." + v5.name(),
                clientV5Definition);

            define(beanDefinitionRegistry, "prepr", PreprPreprImpl.class, channel, v5);
            define(beanDefinitionRegistry, "guides", PreprGuidesImpl.class, channel, v5);
            define(beanDefinitionRegistry, "webhooks", PreprWebhooksImpl.class, channel, v5);
            define(beanDefinitionRegistry, "assets", PreprAssetsImpl.class, channel, v5);
            define(beanDefinitionRegistry, "content", PreprContentImpl.class, channel, v5);
            define(beanDefinitionRegistry, "tags", PreprTagsImpl.class, channel, v5);
            define(beanDefinitionRegistry, "containers", PreprContainersImpl.class, channel, v5);
            define(beanDefinitionRegistry, "persons", PreprPersonsImpl.class, channel, v5);


            String baseUrl = get(properties, "baseUrl", channel);

            beanDefinitionRegistry.registerBeanDefinition(CPREF + "." + channel,
                BeanDefinitionBuilder
                    .genericBeanDefinition(PreprRepositoryImpl.class)
                    .addConstructorArgReference(CLIENT_PREF + "." + channel + "." + v5.name()) // v5
                    .addConstructorArgValue(null) // v6
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


    void define(BeanDefinitionRegistry beanDefinitionRegistry, String name, Class<?> clazz, String channel, PreprRepositoryClient.Version version) {
        beanDefinitionRegistry
            .registerBeanDefinition(CPREF + "." + name + "." + channel,
                BeanDefinitionBuilder
                    .genericBeanDefinition(clazz)
                    .setLazyInit(true)
                    .addConstructorArgReference(CLIENT_PREF + "." + channel + "." + version.name())
                    .getBeanDefinition()
            );
    }


}
