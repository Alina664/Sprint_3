package orderlist;

import lombok.Data;

@Data
public class PageInfo {

    private int page;
    private int total;
    private int limit;

    public PageInfo(int page, int total, int limit) {
        this.page = page;
        this.total = total;
        this.limit = limit;
    }
}
