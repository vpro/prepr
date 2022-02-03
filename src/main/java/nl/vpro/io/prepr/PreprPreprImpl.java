package nl.vpro.io.prepr;

import java.time.LocalDate;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.prepr.domain.PreprSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Named
public class PreprPreprImpl implements PreprPrepr {

    private  final PreprRepositoryClient impl;

    @Inject
    public PreprPreprImpl(PreprRepositoryClient impl) {
        this.impl = impl;
    }


    /**
     * @deprecated Use {@link PreprGuides#getSchedule(LocalDate, LocalDate)}
     */
    @Override
    @Deprecated
    public PreprSchedule getSchedule(UUID channel, LocalDate from, LocalDate until) {
        GenericUrl url = impl.createUrl("prepr", "schedules", channel,  "guide");
        if (from != null) {
            url.set("from", from.toString());
        }
        if (until != null) {
            url.set("until", until.toString());
        }
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "timelines,guide,show{slug,name,body,tags,status,cover{" + Fields.SOURCEFILE_FIELD + "}},users");

        return impl.get(url, PreprSchedule.class);
    }


}
