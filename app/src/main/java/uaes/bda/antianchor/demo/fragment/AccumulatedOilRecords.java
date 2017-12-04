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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import java.io.IOException;
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
import uaes.bda.antianchor.demo.entity.SingleRefuelingRecordEntity;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MPChartHelper;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 10/18/2017.
 * 累计用油记录
 */

public class AccumulatedOilRecords extends Fragment implements OnChartValueSelectedListener {
    Unbinder mUnbinder ;
    @BindView(R.id.mLineChar)
    LineChart mLineChar;
    private Context context;
    private View view;
    private LineDataSet set;
    private ProgressDialog dialog = null;
    private SingleRefuelingRecordEntity singleRefuelingRecordEntity;
    private List<String> xAxisValues;
    private List<Float> yAxisValues;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                case 1:
                    //  IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mBarChart);
                    //   XYMarkerView mv = new XYMarkerView(context, xAxisFormatter);

//                    initdata();
                    break;
                case 44:
                    //    changeData();
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
                    initLineChartData();
                    break;
            }
        }
    };

    private void initLineChartData() {
        xAxisValues = new ArrayList<>();
        yAxisValues = new ArrayList<>();
        for (int i = 0; i < singleRefuelingRecordEntity.getData().size(); i++) {
            xAxisValues.add(i, singleRefuelingRecordEntity.getData().get(i).getMilesages() + "km");
            yAxisValues.add(i, Float.parseFloat(singleRefuelingRecordEntity.getData().get(i).getFuelComsume()));
        }
        MPChartHelper.setLineChart(mLineChar, xAxisValues, yAxisValues, "累计用油记录（升）", true, singleRefuelingRecordEntity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_accumulated_oil_record, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        dialog = GetProgressDialog.getProgressDialog(context);
        initView();
        return view;
    }

    private void initView() {

        getServiceFuelData();

    }

    private void getServiceFuelData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {//判断是否有网络
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
                Log.e("333", "requestSuccess: " + result);
                JSONObject jsonData = JSON.parseObject(result);
                if (jsonData.getString("status").equals("1")) {
//                    JSONObject data = JSON.parseObject(jsonData.getString("data"));
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


    /**
     * Called when a value has been selected inside the chart.
     *
     * @param e The selected Entry
     * @param h The corresponding highlight object that contains information
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    /**
     * Called when nothing has been selected or an "un-select" has been made.
     */
    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
