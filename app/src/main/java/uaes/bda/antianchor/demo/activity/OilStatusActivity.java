package uaes.bda.antianchor.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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
import butterknife.Unbinder;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.customsizeview.DashboardView4;
import uaes.bda.antianchor.demo.entity.EngineOilStatusEntity;
import uaes.bda.antianchor.demo.entity.EngineOilWebSocketEntity;
import uaes.bda.antianchor.demo.service.EngineOilWebSocketService;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 11/6/2017.
 */

public class OilStatusActivity extends Activity {
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
    @BindView(R.id.tv_oil_volume)
    TextView tvOilVolume;
    @BindView(R.id.tv_normal_status)
    TextView tvNormalStatus;
    @BindView(R.id.tv_oil_status)
    TextView tvOilStatus;
    @BindView(R.id.tv_change_oil)
    TextView tvChangeOil;
    @BindView(R.id.dashboard_oil)
    DashboardView4 dashboardOil;
    @BindView(R.id.img_engineer_oil_status)
    ImageView imgEngineerOilStatus;
    private Context context;
    private Unbinder unBinder;
    private WebSocketConnection mConnect;
    private ProgressDialog dialog = null;
    private EngineOilStatusEntity engineOilStatusEntity = null;
    private EngineOilWebSocketEntity engineOilWebSocketEntity = null;
    private final int SERVICE = 0;
    private final int WEBSOCKET = 1;
    public static final String ACTION_UPDATEUI = "engine.oil.action.updateUI";
    UpdateUIBroadcastReceiver broadcastReceiver;
    private String strReciveService;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                case 1:
                    dealHealthStatusView(msg.obj.toString(),WEBSOCKET);
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
        dashboardOil.setVelocity((int)(Float.parseFloat(engineOilStatusEntity.getData().getEngineOilHealthRete())*100));
        dealHealthStatusView(engineOilStatusEntity.getData().getEngineOilHealthStatus(),WEBSOCKET);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_status);
        context = this;
        unBinder = ButterKnife.bind(this);
        //  initView();
        mConnect = new WebSocketConnection();
//        connectWebSocket(mConnect);
        initView();
        dialog = GetProgressDialog.getProgressDialog(context);
        getServiceEngineOilData();
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
        Intent intent = new Intent(this, EngineOilWebSocketService.class);
        startService(intent);
    }

    private void initView() {
        imgLeft.setVisibility(View.VISIBLE);
        title.setText("机油详情");
    }

    private void getServiceEngineOilData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlEngineOilStatus, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                Gson gson = new Gson();
                engineOilStatusEntity = gson.fromJson(result, EngineOilStatusEntity.class);
                if (engineOilStatusEntity.getStatus().equals("1")) {
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

    private void connectWebSocket(WebSocketConnection webSocketConnection) {

        try {
            webSocketConnection.connect(Const.UrlWebSocketGetOilStatus, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    super.onOpen();
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
                    Log.e("WebSocketMsg:", payload);
                    dealHealthStatusView(payload,WEBSOCKET);
                }
            });

        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
    /**
     * 定义广播接收器（内部类）
     *
     * @author lenovo
     *
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            strReciveService = String.valueOf(intent.getExtras().getString("payload"));
            MyUtil.sendHandleMsg(1,strReciveService, handler);
        }

    }
    private void dealHealthStatusView(String status,int SeOrWs){
        switch(SeOrWs){
            case SERVICE:
                if(status.equals("health")){
                    imgEngineerOilStatus.setImageResource(R.mipmap.img_engine_oil_health);
                    if (tvOilVolume.getVisibility() == View.GONE){
                        tvOilVolume.setVisibility(View.VISIBLE);
                    }
                    tvOilVolume.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                    tvOilVolume.setTextColor(getResources().getColor(R.color.color_green));
                    tvOilVolume.setText("机油健康");
                    tvNormalStatus.setTextSize(getResources().getDimension(R.dimen.text_enginer_oil_size_normal));
                    tvNormalStatus.setTextColor(getResources().getColor(R.color.white));
                    tvNormalStatus.setText("剩余机油可行驶里程：");
                    tvOilStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                    tvOilStatus.setTextColor(getResources().getColor(R.color.color_green));
                    tvOilStatus.setText(engineOilStatusEntity.getData().getEngineOilRemainMile());

                }else{
                    imgEngineerOilStatus.setImageResource(R.mipmap.img_engine_oil_problem);
                    tvOilVolume.setVisibility(View.GONE);
                    tvNormalStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                    tvNormalStatus.setTextColor(getResources().getColor(R.color.color_yellow));
                    tvNormalStatus.setText("机油不足");
                    tvOilStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                    tvOilStatus.setTextColor(getResources().getColor(R.color.color_yellow));
                    tvOilStatus.setText("建议尽快更换");
                }
                break;
            case WEBSOCKET:
                Gson gson = new Gson();
                if(status.contains("engineOilHealthRete")){
                    engineOilWebSocketEntity = gson.fromJson(status,EngineOilWebSocketEntity.class);
                    dashboardOil.setVelocity((int)(Float.parseFloat(engineOilWebSocketEntity.getEngineOilHealthRete())*100));

                    if (engineOilWebSocketEntity.getStatus().equals("ASAP")) {
                        imgEngineerOilStatus.setImageResource(R.mipmap.img_engine_oil_problem);
                        tvOilVolume.setVisibility(View.GONE);
                        tvNormalStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                        tvNormalStatus.setTextColor(getResources().getColor(R.color.color_yellow));
                        tvNormalStatus.setText("机油不足");
                        tvOilStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                        tvOilStatus.setTextColor(getResources().getColor(R.color.color_yellow));
                        tvOilStatus.setText("建议尽快更换");
                    }else if(engineOilWebSocketEntity.getStatus().equals("IMME")){
                        imgEngineerOilStatus.setImageResource(R.mipmap.img_engine_oil_problem);
                        tvOilVolume.setVisibility(View.GONE);
                        tvNormalStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                        tvNormalStatus.setTextColor(getResources().getColor(R.color.color_yellow));
                        tvNormalStatus.setText("机油不足");
                        tvOilStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                        tvOilStatus.setTextColor(getResources().getColor(R.color.color_yellow));
                        tvOilStatus.setText("建议立即更换");
                    }else{
                        imgEngineerOilStatus.setImageResource(R.mipmap.img_engine_oil_health);
                        if (tvOilVolume.getVisibility() == View.GONE){
                            tvOilVolume.setVisibility(View.VISIBLE);
                        }
                        tvOilVolume.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                        tvOilVolume.setTextColor(getResources().getColor(R.color.color_green));
                        tvOilVolume.setText("机油健康");
                        tvNormalStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size_normal));
                        tvNormalStatus.setTextColor(getResources().getColor(R.color.white));
                        tvNormalStatus.setText("剩余机油可行驶里程：");
                        tvOilStatus.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.text_enginer_oil_size));
                        tvOilStatus.setTextColor(getResources().getColor(R.color.color_green));
                        tvOilStatus.setText(
                                engineOilWebSocketEntity.getEngineOilRemainMile()+"km" ==null ? 0+"km" :
                                        engineOilWebSocketEntity.getEngineOilRemainMile()+"km");
                    }
                }
                break;
        }

    }
    @OnClick({R.id.img_left, R.id.tv_left, R.id.ll_left, R.id.tv_change_oil})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.img_left:
            case R.id.tv_left:
            case R.id.ll_left:
                finish();
                break;
            case R.id.tv_change_oil:
                intent.setClass(context, BaiqiMapActivity.class);
                startActivity(intent);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
