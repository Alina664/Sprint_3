package createorder;

import lombok.Data;

@Data
public class OrderTrackNumber {

    private int track;

    public OrderTrackNumber(int track) {
        this.track = track;
    }

    public OrderTrackNumber() {
    }
}
