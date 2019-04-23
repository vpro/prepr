package nl.vpro.io.mediaconnect.domain;

/**
 * @author Michiel Meeuwissen
 * @since ...
 */
public class MCEvents {

    private static final String CREATED  =  ".created";
    private static final String CHANGED =  ".changed";
    private static final String DELETED =  ".deleted";
    private static final String PUBLISHED=  ".published";
    private static final String UNPUBLISHED=  ".unpublished";


    static final String SHOWSCHEDULE = "showschedule";

    public static final String SHOWSCHEDULE_CREATED = SHOWSCHEDULE + CREATED;

    public static final String SHOWSCHEDULE_CHANGED = SHOWSCHEDULE + CHANGED;

    public static final String SHOWSCHEDULE_DELETED = SHOWSCHEDULE + DELETED;


    static final String PUBLICATION = "publication";

    public static final String PUBLICATION_CREATED = PUBLICATION + CREATED;

    public static final String PUBLICATION_PUBLISHED = PUBLICATION + PUBLISHED;

    public static final String PUBLICATION_UNPUBLISHED = PUBLICATION + UNPUBLISHED;

    public static final String PUBLICATION_CHANGED = PUBLICATION + CHANGED;

    public static final String PUBLICATION_DELETED = PUBLICATION + DELETED;

}
