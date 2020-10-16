package com.github.euler.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Iterator;

import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

public class EulerBanner implements Banner {

    private static final String EULER = " :: Euler :: ";

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        String line = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(EulerBanner.class.getResourceAsStream("/euler.txt"), "utf-8"))) {
            Iterator<String> lines = reader.lines().iterator();
            while (lines.hasNext()) {
                line = lines.next();
                out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String version = getVersion();
        StringBuilder padding = new StringBuilder();
        while (padding.length() < (line.length() - version.length() - EULER.length())) {
            padding.append(" ");
        }
        out.println(AnsiOutput.toString(AnsiColor.BLUE, EULER, AnsiColor.DEFAULT, padding.toString(),
                AnsiStyle.BOLD, version));
        out.println();
    }

    private String getVersion() {
        String implementationVersion = EulerBanner.class.getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            return implementationVersion;
        } else {
            return "development";
        }
    }

}
