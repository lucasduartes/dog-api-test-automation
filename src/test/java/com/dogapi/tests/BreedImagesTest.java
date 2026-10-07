package com.dogapi.tests;

import com.dogapi.client.DogApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Dog API")
@Feature("Breed Images")
class BreedImagesTest {

    private final DogApiClient dogApiClient =
            new DogApiClient();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName(
            "Should return images for a valid breed"
    )
    void shouldReturnImagesForValidBreed() {

        String breed = "hound";

        Response response =
                dogApiClient.getImagesByBreed(breed);

        assertEquals(
                200,
                response.statusCode(),
                "Expected HTTP status code 200"
        );

        assertEquals(
                "success",
                response.jsonPath().getString("status"),
                "Expected API status to be success"
        );

        List<String> images =
                response.jsonPath()
                        .getList("message", String.class);

        assertNotNull(
                images,
                "Image list should not be null"
        );

        assertFalse(
                images.isEmpty(),
                "Image list should not be empty"
        );
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName(
            "Should return valid image URLs for a breed"
    )
    void shouldReturnValidImageUrlsForBreed() {

        String breed = "hound";

        Response response =
                dogApiClient.getImagesByBreed(breed);

        List<String> images =
                response.jsonPath()
                        .getList("message", String.class);

        assertNotNull(images);
        assertFalse(images.isEmpty());

        images.forEach(imageUrl -> {

            assertNotNull(
                    imageUrl,
                    "Image URL should not be null"
            );

            assertTrue(
                    imageUrl.startsWith(
                            "https://images.dog.ceo/"
                    ),
                    "Unexpected image URL: " + imageUrl
            );
        });
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName(
            "Should return 404 for a non-existent breed"
    )
    void shouldReturnNotFoundForInvalidBreed() {

        String invalidBreed =
                "invalid-breed-for-automation";

        Response response =
                dogApiClient.getImagesByBreed(
                        invalidBreed
                );

        assertEquals(
                404,
                response.statusCode(),
                "Expected HTTP status code 404"
        );

        assertEquals(
                "error",
                response.jsonPath()
                        .getString("status"),
                "Expected API status to be error"
        );

        assertEquals(
                404,
                response.jsonPath()
                        .getInt("code"),
                "Expected API error code 404"
        );

        String errorMessage =
                response.jsonPath()
                        .getString("message");

        assertNotNull(
                errorMessage,
                "Error message should not be null"
        );

        assertTrue(
                errorMessage.contains("Breed not found"),
                "Unexpected error message: "
                        + errorMessage
        );

        response.then()
        .assertThat()
        .body(
                matchesJsonSchemaInClasspath(
                        "schemas/error-schema.json"
                )
        );
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName(
            "Should match the breed images response contract"
    )
    void shouldMatchBreedImagesResponseSchema() {

        String breed = "hound";

        Response response =
                dogApiClient.getImagesByBreed(breed);

        assertEquals(
                200,
                response.statusCode(),
                "Expected HTTP status code 200"
        );

        response.then()
                .assertThat()
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/breed-images-schema.json"
                        )
                );
    }
}