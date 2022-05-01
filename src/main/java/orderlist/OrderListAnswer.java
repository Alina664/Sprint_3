package orderlist;

import lombok.Data;

import java.util.List;

@Data
public class OrderListAnswer {

    private List<Orders> orders;
    private PageInfo pageInfo;
    private List<AvailableStations> availableStations;

    public OrderListAnswer(List<Orders> orders, PageInfo pageInfo, List<AvailableStations> availableStations) {
        this.orders = orders;
        this.pageInfo = pageInfo;
        this.availableStations = availableStations;
    }
}
