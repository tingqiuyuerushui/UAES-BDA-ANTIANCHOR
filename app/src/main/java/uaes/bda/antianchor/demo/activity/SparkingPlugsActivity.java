package uaes.bda.antianchor.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.bean.SparkingHttp;
import uaes.bda.antianchor.demo.bean.SparkingWebSocket;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.service.SparkingWebSocketService;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.OkHttp;

public class SparkingPlugsActivity extends Activity {
    private static final String TAG = "5680";
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
    @BindView(R.id.one_name)
    TextView oneName;
    @BindView(R.id.one_img)
    ImageView oneImg;
    @BindView(R.id.one_healthy)
    TextView oneHealthy;
    @BindView(R.id.two_name)
    TextView twoName;
    @BindView(R.id.two_img)
    ImageView twoImg;
    @BindView(R.id.two_healthy)
    TextView twoHealthy;
    @BindView(R.id.three_name)
    TextView threeName;
    @BindView(R.id.three_img)
    ImageView threeImg;
    @BindView(R.id.three_healthy)
    TextView threeHealthy;
    @BindView(R.id.four_name)
    TextView fourName;
    @BindView(R.id.four_img)
    ImageView fourImg;
    @BindView(R.id.four_healthy)
    TextView fourHealthy;
    @BindView(R.id.sp_bt)
    TextView spbt;
    @BindView(R.id.tv_image)
    TextView tvimage;
    @BindView(R.id.sparking_button)
    LinearLayout sparkingButton;
    @BindView(R.id.sparking_)
    LinearLayout sparking_;
    @BindView(R.id.img_chack)
    LinearLayout img_chack;
    @BindView(R.id.bt_check)
    LinearLayout btcheck;
    @BindView(R.id.title_headers)
    LinearLayout titleheaders;
    private Context mContext;
    private ProgressDialog dialog;
    public static WebSocketConnection mConnectt = new WebSocketConnection();

