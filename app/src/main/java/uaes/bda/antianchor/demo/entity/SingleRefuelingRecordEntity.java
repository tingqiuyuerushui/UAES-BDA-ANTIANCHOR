package uaes.bda.antianchor.demo.entity;

import java.text.NumberFormat;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import uaes.bda.antianchor.demo.utils.MyUtil;

/**
 * Created by lun.zhang on 10/23/2017.
 */

public class SingleRefuelingRecordEntity implements Parcelable {


    /**
     * data : [{"fuelComsume":"2510","fuelFillingCost":"81.62","fuelFillingDate":"2017-08-26","fuelFillingDateStamp":"1503676800000","fuelFillingVolume":"14","fuelPrice":"5.83","fuelType":"89","milesages":"26413","stationAddress":"上海","stationName":"中国石油\u2014\u2014张江加油站","vin":"111111"},{"fuelComsume":"2655","fuelFillingCost":"82.56","fuelFillingDate":"2017-09-24","fuelFillingDateStamp":"1506182400000","fuelFillingVolume":"12","fuelPrice":"6.88","fuelType":"98","milesages":"29487","stationAddress":"上海","stationName":"中国石油\u2014\u2014张江加油站","vin":"111111"},{"fuelComsume":"2805","fuelFillingCost":"118.56","fuelFillingDate":"2017-10-24","fuelFillingDateStamp":"1508774400000","fuelFillingVolume":"19","fuelPrice":"6.24","fuelType":"92","milesages":"29487","stationAddress":"上海","stationName":"中国石油\u2014\u2014张江加油站","vin":"111111"}]
     * info : ok
     * status : 1
     */

    private String info;
    private String status;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * fuelFillingDateStamp : 1499875200000
         * averageConsume : 7.2
         * fuelFillingVolume : 62.61
         * milesages : 7481
         * averageSpeed : 21
         * stationAddress : 上海
         * miles : 500
         * fuelType : 89
         * fuelFillingDate : 2017-07-13
         * fuelComsume : 397
         * ecoRank : 16
         * fuelFillingCost : 365
         * fuelPrice : 5.83
         * stationName : 张江加油站
         * vin : 111111
         * fuelQuality : 1
         */

        private String fuelFillingDateStamp;
        private String averageConsume;
        private String fuelFillingVolume;
        private String milesages;
        private String averageSpeed;
        private String stationAddress;
        private String miles;
        private String fuelType;
        private String fuelFillingDate;
        private String fuelComsume;
        private String ecoRank;
        private String fuelFillingCost;
        private String fuelPrice;
        private String stationName;
        private String vin;
        private String fuelQuality;

        public String getFuelFillingDateStamp() {
            return fuelFillingDateStamp;
        }

        public void setFuelFillingDateStamp(String fuelFillingDateStamp) {
            this.fuelFillingDateStamp = fuelFillingDateStamp;
        }

        public String getAverageConsume() {
            return averageConsume;
        }

        public void setAverageConsume(String averageConsume) {
            this.averageConsume = averageConsume;
        }

        public String getFuelFillingVolume() {
            return fuelFillingVolume;
        }

        public void setFuelFillingVolume(String fuelFillingVolume) {
            this.fuelFillingVolume = fuelFillingVolume;
        }

        public String getMilesages() {
            return milesages;
        }

        public void setMilesages(String milesages) {
            this.milesages = milesages;
        }

        public String getAverageSpeed() {
            return averageSpeed;
        }

        public void setAverageSpeed(String averageSpeed) {
            this.averageSpeed = averageSpeed;
        }

        public String getStationAddress() {
            return stationAddress;
        }

        public void setStationAddress(String stationAddress) {
            this.stationAddress = stationAddress;
        }

        public String getMiles() {
            return miles;
        }

        public void setMiles(String miles) {
            this.miles = miles;
        }

        public String getFuelType() {
            return fuelType;
        }

        public void setFuelType(String fuelType) {
            this.fuelType = fuelType;
        }

        public String getFuelFillingDate() {
            return fuelFillingDate;
        }

        public void setFuelFillingDate(String fuelFillingDate) {
            this.fuelFillingDate = fuelFillingDate;
        }

        public String getFuelComsume() {
            return fuelComsume;
        }

        public void setFuelComsume(String fuelComsume) {
            this.fuelComsume = fuelComsume;
        }

        public String getEcoRank() {
            return ecoRank;
        }

        public void setEcoRank(String ecoRank) {
            this.ecoRank = ecoRank;
        }

        public String getFuelFillingCost() {
            return fuelFillingCost;
        }

        public void setFuelFillingCost(String fuelFillingCost) {
            this.fuelFillingCost = fuelFillingCost;
        }

        public String getFuelPrice() {
            return fuelPrice;
        }

        public void setFuelPrice(String fuelPrice) {
            this.fuelPrice = fuelPrice;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getFuelQuality() {
            return fuelQuality;
        }

        public void setFuelQuality(String fuelQuality) {
            this.fuelQuality = fuelQuality;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.info);
        dest.writeString(this.status);
        dest.writeList(this.data);
    }

    public SingleRefuelingRecordEntity() {
    }

    protected SingleRefuelingRecordEntity(Parcel in) {
        this.info = in.readString();
        this.status = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, List.class.getClassLoader());
    }

    public static final Creator<SingleRefuelingRecordEntity> CREATOR = new Creator<SingleRefuelingRecordEntity>() {
        public SingleRefuelingRecordEntity createFromParcel(Parcel source) {
            return new SingleRefuelingRecordEntity(source);
        }

        public SingleRefuelingRecordEntity[] newArray(int size) {
            return new SingleRefuelingRecordEntity[size];
        }
    };
}
