package uaes.bda.antianchor.demo.bean;

/**
 * Created by gang.cao on 11/9/2017.
 */

public class SparkingHttp {

    /**
     * data : {"forthSparking":"health","firstSparking":"health","secondSparking":"warning","thirdSparking":"fault"}
     * info : ok
     * status : 1
     */

    private DataBean data;
    private String info;
    private String status;

    public void setData(DataBean data) {
        this.data = data;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public String getInfo() {
        return info;
    }

    public String getStatus() {
        return status;
    }

    public static class DataBean {
        /**
         * forthSparking : health
         * firstSparking : health
         * secondSparking : warning
         * thirdSparking : fault
         */

        private String forthSparking;
        private String firstSparking;
        private String secondSparking;
        private String thirdSparking;

        public void setForthSparking(String forthSparking) {
            this.forthSparking = forthSparking;
        }

        public void setFirstSparking(String firstSparking) {
            this.firstSparking = firstSparking;
        }

        public void setSecondSparking(String secondSparking) {
            this.secondSparking = secondSparking;
        }

        public void setThirdSparking(String thirdSparking) {
            this.thirdSparking = thirdSparking;
        }

        public String getForthSparking() {
            return forthSparking;
        }

        public String getFirstSparking() {
            return firstSparking;
        }

        public String getSecondSparking() {
            return secondSparking;
        }

        public String getThirdSparking() {
            return thirdSparking;
        }
    }
}
