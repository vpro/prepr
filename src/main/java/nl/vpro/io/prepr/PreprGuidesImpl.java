package nl.vpro.io.prepr;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Named
@Slf4j
public class PreprGuidesImpl implements PreprGuides {

    private  final PreprRepositoryClient impl;

    @Getter
    @Setter
    private ZoneId zone = ZoneId.of("Europe/Amsterdam");

    @Getter
    @Setter
    private int limit = 1000;

    private UUID guideId;


    private static final Fields SCHEDULE_FIELDS = Fields.builder()
        .field(
            Field.builder("timelines")
                .field(Fields.ASSETS)
                .f("custom")
                .field(Field.builder("publications")
                    /* .fs("tags")
                .field(ASSETS)
                .field(Field.builder("element")
                        .field(Field.builder("media")
                            .field(SOURCEFILE_FIELD)
                            .build()
                        )
                        .build()
                )*/

                .build()
            ).build()
        )
        .f("guide")
        .field(Field.builder("show")
            .fs("slug", "name", "body", "tags", "status", "custom", "scheduled_users")
            .field(Fields.COVER)
            .build()
        )
        .f("users")
        .build();



    @Inject
    public PreprGuidesImpl(PreprRepositoryClient impl) {
        this.impl = impl;
    }

    @Override
    public PreprSchedule getSchedule(@NonNull LocalDate from, @NonNull LocalDate until, boolean exceptions, UUID showId) {
        List<PreprSchedule> results = new ArrayList<>();

        // page the result. Too big results may give bad requests.

        for (LocalDate f = from; f.compareTo(until) < 0;) {
            int maxDays = impl.getGuideCallsMaxDays();
            LocalDate u = f.plusDays(maxDays);
            if (u.compareTo(until) > 0) {
                u = until;
            }
            results.add(_getSchedule(f, u, exceptions, showId));
            f = u;
        }

        if (results.size() == 1) {
            return results.get(0);
        } else {
            PreprSchedule result = new PreprSchedule();
            result.setDays(new TreeMap<>());
            for (PreprSchedule r : results) {
                result.getDays().putAll(r.getDays());
            }
            return result;
        }
    }




    private PreprSchedule _getSchedule(@NonNull LocalDate from, @NonNull LocalDate until, boolean exceptions, UUID showId) {
        UUID guideId = getGuideId();
        if (guideId == null) {
            throw new IllegalStateException("No guide id defined for " + impl);
        }
        GenericUrl url = impl.createUrl(PATH, guideId);
        url.set("from", from.toString());
        url.set("until", until.toString());
        url.set("limit", limit);
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", SCHEDULE_FIELDS);

        url.set("exceptions", exceptions);


        return impl.get(url, PreprSchedule.class);
    }

    @Override
    public PreprItems<PreprGuide> getGuides(String q) {
         GenericUrl url = impl.createUrl(PATH);
        if (q!= null) {
            url.set("q", q);
        }
        url.set("fields", "timelines,guide,show{slug,name,body,tags,status,cover{" + Fields.SOURCEFILE_FIELD + "}},users");

        return impl.get(url, PreprItems.class);
    }

    protected UUID getGuideId() {
        UUID result = impl.getGuideId();
        if (result != null) {
            return  result;
        }
        if (guideId == null) {
            for (PreprGuide item : getGuides(null).getItems()) {
                if (item.getBody() != null && item.getBody().toLowerCase().contains("program")) {
                    guideId = item.getUUID();
                    break;
                }
                guideId = item.getUUID();
            }
            log.info("Guessed guide id {}", guideId);
        }
        return guideId;
    }
}
