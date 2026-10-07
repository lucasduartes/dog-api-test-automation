package com.dogapi.tests;

import io.restassured.response.Response;

import static com.dogapi.specs.RequestSpecFactory.defaultSpec;
import static io.restassured.RestAssured.given;

public class DogApiSmokeTest {

    public Response getAllBreeds() {

        return given()
                .spec(defaultSpec())
        .when()
                .get("/breeds/list/all");
    }

    public Response getImagesByBreed(String breed) {

        return given()
                .spec(defaultSpec())
                .pathParam("breed", breed)
        .when()
                .get("/breed/{breed}/images");
    }

    public Response getRandomImage() {

        return given()
                .spec(defaultSpec())
        .when()
                .get("/breeds/image/random");
    }
}