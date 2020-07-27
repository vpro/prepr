package nl.vpro.io.prepr.spring;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import nl.vpro.io.prepr.*;
import nl.vpro.io.prepr.domain.PreprObjectMapper;
import nl.vpro.util.TimeUtils;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Slf4j
public class SpringPreprRepositories implements PreprRepositories, ApplicationContextAware  {

    public static final String PREF = "prepr.";

    public static final String LOG_AS_CURL = PREF + "logascurl";
    public static final String SCOPES = PREF + "scopes";
    public static final String DELAY_AFTER_TOKEN = PREF + "delayAfterToken";
    public static final String LENIENT_JSON = PREF + "lenientjson";


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
        applicationContext.getBeansOfType(PreprRepository.class).values().forEach(mc -> {
            repositories.put(mc.getChannel(), mc);
            getSetting(LOG_AS_CURL, mc.getChannel()).ifPresent(s -> mc.getClient().setLogAsCurl(Boolean.parseBoolean(s)));
            getSetting(SCOPES, mc.getChannel()).ifPresent(s ->
                mc.getClient().setScopes(Arrays.stream(s.split("\\s*,\\s*")).map(Scope::valueOf).collect(Collectors.toList()))
            );
            getSetting(DELAY_AFTER_TOKEN, mc.getChannel()).ifPresent(s ->
                mc.getClient().setDelayAfterToken(TimeUtils.parseDuration(s).orElse(null))
            );
        });
        if (applicationContext.containsBean(LENIENT_JSON)) {
            String lenient = applicationContext.getBean(LENIENT_JSON, String.class);
            PreprObjectMapper.configureInstance(StringUtils.isBlank(lenient) || Boolean.parseBoolean(lenient));
        }
        log.info("{}", repositories.values());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " for " + repositories.keySet();
    }

    protected Optional<String> getSetting(String key, String channel) {
        boolean containsKey = applicationContext.containsBean(key + "." + channel);
        if (containsKey) {
            return Optional.of((String) applicationContext.getBean(key + "." + channel));
        }
        containsKey = applicationContext.containsBean(key);
        if (containsKey) {
            return Optional.of(String.valueOf(applicationContext.getBean(key)));
        }
        return Optional.empty();
    }

}
