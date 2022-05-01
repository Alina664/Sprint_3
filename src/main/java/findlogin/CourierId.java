package findlogin;

import lombok.Data;

@Data
public class CourierId {
    private String id;

    public CourierId() {
    }

    public CourierId(String id) {
        this.id = id;
    }
}
