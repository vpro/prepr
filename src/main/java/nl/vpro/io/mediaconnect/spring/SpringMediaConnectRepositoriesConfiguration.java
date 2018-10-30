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
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import nl.vpro.io.mediaconnect.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
@Configuration
public class SpringMediaConnectRepositoriesConfiguration implements BeanDefinitionRegistryPostProcessor {

    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        Properties properties = new Properties();
        properties.load(new ClassPathResource("mediaconnect.properties").getInputStream());
        String prefix = "mediaconnect.clientId.";
        List<String> channels = properties.entrySet().stream()
            .map(e -> new AbstractMap.SimpleEntry<>(e.getKey().toString(), e.getValue().toString()))
            .filter((e) -> StringUtils.isNotBlank(e.getValue()))
            .map(Map.Entry::getKey)
            .filter((k) -> k.startsWith(prefix))
            .map((k) -> k.substring(prefix.length()))
            .collect(Collectors.toList());
        for (String channel : channels) {



            BeanDefinition client = BeanDefinitionBuilder
                .genericBeanDefinition(MediaConnectRepositoryClient.class)
                .addConstructorArgValue(properties.get("mediaconnect.api"))
                .addConstructorArgValue(channel)
                .addConstructorArgValue(properties.get("mediaconnect.clientId." + channel))
                .addConstructorArgValue(properties.get("mediaconnect.clientSecret." + channel))
                .addConstructorArgValue(properties.get("mediaconnect.guideId." + channel))
                .addConstructorArgValue(properties.get("mediaconnect.roles"))
                .addConstructorArgValue(properties.get("mediaconnect.logascurl"))
                .getBeanDefinition();

            beanDefinitionRegistry
                .registerBeanDefinition("mediaconnectrepository.client." + channel, client);

            define(beanDefinitionRegistry, "assets", MediaConnectAssetsImpl.class, channel);
            define(beanDefinitionRegistry, "containers", MediaConnectContainersImpl.class, channel);
            define(beanDefinitionRegistry, "content", MediaConnectContentImpl.class, channel);
            define(beanDefinitionRegistry, "guides", MediaConnectGuidesImpl.class, channel);
            define(beanDefinitionRegistry, "prepr", MediaConnectPreprImpl.class, channel);
            define(beanDefinitionRegistry, "tags", MediaConnectTagsImpl.class, channel);
            define(beanDefinitionRegistry, "webhooks", MediaConnectWebhooksImpl.class, channel);

            BeanDefinition repository  = BeanDefinitionBuilder
                .genericBeanDefinition(MediaConnectRepositoryImpl.class)
                .addConstructorArgValue(channel)
                .addConstructorArgReference("mediaconnectrepository.prepr." + channel)
                .addConstructorArgReference("mediaconnectrepository.guides." + channel)
                .addConstructorArgReference("mediaconnectrepository.webhooks." + channel)
                .addConstructorArgReference("mediaconnectrepository.assets." + channel)
                .addConstructorArgReference("mediaconnectrepository.content." + channel)
                .addConstructorArgReference("mediaconnectrepository.tags." + channel)
                .addConstructorArgReference("mediaconnectrepository.containers." + channel)
                .getBeanDefinition();

            beanDefinitionRegistry
                .registerBeanDefinition("mediaconnectrepository." + channel, repository);





        }
        BeanDefinitionBuilder mr = BeanDefinitionBuilder.genericBeanDefinition(SpringMediaConnectRepositories.class);
        beanDefinitionRegistry.registerBeanDefinition("mediaconnectrepositories", mr.getBeanDefinition());

    }

    BeanDefinition define(BeanDefinitionRegistry beanDefinitionRegistry, String name, Class<?> clazz, String channel) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz).setLazyInit(true);
        beanDefinitionRegistry.registerBeanDefinition("mediaconnectrepository." + name + "." + channel, builder.getBeanDefinition());
        builder.addConstructorArgReference("mediaconnectrepository.client." + channel).setLazyInit(true);
        return builder.getBeanDefinition();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {


    }
}
