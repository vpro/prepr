package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MCShow extends MCContent {

    String slug;
    String name;
    String body;

    List<MCTag> tags;

    MCAsset cover;

}
