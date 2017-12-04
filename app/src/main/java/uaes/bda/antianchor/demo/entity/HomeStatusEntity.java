package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 11/24/2017.
 */

public class HomeStatusEntity {

    /**
     * data : {"remainMileage":"15889","remainFuelPer":"0.36","batteryStatus":"Normal","health":"2","mtCoef":"85"}
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
         * remainMileage : 15889
         * remainFuelPer : 0.36
         * batteryStatus : Normal
         * health : 2
         * mtCoef : 85
         */

        private String remainMileage;
        private String remainFuelPer;
        private String batteryStatus;
        private String health;
        private String mtCoef;

        public String getRemainMileage() {
            return remainMileage;
        }

        public void setRemainMileage(String remainMileage) {
            this.remainMileage = remainMileage;
        }

        public String getRemainFuelPer() {
            return remainFuelPer;
        }

        public void setRemainFuelPer(String remainFuelPer) {
            this.remainFuelPer = remainFuelPer;
        }

        public String getBatteryStatus() {
            return batteryStatus;
        }

        public void setBatteryStatus(String batteryStatus) {
            this.batteryStatus = batteryStatus;
        }

        public String getHealth() {
            return health;
        }

        public void setHealth(String health) {
            this.health = health;
        }

        public String getMtCoef() {
            return mtCoef;
        }

        public void setMtCoef(String mtCoef) {
            this.mtCoef = mtCoef;
        }
    }
}
