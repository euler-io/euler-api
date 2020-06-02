package com.github.euler.api;

import com.typesafe.config.Config;

public class ESEulerHTTPAPI extends EulerHTTPAPI {

    public ESEulerHTTPAPI(Config config) {
        super(config);
    }

    public ESEulerHTTPAPI() {
        super();
    }

    public static void main(String[] args) {
        ESEulerHTTPAPI api = new ESEulerHTTPAPI();
        api.start(args);
    }

}
