package nl.vpro.io.prepr.rs;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import nl.vpro.io.prepr.PreprRepositories;
import nl.vpro.io.prepr.PreprRepository;
import nl.vpro.logging.simple.*;

import static nl.vpro.io.prepr.Paging.limit;

/**
 * When using {@link SignatureValidatorInterceptor} it needs to know the webhook ids. This class finds and registers them in {@link SignatureValidatorInterceptor#put(String, UUID)}
 * @author Michiel Meeuwissen*
 * @since 0.1
 */
@Slf4j
@ManagedResource(
    description = "Makes sure the prepr webhooks are recognized",
    objectName = "nl.vpro.io.prepr:name=prepr-webookids"
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
    public String registerWebhooks()  {
        final int before = SignatureValidatorInterceptor.WEBHOOK_IDS.size();
        if (! SignatureValidatorInterceptor.isReadyForRequests()) {
            log.info("Registering webhooks");
        }
        final StringBuilder builder = new StringBuilder();
        final SimpleLogger logger = StringBuilderSimpleLogger.builder()
            .level(Level.INFO)
            .stringBuilder(builder).chain(Slf4jSimpleLogger.of(log));

        for (PreprRepository repository : repositories) {
            try {
                repository.getWebhooks().get(limit(500)).forEach((mc) -> {
                    boolean found = false;
                    for (String b : baseUrl.split(",")) {
                        if (mc.getCallback_url().startsWith(b)) {
                            URI uri = URI.create(mc.getCallback_url());
                            String[] path = uri.getPath().split("/");
                            if (SignatureValidatorInterceptor.put(path[path.length - 1], mc.getUUID())) {
                                logger.info("Registered {}", mc);
                            } else {
                                logger.debug("Ready registered {}", mc);
                            }
                            found = true;
                        }
                    }
                    if (!found) {
                        logger.debug("Ignoring {}", mc);
                    }
                    }
                );
            } catch (Exception e) {
                logger.error("For {}: {}", repository, e.getMessage(), e);
            }
        }
        final int after =  SignatureValidatorInterceptor.WEBHOOK_IDS.size();
        if (before != after) {
            logger.info("Registered {} webhooks", after - before);
        }
        if (after == 0) {
             logger.debug("No webhooks registered for {}", repositories);
        }
        SignatureValidatorInterceptor.readyForRequests();
        return builder.toString();
    }


}
