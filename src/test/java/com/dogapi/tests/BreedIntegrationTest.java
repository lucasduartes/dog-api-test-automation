package com.dogapi.tests;

import com.dogapi.client.DogApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Dog API")
@Feature("Breed Integration")
class BreedIntegrationTest {

    private final DogApiClient dogApiClient =
            new DogApiClient();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName(
            "Should return images for a breed provided by the breeds endpoint"
    )
    void listedBreedShouldHaveImagesAvailable() {

        Response breedsResponse =
                dogApiClient.getAllBreeds();

        assertEquals(
                200,
                breedsResponse.statusCode(),
                "Expected breeds endpoint to return HTTP 200"
        );

        Map<String, Object> breeds =
                breedsResponse.jsonPath()
                        .getMap("message");

        assertNotNull(
                breeds,
                "Breed list should not be null"
        );

        assertFalse(
                breeds.isEmpty(),
                "Breed list should not be empty"
        );

        String breed =
                breeds.keySet()
                        .stream()
                        .sorted()
                        .findFirst()
                        .orElseThrow(
                                () -> new AssertionError(
                                        "No breed available for integration test"
                                )
                        );

        Response imagesResponse =
                dogApiClient.getImagesByBreed(breed);

        assertEquals(
                200,
                imagesResponse.statusCode(),
                "Expected images endpoint to return HTTP 200 "
                        + "for breed: " + breed
        );

        assertEquals(
                "success",
                imagesResponse.jsonPath()
                        .getString("status"),
                "Expected images endpoint status to be success"
        );

        List<String> images =
                imagesResponse.jsonPath()
                        .getList("message", String.class);

        assertNotNull(
                images,
                "Image list should not be null"
        );

        assertFalse(
                images.isEmpty(),
                "Expected at least one image for breed: "
                        + breed
        );
    }
}