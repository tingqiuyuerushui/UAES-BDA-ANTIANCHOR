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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
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
import uaes.bda.antianchor.demo.entity.BatteryStatusEntity;
import uaes.bda.antianchor.demo.service.BatteryWebSocketService;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 11/6/2017.
 */

public class BatteryHelperActivity extends Activity {
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
    @BindView(R.id.img_battery_status)
    ImageView imgBatteryStatus;
    @BindView(R.id.tv_battery_status)
    TextView tvBatteryStatus;
    @BindView(R.id.tv_tips_battery_status)
    TextView tvTipsBatteryStatus;
    @BindView(R.id.tv_tips_battery_status_advice)
    TextView tvTipsBatteryStatusAdvice;
    @BindView(R.id.tv_tips_battery_status_advice_driver)
    TextView tvTipsBatteryStatusAdviceDriver;
    private Context context;
    private Unbinder unBinder;
    private WebSocketConnection mConnect;
    private ProgressDialog dialog = null;
    private BatteryStatusEntity batteryStatusEntity = null;
    private final int SERVICE = 0;
    private final int WEBSOCKET = 1;
    public static final String ACTION_UPDATEUI = "battery.action.updateUI";
    UpdateUIBroadcastReceiver broadcastReceiver;
    private String strReciveService;
    private MyHandler handler = null;
//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            if (dialog != null && dialog.isShowing()) {
//                dialog.cancel();
//            }
//            switch (msg.what) {
//                case 1:
//                    dealHealthStatusView(strReciveService,WEBSOCKET);
//                    break;
//                case 2:
//                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
//                            .show();
//                    break;
//                case 3:
//                    FinalToast.netTimeOutMakeText(context);
//                    break;
//                case 4:
//                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
//                            .show();
//                    LoadDataToView();
//                    break;
//            }
//        }
//    };
    public class MyHandler extends Handler {
        private WeakReference<Activity> reference;

        public MyHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if(reference.get() != null) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                }
                switch (msg.what) {
                    case 1:
                        dealHealthStatusView(strReciveService, WEBSOCKET);
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
        }
    }
    private void LoadDataToView() {
        dealHealthStatusView(batteryStatusEntity.getData().getBatteryStatus(),WEBSOCKET);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_helper);
        context = this;
        unBinder = ButterKnife.bind(this);
        handler = new MyHandler(this);
        mConnect = new WebSocketConnection();
        initView();
        dialog = GetProgressDialog.getProgressDialog(context);
        getServiceBatteryData();
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI);
        broadcastReceiver = new UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
        Intent intent = new Intent(this, BatteryWebSocketService.class);
        startService(intent);
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        connectWebSocket(mConnect);
    }

    private void initView() {
        title.setText("电池小助手");
        imgLeft.setVisibility(View.VISIBLE);
    }
    private void getServiceBatteryData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlBatteryStatus, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                Gson gson = new Gson();
                batteryStatusEntity = gson.fromJson(result, BatteryStatusEntity.class);
                if (batteryStatusEntity.getStatus().equals("1")) {
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
            webSocketConnection.connect(Const.UrlWebSocketGetBatteryStatus, new WebSocketHandler() {

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
     * @author zhanglun
     *
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            strReciveService = String.valueOf(intent.getExtras().getString("payload"));
            MyUtil.sendHandleMsg(1,strReciveService, handler);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销广播
        unregisterReceiver(broadcastReceiver);
//        stopService(intent);
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
    private void dealHealthStatusView(String status,int SeOrWs) {
        switch (SeOrWs) {
            case SERVICE:
                if (status.equals("full")) {
                    imgBatteryStatus.setImageResource(R.mipmap.current_battery_status);
                    tvTipsBatteryStatus.setText("电量充足");
                    tvTipsBatteryStatusAdvice.setText("请放心使用");
                    tvTipsBatteryStatusAdviceDriver.setText("建议：熄火前请首先关闭空调并拔掉外接充电设备");
                }else{
                    imgBatteryStatus.setImageResource(R.mipmap.battery_low_status);
                    tvTipsBatteryStatus.setText("电量不足");
                    tvTipsBatteryStatusAdvice.setText("请及时充电");
                    tvTipsBatteryStatusAdviceDriver.setText("建议：启动发动机关闭空调并拔掉外接设备");

                }
                break;
            case WEBSOCKET:
                if (status.equals("Low")) {
                    imgBatteryStatus.setImageResource(R.mipmap.battery_low_status);
                    tvTipsBatteryStatus.setText("电量不足");
                    tvTipsBatteryStatusAdvice.setText("请及时充电");
                    tvTipsBatteryStatusAdviceDriver.setText("建议：启动发动机关闭空调并拔掉外接设备");
                }else{
                    imgBatteryStatus.setImageResource(R.mipmap.current_battery_status);
                    tvTipsBatteryStatus.setText("电量充足");
                    tvTipsBatteryStatusAdvice.setText("请放心使用");
                    tvTipsBatteryStatusAdviceDriver.setText("建议：熄火前请首先关闭空调并拔掉外接充电设备");
                }
                break;
        }
    }
}
