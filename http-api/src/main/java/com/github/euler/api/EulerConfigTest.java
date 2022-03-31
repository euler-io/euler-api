package com.github.euler.api;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class EulerConfigTest {

    public static void main(String[] args) {
        Config config = ConfigFactory.load();
        System.out.println(config.root().render(ConfigRenderOptions.concise()));
    }

}
