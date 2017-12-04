package uaes.bda.antianchor.demo.bean;

import java.util.List;

/**
 * Created by gang.cao on 11/17/2017.
 */

public class LatLng {
    private List<LatLng> mlist;

  /*  public LatLng(List<LatLng> mLatLng) {
        this.mlist = mLatLng;
    }*/

    public LatLng(List<LatLng> points) {
        this.mlist = points;
    }

    public List<LatLng> getLatLng(){
        return mlist;
    }

}
