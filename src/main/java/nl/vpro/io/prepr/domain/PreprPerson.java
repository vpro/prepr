package nl.vpro.io.prepr.domain;

import lombok.*;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprPerson.LABEL)
public class PreprPerson extends PreprAbstractObject {

    public static final String LABEL = "Person";

    Instant last_seen;

    String preposition; // deheer/devrouw

    String initials;

    String firstname;

    String prefix_lastname;

    String lastname;

    String full_name;

    String nickname;

    boolean tracking;

    String reference;

    /**
     * Has to do with prijsvragen
     */
    Boolean exclude_until;

    String job_title;

    String company;

    Instant blocked_on;

}
