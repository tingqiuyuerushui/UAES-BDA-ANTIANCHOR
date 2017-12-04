package uaes.bda.antianchor.demo.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.customsizeview.DashboardView;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 10/18/2017.
 * 用油助手
 */

public class UseOilHelperFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.tv_quality)
    TextView tvQuality;
    @BindView(R.id.ll_text_view)
    LinearLayout llTextView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    @BindView(R.id.dashboard_view)
    DashboardView dashboardView;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.tv_surplus)
    TextView tvSurplus;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.tv_oil_band)
    TextView tvOilBand;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.tv_mileage)
    TextView tvMileage;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.tv_cost_avg)
    TextView tvCostAvg;
    private ProgressDialog dialog = null;
    private Context context;
    private View view;
    private String ecoRanking;
    private String remainFuelPer;
    private String fuelConsumerPer100KM;
    private String fuelType;
    private String fuleQuality;
    private String remainMileageMin;
    private String remainMileageMax;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                // 处理UI更新等操作
                case 1:
                    setDataToView();
                    break;
                case 2:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 3:
                    FinalToast.netTimeOutMakeText(context);
                    break;
            }
        }
    };

    private void setDataToView() {
        if (fuleQuality.equals("2")) {
            fuleQuality = "良";
        }
        if (fuleQuality.equals("1")) {
            fuleQuality = "优";
        }
        if (fuleQuality.equals("3")) {
            fuleQuality = "中";
        }
        tvOilBand.setText(fuleQuality);
        tvRank.setText("打败了"+ecoRanking + "%" + "对手");
        tvMileage.setText(remainMileageMin + "-" + remainMileageMax + "km");
        tvQuality.setText(fuleQuality);
        tvSurplus.setText(Float.parseFloat(remainFuelPer)*100 + "%");
        tvCostAvg.setText(fuelConsumerPer100KM + "L");
        dashboardView.setRealTimeValue((int)(Float.parseFloat(remainFuelPer)*100));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_useoil_help, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        context = getActivity();
        initView();
        getServiceData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        getServiceData();
    }

    private void getServiceData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlGetOilHelper, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                JSONObject jsonData = JSON.parseObject(result);
                if (jsonData.getString("status").equals("1")) {
                    JSONObject data = JSON.parseObject(jsonData.getString("data"));
                    ecoRanking = data.getString("ecoRanking");
                    remainFuelPer = data.getString("remainFuelPer");
                    fuelConsumerPer100KM = data.getString("fuelConsumerPer100KM");
                    fuelType = data.getString("fuelType");
                    fuleQuality = data.getString("fuleQuality");
                    remainMileageMin = data.getString("remainMileageMin");
                    remainMileageMax = data.getString("remainMileageMax");
                    MyUtil.sendHandleMsg(1, "数据获取成功", handler);

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

    private void initView() {
        dialog = GetProgressDialog.getProgressDialog(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
