package uaes.bda.antianchor.demo.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.customsizeview.CustomPopupWindow;
import uaes.bda.antianchor.demo.customsizeview.XYMarkerView;
import uaes.bda.antianchor.demo.entity.SingleRefuelingRecordEntity;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MPChartHelper;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 10/18/2017.
 * 单次加油记录
 */

public class SingleRefuelingRecord extends Fragment implements OnChartValueSelectedListener {
    Unbinder mUnbinder;
    @BindView(R.id.tv_oil_date)
    TextView tvOilDate;
    @BindView(R.id.tv_oil_location)
    TextView tvOilLocation;
    @BindView(R.id.tv_actual_oil)
    TextView tvActualOil;
    @BindView(R.id.tv_shout_oil)
    TextView tvShoutOil;
    @BindView(R.id.tv_oil_money)
    TextView tvOilMoney;
    @BindView(R.id.tv_actual_oil_money)
    TextView tvActualOilMoney;
    @BindView(R.id.tv_oil_consumption)
    TextView tvOilConsumption;
    @BindView(R.id.tv_driven_distance)
    TextView tvDrivenDistance;
    @BindView(R.id.tv_average_fuel_consumption)
    TextView tvAverageFuelConsumption;
    @BindView(R.id.tv_average_speed)
    TextView tvAverageSpeed;
    @BindView(R.id.tv_economic_ranking)
    TextView tvEconomicRanking;
    @BindView(R.id.tv_gasoline_quality)
    TextView tvGasolineQuality;
    @BindView(R.id.tv_fuel_total_times)
    TextView tvFuelTotalTimes;
    @BindView(R.id.tv_fuel_total_volume)
    TextView tvFuelTotalVolume;
    @BindView(R.id.mBarChart)
    BarChart mBarChart;
    private Context context;
    private View view;
    private ProgressDialog dialog = null;
    private XYMarkerView mv;
    private List<String> xAxisValues;
    private List<Float> yAxisValues;
    private SingleRefuelingRecordEntity singleRefuelingRecordEntity;
    private CustomPopupWindow popupWindow = null;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                case 1:
                    Entry e = (Entry) msg.obj;
                    int i = (int) (e.getX());
                    setDataToViewBySelectBar(i);
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
                    initmBarChartData();
                    break;
            }
        }
    };

    private void initmBarChartData() {
        xAxisValues = new ArrayList<>();
        yAxisValues = new ArrayList<>();
        for (int i = 0; i < singleRefuelingRecordEntity.getData().size(); i++) {
            xAxisValues.add(i, singleRefuelingRecordEntity.getData().get(i).getFuelFillingDate());
            if (singleRefuelingRecordEntity.getData().get(i).getFuelFillingVolume().isEmpty()) {
                singleRefuelingRecordEntity.getData().get(i).setFuelFillingVolume("0");
            }
            yAxisValues.add(i, Float.parseFloat(singleRefuelingRecordEntity.getData().get(i).getFuelFillingVolume()));
        }
        tvFuelTotalTimes.setText("加油总次数：" + singleRefuelingRecordEntity.getData().size());
        float tempVolme = 0;
        for (int i = 0; i < singleRefuelingRecordEntity.getData().size(); i++) {
            tempVolme = tempVolme + Float.parseFloat(singleRefuelingRecordEntity.getData().get(i).getFuelFillingVolume());
        }
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        tvFuelTotalVolume.setText("总加油量：" + decimalFormat.format(tempVolme)+"L");
        MPChartHelper.setBarChart(mBarChart, xAxisValues, yAxisValues, "加油记录（升）", 12, null);
//        setData2(yVals1);

    }

    private void setDataToViewBySelectBar(int index) {
        if (popupWindow == null || !popupWindow.isShowing()) {
            popupWindow = new CustomPopupWindow.Builder(context)
                    .setContentView(R.layout.popwindow_fuel_record_detail_layout)
                    .setwidth(LinearLayout.LayoutParams.MATCH_PARENT)
                    .setheight(LinearLayout.LayoutParams.WRAP_CONTENT)
                    .setFouse(true)
                    .setOutSideCancel(true)
                    .setAnimationStyle(R.style.MyPopupWindow_anim_style)
                    .builder()
                    .showAsLaction(R.layout.fragment_single_refueling_record, Gravity.CENTER, 30, 30);
            ((TextView) popupWindow.getItemView(R.id.tv_oil_date)).setText(singleRefuelingRecordEntity.getData().get(index).getFuelFillingDate());
            ((TextView) popupWindow.getItemView(R.id.tv_oil_location)).setText(singleRefuelingRecordEntity.getData().get(index).getStationAddress());
            ((TextView) popupWindow.getItemView(R.id.tv_actual_oil)).setText(singleRefuelingRecordEntity.getData().get(index).getFuelFillingVolume() + "L");
            ((TextView) popupWindow.getItemView(R.id.tv_shout_oil)).setText(singleRefuelingRecordEntity.getData().get(index).getFuelFillingVolume() + "L");
            ((TextView) popupWindow.getItemView(R.id.tv_oil_money)).setText(singleRefuelingRecordEntity.getData().get(index).getFuelFillingCost() + "元");
            ((TextView) popupWindow.getItemView(R.id.tv_actual_oil_money)).setText(singleRefuelingRecordEntity.getData().get(index).getFuelPrice() + "元");
            ((TextView) popupWindow.getItemView(R.id.tv_oil_consumption)).setText(singleRefuelingRecordEntity.getData().get(index).getFuelFillingVolume() + "L");
            ((TextView) popupWindow.getItemView(R.id.tv_driven_distance)).setText(singleRefuelingRecordEntity.getData().get(index).getMiles() + "km");
            ((TextView) popupWindow.getItemView(R.id.tv_average_fuel_consumption)).setText(singleRefuelingRecordEntity.getData().get(index).getAverageConsume()+"L/百公里");
            ((TextView) popupWindow.getItemView(R.id.tv_average_speed)).setText(singleRefuelingRecordEntity.getData().get(index).getAverageSpeed()+"km/小时");
            ((TextView) popupWindow.getItemView(R.id.tv_economic_ranking)).setText(singleRefuelingRecordEntity.getData().get(index).getEcoRank());
            if(singleRefuelingRecordEntity.getData().get(index).getFuelQuality().equals("1")){
                ((TextView) popupWindow.getItemView(R.id.tv_gasoline_quality)).setText("良");
            }else if(singleRefuelingRecordEntity.getData().get(index).getFuelQuality().equals("0")){
                ((TextView) popupWindow.getItemView(R.id.tv_gasoline_quality)).setText("优");
            }
        } else {
            popupWindow.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_single_refueling_record, container, false);
        mUnbinder  = ButterKnife.bind(this, view);
//        initmBarChart();
        dialog = GetProgressDialog.getProgressDialog(context);
        mBarChart.setOnChartValueSelectedListener(this);
        getServiceFuelData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        getServiceFuelData();
    }

    private void getServiceFuelData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlPostFuelRecordList, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                Gson gson = new Gson();
                singleRefuelingRecordEntity = gson.fromJson(result, SingleRefuelingRecordEntity.class);
                if (singleRefuelingRecordEntity.getStatus().equals("1")) {
                    if (singleRefuelingRecordEntity.getData().size() <= 0) {
                        MyUtil.sendHandleMsg(2, "没有数据", handler);
                    } else {
                        MyUtil.sendHandleMsg(4, "数据获取成功", handler);
                    }

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

    /**
     * Called when a value has been selected inside the chart.
     *
     * @param e The selected Entry
     * @param h The corresponding highlight object that contains information
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        MyUtil.sendHandleMsg(1, e, handler);
    }

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    @Override
    public void onNothingSelected() {

    }
}
