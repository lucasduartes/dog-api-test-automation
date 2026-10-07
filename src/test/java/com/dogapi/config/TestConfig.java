package com.dogapi.config;

public final class TestConfig {

    private static final String DEFAULT_BASE_URL =
            "https://dog.ceo/api";

    private TestConfig() {
    }

    public static String getBaseUrl() {
        return System.getProperty(
                "baseUrl",
                DEFAULT_BASE_URL
        );
    }
}