package com.github.euler.api;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

public class TrustStoreLoader {

    public static KeyStore loadTrustStore(String password, String trustStore) throws GeneralSecurityException, IOException {
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (InputStream keystoreStream = TrustStoreLoader.class.getResourceAsStream(trustStore)) {
            if (password != null) {
                keystore.load(keystoreStream, password.toCharArray());
            } else {
                keystore.load(keystoreStream, null);
            }
        }
        return keystore;
    }

    public static KeyStore loadTrustStore(String trustStore) throws GeneralSecurityException, IOException {
        return loadTrustStore(null, trustStore);
    }

}
