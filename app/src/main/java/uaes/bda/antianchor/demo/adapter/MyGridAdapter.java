package uaes.bda.antianchor.demo.adapter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.widget.BaseAdapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.TabMainActivity;

/**
 * Created by gang.cao on 11/20/2017.
 */

public class MyGridAdapter extends BaseAdapter {

    private GridView mGridView;
    private Context context;
    private List<String> list;
    TextView kind;
    private int a = 10;

    private int clickTemp = -1;



    // 标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    public MyGridAdapter(Context context, List<String> list, GridView gridView) {
        super();
        this.context = context;
        this.list = list;
        this.mGridView = gridView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.tab_main_pop_item, null);
            kind = (TextView) convertView.findViewById(R.id.gname);
            convertView.setTag(kind);
        } else {
            kind = (TextView) convertView.getTag();
        }
        kind.setText(list.get(position));

        if (clickTemp == position) {
            kind.setBackgroundResource(R.drawable.yellow_but);
        } else {
            kind.setBackgroundResource(R.drawable.kinds);
        }
        return convertView;
    }

}
