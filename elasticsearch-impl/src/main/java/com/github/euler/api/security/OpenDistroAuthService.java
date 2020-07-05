package com.github.euler.api.security;

import java.io.IOException;

import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.client.RequestOptions;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.github.euler.api.OpenDistroConfiguration;
import com.github.euler.opendistro.OpenDistroClient;

@Service
public class OpenDistroAuthService implements AuthService {

    private final OpenDistroConfiguration opendistroConfig;

    public OpenDistroAuthService(OpenDistroConfiguration opendistroConfig) {
        super();
        this.opendistroConfig = opendistroConfig;
    }

    @Override
    public AuthResponse authenticate(String username, String password) throws IOException {

        OpenDistroClient client = null;
        try {
            client = opendistroConfig.startClient(username, password);
            AuthResponse resp = client.authInfo().authInfo(RequestOptions.DEFAULT);
            if (resp != null) {
                return resp;
            } else {
                throw new BadCredentialsException(username + " not authorized.");
            }
        } catch (ElasticsearchStatusException e) {
            throw new BadCredentialsException(username + " not authorized.");
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
