import findlogin.FindLoginCourierAnswer;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Создание курьера")
public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создаем курьера с полными данными")
    @Description("Проверяем, что курьера можно создать. Курьер с полными данными login alina22222, password 1234. Ожидаемый код ответа 201")
    public void createCourierWithLoginPasswordAndFirstName() {
        RegisterNewCourier registerNewCourier = new RegisterNewCourier("alina22222", "1234", "alina");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(registerNewCourier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Создаем курьера без имени")
    @Description("Проверяем, что курьера можно создать. Курьер без имени, с login alina22222, password 1234. Ожидаемый код ответа 201")
    public void createCourierWithLoginAndPassword() {
        RegisterNewCourier registerNewCourier = new RegisterNewCourier("alina22222", "1234", "alina");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(registerNewCourier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Создаем курьера без логина")
    @Description("Создаем курьера без логина. Курьер с password 1234. Ожидаемый код ответа 400 и текст сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithOutLogin() {
        String newCourier = "{\"password\": \"1234\", \"firstName\": \"alina\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newCourier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Создаем курьера без пароля")
    @Description("Создаем курьера без пароля. Курьер с login alina22222. Ожидаемый код ответа 400 и текст сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithOutPassword() {
        String newCourier = "{\"login\": \"alina22222\", \"firstName\": \"alina\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(newCourier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Нельзя создать двух одинаковых курьеров. Ожидаемый код ответа 409 и текст сообщения 'Этот логин уже используется'")
    public void createCourierWithFullDuplicateLoginPasswordAndFirstName() {
        RegisterNewCourier registerNewCourier = new RegisterNewCourier("alina22222", "1234", "alina");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(registerNewCourier)
                .when()
                .post("/api/v1/courier");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(registerNewCourier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }

    @Test
    @DisplayName("Нельзя создать курьеров с одинаковым login")
    @Description("Нельзя создать курьеров с одинаковым login. Ожидаемый код ответа 409 и текст сообщения 'Этот логин уже используется'")
    public void createCourierWithDuplicateLogin() {
        RegisterNewCourier registerNewCourier = new RegisterNewCourier("alina22222", "1234", "alina");
        RegisterNewCourier duplicateLoginCourier = new RegisterNewCourier("alina22222", "12345", "alina1");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(registerNewCourier)
                .when()
                .post("/api/v1/courier");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(duplicateLoginCourier)
                        .when()
                        .post("/api/v1/courier");
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"))
                .and()
                .statusCode(409);
    }

    @After
    @Description("Удаляем пользователя с login: alina22222")
    public void deleteCourier() {
        String json = "{\"login\": \"alina22222\", \"password\": \"1234\"}";

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
