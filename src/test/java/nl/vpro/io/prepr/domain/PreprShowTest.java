package nl.vpro.io.prepr.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PreprShowTest {

    @Test
    void testToString() {
        PreprShow show = new PreprShow();
        assertThat(show.toString()).isEqualTo("PreprShow[slug=<null>,name=<null>,body=<null>,tags=<null>,status=<null>,crid=<null>,label=<null>]");
        show.setSlug("foo");
        assertThat(show.toString()).isEqualTo("PreprShow[slug=foo,name=<null>,body=<null>,tags=<null>,status=<null>,crid=<null>,label=<null>]");
    }
}
