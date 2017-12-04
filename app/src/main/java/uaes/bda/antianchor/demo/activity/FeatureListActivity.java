package uaes.bda.antianchor.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uaes.bda.antianchor.demo.R;

/**
 * Created by lun.zhang on 11/22/2017.
 */

public class FeatureListActivity extends Activity {

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
    @BindView(R.id.tv_do_note_money)
    TextView tvDoNoteMoney;
    @BindView(R.id.tv_fuel_helper)
    TextView tvFuelHelper;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.tv_battery_helper)
    TextView tvBatteryHelper;
    @BindView(R.id.tv_sparking)
    TextView tvSparking;
    @BindView(R.id.tv_engine_oil)
    TextView tvEngineOil;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_driving_track)
    TextView tvDrivingTrack;
    @BindView(R.id.tv_more)
    TextView tvMore;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature_list);
        context = this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        imgLeft.setVisibility(View.VISIBLE);
        title.setText("功能列表");
    }

    @OnClick({R.id.img_left, R.id.tv_left, R.id.ll_left, R.id.tv_do_note_money, R.id.tv_fuel_helper, R.id.tv_setting, R.id.tv_battery_helper, R.id.tv_sparking, R.id.tv_engine_oil, R.id.tv_weather, R.id.tv_driving_track, R.id.tv_more})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.img_left:
            case R.id.tv_left:
            case R.id.ll_left:
                finish();
                break;
            case R.id.tv_do_note_money:
                intent.setClass(context, DoNoteActivity.class);
                context.startActivity(intent);
                break;
            case R.id.tv_fuel_helper:
                intent.setClass(context, OilHelperActivity.class);
                context.startActivity(intent);
                break;
            case R.id.tv_setting:
                intent.setClass(context, GaoDeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_battery_helper:
                intent.setClass(context, BatteryHelperActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sparking:
                intent.setClass(context, SparkingPlugsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_engine_oil:
                intent.setClass(context, OilStatusActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_driving_track:
                intent.setClass(context, TravelingTraceActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_weather:
                intent.setClass(context, WeatherActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more:
                break;
        }
    }
}
