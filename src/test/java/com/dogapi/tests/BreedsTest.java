package com.dogapi.tests;

import com.dogapi.client.DogApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

@Epic("Dog API")
@Feature("Breeds")
class BreedsTest {

    private final DogApiClient dogApiClient =
            new DogApiClient();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName(
            "Should return all available breeds successfully"
    )
    void shouldReturnAllAvailableBreeds() {

        Response response =
                dogApiClient.getAllBreeds();

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

        Map<String, Object> breeds =
                response.jsonPath().getMap("message");

        assertNotNull(
                breeds,
                "Breed list should not be null"
        );

        assertFalse(
                breeds.isEmpty(),
                "Breed list should not be empty"
        );
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName(
            "Should return valid breed and sub-breed structure"
    )
    void shouldReturnValidBreedStructure() {

        Response response =
                dogApiClient.getAllBreeds();

        Map<String, Object> breeds =
                response.jsonPath().getMap("message");

        assertNotNull(breeds);
        assertFalse(breeds.isEmpty());

        breeds.forEach((breed, subBreeds) -> {

            assertNotNull(
                    breed,
                    "Breed name should not be null"
            );

            assertFalse(
                    breed.trim().isEmpty(),
                    "Breed name should not be empty"
            );

            assertNotNull(
                    subBreeds,
                    "Sub-breed collection should not be null"
            );
        });
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName(
            "Should match the breeds list response contract"
    )
    void shouldMatchBreedsListResponseSchema() {

        Response response =
                dogApiClient.getAllBreeds();

        assertEquals(
                200,
                response.statusCode(),
                "Expected HTTP status code 200"
        );

        response.then()
                .assertThat()
                .body(
                        matchesJsonSchemaInClasspath(
                                "schemas/breeds-list-schema.json"
                        )
                );
    }
}