    private String address = "ws://139.224.8.68:5680/sparkingStatus";
    private int b;
    SparkingPlugsActivity.UpdateUIBroadcastReceiver broadcastReceiver;
    private String strReciveService;
    public static final String ACTION_UPDATEUI = "updata.action.updateUI";
    private Gson gson = new Gson();
    /**
     * Notification构造器
     */
    NotificationCompat.Builder mBuilder;
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;
    /**
     * Notification的ID
     */
    int notifyId = 100;
    /*不规范的写法：
 private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        };
    };
正确的写法：
 private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
     @SuppressLint(“HandlerLeak”)
    原因：Handler在Android中用于消息的发送与异步处理，常常在Activity中作为一个匿名内部类来定义，
    此时Handler会隐式地持有一个外部类对象（通常是一个Activity）的引用。当Activity已经被用户关闭时，
    由于Handler持有Activity的引用造成Activity无法被GC回收，这样容易造成内存泄露。
    解决办法：将其定义成一个静态内部类（此时不会持有外部类对象的引用），
    在构造方法中传入Activity并对Activity对象增加一个弱引用，
    这样Activity被用户关闭之后，即便异步消息还未处理完毕，Activity也能够被GC回收，从而避免了内存泄露。*/
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    Spaking(msg);
                    dialog.dismiss();
                    break;
                case 0x02:
                    Toast.makeText(getApplicationContext(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                    break;
                case 0x03:
                    // msg.obj;
                    //MyGson(msg01);
                    break;
            }
        }
    };

    private void Spaking(Message msg) {
        SparkingHttp.DataBean obj = (SparkingHttp.DataBean) msg.obj;
        String firstSparking = obj.getFirstSparking();
        String secondSparking = obj.getSecondSparking();
        String thirdSparking = obj.getThirdSparking();
        String forthSparking = obj.getForthSparking();
        Drawable drawable1 = getApplicationContext().getResources().getDrawable(R.mipmap.sparking_normal);
        int color = getApplicationContext().getResources().getColor(R.color.white);
        Drawable drawable2 = getApplicationContext().getResources().getDrawable(R.mipmap.sparking_warming);
        int color2 = getApplicationContext().getResources().getColor(R.color.sparking_warn);
        //Drawable drawable3 = getApplicationContext().getResources().getDrawable(R.mipmap.sparking_warm);
        //   int color3 = getApplicationContext().getResources().getColor(R.color.sparking_warning);
        Drawable drawable3 = getApplicationContext().getResources().getDrawable(R.mipmap.sparker_lift);//sparking_warm
        Drawable drawable4 = getApplicationContext().getResources().getDrawable(R.mipmap.sparker_right);//sparking_warm

        if (firstSparking.equals("health")) {
            sparking_check_one(drawable1, color, drawable3);
        } else if (firstSparking.equals("fault")) {//故障
            sparking_check_one(drawable2, color2, drawable4);
            change();
        }

        if (secondSparking.equals("health")) {
            sparking_check_two(drawable1, color, drawable3);
        } else if (secondSparking.equals("fault")) {
            sparking_check_two(drawable2, color2, drawable4);
            change();
        }

        if (thirdSparking.equals("health")) {
            sparking_check_three(drawable1, color, drawable3);
        } else if (thirdSparking.equals("fault")) {
            sparking_check_three(drawable2, color2, drawable4);
            change();
        }

        if (forthSparking.equals("health")) {
            sparking_checkfore(drawable1, color, drawable3);
        } else if (forthSparking.equals("fault")) {
            sparking_checkfore(drawable2, color2, drawable4);
            change();
        }
    }

    private void change() {
        img_chack.setVisibility(View.GONE);//图片隐藏
        sparking_.setVisibility(View.VISIBLE);//影响点
        sparkingButton.setVisibility(View.VISIBLE);//更换的按钮
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparking_plugs);
        ButterKnife.bind(this);
        mContext = this;

        init();
    }

    private void init() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        dialog = GetProgressDialog.getProgressDialog(mContext);
        initdata();
        // 动态注册广播
       IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATEUI);
        broadcastReceiver = new SparkingPlugsActivity.UpdateUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
       Intent intent = new Intent(this, SparkingWebSocketService.class);
       startService(intent);

        title.setText("火花塞");
        imgLeft.setVisibility(View.VISIBLE);
        Drawable drawable = getApplicationContext().getResources().getDrawable(R.color.white);
        titleheaders.setBackground(drawable);
    }

    private void initdata() {
         http();
        // https();
         // webSocket();
    }

    private void https() {
        dialog.show();
        final OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder().add("vin", "111111").build();
        final Request request = new Request.Builder().url(Const.UrlPostSparking).post(formBody).build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        dialog.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "c成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void http() {
        dialog.show();
        OkHttp.postAsync(Const.UrlPostSparking, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                dialog.dismiss();
                Message message = new Message();
                message.what = 0x02;
                mHandler.sendMessage(message);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                //dialog.dismiss();
                dialog.cancel();
                //   Log.e(TAG, "requestSuccess: "+result );//<html><body><h2>500 Internal Server Error</h2></body></html>
                SparkingHttp sparkingHttp = gson.fromJson(result, SparkingHttp.class);
                // SparkingHttp.DataBean dataBean = gson.fromJson(result, SparkingHttp.DataBean.class);
                SparkingHttp.DataBean data = sparkingHttp.getData();
                if (data != null && mHandler != null) {
                    Message obtain = Message.obtain(mHandler, 0x01, data);
                    mHandler.sendMessage(obtain);
                }
            }
        });


    }

    private Map<String, String> addParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("vin", "111111");
        return params;
    }

    private void webSocket() {
        Log.e(TAG, "we connect... 5678");
        try {
            mConnectt.connect(address, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    Log.i(TAG, "Status:Connect to " + address);
                    sendMessage();
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
                    // waits();
                    Log.e(TAG, "payload: " + payload);
                    MyGson(payload);

                    if (payload.equals("Fault")) {
                        showIntentActivityNotify();
                    }
                    mConnectt.sendTextMessage("I am android client5680");
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                    Log.i(TAG, "Connection lost..");
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private void showIntentActivityNotify() {
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
        int flagAutoCancel = Notification.FLAG_AUTO_CANCEL;//在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle("火花塞详情")
                .setContentText("火花塞有问题，点击查看")
                .setSmallIcon(R.mipmap.sparking_warming)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTicker("警告");

        //点击的意图ACTION是跳转到Intent
        Intent resultIntent = new Intent(this, SparkingPlugsActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    private void MyGson(String payload) {
        //{"fault":["second","third"]}
        /*{"fault":["second","forth"],"warning":["first"]}
        *  f : second
        *  f : forth
        *  w: first
        * */

        SparkingWebSocket sws = gson.fromJson(payload, SparkingWebSocket.class);
        List<String> fault = sws.getFault();
        Drawable drawable2 = getApplicationContext().getResources().getDrawable(R.mipmap.sparking_warming);//警告
        int color2 = getApplicationContext().getResources().getColor(R.color.sparking_warn);//黄色
        Drawable drawable22 = getApplicationContext().getResources().getDrawable(R.mipmap.sparker_right);//一小格
        for (int i = 0; i < fault.size(); i++) {
            String f = fault.get(i);
            Log.e(TAG, "f : " + f);
            if (f.equals("first")) {

                sparking_check_one(drawable2, color2, drawable22);
                sparkingButton.setVisibility(View.VISIBLE);

            } else if (f.equals("second")) {

                sparking_check_two(drawable2, color2, drawable22);
                sparkingButton.setVisibility(View.VISIBLE);

            } else if (f.equals("third")) {//second

                sparking_check_three(drawable2, color2, drawable22);
                sparkingButton.setVisibility(View.VISIBLE);

            } else if (f.equals("forth")) {//forth
                sparking_checkfore(drawable2, color2, drawable22);
                sparkingButton.setVisibility(View.VISIBLE);
            } else {
                Normal();

            }
        }
      /*  List<String> warning = sws.getWarning();
        for (int i = 0; i < warning.size(); i++) {
            String w = warning.get(i);
            Log.e(TAG, "w: " + w);
            Drawable drawable3 = getApplicationContext().getResources().getDrawable(R.mipmap.sparking_warm);//sparking_warm
            Drawable drawable4 = getApplicationContext().getResources().getDrawable(R.mipmap.sparker_right);//sparking_warm
            int color3 = getApplicationContext().getResources().getColor(R.color.sparking_warn);//sparking_warning

            if (w.equals("second")) {
                sparking_check_one(drawable3, color3, drawable4);
            } else if (w.equals("forth")) {
                sparking_check_two(drawable3, color3, drawable4);
            } else if (w.equals("first")) {
                sparking_check_three(drawable3, color3, drawable4);
            } else if (w.equals("third")) {
                sparking_check(drawable3, color3, drawable4);
            } else {
                Normal();
            }
        }*/
    }

    private void sparking_check_one(Drawable drawable, int color, Drawable drawable4) {
        oneImg.setBackground(drawable);
        oneHealthy.setBackground(drawable4);
        oneName.setText("一缸火花塞");
        // oneHealthy.setTextColor(color);
        oneName.setTextColor(color);
        //  sparkingButton.setVisibility(View.VISIBLE);
    }

    private void sparking_check_two(Drawable drawable, int color, Drawable drawable4) {
        twoImg.setBackground(drawable);
        twoHealthy.setBackground(drawable4);
        twoName.setText("二缸火花塞");
        twoHealthy.setTextColor(color);
        twoName.setTextColor(color);
        // sparkingButton.setVisibility(View.VISIBLE);
    }

    private void sparking_check_three(Drawable drawable, int color, Drawable drawable4) {
        threeImg.setBackground(drawable);
        threeHealthy.setBackground(drawable4);
        threeName.setText("三缸火花塞");
        threeHealthy.setTextColor(color);
        threeName.setTextColor(color);
        //   sparkingButton.setVisibility(View.VISIBLE);
    }

    private void sparking_checkfore(Drawable drawable, int color, Drawable drawable4) {

        fourImg.setBackground(drawable);
        fourHealthy.setBackground(drawable4);
        fourName.setText("四缸火花塞");
        fourHealthy.setTextColor(color);
        fourName.setTextColor(color);
        //  sparkingButton.setVisibility(View.VISIBLE);
    }

    private void Normal() {
        Drawable normal = getApplicationContext().getResources().getDrawable(R.mipmap.sparking_normal);
        int white = getApplicationContext().getResources().getColor(R.color.white);
        oneHealthy.setTextColor(white);
        oneName.setTextColor(white);
        twoHealthy.setTextColor(white);
        twoName.setTextColor(white);
        threeHealthy.setTextColor(white);
        threeName.setTextColor(white);
        fourHealthy.setTextColor(white);
        fourName.setTextColor(white);
        oneImg.setBackground(normal);
        twoImg.setBackground(normal);
        threeImg.setBackground(normal);
        fourImg.setBackground(normal);
        sparkingButton.setVisibility(View.INVISIBLE);
    }

    //    发送信息给服务器
    private void sendMessage() {
        String name = " hello master5680 ";
        if (name != null && name.length() != 0)
            mConnectt.sendTextMessage(name);
        else
            Toast.makeText(getApplicationContext(), "不能为空", Toast.LENGTH_SHORT).show();

    }

    @OnClick({R.id.img_left, R.id.one_img, R.id.two_img, R.id.three_img, R.id.four_img, R.id.sparking_button, R.id.ll_left})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.img_left:
                break;
            case R.id.ll_left:
                finish();
                break;
            case R.id.one_img:
                break;
            case R.id.two_img:
                break;
            case R.id.three_img:
                break;
            case R.id.four_img:
                break;
            case R.id.sparking_button:
                intent.setClass(mContext, BaiqiMapActivity.class);
                startActivity(intent);
                break;
        }
    }



    /**
     * 定义广播接收器（内部类）
     *
     * @author lenovo
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            strReciveService = String.valueOf(intent.getExtras().getString("payload"));
            Log.e("119", "strReciveService: " + strReciveService);
            //  MyGson(strReciveService);
            // MyUtil.sendHandleMsg(1,strReciveService, handler);
            // Message obtain = Message.obtain(mHandler, 0x03, strReciveService);
            // mHandler.sendMessage(obtain);
            // { fault : ["second","third"]}
            final String A = "{ fault : [\"second\",\"third\"]}";
           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MyGson(strReciveService);

                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " + 6);
        unregisterReceiver(broadcastReceiver);

    }
}
