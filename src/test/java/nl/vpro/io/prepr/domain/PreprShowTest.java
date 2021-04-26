package nl.vpro.io.prepr.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PreprShowTest {

    @Test
    void testToString() {
        PreprShow show = new PreprShow();
        assertThat(show.toString()).isEqualTo("PreprShow{}");
        show.setSlug("foo");
        assertThat(show.toString()).isEqualTo("PreprShow{slug=foo}");
    }
}
