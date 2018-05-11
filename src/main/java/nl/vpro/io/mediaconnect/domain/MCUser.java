package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MCUser extends MCAbstractObject {

    Instant last_seen;
    String preposition; // deheer/devrouw
    String initials;
    String firstname;
    String prefix_lastname;
    String lastname;
    String nickname;
    boolean tracking;


}
