package is.fyp.api.requests;

/**
 * Created by Jason on 5/4/2017.
 */

public class TransactionRequest extends BaseRequest {

    private int limit;
    private int offset;

    public TransactionRequest() {
        this.setLimit(100);
        this.setOffset(0);
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
