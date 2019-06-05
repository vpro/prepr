package nl.vpro.io.prepr;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.PreprGuide;
import nl.vpro.io.prepr.domain.PreprItems;
import nl.vpro.io.prepr.domain.PreprSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Named
public class PreprGuidesImpl implements PreprGuides {

    private  final PreprRepositoryClient impl;

    @Getter
    @Setter
    private int maxDays = 1;

    @Getter
    @Setter
    private ZoneId zone = ZoneId.of("Europe/Amsterdam");


    private final Fields SCHEDULE_FIELDS = Fields.builder()
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
    public PreprSchedule getSchedule(@Nonnull  LocalDate from, @Nonnull LocalDate until, boolean exceptions, UUID showId) {
        List<PreprSchedule> results = new ArrayList<>();

        // page the result. Too big results may give bad requests.
        for (LocalDate f = from; f.compareTo(until) < 0; f = f.plusDays(maxDays)) {
            LocalDate u = f.plusDays(maxDays);
            if (u.compareTo(until) > 0) {
                u = until;
            }
            results.add(_getSchedule(f, u, exceptions, showId));
        }
        if (results.size() == 1) {
            return results.get(0);
        } else {
            PreprSchedule result = new PreprSchedule();
            result.setDays(new HashMap<>());
            for (PreprSchedule r : results) {
                result.getDays().putAll(r.getDays());
            }
            return result;
        }

    }




    private PreprSchedule _getSchedule(@Nonnull LocalDate from, @Nonnull LocalDate until, boolean exceptions, UUID showId) {
        if (impl.getGuideId() == null) {
            throw new IllegalStateException("No guide id defined for " + impl);
        }
        GenericUrl url = impl.createUrl("guides", impl.getGuideId());
        url.set("from", from.toString());
        url.set("until", until.toString());
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", SCHEDULE_FIELDS);

        url.set("exceptions", exceptions);


        return impl.get(url, PreprSchedule.class);
    }

    @Override
    public PreprItems<PreprGuide> getGuides(String q) {
         GenericUrl url = impl.createUrl("guides");
        if (q!= null) {
            url.set("q", q);
        }
        url.set("fields", "timelines,guide,show{slug,name,body,tags,status,cover{" + Fields.SOURCEFILE_FIELD + "}},users");

        return impl.get(url, PreprItems.class);


    }


}
