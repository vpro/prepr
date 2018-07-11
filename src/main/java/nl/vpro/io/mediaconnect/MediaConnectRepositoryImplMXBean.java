package nl.vpro.io.mediaconnect;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectRepositoryImplMXBean {
    Integer getRateLimitReset();
    Integer getRateLimitHourRemaining();
    Integer getRateLimitHourLimit();
}
