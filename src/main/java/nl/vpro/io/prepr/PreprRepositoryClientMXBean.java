package nl.vpro.io.prepr;

import javax.validation.constraints.Size;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */

public interface PreprRepositoryClientMXBean  {
    Integer getRateLimitReset();
    Integer getRateLimitHourRemaining();
    Integer getRateLimitHourLimit();

    Integer getAuthenticationCount();
    Integer getCallCount();

    String getScopesAsString();

    String getDescription();

    String getExpirationAsString();

    String getRefreshesAfterAsString();

    String getConnectTimeoutForGetAsString();
    void setConnectTimeoutForGetAsString(String connectTimeoutForGetAsString);

    String getReadTimeoutForGetAsString();
    void setReadTimeoutForGetAsString(String readTimeoutForGetAsString);


    Integer getGuideCallsMaxDays();

    void setGuideCallsMaxDays(@Size(min=1) int guideCallsMaxDays);
}
