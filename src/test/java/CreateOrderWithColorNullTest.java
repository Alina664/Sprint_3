import createorder.Order;

import createorder.OrderTrackNumber;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

import static io.restassured.RestAssured.given;

@Epic("Создание заказа")
public class CreateOrderWithColorNullTest {
    int track;

    @Test
    @DisplayName("Создаем заказ без указания цвета")
    @Description("Создаем заказ без указания цвета. После успешного создания заказа, должно вернуться номер заказа и статус код 201")
    public void createOrderWithNotColor() {
        Order newOrder = new Order(
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
        track = response.getBody().as(OrderTrackNumber.class).getTrack();
    }
}
