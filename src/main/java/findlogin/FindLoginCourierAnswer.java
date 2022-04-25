package findlogin;

import lombok.Data;

@Data
public class FindLoginCourierAnswer {
    private String id;

    public FindLoginCourierAnswer() {
    }

    public FindLoginCourierAnswer(String id) {
        this.id = id;
    }
}
