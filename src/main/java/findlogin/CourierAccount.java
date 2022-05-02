package findlogin;

import lombok.Data;

import java.util.HashMap;

@Data
public class CourierAccount {
    private String login;
    private String password;
    private HashMap<String, String> account = new HashMap<>();

    public CourierAccount() {
    }

    public CourierAccount(HashMap<String, String> account) {
        this.login = account.get("login");
        this.password = account.get("password");
    }
}
