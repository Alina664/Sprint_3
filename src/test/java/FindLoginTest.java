import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Логин курьера в системе")
public class FindLoginTest extends BaseTest {
    // создаём список, для логина и пароля
    ArrayList<String> loginPass = new ArrayList<>();

    @Before
    public void newCourier() {
        //создаем курьера, с рандомными параметрами
        RegisterNewCourier courier = new RegisterNewCourier();
        //записываем в переменную, чтобы использовать в тестах дальше
        loginPass = courier.registerNewCourierAndReturnLoginPassword();
    }


    @Test
    @DisplayName("Поиск курьера по логину и паролю")
    @Description("Ищем курьера по логину и паролю. В ответ должен вернуться id, со статусом 200")
    public void findCourierWithCorrectLoginAndPassword() {
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", loginPass.get(0), loginPass.get(1));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("id", notNullValue())
                .and()
                .statusCode(HttpURLConnection.HTTP_OK);
    }


    @Test
    @DisplayName("Запрос на поиск курьера без логина")
    @Description("Ищем курьера по паролю. Должно вернуться сообщение 'Недостаточно данных для входа' со статусом 400")
    public void findCourierWithOutLogin() {
        String json = String.format("{\"password\": \"%s\"}", loginPass.get(1));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Запрос на поиск курьера без пароля")
    @Description("Ищем курьера только по логину. Должно вернуться сообщение 'Недостаточно данных для входа' со статусом 400")
    public void findCourierWithOutPassword() {
        String json = String.format("{\"login\": \"%s\"}", loginPass.get(0));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Test
    @DisplayName("Запрос с несуществующей парой логин-пароль")
    @Description("Запрос с несуществующей парой логин-пароль. Должно вернуться сообщение 'Учетная запись не найдена' со статусом 404")
    public void findCourierWithNotExistLoginPassword() {
        //сперва удалим пользователя, если он есть в системе
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", loginPass.get(0), loginPass.get(1));
        //отправляем запрос на его удаление
        deleteCourier(loginPass.get(0), loginPass.get(1));
        //теперь проверяем статус и сообщение, при отправке запроса на несуществующего пользователя
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND);
    }

    @Test
    @DisplayName("Поиск курьера по логину и неверному паролю")
    @Description("Ищем курьера по логину и неверному паролю. Должно вернуться сообщение 'Учетная запись не найдена' со статусом 404")
    public void findCourierWithIncorrectPassword() {
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", loginPass.get(0), "111");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND);
    }

    @Test
    @DisplayName("Поиск курьера по неверному логину и существующему паролю")
    @Description("Ищем курьера по неверному логину и существующему паролю. Должно вернуться сообщение 'Учетная запись не найдена' со статусом 404")
    public void findCourierWithIncorrectLogin() {
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", "111", loginPass.get(1));
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND);
    }

    @After
    public void deleteCourier() {
        deleteCourier(loginPass.get(0), loginPass.get(1));
    }
}
