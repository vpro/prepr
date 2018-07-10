package nl.vpro.io.mediaconnect;

import java.time.LocalDate;
import java.util.UUID;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.mediaconnect.domain.MCSchedule;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class MediaConnectTimelinesImpl implements MediaConnectTimelines {


    private  final MediaConnectRepositoryImpl impl;

    public MediaConnectTimelinesImpl(MediaConnectRepositoryImpl impl) {
        this.impl = impl;
    }

    @Override
    public MCSchedule getSchedule(UUID channel, LocalDate from, LocalDate until) {
        GenericUrl url = impl.createUrl("prepr", "schedules", channel,  "guide");
        if (from != null) {
            url.set("from", from.toString());
        }
        if (until != null) {
            url.set("until", until.toString());
        }
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", "timelines,guide,show{slug,name,body,tags,status,cover{source_file}},users");

        return impl.get(url, MCSchedule.class);
    }


}
