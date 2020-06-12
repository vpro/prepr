package nl.vpro.io.prepr;

import java.net.URI;

/**
 *
 * <p>A java representation of the rest services provided by the <a href="https://developers.prepr.io/">PREPR API</a>
 * Currently this is in no way complete. It only is complete in so far as that was needed by <a href="https://poms.omroep.nl">POMS</a>, because
 * POMS syncs metadata from prepr to its own database (as far as that concerns the dutch public broadcasters).</p>
 *
 * <p>That data therefore can also be accessed via the <a href="https://rs.poms.omroep.nl">NPO frontend API</a> (as soon as this is taken in use).</p>
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprRepository {

    PreprPrepr getPrepr();
    PreprGuides getGuides();
    PreprWebhooks getWebhooks();
    PreprAssets getAssets();
    PreprContent getContent();
    PreprTags getTags();
    PreprContainers getContainers();
    PreprPersons getPersons();

    PreprRepositoryClient getV5Client();

    PreprRepositoryClient getV6Client();

    default PreprRepositoryClient getClient() {
        return getV5Client();
    }

    String getChannel();
    URI getBaseUrl();


}
