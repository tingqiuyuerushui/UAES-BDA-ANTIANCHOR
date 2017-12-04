package uaes.bda.antianchor.demo.adapter;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;


import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.bean.Petrol;
import uaes.bda.antianchor.demo.bean.Station;


public class StationListAdapter extends BaseAdapter {

	private Context mContext;
	private List<Station> list;
	private LayoutInflater mInflater;

	public StationListAdapter(Context context, List<Station> list){
		this.list = list;
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Station getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View rowView = null;
		if(convertView == null){
			rowView = mInflater.inflate(R.layout.item_station_lists, null);
		}else{
			rowView = convertView;
		}
		TextView tv_id = rowView.findViewById(R.id.tv_id);
		TextView tv_name = rowView.findViewById(R.id.tv_name);
		TextView tv_distance = rowView.findViewById(R.id.tv_distance);
		TextView tv_addr = rowView.findViewById(R.id.tv_addr);
		TextView tv_garde = rowView.findViewById(R.id.grade);
		int max=5;
		int min=4;
		Random random = new Random();

		int ran = random.nextInt(max)%(max-min+1) + min;
		tv_garde.setText(ran+"分");
		Station s = getItem(position);
		tv_id.setText((position+1)+".");
		tv_name.setText(s.getName());
		tv_distance.setText("距我: " + s.getDistance()+"m");
		tv_addr.setText(s.getAddr());
		PriceAndType(rowView, s);

		return rowView;
	}

	private void PriceAndType(View rowView, Station s) {
		GridView gv = rowView.findViewById(R.id.gv_price);
		List<Petrol> priceList = s.getPriceList();
//把数组转换json数组
		net.sf.json.JSONArray jsonArray = JSONArray.fromObject(priceList);
		ArrayList<Object> listtype = new ArrayList<>();
		ArrayList<Object> listprice = new ArrayList<>();
		ArrayList<Object> listzon = new ArrayList<>();
		if (jsonArray.size() > 0){
			for (int i = 0; i < jsonArray.size(); i++) {
				//遍历jsonarray数组，把每一个对象转换json对象
				net.sf.json.JSONObject jsonObject = jsonArray.getJSONObject(i);

				String typeold = (String) jsonObject.get("type");
				String type = typeold.replace("E", "#");
				String price = (String)  jsonObject.get("price");
				listtype.add(type);
				listprice.add(price);
				listzon.add("型号："+type + "    " +"¥ :"+price);
			}
		}



		GVAdapter gvAdapter = new GVAdapter(mContext,listzon);
		//	GVAdapter gvAdapter = new GVAdapter(mContext,s.getPriceList());
		gv.setAdapter(gvAdapter);
		for (int i = 0; i < priceList.size(); i++) {
			Log.e("333", "priceList.get(i).getPrice(): " + priceList.get(i) );
		//	java.lang.ClassCastException: java.util.HashMap cannot be cast to com.uaes.antianchor.bean.Petrol
			//	Log.e("222", "priceList.get(i).getType(): " + priceList.get(i).getType() );
//			E/333: priceList.get(i).getPrice(): {type=E90, price=5.83}
//			 E/333: priceList.get(i).getPrice(): {type=E93, price=6.24}
//			 E/333: priceList.get(i).getPrice(): {type=E97, price=6.65}
//			 E/333: priceList.get(i).getPrice(): {type=E0, price=5.86}
			Log.e("333", "priceList.get(i).getPrice(): " + priceList );
//			 E/333: priceList.get(i).getPrice(): [{type=E90, price=5.83}, {type=E93, price=6.24}, {type=E97, price=6.65}, {type=E0, price=5.86}]

		}
		//ListGridViewAdapter adapter = new ListGridViewAdapter(mContext, s.getGastPriceList());
		//gv.setAdapter(adapter);
	}

}
