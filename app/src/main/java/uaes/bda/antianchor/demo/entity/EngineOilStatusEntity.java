package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 11/9/2017.
 */

public class EngineOilStatusEntity {

    /**
     * data : {"engineOilHealthRete":"0.65","engineOilRemainMile":"2000","engineOilHealthStatus":"health"}
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
         * engineOilHealthRete : 0.65
         * engineOilRemainMile : 2000
         * engineOilHealthStatus : health
         */

        private String engineOilHealthRete;
        private String engineOilRemainMile;
        private String engineOilHealthStatus;

        public String getEngineOilHealthRete() {
            return engineOilHealthRete;
        }

        public void setEngineOilHealthRete(String engineOilHealthRete) {
            this.engineOilHealthRete = engineOilHealthRete;
        }

        public String getEngineOilRemainMile() {
            return engineOilRemainMile;
        }

        public void setEngineOilRemainMile(String engineOilRemainMile) {
            this.engineOilRemainMile = engineOilRemainMile;
        }

        public String getEngineOilHealthStatus() {
            return engineOilHealthStatus;
        }

        public void setEngineOilHealthStatus(String engineOilHealthStatus) {
            this.engineOilHealthStatus = engineOilHealthStatus;
        }
    }
}
