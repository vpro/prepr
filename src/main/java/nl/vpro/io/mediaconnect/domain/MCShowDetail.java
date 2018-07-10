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
@JsonTypeName("ShowDetail")
public class MCShowDetail extends MCContent {

    String slug;
    String name;

    MCShow container;

    String element;

    List<MCTag> tags;


}
