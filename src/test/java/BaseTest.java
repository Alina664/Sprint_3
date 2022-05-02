import findlogin.CourierAccount;
import findlogin.CourierId;
import io.restassured.RestAssured;
import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public void deleteCourier(String login, String password) {
        HashMap<String, String> logPass = new HashMap<>();
        logPass.putAll(Map.of("login", login, "password", password));
        CourierAccount account = new CourierAccount(logPass);

        CourierId courierId = given()
                .header("Content-type", "application/json")
                .and()
                .body(account)
                .when()
                .post("/api/v1/courier/login").as(CourierId.class);


        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + courierId.getId());
    }
}
