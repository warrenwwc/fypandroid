package is.fyp.api.requests;

public class RegisterRequest extends BaseRequest {
    private String device_id;

    public RegisterRequest() {
        this.setType("RU");
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}