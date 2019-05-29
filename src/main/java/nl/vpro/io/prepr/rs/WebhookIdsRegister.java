package nl.vpro.io.prepr.rs;

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

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import nl.vpro.io.prepr.PreprRepositories;
import nl.vpro.io.prepr.PreprRepository;

import static nl.vpro.io.prepr.Paging.limit;

/**
 * When using {@link SignatureValidatorInterceptor} it needs to know the webhook ids. This class finds and registers them in {@link SignatureValidatorInterceptor#put(String, UUID)}
 * @author Michiel Meeuwissen*
 * @since 0.1
 */
@Slf4j
@ManagedResource(
    description = "Makes sure the mediaconnect webhooks are recognized",
    objectName = "nl.vpro.media:name=prepr-webookids"
)
public class WebhookIdsRegister {

    public static final ScheduledExecutorService backgroundExecutor =
        Executors.newScheduledThreadPool(1);

    private final PreprRepositories repositories;

    private final String baseUrl;


    @Inject
    WebhookIdsRegister(
        PreprRepositories repositories,
        @Named("prepr.webhook.baseUrl") String baseUrl
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


    @ManagedOperation
    public void registerWebhooks()  {
        int before = SignatureValidatorInterceptor.WEBHOOK_IDS.size();
        for (PreprRepository repository : repositories) {
            try {
                repository.getWebhooks().get(limit(100)).forEach((mc) -> {
                    if (mc.getCallback_url().startsWith(baseUrl)) {
                        URI uri = URI.create(mc.getCallback_url());
                        String[] path = uri.getPath().split("/");
                        if (SignatureValidatorInterceptor.put(path[path.length - 1], mc.getUUID())) {
                            log.info("Registered {}", mc);
                        }
                    } else {
                        log.debug("Ignoring {}", mc);
                    }
                    }
                );
            } catch (Exception e) {
                log.error("For {}: {}", repository, e.getMessage(), e);
            }
        }
        int after =  SignatureValidatorInterceptor.WEBHOOK_IDS.size();
        if (before != after) {
            log.info("Registered {} webhooks", after - before);
        }
        SignatureValidatorInterceptor.readyForRequests();
    }


}
