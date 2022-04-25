package createorder;

import lombok.Data;

@Data
public class CreateOrderAnswer {

    private int track;

    public CreateOrderAnswer(int track) {
        this.track = track;
    }

    public CreateOrderAnswer() {
    }
}
