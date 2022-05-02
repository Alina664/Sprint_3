import createorder.Order;
import findlogin.CourierAccount;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Логин курьера в системе")
public class FindLoginTest extends BaseTest {
    // создаём список, для логина и пароля
    ArrayList<String> loginPass = new ArrayList<>();
    HashMap<String, String> logPass = new HashMap<>();

    @Before
    public void newCourier() {
        //создаем курьера, с рандомными параметрами
        RegisterNewCourier courier = new RegisterNewCourier();
        //записываем в переменную, чтобы использовать в тестах дальше
        loginPass = courier.registerNewCourierAndReturnLoginPassword();
    }

    @Step("Send POST request /api/v1/courier/login")
    public Response sendPostRequestLogin(CourierAccount account) {
        return given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(account)
                        .when()
                        .post("/api/v1/courier/login");
    }

    @Step("Compare result HTTP_CREATED")
    public void compareBodyMessageOkAndStatusHttpOK(Response response) {
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(HttpURLConnection.HTTP_OK);
    }

    @Step("Compare result HTTP_BAD_REQUEST")
    public void compareMessageAndStatusHttpBadRequest(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Step("Compare result HTTP_NOT_FOUND")
    public void compareMessageAndStatusHttpNotFound(Response response) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND);
    }


    @Test
    @DisplayName("Поиск курьера по логину и паролю")
    @Description("Ищем курьера по логину и паролю. В ответ должен вернуться id, со статусом 200")
    public void findCourierWithCorrectLoginAndPassword() {
        logPass.putAll(Map.of("login", loginPass.get(0), "password", loginPass.get(1)));
        CourierAccount account = new CourierAccount(logPass);
        Response response = sendPostRequestLogin(account);
        compareBodyMessageOkAndStatusHttpOK(response);
    }


    @Test
    @DisplayName("Запрос на поиск курьера без логина")
    @Description("Ищем курьера по паролю. Должно вернуться сообщение 'Недостаточно данных для входа' со статусом 400")
    public void findCourierWithOutLogin() {
        logPass.put("password", loginPass.get(1));
        CourierAccount account = new CourierAccount(logPass);
        Response response = sendPostRequestLogin(account);
        compareMessageAndStatusHttpBadRequest(response);
    }

    @Test
    @DisplayName("Запрос на поиск курьера без пароля")
    @Description("Ищем курьера только по логину. Должно вернуться сообщение 'Недостаточно данных для входа' со статусом 400")
    public void findCourierWithOutPassword() {
        logPass.put("login", loginPass.get(0));
        CourierAccount account = new CourierAccount(logPass);
        Response response = sendPostRequestLogin(account);
        compareMessageAndStatusHttpBadRequest(response);
    }

    @Test
    @DisplayName("Запрос с несуществующей парой логин-пароль")
    @Description("Запрос с несуществующей парой логин-пароль. Должно вернуться сообщение 'Учетная запись не найдена' со статусом 404")
    public void findCourierWithNotExistLoginPassword() {
        //сперва удалим пользователя, если он есть в системе
        logPass.putAll(Map.of("login", loginPass.get(0), "password", loginPass.get(1)));
        CourierAccount account = new CourierAccount(logPass);
        //отправляем запрос на его удаление
        deleteCourier(loginPass.get(0), loginPass.get(1));
        //теперь проверяем статус и сообщение, при отправке запроса на несуществующего пользователя
        Response response = sendPostRequestLogin(account);
        compareMessageAndStatusHttpNotFound(response);
    }

    @Test
    @DisplayName("Поиск курьера по логину и неверному паролю")
    @Description("Ищем курьера по логину и неверному паролю. Должно вернуться сообщение 'Учетная запись не найдена' со статусом 404")
    public void findCourierWithIncorrectPassword() {
        logPass.putAll(Map.of("login", loginPass.get(0), "password", loginPass.get(0)));
        CourierAccount account = new CourierAccount(logPass);
        Response response = sendPostRequestLogin(account);
        compareMessageAndStatusHttpNotFound(response);
    }

    @Test
    @DisplayName("Поиск курьера по неверному логину и существующему паролю")
    @Description("Ищем курьера по неверному логину и существующему паролю. Должно вернуться сообщение 'Учетная запись не найдена' со статусом 404")
    public void findCourierWithIncorrectLogin() {
        logPass.putAll(Map.of("login", loginPass.get(1), "password", loginPass.get(1)));
        CourierAccount account = new CourierAccount(logPass);
        Response response = sendPostRequestLogin(account);
        compareMessageAndStatusHttpNotFound(response);
    }

    @After
    public void deleteCourier() {
        deleteCourier(loginPass.get(0), loginPass.get(1));
    }
}
