package nl.vpro.io.mediaconnect.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@lombok.Builder
@JsonTypeName("Webhook")
public class MCWebhook extends MCAbstractObject {

    String callback_url;

    @Singular
    List<String> events;

    public MCWebhook() {

    }
}
