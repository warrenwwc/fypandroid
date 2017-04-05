package is.fyp.api.requests;

/**
 * Created by Jason on 5/4/2017.
 */

public class TransactionRequest extends BaseRequest {

    private int limit = 0;
    private int offset = 100;

    public TransactionRequest() {
        this.setType("RR");
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
