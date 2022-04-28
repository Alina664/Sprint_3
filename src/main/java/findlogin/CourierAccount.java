package findlogin;

import lombok.Data;

@Data
public class CourierAccount {
    private String login;
    private String password;

    public CourierAccount() {
    }

    public CourierAccount(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
