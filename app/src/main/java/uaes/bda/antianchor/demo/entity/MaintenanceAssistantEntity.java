package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 4/23/2017.
 */

public class MaintenanceAssistantEntity {
    private int imgResource;
    private String itemText;
    private String itemTextStatus;

    public MaintenanceAssistantEntity(int imgResource, String itemText, String itemTextStatus) {
        this.imgResource = imgResource;
        this.itemText = itemText;
        this.itemTextStatus = itemTextStatus;
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

    public String getItemTextStatus() {
        return itemTextStatus;
    }

    public void setItemTextStatus(String itemTextStatus) {
        this.itemTextStatus = itemTextStatus;
    }
}
