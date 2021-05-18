package nl.vpro.io.prepr.rs;

import javax.management.MXBean;

@MXBean
public interface SignatureValidatorInterceptorMXBean {


    InvalidSignatureAction getInvalidSignatureAction();
    void setInvalidSignatureAction(InvalidSignatureAction invalidSignatureAction);
}
