package nl.vpro.io.mediaconnect;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface MediaConnectRepositoryClientMXBean {
    Integer getRateLimitReset();
    Integer getRateLimitHourRemaining();
    Integer getRateLimitHourLimit();

    Integer getAuthenticationCount();
    Integer getCallCount();

    String getScopesAsString();

    String getDescription();

    String getConnectTimeoutForGetAsString();
    void setConnectTimeoutForGetAsString(String connectTimeoutForGetAsString);

    String getReadTimeoutForGetAsString();
    void setReadTimeoutForGetAsString(String readTimeoutForGetAsString);
}
