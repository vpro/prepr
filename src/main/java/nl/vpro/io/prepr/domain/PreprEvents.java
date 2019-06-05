package nl.vpro.io.prepr.domain;

/**
 * @author Michiel Meeuwissen
 * @since 0.6
 */
public class PreprEvents {

    private static final String CREATED  =  ".created";
    private static final String CHANGED =  ".changed";
    private static final String DELETED =  ".deleted";
    private static final String PUBLISHED=  ".published";
    private static final String UNPUBLISHED=  ".unpublished";


    public static final String SHOWSCHEDULE = "showschedule";

    public static final String SHOWSCHEDULE_CREATED = SHOWSCHEDULE + CREATED;

    public static final String SHOWSCHEDULE_CHANGED = SHOWSCHEDULE + CHANGED;

    public static final String SHOWSCHEDULE_DELETED = SHOWSCHEDULE + DELETED;


    public static final String PUBLICATION = "publication";

    public static final String PUBLICATION_CREATED = PUBLICATION + CREATED;

    public static final String PUBLICATION_PUBLISHED = PUBLICATION + PUBLISHED;

    public static final String PUBLICATION_UNPUBLISHED = PUBLICATION + UNPUBLISHED;

    public static final String PUBLICATION_CHANGED = PUBLICATION + CHANGED;

    public static final String PUBLICATION_DELETED = PUBLICATION + DELETED;


    public static final String ASSET = "asset";


    public static final String ASSET_CREATED = ASSET + CREATED;

    public static final String ASSET_CHANGED = ASSET + CHANGED;

    public static final String ASSET_DELETED = ASSET + DELETED;

    public static final String ASSET_UPLOADED = ASSET + ".uploaded";


    public static final String CONTAINER = "container";


    public static final String CONTAINER_CREATED = CONTAINER + CREATED;

    public static final String CONTAINER_CHANGED = CONTAINER + CHANGED;

    public static final String CONTAINER_DELETED = CONTAINER + DELETED;

    public static final String CONTAINER_PUBLISHED = CONTAINER + PUBLISHED;

    public static final String CONTAINER_UNPUBLISHED = CONTAINER + UNPUBLISHED;

    public static final String TIMELINES_CREATED = CONTAINER + UNPUBLISHED;









}
