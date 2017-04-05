package is.fyp.api;

import is.fyp.api.contracts.Signable;

/**
 * Created by Jason on 5/4/2017.
 */

public class Coin implements Signable {
    private String faddr;
    private String taddr;
    private String type;
    private String sn;
    private String sign;

    public Coin() {
        this.setType("TX");
    }

    public String getFaddr() {
        return faddr;
    }

    public String getTaddr() {
        return taddr;
    }

    public String getType() {
        return type;
    }

    public String getSn() {
        return sn;
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

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
