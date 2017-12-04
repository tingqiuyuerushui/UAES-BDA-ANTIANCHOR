package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 4/14/2017.
 */

public class FaultPredictionEntity {
    private int imgResource;
    private String itemText;
    private int imgValue;
    private String tvHealthStatus;

    public FaultPredictionEntity(int imgResource, String itemText, int imgValue, String tvHealthStatus) {
        this.imgResource = imgResource;
        this.itemText = itemText;
        this.imgValue = imgValue;
        this.tvHealthStatus = tvHealthStatus;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public int getImgValue() {
        return imgValue;
    }

    public void setImgValue(int imgValue) {
        this.imgValue = imgValue;
    }

    public String getTvHealthStatus() {
        return tvHealthStatus;
    }

    public void setTvHealthStatus(String tvHealthStatus) {
        this.tvHealthStatus = tvHealthStatus;
    }
}
