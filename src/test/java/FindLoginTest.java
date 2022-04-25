import findlogin.FindLoginCourierAnswer;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Логин курьера в системе")
public class FindLoginTest {
    // создаём список, для логина и пароля
    ArrayList<String> loginPass = new ArrayList<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
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
                .statusCode(200);
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
                .statusCode(400);
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
                .statusCode(400);
    }

    @Test
    @DisplayName("Запрос с несуществующей парой логин-пароль")
    @Description("Запрос с несуществующей парой логин-пароль. Должно вернуться сообщение 'Учетная запись не найдена' со статусом 404")
    public void findCourierWithNotExistLoginPassword() {
        //сперва удалим пользователя, если он есть в системе
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", loginPass.get(0), loginPass.get(1));
        //ищем id нашего пользователя, чтобы его удалить
        FindLoginCourierAnswer findLoginCourierAnswer = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login").as(FindLoginCourierAnswer.class);
        //отправляем запрос на его удаление
        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + findLoginCourierAnswer.getId());
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
                .statusCode(404);
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
                .statusCode(404);
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
                .statusCode(404);
    }

    @After
    @Description("Удаляем курьера которого создали")
    public void deleteCourier() {
        String json = String.format("{\"login\": \"%s\", \"password\": \"%s\"}", loginPass.get(0), loginPass.get(1));

        FindLoginCourierAnswer findLoginCourierAnswer = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login").as(FindLoginCourierAnswer.class);


        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + findLoginCourierAnswer.getId());
    }
}
