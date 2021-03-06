package is.fyp.api.requests;

import android.view.View;
import android.widget.TextView;

/**
 * Created by Jason on 5/4/2017.
 */

public class TransactionRequest extends BaseRequest {

    private int limit;
    private int offset;
    private boolean groupby;

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

    public boolean isGroupby() {
        return groupby;
    }

    public void setGroupby(boolean groupby) {
        this.groupby = groupby;
    }
}
