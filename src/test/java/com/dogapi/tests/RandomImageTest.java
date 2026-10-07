package com.dogapi.tests;

import com.dogapi.client.DogApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Dog API")
@Feature("Random Image")
class RandomImageTest {

    private final DogApiClient dogApiClient =
            new DogApiClient();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName(
            "Should return a random dog image successfully"
    )
    void shouldReturnRandomDogImage() {

        Response response =
                dogApiClient.getRandomImage();

        assertEquals(
                200,
                response.statusCode(),
                "Expected HTTP status code 200"
        );

        assertEquals(
                "success",
                response.jsonPath()
                        .getString("status"),
                "Expected API status to be success"
        );

        String imageUrl =
                response.jsonPath()
                        .getString("message");

        assertNotNull(
                imageUrl,
                "Image URL should not be null"
        );

        assertFalse(
                imageUrl.trim().isEmpty(),
                "Image URL should not be empty"
        );

        assertTrue(
                imageUrl.startsWith(
                        "https://images.dog.ceo/"
                ),
                "Unexpected image URL: "
                        + imageUrl
        );
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName(
            "Should match the random image response contract"
    )
    void shouldMatchRandomImageResponseSchema() {

        Response response =
                dogApiClient.getRandomImage();

        assertEquals(
                200,
                response.statusCode(),
                "Expected HTTP status code 200"
        );

        response.then()
                .assertThat()
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/random-image-schema.json"
                        )
                );
    }
}