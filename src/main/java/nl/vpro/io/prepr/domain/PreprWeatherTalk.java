package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName(PreprWeatherTalk.LABEL)
public class PreprWeatherTalk extends AbstractPreprContent {
    public static final String LABEL = "WeatherTalk";


    private String name;

    private Duration length;

    private String tease;


}
