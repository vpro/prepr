package nl.vpro.io.mediaconnect.rs;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import nl.vpro.io.mediaconnect.MediaConnectRepository;

/**
 * When using {@link SignatureValidatorInterceptor} it needs to know the webhook ids. This class finds and registers them.
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Slf4j
public class WebhookIdsRegister {

    public static final ScheduledExecutorService backgroundExecutor = Executors.newScheduledThreadPool(1);

    private final MediaConnectRepository repository;

    private final String baseUrl;


    @Inject
    WebhookIdsRegister(
        MediaConnectRepository repository,
        @Named("mediaconnect.webhook.baseUrl") String baseUrl
        ) {
        this.repository = repository;
        this.baseUrl = baseUrl;
    }


    @PostConstruct
    public void init() {
        backgroundExecutor.scheduleAtFixedRate(this::registerWebhooks
            , 0, Duration.ofMinutes(5).toMillis(), TimeUnit.MILLISECONDS
        );
    }


    protected void registerWebhooks()  {
        try {
            repository.getWebhooks(100L).forEach((mc) -> {
                if (mc.getCallback_url().startsWith(baseUrl)) {
                    URI uri = URI.create(mc.getCallback_url());
                    String[] path = uri.getPath().split("/");
                    SignatureValidatorInterceptor.put(path[path.length - 1], mc.getId());
                }
                }
            );
        } catch (IOException ioe) {
            log.error(ioe.getMessage());
        }
    }


}
