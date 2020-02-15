package nl.vpro.io.prepr.domain;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class PreprObjectMapperTest {

    @Test
    public void unmap() throws IOException {
        ObjectNode payload = (ObjectNode) PreprObjectMapper.INSTANCE.readTree(new StringReader("{\"id\":\"81a2a44d-3e7b-4410-a095-a8d3bc347fc4\",\"created_on\":\"2019-06-03T09:24:39+00:00\",\"changed_on\":\"2019-06-03T09:24:39+00:00\",\"label\":\"TEST\",\"name\":\"2eaeb5e2-5c2e-4ba5-bea4-d0685152ef2a\",\"body\":null,\"reference_id\":null,\"reference\":null,\"status\":null,\"source_file\":{\"id\":\"f0c74651-677e-4995-a868-dffa37421ea5\",\"created_on\":\"2019-06-03T09:24:39+00:00\",\"changed_on\":\"2019-06-03T09:24:39+00:00\",\"label\":\"SourceFile\",\"original_name\":\"2eaeb5e2-5c2e-4ba5-bea4-d0685152ef2a.jpg\",\"mime_type\":\"image/jpeg\",\"file_size\":114338,\"extension\":\"jpg\",\"aws_bucket\":\"s3-funx-nederland\",\"aws_file\":\"f0c74651-677e-4995-a868-dffa37421ea5\",\"file\":\"f0c74651-677e-4995-a868-dffa37421ea5\"}}"));

        assertThat(PreprObjectMapper.unmap(payload, PreprAsset.class)).isEmpty();
        payload.put("label", PreprVideo.LABEL);
        Optional<PreprAsset> unmapped = PreprObjectMapper.unmap(payload, PreprAsset.class);
        assertThat(unmapped).isNotEmpty().get().isInstanceOf(PreprVideo.class);
        assertThat(unmapped.get().getCreated_on()).isEqualTo("2019-06-03T09:24:39Z");

    }
}
