package nl.vpro.io.mediaconnect.rs;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import nl.vpro.io.mediaconnect.StandaloneMediaConnectRepositories;
import nl.vpro.io.mediaconnect.MediaConnectRepository;

import static nl.vpro.io.mediaconnect.Paging.limit;

/**
 * When using {@link SignatureValidatorInterceptor} it needs to know the webhook ids. This class finds and registers them in {@link SignatureValidatorInterceptor#put(String, UUID)}
 * @author Michiel Meeuwissen*
 * @since 0.1
 */
@Slf4j
public class WebhookIdsRegister {

    public static final ScheduledExecutorService backgroundExecutor =
        Executors.newScheduledThreadPool(1);

    private final StandaloneMediaConnectRepositories repositories;

    private final String baseUrl;


    @Inject
    WebhookIdsRegister(
        StandaloneMediaConnectRepositories repositories,
        @Named("mediaconnect.webhook.baseUrl") String baseUrl
        ) {
        this.repositories = repositories;
        this.baseUrl = baseUrl;
    }


    @PostConstruct
    public void init() {
        log.info("Webhookids Registry: {} for {}", baseUrl, repositories);
        backgroundExecutor.scheduleAtFixedRate(this::registerWebhooks
            , 0, Duration.ofMinutes(5).toMillis(), TimeUnit.MILLISECONDS
        );
    }
    @PreDestroy
    public void shutdown() {
        backgroundExecutor.shutdownNow();
    }


    protected void registerWebhooks()  {
        try {
            for (MediaConnectRepository repository : repositories) {
                repository.getWebhooks().get(limit(100)).forEach((mc) -> {
                    if (mc.getCallback_url().startsWith(baseUrl)) {
                        URI uri = URI.create(mc.getCallback_url());
                        String[] path = uri.getPath().split("/");
                        SignatureValidatorInterceptor.put(path[path.length - 1], mc.getUUID());
                    }
                    }
                );
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


}
