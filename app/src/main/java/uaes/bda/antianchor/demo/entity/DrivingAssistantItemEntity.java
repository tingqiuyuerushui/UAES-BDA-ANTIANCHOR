package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 10/21/2017.
 */

public class DrivingAssistantItemEntity {
    private String ecoRanking;
    private String remainFuelPer;
    private String fuelConsumerPer100KM;
    private String fuelType;
    private String fuleQuality;
    private String remainMileageMin;
    private String remainMileageMax;
    private String remainMileageMaxMin;

    public String getRemainMileageMaxMin() {
        return remainMileageMin+"-"+remainMileageMax +"km";
    }

    public void setRemainMileageMaxMin(String remainMileageMaxMin) {
        this.remainMileageMaxMin = remainMileageMaxMin;
    }

    public String getEcoRanking() {
        return ecoRanking;
    }

    public void setEcoRanking(String ecoRanking) {
        this.ecoRanking = ecoRanking;
    }

    public String getRemainFuelPer() {
        return remainFuelPer+"L";
    }

    public void setRemainFuelPer(String remainFuelPer) {
        this.remainFuelPer = remainFuelPer;
    }

    public String getFuelConsumerPer100KM() {
        return fuelConsumerPer100KM + "km";
    }

    public void setFuelConsumerPer100KM(String fuelConsumerPer100KM) {
        this.fuelConsumerPer100KM = fuelConsumerPer100KM;
    }

    public String getFuelType() {
        return fuelType + "#";
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFuleQuality() {
        if(fuleQuality.equals("2")){
            fuleQuality = "良";
        }
        if(fuleQuality.equals("1")){
            fuleQuality = "优";
        }
        if(fuleQuality.equals("3")){
            fuleQuality = "中";
        }
        return fuleQuality;
    }

    public void setFuleQuality(String fuleQuality) {
        this.fuleQuality = fuleQuality;
    }

    public String getRemainMileageMin() {
        return remainMileageMin;
    }

    public void setRemainMileageMin(String remainMileageMin) {
        this.remainMileageMin = remainMileageMin;
    }

    public String getRemainMileageMax() {
        return remainMileageMax;
    }

    public void setRemainMileageMax(String remainMileageMax) {
        this.remainMileageMax = remainMileageMax;
    }

    public DrivingAssistantItemEntity(String ecoRanking, String remainFuelPer, String fuelConsumerPer100KM, String fuelType, String fuleQuality, String remainMileageMin, String remainMileageMax) {
        this.ecoRanking = ecoRanking;
        this.remainFuelPer = remainFuelPer;
        this.fuelConsumerPer100KM = fuelConsumerPer100KM;
        this.fuelType = fuelType;
        this.fuleQuality = fuleQuality;
        this.remainMileageMin = remainMileageMin;
        this.remainMileageMax = remainMileageMax;

    }
}
