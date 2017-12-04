package uaes.bda.antianchor.demo.entity;

/**
 * Created by lun.zhang on 11/24/2017.
 */

public class EngineOilWebSocketEntity {

    /**
     * engineOilHealthRete : 0.71
     * engineOilRemainMile : 2000
     * status : Normal
     */

    private String engineOilHealthRete;
    private String engineOilRemainMile;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
