package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Instant;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprPublication.LABEL)
public  class PreprPublication extends PreprAbstractObject {

    public static final String LABEL = "Publication";

    private JsonNode slug;

    private Map<Locale, Instant> publish_on;

    private Map<Locale, Long> read_time;

    private Map<Locale, List<String>> seo_keywords;

    private Map<Locale, Instant> expire_on;

    private List<Locale> locales;

    private PreprPublicationModel model;

    private Boolean channels;

    private List<PreprTimeline> containers;

    private Map<Locale, PreprText> title;
    private Map<Locale, PreprText> items;

}
