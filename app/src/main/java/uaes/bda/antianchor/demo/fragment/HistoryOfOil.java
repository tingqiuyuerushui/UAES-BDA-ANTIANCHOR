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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;
import uaes.bda.antianchor.demo.utils.TimePickerDialog;

/**
 * Created by lun.zhang on 10/18/2017.
 * 用油历史
 */

public class HistoryOfOil extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.tv_data_start_end)
    TextView tvDataStartEnd;
    @BindView(R.id.tv_total_fuel_consumption)
    TextView tvTotalFuelConsumption;
    @BindView(R.id.tv_total_cost)
    TextView tvTotalCost;
    @BindView(R.id.tv_driving_fuel_consumption)
    TextView tvDrivingFuelConsumption;
    @BindView(R.id.tv_idle_fuel_consumption)
    TextView tvIdleFuelConsumption;
    @BindView(R.id.tv_air_consumption)
    TextView tvAirConsumption;
    @BindView(R.id.tv_other_consumption)
    TextView tvOtherConsumption;
    @BindView(R.id.ll_search_result)
    LinearLayout llSearchResult;
    @BindView(R.id.tv_date_distance)
    TextView tvDateDistance;
    @BindView(R.id.et_first_date)
    EditText etFirstDate;
    @BindView(R.id.et_end_date)
    EditText etEndDate;
    @BindView(R.id.btn_search_from_date)
    Button btnSearchFromDate;
    @BindView(R.id.ll_by_date_search)
    LinearLayout llByDateSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.ll_by_distance_search)
    LinearLayout llByDistanceSearch;
    @BindView(R.id.tv_date_miles)
    TextView tvDateMiles;
    private Context context;
    private View view;
    private TimePickerDialog mTimePickerDialog;
    private String startDate;
    private String endDate;
    private String sumConsumerFuel;
    private String sumSpend;
    private String travelConsumption;
    private String idleConsumption;
    private String fanConsumption;
    private String otherConsumption;
    private ProgressDialog dialog;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                // 处理UI更新等操作
                case 1:
                    setDataToDateView();
                    break;
                case 2:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 3:
                    FinalToast.netTimeOutMakeText(context);
                    break;
                case 4:
                    setDataToMilesView();
                    break;
            }
        }
    };

    private void setDataToMilesView() {
        if (llSearchResult.getVisibility() == View.GONE)
            llSearchResult.setVisibility(View.VISIBLE);
        tvDateMiles.setText("起止里程：");
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        tvDataStartEnd.setText(etFirstDistance.getText().toString() + "百公里至" + etEndDistance.getText().toString()+"百公里");
        tvTotalFuelConsumption.setText(decimalFormat.format(Double.parseDouble(sumConsumerFuel)) + "升");
        tvTotalCost.setText(decimalFormat.format(Double.parseDouble(sumSpend)) + "元");
        tvDrivingFuelConsumption.setText(decimalFormat.format(Double.parseDouble(travelConsumption)) + "升");
        tvIdleFuelConsumption.setText(decimalFormat.format(Double.parseDouble(idleConsumption)) + "升");
        tvAirConsumption.setText(decimalFormat.format(Double.parseDouble(fanConsumption)) + "升");
        tvOtherConsumption.setText(decimalFormat.format(Double.parseDouble(otherConsumption)) + "升");
    }

    private EditText etFirstDistance;//距离
    private EditText etEndDistance;
    private EditText etFirstData;//日期
    private EditText etEndData;

    private void setDataToDateView() {
        tvDateMiles.setText("起止日期：");
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        if (llSearchResult.getVisibility() == View.GONE)
            llSearchResult.setVisibility(View.VISIBLE);
        tvDataStartEnd.setText(etFirstData.getText().toString() + "至" + etEndData.getText().toString());
        tvTotalFuelConsumption.setText(decimalFormat.format(Double.parseDouble(sumConsumerFuel)) + "升");
        tvTotalCost.setText(decimalFormat.format(Double.parseDouble(sumSpend)) + "元");
        tvDrivingFuelConsumption.setText(decimalFormat.format(Double.parseDouble(travelConsumption)) + "升");
        tvIdleFuelConsumption.setText(decimalFormat.format(Double.parseDouble(idleConsumption)) + "升");
        tvAirConsumption.setText(decimalFormat.format(Double.parseDouble(fanConsumption)) + "升");
        tvOtherConsumption.setText(decimalFormat.format(Double.parseDouble(otherConsumption)) + "升");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_history_of_oil, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        dialog = GetProgressDialog.getProgressDialog(context);
        initView();
        return view;
    }

    private void initView() {
        etFirstData = view.findViewById(R.id.et_first_date);
        etEndData = view.findViewById(R.id.et_end_date);
        etFirstDistance = view.findViewById(R.id.et_first_distance);
        etEndDistance = view.findViewById(R.id.et_end_distance);
        llByDistanceSearch.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private void getServiceData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlPostFuelHistoryByDate, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
//                Gson gson = new Gson();
//                FuelHistoryEntry fuelHistoryEntry = gson.fromJson(result,FuelHistoryEntry.class);
//                FuelHistoryEntry fuelHistoryEntry = JSON.parseObject(result,FuelHistoryEntry.class);
                Log.e("444", "result: " + result);
                JSONObject jsonData = JSON.parseObject(result);
                if (jsonData.getString("status").equals("1")) {
                    JSONObject data = JSON.parseObject(jsonData.getString("data"));
                    endDate = data.getString("endDate");
                    startDate = data.getString("startDate");
                    sumConsumerFuel = data.getString("sumConsumerFuel");
                    sumSpend = data.getString("sumCost");
                    travelConsumption = data.getString("travelConsume");
                    idleConsumption = data.getString("idleConsume");
                    fanConsumption = data.getString("fanConsume");
                    otherConsumption = data.getString("otherConsume");
                    MyUtil.sendHandleMsg(1, "数据获取成功", handler);

                }
                Log.e("result", result);
            }
        });
    }

    private Map<String, String> addParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("vin", "111111");
        if (!etFirstData.getText().toString().isEmpty()) {
            params.put("startDate", etFirstData.getText().toString());
        }
        if (!etEndData.getText().toString().isEmpty()) {
            params.put("endDate", etEndData.getText().toString());
        }
        return params;
    }

    @OnClick({R.id.tv_date_distance, R.id.et_first_date, R.id.et_end_date, R.id.btn_search, R.id.btn_search_from_date})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date_distance:
                if (llByDistanceSearch.getVisibility() == View.VISIBLE) {
                    llByDistanceSearch.setVisibility(View.GONE);
                    llByDateSearch.setVisibility(View.VISIBLE);
                    tvDateDistance.setText("里程");
                } else {
                    llByDistanceSearch.setVisibility(View.VISIBLE);
                    llByDateSearch.setVisibility(View.GONE);
                    tvDateDistance.setText("日期");
                }
                break;
            case R.id.et_first_date:
                mTimePickerDialog = new TimePickerDialog(context, etFirstData);
                mTimePickerDialog.showDatePickerDialog();
                break;
            case R.id.et_end_date:
                mTimePickerDialog = new TimePickerDialog(context, etEndData);
                mTimePickerDialog.showDatePickerDialog();
                break;
            case R.id.btn_search:
                getServiceDatatwo();
                break;
            case R.id.btn_search_from_date:
                getServiceData();

                break;
        }
    }

    private void getServiceDatatwo() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {//判断网络是否可用
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlPostFuelHistoryByMile, addParamss(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                Log.e("555", "result: " + result);
                JSONObject jsonData = JSON.parseObject(result);
                if (jsonData.getString("status").equals("1")) {
                    JSONObject data = JSON.parseObject(jsonData.getString("data"));
                    endDate = data.getString("endDate");
                    startDate = data.getString("startDate");
                    sumConsumerFuel = data.getString("sumConsumerFuel");
                    sumSpend = data.getString("sumCost");
                    travelConsumption = data.getString("travelConsume");
                    idleConsumption = data.getString("idleConsume");
                    fanConsumption = data.getString("fanConsume");
                    otherConsumption = data.getString("otherConsume");
                    MyUtil.sendHandleMsg(4, "数据获取成功", handler);

                }
                Log.e("555", result);
            }
        });

    }

    private Map<String, String> addParamss() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("vin", "111111");
        if (!etFirstDistance.getText().toString().isEmpty()) {
            params.put("startMile", etFirstDistance.getText().toString());
        }
        if (!etEndDistance.getText().toString().isEmpty()) {
            params.put("endMile", etEndDistance.getText().toString());
        }
        return params;


    }

}
