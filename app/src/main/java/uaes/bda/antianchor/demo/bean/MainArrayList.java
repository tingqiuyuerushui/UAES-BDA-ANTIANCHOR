package uaes.bda.antianchor.demo.bean;

import java.util.ArrayList;

/**
 * Created by gang.cao on 11/17/2017.
 */

public class MainArrayList {
    private ArrayList<Double> mlist;

    public MainArrayList(ArrayList<Double> mDoublelist) {
        this.mlist = mDoublelist;
    }

    public ArrayList<Double> getlat(){
       return mlist;
   }

/*   public void setlat(ArrayList<Double> list){
       this.mlist = list;
   }*/

   /*	public ArrayList<Petrol> getGastPriceList() {
		return gastPriceList;
	}

	public void setGastPriceList(ArrayList<Petrol> gastPriceList) {
		this.gastPriceList = gastPriceList;
	}*/
}
