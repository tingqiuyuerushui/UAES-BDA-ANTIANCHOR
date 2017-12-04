package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 10/23/2017.
 */

public class FuelHistoryEntry {
    private String status;
    private DataEntity data;
    private String info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public FuelHistoryEntry(String status, DataEntity data, String info) {
        this.status = status;
        this.data = data;
        this.info = info;

    }

    public static class DataEntity {
        private String startDate;
        private String endDate;
        private String sumConsumerFuel;
        private String sumSpend;
        private String travelConsumption;
        private String idleConsumption;
        private String fanConsumption;
        private String otherConsumption;

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getSumConsumerFuel() {
            return sumConsumerFuel;
        }

        public void setSumConsumerFuel(String sumConsumerFuel) {
            this.sumConsumerFuel = sumConsumerFuel;
        }

        public String getSumSpend() {
            return sumSpend;
        }

        public void setSumSpend(String sumSpend) {
            this.sumSpend = sumSpend;
        }

        public String getTravelConsumption() {
            return travelConsumption;
        }

        public void setTravelConsumption(String travelConsumption) {
            this.travelConsumption = travelConsumption;
        }

        public String getIdleConsumption() {
            return idleConsumption;
        }

        public void setIdleConsumption(String idleConsumption) {
            this.idleConsumption = idleConsumption;
        }

        public String getFanConsumption() {
            return fanConsumption;
        }

        public void setFanConsumption(String fanConsumption) {
            this.fanConsumption = fanConsumption;
        }

        public String getOtherConsumption() {
            return otherConsumption;
        }

        public void setOtherConsumption(String otherConsumption) {
            this.otherConsumption = otherConsumption;
        }

        public DataEntity(String startDate, String endDate, String sumConsumerFuel, String sumSpend, String travelConsumption, String idleConsumption, String fanConsumption, String otherConsumption) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.sumConsumerFuel = sumConsumerFuel;
            this.sumSpend = sumSpend;
            this.travelConsumption = travelConsumption;
            this.idleConsumption = idleConsumption;
            this.fanConsumption = fanConsumption;
            this.otherConsumption = otherConsumption;

        }
    }
}
