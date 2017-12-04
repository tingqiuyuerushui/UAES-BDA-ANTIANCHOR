package uaes.bda.antianchor.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.entity.WeatherListEntity;

/**
 * Created by lun.zhang on 11/16/2017.
 */

public class WeatherAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<WeatherListEntity.ResultsBean.DailyBean> listEntity;

    public WeatherAdapter(Context context, List<WeatherListEntity.ResultsBean.DailyBean> listEntity) {
        this.context = context;
        this.listEntity = listEntity;
    }

    @Override
    public int getCount() {
        return listEntity.size();
    }

    @Override
    public Object getItem(int position) {
        return listEntity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherAdapter.ViewHolder holder = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_weather_item, null);
            holder = new WeatherAdapter.ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (WeatherAdapter.ViewHolder) convertView.getTag();
        }
        if(position == 0){
            holder.textView8.setVisibility(View.VISIBLE);
        }else{
            holder.textView8.setVisibility(View.GONE);
        }
        holder.tvWeekday.setText(listEntity.get(position).getDate());
        holder.tvTempNum.setText(listEntity.get(position).getHigh()+"°- "+listEntity.get(position).getLow()+"°");
        if (listEntity.get(position).getText_day().equals("晴")){
            holder.imgWeather.setImageResource(R.mipmap.ico_sunny);
        }else if (listEntity.get(position).getText_day().equals("多云")){
            holder.imgWeather.setImageResource(R.mipmap.ico_cloudy);
        }else if (listEntity.get(position).getText_day().equals("阴")){
            holder.imgWeather.setImageResource(R.mipmap.ico_cloudy);
        }else if (listEntity.get(position).getText_day().contains("雨")){
            holder.imgWeather.setImageResource(R.mipmap.ico_shower);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_weekday)
        TextView tvWeekday;
        @BindView(R.id.textView8)
        TextView textView8;
        @BindView(R.id.img_weather)
        ImageView imgWeather;
        @BindView(R.id.tv_temp_num)
        TextView tvTempNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
