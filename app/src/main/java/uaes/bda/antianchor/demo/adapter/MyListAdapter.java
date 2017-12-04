package uaes.bda.antianchor.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;


import java.util.List;

import uaes.bda.antianchor.demo.R;

/**
 * Created by gang.cao on 10/31/2017.
 */

public class MyListAdapter extends BaseAdapter implements AdapterView.OnClickListener {
    private final List<PoiItem> poiItems;
    private final Context mTx;
    private ViewHolder holder;

    public MyListAdapter(List<PoiItem> poiItems, Context context) {
        this.mTx = context;
        this.poiItems = poiItems;

    }

    @Override
    public int getCount() {
        return poiItems.size();
    }

    @Override
    public PoiItem getItem(int position) {
        return poiItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null){
             convertView  = LayoutInflater.from(mTx).inflate(R.layout.map_listview_item, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.item_name);
            holder.distance = convertView.findViewById(R.id.item_distance);
            holder.address = convertView.findViewById(R.id.item_address);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.address.setOnClickListener(this);
        holder.name.setOnClickListener(this);
        holder.address.setTag(position);
        holder.name.setTag(position);
        holder.name.setText(poiItems.get(position).getTitle()+"");
        holder.distance.setText(poiItems.get(position).getDistance()+"米");
        holder.address.setText(poiItems.get(position).getCityName()     //上海市
                +poiItems.get(position).getAdName()+"");                //浦东新区

        return convertView;
    }

    public final class ViewHolder{
        public TextView name;
        public TextView distance;
        public TextView address;
    }

    private InnerItemOnclickListener mListener;

    public interface InnerItemOnclickListener{
        void itemClick(View v );
    }

    public void setOnInnerItemOnclickListener(InnerItemOnclickListener listener){
        this.mListener = listener;
    }

    @Override
    public void onClick(View view) {
        mListener.itemClick(view);
    }
}
