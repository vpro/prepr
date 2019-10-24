package nl.vpro.io.prepr.spring;

import lombok.extern.slf4j.Slf4j;
import nl.vpro.io.prepr.PreprRepositories;
import nl.vpro.io.prepr.PreprRepository;
import nl.vpro.io.prepr.Scope;
import nl.vpro.io.prepr.domain.PreprObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
public class SpringPreprRepositories implements PreprRepositories, ApplicationContextAware  {

    final Map<String, PreprRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public Optional<PreprRepository> get(String channel) {
        return Optional.ofNullable(repositories.get(channel));

    }

    @Override
    public Map<String, PreprRepository> getRepositories() {
        return repositories;

    }

    @NonNull
    @Override
    public Iterator<PreprRepository> iterator() {
        return repositories.values().iterator();

    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;


    }
    @PostConstruct
    public void fill() {
        boolean foundLogAsCurl = applicationContext.containsBean("prepr.logascurl");
        boolean logAsCurl = foundLogAsCurl && Boolean.valueOf(applicationContext.getBean("prepr.logascurl", String.class));
        boolean foundScopes = applicationContext.containsBean("prepr.scopes");
        final String scopes;
        if (foundScopes) {
             scopes = applicationContext.getBean("prepr.scopes", String.class);
        } else {
            scopes = "";
        }
        applicationContext.getBeansOfType(PreprRepository.class).values().forEach(mc -> {
            repositories.put(mc.getChannel(), mc);
            if (foundLogAsCurl) {
                mc.getClient().setLogAsCurl(logAsCurl);
            }
            if (foundScopes) {
                mc.getClient().setScopes(Arrays.stream(scopes.split("\\s*,\\s*")).map(Scope::valueOf).collect(Collectors.toList()));
            }
        });
        if (applicationContext.containsBean("prepr.lenientjson")) {
            String lenient = applicationContext.getBean("prepr.lenientjson", String.class);
            PreprObjectMapper.configureInstance(StringUtils.isBlank(lenient) || Boolean.parseBoolean(lenient));
        }
        log.info("{}", repositories.values());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " for " + repositories.keySet();
    }

}
