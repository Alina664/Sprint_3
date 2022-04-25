import createorder.CreateOrderAnswer;
import createorder.CreateOrderRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Создание заказа")
public class CreateOrderTest {
    //Будем запоминать номер заказа, чтобы его удалять
    int track;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создаем заказ со цветом BLACK")
    @Description("Создаем заказ со цветом BLACK. После успешного создания заказа, должно вернуться номер заказ с кодом 201")
    public void createOrderWithColorBlack() {
        ArrayList<String> color = new ArrayList<>();
        color.add("BLACK");
        CreateOrderRequest newOrder = new CreateOrderRequest(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newOrder)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
        track = response.getBody().as(CreateOrderAnswer.class).getTrack();
    }

    @Test
    @DisplayName("Создаем заказ со цветом GREY")
    @Description("Создаем заказ со цветом GREY. После успешного создания заказа, должно вернуться номер заказ с кодом 201")
    public void createOrderWithColorGrey() {
        ArrayList<String> color = new ArrayList<>();
        color.add("GREY");
        CreateOrderRequest newOrder = new CreateOrderRequest(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newOrder)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
        track = response.getBody().as(CreateOrderAnswer.class).getTrack();
    }

    @Test
    @DisplayName("Создаем заказ с обоими цветами")
    @Description("Создаем заказ с обоими цветами. После успешного создания заказа, должно вернуться номер заказ с кодом 201")
    public void createOrderWithTwoColor() {
        ArrayList<String> color = new ArrayList<>();
        color.add("BLACK");
        color.add("GREY");
        CreateOrderRequest newOrder = new CreateOrderRequest(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newOrder)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
        track = response.getBody().as(CreateOrderAnswer.class).getTrack();
    }

    @Test
    @DisplayName("Создаем заказ без указания цвета")
    @Description("Создаем заказ без указания цвета. После успешного создания заказа, должно вернуться номер заказ с кодом 201")
    public void createOrderWithNotColor() {
        CreateOrderRequest newOrder = new CreateOrderRequest(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newOrder)
                        .when()
                        .post("/api/v1/orders");
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
        track = response.getBody().as(CreateOrderAnswer.class).getTrack();
    }

    @After
    @Description("Отменяем созданный заказ")
    public void deleteCourier() {
        String json = String.format("{\"track\": \"%s\"}", track);

        given()
                .header("Content-type", "application/json")
                .body(json)
                .when()
                .put("/api/v1/orders/cancel");
    }
}
