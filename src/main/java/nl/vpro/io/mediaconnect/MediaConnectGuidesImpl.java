package nl.vpro.io.mediaconnect;

import java.time.LocalDate;
import java.util.UUID;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.mediaconnect.domain.MCGuide;
import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCSchedule;

import static nl.vpro.io.mediaconnect.MediaConnectRepositoryImpl.SOURCEFILE_FIELD;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class MediaConnectGuidesImpl implements MediaConnectGuides {

    private  final MediaConnectRepositoryImpl impl;

    public MediaConnectGuidesImpl(MediaConnectRepositoryImpl impl) {
        this.impl = impl;
    }

    @Override
    public MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until) {
        GenericUrl url = impl.createUrl("guides", channel);
        //GenericUrl url = impl.createUrl("prepr", "schedules", channel,  "guide");
        if (from != null) {
            url.set("from", from.toString());
        }
        if (until != null) {
            url.set("until", until.toString());
        }
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "timelines,guide,show{slug,name,body,tags,status,cover{" + SOURCEFILE_FIELD + "}},users");

        return impl.get(url, MCSchedule.class);
    }

    @Override
    public MCItems<MCGuide> getGuides(String q) {
         GenericUrl url = impl.createUrl("guides");
        if (q!= null) {
            url.set("q", q);
        }
        url.set("fields", "timelines,guide,show{slug,name,body,tags,status,cover{" + SOURCEFILE_FIELD + "}},users");

        return impl.get(url, MCItems.class);


    }


}
