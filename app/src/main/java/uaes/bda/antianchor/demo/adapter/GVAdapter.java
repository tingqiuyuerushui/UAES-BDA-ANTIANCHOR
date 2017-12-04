package uaes.bda.antianchor.demo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;

import uaes.bda.antianchor.demo.R;


/**
 * Created by gang.cao on 10/8/2017.
 */

public class GVAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList priceList;

    public GVAdapter(Context context, ArrayList priceList ) {
        this.priceList = priceList;
        this.mContext =context;
        for (int i = 0; i < priceList.size(); i++) {
            Log.e("333", "priceList.get(i).getPrice(): " + priceList.get(i) );
            //	java.lang.ClassCastException: java.util.HashMap cannot be cast to com.uaes.antianchor.bean.Petrol
            //	Log.e("222", "priceList.get(i).getType(): " + priceList.get(i).getType() );
        }
    }

    @Override
    public int getCount() {
        return priceList.size();
    }

    @Override
    public Object getItem(int position) {
        return priceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;
        if (convertView == null){
            rowView= LayoutInflater.from(mContext).inflate(R.layout.item_info_list, null);
        }else {
            rowView = convertView;
        }
        TextView tv_name = rowView.findViewById(R.id.tv_name);
        TextView tv_price = rowView.findViewById(R.id.tv_price);
        Object item = getItem(position);

        Log.e("555", "item: "+item.toString() );
       // tv_name.setText(item.getType());
       // tv_price.setText(item.getPrice());
        tv_name.setText(item.toString().substring(3,item.toString().length()));
        return rowView;
    }
}
