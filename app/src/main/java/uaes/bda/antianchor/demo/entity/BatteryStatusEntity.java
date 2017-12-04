package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 11/9/2017.
 */

public class BatteryStatusEntity {

    /**
     * data : {"batteryStatus":"full"}
     * info : ok
     * status : 1
     */

    private DataBean data;
    private String info;
    private String status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * batteryStatus : full
         */

        private String batteryStatus;

        public String getBatteryStatus() {
            return batteryStatus;
        }

        public void setBatteryStatus(String batteryStatus) {
            this.batteryStatus = batteryStatus;
        }
    }
}
