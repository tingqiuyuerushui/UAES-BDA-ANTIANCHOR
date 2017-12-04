package uaes.bda.antianchor.demo.bean;

import java.util.List;

/**
 * Created by gang.cao on 11/24/2017.
 */

public class SparkingWebSocket02 {

    /**
     * fault : ["second","third"]
     */

    private List<String> fault;

    public void setFault(List<String> fault) {
        this.fault = fault;
    }

    public List<String> getFault() {
        return fault;
    }
}
