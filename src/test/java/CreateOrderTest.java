import createorder.OrderTrackNumber;
import createorder.Order;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Создание заказа")
@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {

    //Будем запоминать номер заказа, чтобы его удалять
    int track;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    final List<String> color;
    private final int StatusCode;

    public CreateOrderTest(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color, int StatusCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
        this.StatusCode = StatusCode;
    }


    @Parameterized.Parameters
    public static Object[] getOrderWithDifferentColor() {
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", Arrays.asList(""), HttpURLConnection.HTTP_CREATED},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", Arrays.asList("BLACK", "GREY"), HttpURLConnection.HTTP_CREATED},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", Arrays.asList("BLACK"), HttpURLConnection.HTTP_CREATED},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", Arrays.asList("GREY"), HttpURLConnection.HTTP_CREATED},
        };
    }

    @Step("Send POST request /api/v1/orders")
    public Response sendPostRequestOrder(Order newOrder) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newOrder)
                        .when()
                        .post("/api/v1/orders");
        return response;
    }

    @Step("Create order with color {color}")
    public Order createOrder(String firstName, String lastName, String address, int metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        Order newOrder = new Order(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color);
        return newOrder;
    }

    @Step("Compare result HTTP_CREATED")
    public void compareBodyIsNotNullAndStatusHttpCreated(Response response) {
        response.then().assertThat().body("track", notNullValue())
                .and()
                .statusCode(StatusCode);
    }

    @Test
    @DisplayName("Создаем заказ с разными цветами")
    @Description("Создаем заказ с разными цветами. После успешного создания заказа, должно вернуться номер заказа и статус код 201")
    public void createOrder() {
        Order newOrder = createOrder(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color);
        Response response = sendPostRequestOrder(newOrder);
        compareBodyIsNotNullAndStatusHttpCreated(response);
        track = response.getBody().as(OrderTrackNumber.class).getTrack();
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
