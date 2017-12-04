package uaes.bda.antianchor.demo.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.TabMainActivity;
import uaes.bda.antianchor.demo.activity.BatteryHelperActivity;
import uaes.bda.antianchor.demo.activity.OilHelperActivity;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.customsizeview.DashboardViewHome;
import uaes.bda.antianchor.demo.entity.BatteryStatusEntity;
import uaes.bda.antianchor.demo.entity.HomeStatusEntity;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 4/19/2017.
 */

public class HomeFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.dashboard_oil)
    DashboardViewHome dashboardOil;
    @BindView(R.id.imageView7)
    ImageView imageView7;
    @BindView(R.id.tv_remaining_mileage)
    TextView tvRemainingMileage;
    @BindView(R.id.img_home_battery_status)
    ImageView imgHomeBatteryStatus;
    @BindView(R.id.tv_home_battery_status)
    TextView tvHomeBatteryStatus;
    @BindView(R.id.tv_home_health_status)
    TextView tvHomeHealthStatus;
    @BindView(R.id.tv_home_maintenance_factor)
    TextView tvHomeMaintenanceFactor;
    @BindView(R.id.ll_battery_status)
    LinearLayout llBatteryStatus;
    @BindView(R.id.ll_maintenance_factor)
    LinearLayout llMaintenanceFactor;
    @BindView(R.id.ll_car_status)
    LinearLayout llCarStatus;
    private Context context;
    private View view;
    private TabMainActivity parentActivity;
    private int IMGHEIGHT = 90, IMGWEIGHT = 100;
    private ProgressDialog dialog = null;
    private HomeStatusEntity homeStatusEntity;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                case 1:
                    break;
                case 2:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                        .show();
                    break;
                case 3:
                    FinalToast.netTimeOutMakeText(context);
                    break;
                case 4:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    LoadDataToView();
                    break;
            }
        }
    };

    private void LoadDataToView() {
        dashboardOil.setRealTimeValue((int) (Float.parseFloat(homeStatusEntity.getData().getRemainFuelPer()) * 100));
        tvRemainingMileage.setText(homeStatusEntity.getData().getRemainMileage() + "km");
        if (homeStatusEntity.getData().getBatteryStatus().equals("Normal")) {
            tvHomeBatteryStatus.setText("电量充足");
            imgHomeBatteryStatus.setImageResource(R.mipmap.current_battery_status);
        } else if (homeStatusEntity.getData().getBatteryStatus().equals("Low")) {
            tvHomeBatteryStatus.setText("电量不足");
            imgHomeBatteryStatus.setImageResource(R.mipmap.battery_low_status);
        }
        if (homeStatusEntity.getData().getHealth().equals("2")) {
            tvHomeHealthStatus.setText("良好");
        } else if (homeStatusEntity.getData().getHealth().equals("1")) {
            tvHomeHealthStatus.setText("一般");
        }
        tvHomeMaintenanceFactor.setText(homeStatusEntity.getData().getMtCoef());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        parentActivity = (TabMainActivity) getActivity();
        view = inflater.inflate(R.layout.activity_home_fm, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        dialog = GetProgressDialog.getProgressDialog(context);
        dashboardOil.setRealTimeValue((int) (Float.parseFloat("0.4") * 100));
//        getServiceHomeData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getServiceHomeData();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {

        } else {
        }
    }
    private void getServiceHomeData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlHomeStatus, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                Gson gson = new Gson();
                homeStatusEntity = gson.fromJson(result, HomeStatusEntity.class);
                if (homeStatusEntity.getStatus().equals("1")) {
                    MyUtil.sendHandleMsg(4, "数据获取成功", handler);
                }
                Log.e("result", result);
            }
        });
    }

    private Map<String, String> addParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("vin", "111111");
        return params;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.dashboard_oil, R.id.ll_battery_status, R.id.ll_maintenance_factor, R.id.ll_car_status})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.dashboard_oil:
                intent.setClass(getActivity(), OilHelperActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_battery_status:
                intent.setClass(getActivity(), BatteryHelperActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_maintenance_factor:
                parentActivity.onClick(parentActivity.findViewById(R.id.ll_radio_user));
                break;
            case R.id.ll_car_status:
                parentActivity.onClick(parentActivity.findViewById(R.id.ll_radio_help));
                break;
        }
    }
}
