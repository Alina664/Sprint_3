import orderlist.OrderListAnswer;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;

@Epic("Получение списка заказов")
public class OrderListTest extends BaseTest {

    @Test
    @DisplayName("Получение списка заказов без courierId")
    @Description("Получение списка заказов без courierId. В ответ должен вернуться список с заказами, со статусом 200")
    public void getOrderListWithStatusCode200() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders");
        response.then().assertThat().statusCode(HttpURLConnection.HTTP_OK);
    }

    @Test
    @DisplayName("Проверка количества заказов без courierId")
    @Description("Проверка количества заказов без courierId. В ответ должен вернуться не пустой список с заказов")
    public void orderListIsNotNull() {
        OrderListAnswer response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/v1/orders").as(OrderListAnswer.class);
        int sizeOrders = response.getOrders().size();
        Assert.assertNotEquals("Количество заказов не равно 0", 0, sizeOrders);
    }
}
