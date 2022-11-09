package io.gbloch.falcon.challenge.web;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import java.io.File;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OddsControllerTest {

    @Test
    void whatAreTheOddsIT() {
        given()
                .multiPart("file", new File("src/test/resources/empire.json"))
                .when()
                .post("/api/v1/odds")
                .then()
                .statusCode(200)
                .body(is("100"));
    }
}
