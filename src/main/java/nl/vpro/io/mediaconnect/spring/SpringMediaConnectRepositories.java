package nl.vpro.io.mediaconnect.spring;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import nl.vpro.io.mediaconnect.MediaConnectRepositories;
import nl.vpro.io.mediaconnect.MediaConnectRepository;
import nl.vpro.io.mediaconnect.Scope;
import nl.vpro.io.mediaconnect.domain.MCObjectMapper;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
public class SpringMediaConnectRepositories  implements MediaConnectRepositories, ApplicationContextAware  {

    final Map<String, MediaConnectRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public Optional<MediaConnectRepository> get(String channel) {
        return Optional.ofNullable(repositories.get(channel));

    }

    @Override
    public Map<String, MediaConnectRepository> getRepositories() {
        return repositories;

    }

    @Override
    public Iterator<MediaConnectRepository> iterator() {
        return repositories.values().iterator();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;


    }
    @PostConstruct
    public void fill() {
        boolean foundLogAsCurl = applicationContext.containsBean("mediaconnect.logascurl");
        boolean logAsCurl = foundLogAsCurl && Boolean.valueOf(applicationContext.getBean("mediaconnect.logascurl", String.class));
        boolean foundScopes = applicationContext.containsBean("mediaconnect.scopes");
        final String scopes;
        if (foundScopes) {
             scopes = applicationContext.getBean("mediaconnect.scopes", String.class);
        } else {
            scopes = "";
        }
        applicationContext.getBeansOfType(MediaConnectRepository.class).values().forEach(mc -> {
            repositories.put(mc.getChannel(), mc);
            if (foundLogAsCurl) {
                mc.getClient().setLogAsCurl(logAsCurl);
            }
            if (foundScopes) {
                mc.getClient().setScopes(Arrays.stream(scopes.split("\\s*,\\s*")).map(Scope::valueOf).collect(Collectors.toList()));
            }
        });
        if (applicationContext.containsBean("mediaconnect.lenientjson")) {
            String lenient = applicationContext.getBean("mediaconnect.lenientjson", String.class);
            MCObjectMapper.configureInstance(StringUtils.isBlank(lenient) || Boolean.parseBoolean(lenient));
        }
        log.info("{}", repositories.values());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " for " + repositories.keySet();
    }

}
