package findlogin;

import lombok.Data;

@Data
public class FindLoginCourierRequest {
    private String login;
    private String password;

    public FindLoginCourierRequest() {
    }

    public FindLoginCourierRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
