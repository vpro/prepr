package nl.vpro.io.prepr.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("Person")
public class PreprPerson extends PreprAbstractObject {

    Instant last_seen;

    String preposition; // deheer/devrouw

    String initials;

    String firstname;

    String prefix_lastname;

    String lastname;

    String nickname;

    boolean tracking;

    String reference;

    /**
     * Has to do with prijsvragen
     */
    boolean exclude_until;

    String job_title;

    String company;

    Instant blocked_on;

}
