package nl.vpro.io.prepr.rs;

import java.time.Clock;
import java.time.Instant;
import java.util.function.Predicate;

public enum InvalidSignatureAction implements Predicate<Instant> {

    /**
     * Just always process, even if the signature was wrong
     */
    PROCESS((acceptBefore) -> true),

    /**
     * Just  process, even if the signature was wrong, but only if we started recently.
     */
    PROCESS_IF_JUST_STARTED((acceptBefore) -> getClock().instant().isBefore(acceptBefore)),


    /**
     * Send 'ACCEPT' back to Prepr, but just drop the entire message
     */
    IGNORE((acceptBefore) -> false),

    /**
     * Send unauthorize back to Prepr
     */
    UNAUTHORIZED((acceptBefore) -> false);

    static Clock CLOCK = Clock.systemUTC();

    private static Clock getClock() {
        return CLOCK;
    }

    final Predicate<Instant> accept;

    InvalidSignatureAction(Predicate<Instant> accept) {
        this.accept = accept;
    }

    /**
     * Whether the message belonging to the signature must be processed
     */
    @Override
    public boolean test(Instant acceptBefore) {
        return accept.test(acceptBefore);
    }
}
