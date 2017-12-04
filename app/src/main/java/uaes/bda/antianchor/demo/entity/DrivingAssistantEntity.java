package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 4/23/2017.
 */

public class DrivingAssistantEntity {
    private int imgResource;
    private String itemText;
    private DrivingAssistantItemEntity itemEntity;
    public DrivingAssistantEntity(int imgResource, String itemText,DrivingAssistantItemEntity itemEntity) {
        this.imgResource = imgResource;
        this.itemText = itemText;
        this.itemEntity = itemEntity;
    }

    public DrivingAssistantItemEntity getItemEntity() {
        return itemEntity;
    }

    public void setItemEntity(DrivingAssistantItemEntity itemEntity) {
        this.itemEntity = itemEntity;
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
}
