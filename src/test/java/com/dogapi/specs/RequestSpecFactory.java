package com.dogapi.specs;

import com.dogapi.config.TestConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

import static io.restassured.http.ContentType.JSON;
import io.qameta.allure.restassured.AllureRestAssured;

public final class RequestSpecFactory {

    private RequestSpecFactory() {
    }

    public static RequestSpecification defaultSpec() {

        return new RequestSpecBuilder()
                .setBaseUri(TestConfig.getBaseUrl())
                .setAccept(JSON)
                .addFilter(
                        new AllureRestAssured()
                                .setRequestAttachmentName(
                                        "HTTP Request"
                                )
                                .setResponseAttachmentName(
                                        "HTTP Response"
                                )
                )
                .build();
    }
}