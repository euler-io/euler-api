package com.github.euler.api.security;

import java.util.Collection;

public interface AuthResponse {

    String getUserName();

    Collection<String> getRoles();

}
