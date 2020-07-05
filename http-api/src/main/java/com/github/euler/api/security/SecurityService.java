package com.github.euler.api.security;

import java.time.Duration;
import java.util.Date;

public interface SecurityService {

    void renewSecret();

    byte[] getSecret();

    Date calculateExpirationFromNow();

    Duration getTokenMaxAge();

}
