package uaes.bda.antianchor.demo.bean;

import java.util.List;

/**
 * Created by gang.cao on 11/9/2017.
 *  {"fault":["second","forth"],"warning":["first"]}
 *
 */

public class SparkingWebSocket {
    /**
     * fault : ["second","forth"]
     * warning : ["first"]
     */

    private List<String> fault;
    private List<String> warning;

    public void setFault(List<String> fault) {
        this.fault = fault;
    }

    public void setWarning(List<String> warning) {
        this.warning = warning;
    }

    public List<String> getFault() {
        return fault;
    }

    public List<String> getWarning() {
        return warning;
    }
}
