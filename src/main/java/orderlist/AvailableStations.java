package orderlist;

import lombok.Data;

@Data
public class AvailableStations {

    private String name;
    private String number;
    private String color;

    public AvailableStations(String name, String number, String color) {
        this.name = name;
        this.number = number;
        this.color = color;
    }
}
