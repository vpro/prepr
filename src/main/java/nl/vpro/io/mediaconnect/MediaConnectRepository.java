package nl.vpro.io.mediaconnect;

/**
 *
 * <p>A java representation of the rest services provided by the <a href="https://developers.mediaconnect.io/">mediaconnect API</a>
 * Currently this is in no way complete. It only is complete in so far as that was needed by <a href="https://poms.omroep.nl">POMS</a>, because
 * POMS syncs metadata from mediaconnect/prepr to its own database (as far as that concerns the dutch public broadcasters).</p>
 *
 * <p>That data therefore can also be accessed via the <a href="https://rs.poms.omroep.nl">NPO frontend API</a> (as soon as this is taken in use).</p>
 *
 *
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectRepository {

    MediaConnectPrepr     getPrepr();
    MediaConnectGuides    getGuides();
    MediaConnectWebhooks  getWebhooks();
    MediaConnectAssets    getAssets();
    MediaConnectContent   getContent();
    MediaConnectTags      getTags();



}
