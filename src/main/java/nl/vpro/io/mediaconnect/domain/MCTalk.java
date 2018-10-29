package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * A talk seems to a kind 'segment' in the show. It seems that it at last approximately know when it was, but we don't get it back.
 * So for now we ignore it.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("Talk")
public class MCTalk extends MCContent {

    private String note;

    private String name;

    private Duration length;

    private String tease;

    private MCChannel channel;

    private List<MCTag> tags;
}
