package uaes.bda.antianchor.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.adapter.WeatherAdapter;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.customsizeview.MyListView;
import uaes.bda.antianchor.demo.entity.WeatherAirQuiltyEntity;
import uaes.bda.antianchor.demo.entity.WeatherListEntity;
import uaes.bda.antianchor.demo.entity.WeatherRealTimeEntity;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 11/16/2017.
 */

public class WeatherActivity extends Activity {
    @BindView(R.id.img_left)
    ImageView imgLeft;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.img_right)
    ImageView imgRight;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.title_header)
    RelativeLayout titleHeader;
    @BindView(R.id.title_headers)
    LinearLayout titleHeaders;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_temp)
    TextView tvTemp;
    @BindView(R.id.list_weather_day)
    MyListView listWeatherDay;
    @BindView(R.id.tv_visibility)
    TextView tvVisibility;
    @BindView(R.id.tv_feel_temp)
    TextView tvFeelTemp;
    @BindView(R.id.tv_pa)
    TextView tvPa;
    @BindView(R.id.tv_rainfall_probability)
    TextView tvRainfallProbability;
    @BindView(R.id.tv_humidity)
    TextView tvHumidity;
    @BindView(R.id.tv_air_quality)
    TextView tvAirQuality;
    @BindView(R.id.tv_wind_speed)
    TextView tvWindSpeed;
    private Context context;
    private ProgressDialog dialog;
    private WeatherListEntity weatherListEntity;
    private WeatherRealTimeEntity weatherRealTimeEntity;
    private WeatherAirQuiltyEntity weatherAirQuiltyEntity;
    private WeatherAdapter weatherAdapter;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                // 处理UI更新等操作
                case 1:
                    LoadDataToView();
                    break;
                case 2:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 3:
                    FinalToast.netTimeOutMakeText(context);
                    break;
                case 4:
                    LoadRealTimeDataToView();
                    break;
                case 5:
                    tvAirQuality.setText(weatherAirQuiltyEntity.getResults().get(0).getAir().getCity().getAqi());
                    break;
            }
        }
    };

    private void LoadRealTimeDataToView() {
        tvLocation.setText(Const.district);
        tvWeather.setText(weatherRealTimeEntity.getResults().get(0).getNow().getText());
        tvTemp.setText(weatherRealTimeEntity.getResults().get(0).getNow().getTemperature()+"°");
        if (!weatherRealTimeEntity.getResults().get(0).getNow().getVisibility().isEmpty()){
            tvVisibility.setText(weatherRealTimeEntity.getResults().get(0).getNow().getVisibility()+"公里");
        }
        tvFeelTemp.setText(weatherRealTimeEntity.getResults().get(0).getNow().getFeels_like()+"°");
        tvPa.setText(weatherRealTimeEntity.getResults().get(0).getNow().getPressure()+"百帕");
        tvRainfallProbability.setText(weatherRealTimeEntity.getResults().get(0).getNow().getClouds()+"%");
        tvHumidity.setText(weatherRealTimeEntity.getResults().get(0).getNow().getHumidity()+"%");
        tvWindSpeed.setText(weatherRealTimeEntity.getResults().get(0).getNow().getWind_scale()+"级");
    }

    private void LoadDataToView() {
        if(weatherAdapter == null){
            weatherAdapter = new WeatherAdapter(context,weatherListEntity.getResults().get(0).getDaily());
            listWeatherDay.setAdapter(weatherAdapter);
        }else {
            weatherAdapter.notifyDataSetChanged();
        }
//        tvLocation.setText(weatherListEntity.getResults().get(0).getLocation().getName());
//        tvWeather.setText(weatherListEntity.getResults().get(0).getLocation().getName());
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        context = this;
        context.setTheme(R.style.AppTheme);
        dialog = GetProgressDialog.getProgressDialog(context);
        initView();
        getRealTimeWeatherData();
        getServiceWeatherData();
        getAitQuilty();
    }

    private void initView() {
        title.setText("天气");
        imgLeft.setVisibility(View.VISIBLE);
        listWeatherDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    private void getServiceWeatherData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        String getUrl = Const.UrlXinZhiWeatherDay + "&location="+ Const.Latitude+":"+Const.Longitude + "&language=zh-Hans&unit=c&start=0&days=3";
        OkHttp.getAsync(getUrl, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                Gson gson = new Gson();
                weatherListEntity = gson.fromJson(result, WeatherListEntity.class);
                if (!weatherListEntity.getResults().isEmpty()) {
                    MyUtil.sendHandleMsg(1, "数据获取成功", handler);

                }
                Log.e("result", result);
            }
        });
    }
    private void getRealTimeWeatherData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        String getUrl = Const.UrlXinZhiWeatherRealTime + "location="+ Const.Latitude + ":" + Const.Longitude +"&language=zh-Hans&unit=c";
        OkHttp.getAsync(getUrl, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                Gson gson = new Gson();
                weatherRealTimeEntity = gson.fromJson(result, WeatherRealTimeEntity.class);
                if (!weatherRealTimeEntity.getResults().isEmpty()) {
                    MyUtil.sendHandleMsg(4, "数据获取成功", handler);

                }
                Log.e("result", result);
            }
        });
    }
    private void getAitQuilty() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        String getUrl = Const.UrlXinZhiAirQuilty + "location="+ Const.Latitude + ":" + Const.Longitude +"&language=zh-Hans&scope=city";
        OkHttp.getAsync(getUrl, new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                Gson gson = new Gson();
                weatherAirQuiltyEntity = gson.fromJson(result, WeatherAirQuiltyEntity.class);
                if (!weatherAirQuiltyEntity.getResults().isEmpty()) {
                    MyUtil.sendHandleMsg(5, "数据获取成功", handler);

                }
                Log.e("result", result);
            }
        });
    }

    @OnClick({R.id.img_left, R.id.tv_left, R.id.ll_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_left:
            case R.id.tv_left:
            case R.id.ll_left:
                finish();
                break;
        }
    }
}
