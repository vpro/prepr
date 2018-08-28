package nl.vpro.io.mediaconnect.rs;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import nl.vpro.io.mediaconnect.MediaConnectRepository;

import static nl.vpro.io.mediaconnect.Paging.limit;

/**
 * When using {@link SignatureValidatorInterceptor} it needs to know the webhook ids. This class finds and registers them.
 * @author Michiel Meeuwissen*
 * @since 0.1
 */
@Slf4j
public class WebhookIdsRegister {

    public static final ScheduledExecutorService backgroundExecutor = Executors.newScheduledThreadPool(1);

    private final List<MediaConnectRepository> repositories;

    private final String baseUrl;


    @Inject
    WebhookIdsRegister(
        MediaConnectRepository repository,
        @Named("mediaconnect.webhook.baseUrl") String baseUrl
        ) {
        this.repositories = Arrays.asList(repository);
        this.baseUrl = baseUrl;
    }


    @PostConstruct
    public void init() {
        log.info("Webhookids Registry: {}", baseUrl);
        backgroundExecutor.scheduleAtFixedRate(this::registerWebhooks
            , 0, Duration.ofMinutes(5).toMillis(), TimeUnit.MILLISECONDS
        );
    }


    protected void registerWebhooks()  {
        for (MediaConnectRepository repository : repositories) {
            repository.getWebhooks().get(limit(100)).forEach((mc) -> {
                if (mc.getCallback_url().startsWith(baseUrl)) {
                    URI uri = URI.create(mc.getCallback_url());
                    String[] path = uri.getPath().split("/");
                    SignatureValidatorInterceptor.put(path[path.length - 1], mc.getId());
                }
                }
            );
        }
    }


}
