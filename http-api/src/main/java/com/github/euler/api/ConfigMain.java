package com.github.euler.api;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

public class ConfigMain {

    public static void main(String[] args) {
        System.out.println(ConfigFactory.load().root().render(ConfigRenderOptions.defaults()));
    }

}
