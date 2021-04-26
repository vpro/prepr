package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprWeatherTalk.LABEL)
public class PreprWeatherTalk extends AbstractPreprContent {

    public static final String LABEL = "WeatherTalk";

    private String name;

    private Duration length;

    private String tease;


}
