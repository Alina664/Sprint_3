package orderlist;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Orders {
    private int id;
    private String courierId;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private int track;
    private ArrayList<String> color;
    private String comment;
    private String createdAt;
    private String updatedAt;
    private int status;

    public Orders(int id, String courierId, String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, int track, ArrayList<String> color, String comment, String createdAt, String updatedAt, int status) {
        this.id = id;
        this.courierId = courierId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.track = track;
        this.color = color;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }
}
