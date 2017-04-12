package is.fyp.api.responses;

/**
 * Created by eie3333 on 12/29/2016.
 */

public class BaseResponse {
    public String message;
    public String error;

    public BaseResponse() {
        this.message = this.error = null;
    }

    public boolean hasError() {
        return this.error != null && !this.error.isEmpty();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
