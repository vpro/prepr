package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("TagGroup")
public class MCTagGroup extends MCAbstractObject {


    boolean visible;
    String name;
    List<String> visible_in;

    String type;

    List<MCTag> tags;
}
