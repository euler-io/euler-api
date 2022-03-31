package com.github.euler.api;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class EulerConfigTest {

    public static void main(String[] args) {
        Config config = ConfigFactory.load();
        ConfigRenderOptions options = ConfigRenderOptions.defaults();
        options.setFormatted(true);
        options.setJson(true);
        options.setOriginComments(false);
        System.out.println(config.root().render(options));
    }

}
