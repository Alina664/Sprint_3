import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Создание курьера")
public class CreateCourierTest extends BaseTest {
    final String login = "alina22222";
    final String password = "1234";
    final String firstName = "alina";
    HashMap<String, String> account = new HashMap<>();
    HashMap<String, String> accountDublicate = new HashMap<>();


    // метод для шага "Отправить запрос":
    @Step("Send POST request /api/v1/courier")
    public Response sendPostRequestCourier(RegisterNewCourier registerNewCourier) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(registerNewCourier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Create courier")
    public RegisterNewCourier registerCourier(HashMap<String, String> account) {
        RegisterNewCourier registerCourier = new RegisterNewCourier(account);
        return registerCourier;
    }

    @Step("Compare result HTTP_CREATED")
    public void compareBodyMessageOkAndStatusHttpCreated(Response response) {
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(HttpURLConnection.HTTP_CREATED);
    }

    @Step("Compare result HTTP_BAD_REQUEST")
    public void compareMessageAndStatusHttpBadRequest(Response response) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
    }

    @Step("Compare result HTTP_CONFLICT")
    public void compareMessageAndStatusHttpConflict(Response response) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"))
                .and()
                .statusCode(HttpURLConnection.HTTP_CONFLICT);
    }


    @Test
    @DisplayName("Создаем курьера с полными данными")
    @Description("Проверяем, что курьера можно создать. Курьер с полными данными login alina22222, password 1234. Ожидаемый код ответа 201")
    public void createCourierWithLoginPasswordAndFirstName() {
        account.put("login", login);
        account.put("password", password);
        account.put("firstName", firstName);
        RegisterNewCourier registerNewCourier = registerCourier(account);
        Response response = sendPostRequestCourier(registerNewCourier);
        compareBodyMessageOkAndStatusHttpCreated(response);
    }


    @Test
    @DisplayName("Создаем курьера без имени")
    @Description("Проверяем, что курьера можно создать. Курьер без имени, с login alina22222, password 1234. Ожидаемый код ответа 201")
    public void createCourierWithLoginAndPassword() {
        account.put("login", login);
        account.put("password", password);
        RegisterNewCourier registerNewCourier = new RegisterNewCourier(account);
        Response response = sendPostRequestCourier(registerNewCourier);
        compareBodyMessageOkAndStatusHttpCreated(response);
    }

    @Test
    @DisplayName("Создаем курьера без логина")
    @Description("Создаем курьера без логина. Курьер с password 1234. Ожидаемый код ответа 400 и текст сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithOutLogin() {
        account.put("password", password);
        account.put("firstName", firstName);
        RegisterNewCourier registerNewCourier = registerCourier(account);
        Response response = sendPostRequestCourier(registerNewCourier);
        compareMessageAndStatusHttpBadRequest(response);
    }

    @Test
    @DisplayName("Создаем курьера без пароля")
    @Description("Создаем курьера без пароля. Курьер с login alina22222. Ожидаемый код ответа 400 и текст сообщения 'Недостаточно данных для создания учетной записи'")
    public void createCourierWithOutPassword() {
        account.put("login", login);
        account.put("firstName", firstName);
        RegisterNewCourier registerNewCourier = registerCourier(account);
        Response response = sendPostRequestCourier(registerNewCourier);
        compareMessageAndStatusHttpBadRequest(response);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Нельзя создать двух одинаковых курьеров. Ожидаемый код ответа 409 и текст сообщения 'Этот логин уже используется'")
    public void createCourierWithFullDuplicateLoginPasswordAndFirstName() {
        account.put("login", login);
        account.put("password", password);
        account.put("firstName", firstName);
        RegisterNewCourier registerNewCourier = registerCourier(account);
        sendPostRequestCourier(registerNewCourier);

        Response response = sendPostRequestCourier(registerNewCourier);
        compareMessageAndStatusHttpConflict(response);
    }

    @Test
    @DisplayName("Нельзя создать курьеров с одинаковым login")
    @Description("Нельзя создать курьеров с одинаковым login. Ожидаемый код ответа 409 и текст сообщения 'Этот логин уже используется'")
    public void createCourierWithDuplicateLogin() {
        account.put("login", login);
        account.put("password", password);
        account.put("firstName", firstName);
        accountDublicate.put("login", login);
        accountDublicate.put("password", firstName);
        accountDublicate.put("firstName", password);
        RegisterNewCourier registerNewCourier = registerCourier(account);
        RegisterNewCourier duplicateLoginCourier = registerCourier(accountDublicate);
        sendPostRequestCourier(registerNewCourier);
        Response response = sendPostRequestCourier(duplicateLoginCourier);
        compareMessageAndStatusHttpConflict(response);
    }

    @After
    public void deleteCourier() {
        deleteCourier(login, password);
    }


}
