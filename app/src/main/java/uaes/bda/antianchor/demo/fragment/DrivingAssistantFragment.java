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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.activity.BatteryHelperActivity;
import uaes.bda.antianchor.demo.activity.OilHelperActivity;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 4/23/2017.
 */

public class DrivingAssistantFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.imageView5)
    ImageView imageView5;
    @BindView(R.id.re_oil_helper)
    RelativeLayout reOilHelper;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.imageView6)
    ImageView imageView6;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.re_battery_helper)
    RelativeLayout reBatteryHelper;
    private Context context;
    private View view;
    private String ecoRanking;
    private String remainFuelPer;
    private String fuelConsumerPer100KM;
    private String fuelType;
    private String fuleQuality;
    private String remainMileageMin;
    private String remainMileageMax;
    private ProgressDialog dialog;
    public static int currentPosition = -1;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                // 处理UI更新等操作
                case 1:
                    setAdapter();
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

    private void setAdapter() {
//        if (listEntity.size() > 0) {
//            listEntity.clear();
//        }
//        itemEntity = new DrivingAssistantItemEntity(ecoRanking, remainFuelPer, fuelConsumerPer100KM, fuelType, fuleQuality, remainMileageMin, remainMileageMax);
//        entity = new DrivingAssistantEntity(R.mipmap.fuel_help, "加油助手", itemEntity);
//        listEntity.add(entity);
//        if (adapter == null) {
//            adapter = new DrivingAssistantAdapter(context, listEntity);
//            lvDriving.setAdapter(adapter);
//        } else {
//            adapter.notifyDataSetChanged();
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_driving_assistant_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
//        getServiceData();
//        getServiceFuelData();
//        initListView();
//        setListViewHeightBasedOnChildren(lvDriving);
        return view;
    }

    private void initView() {
        dialog = GetProgressDialog.getProgressDialog(context);
//        lvDriving.setFocusable(false);
//        lvDriving.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                currentPosition = position;
//                adapter.notifyDataSetChanged();
//            }
//        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        getServiceData();
    }

    private void getServiceData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(7);
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
                JSONObject jsonData = JSON.parseObject(result);
                if (jsonData.getString("status").equals("1")) {
//                    JSONObject data = JSON.parseObject(jsonData.getString("data"));
                    MyUtil.sendHandleMsg(2, "数据获取成功", handler);

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
        ;
    }

    @OnClick({R.id.re_oil_helper, R.id.re_battery_helper})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.re_oil_helper:
                intent.setClass(context, OilHelperActivity.class);
                context.startActivity(intent);
                break;
            case R.id.re_battery_helper:
                intent.setClass(context, BatteryHelperActivity.class);
                startActivity(intent);
                break;
        }
    }
}
