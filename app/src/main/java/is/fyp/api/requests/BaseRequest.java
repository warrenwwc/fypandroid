package is.fyp.api.requests;

public class BaseRequest {
    private String faddr;
    private String taddr;
    private String type;
    private String sign;

    public String getFaddr() {
        return faddr;
    }

    public String getTaddr() {
        return taddr;
    }

    public String getType() {
        return type;
    }

    public String getSign() {
        return sign;
    }

    public void setFaddr(String faddr) {
        this.faddr = faddr;
    }

    public void setTaddr(String taddr) {
        this.taddr = taddr;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void sign() {

    }
}
