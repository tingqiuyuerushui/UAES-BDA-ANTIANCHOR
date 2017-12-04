package uaes.bda.antianchor.demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;


import java.io.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import uaes.bda.antianchor.demo.activity.GaoDeActivity;
import uaes.bda.antianchor.demo.activity.UserActivity;
import uaes.bda.antianchor.demo.adapter.MyGridAdapter;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.customsizeview.NestRadioGroup;
import uaes.bda.antianchor.demo.fragment.DrivingAssistantFragment;
import uaes.bda.antianchor.demo.fragment.FaultPredictionFragment_change;
import uaes.bda.antianchor.demo.fragment.FragmentTabAdapter;
import uaes.bda.antianchor.demo.fragment.HomeFragment;
import uaes.bda.antianchor.demo.fragment.MaintenanceAssistantFragment;
import uaes.bda.antianchor.demo.fragment.SettingFragment;
import uaes.bda.antianchor.demo.utils.saveString;

/**
 * Created by lun.zhang on 4/19/2017.
 */

public class TabMainActivity extends FragmentActivity implements AMapLocationListener {
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
    @BindView(R.id.fragment_content)
    FrameLayout fragmentContent;
    @BindView(R.id.ico_radio_home)
    ImageView icoRadioHome;
    @BindView(R.id.radio_home)
    RadioButton radioHome;
    @BindView(R.id.ll_radio_home)
    LinearLayout llRadioHome;
    @BindView(R.id.ico_radio_fault)
    ImageView icoRadioFault;
    @BindView(R.id.radio_fault)
    RadioButton radioFault;
    @BindView(R.id.ll_radio_fault)
    LinearLayout llRadioFault;
    @BindView(R.id.ico_radio_help)
    ImageView icoRadioHelp;
    @BindView(R.id.radio_help)
    RadioButton radioHelp;
    @BindView(R.id.ll_radio_help)
    LinearLayout llRadioHelp;
    @BindView(R.id.ico_radio_user)
    ImageView icoRadioUser;
    @BindView(R.id.radio_user)
    RadioButton radioUser;
    @BindView(R.id.ll_radio_user)
    LinearLayout llRadioUser;
    @BindView(R.id.ico_radio_setting)
    ImageView icoRadioSetting;
    @BindView(R.id.radio_setting)
    RadioButton radioSetting;
    @BindView(R.id.ll_radio_setting)
    LinearLayout llRadioSetting;
    @BindView(R.id.tabs_rg)
    NestRadioGroup tabsRg;
    private NestRadioGroup rgs;
    public List<Fragment> fragments = new ArrayList<Fragment>();
    public static Context context;
    public static FragmentTabAdapter tabAdapter;
    public int i = 0;
    private static final String TAG = "333";
    private String address = "ws://139.224.8.68:4567/fuel";
    private String address2 = "ws://139.224.8.68:4567/fuelwarning";
    private String name = "UAES";
    private static WebSocketConnection mConnect = new WebSocketConnection();
    private String Warning = "Warning";
    /**
     * Notification构造器
     */
    NotificationCompat.Builder mBuilder;
    /**
     * Notification的ID
     */
    int notifyId = 100;
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;
    int a = 0;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient;
    private String[] kinds = {"开始记录", "停止记录", "清除记录"};
    private int c = 0;
    private View contentView;
    private ArrayList<String> list;
    private MyGridAdapter myGridAdapter;
    private PopupWindow popupWindow;
    private String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "CaoGang/caogang03/";
    private File file = new File(path);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);
        ButterKnife.bind(this);
        context = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        initView();
        initLocation();
        webSocket();
        a = 0;
        icoRadioHome.setImageResource(R.mipmap.index_footer_ico01a);
        radioHome.setChecked(true);
        i = 1;
    }

    //Warning
    private void webSocket() {
        Log.e(TAG, "we connect... ");
        try {
            mConnect.connect(address2, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    Log.i(TAG, "Status:Connect to " + address);
                    sendMessage();
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
//                    Log.e(TAG, "payload: "+payload );
//                    Log.e(TAG, "onTextMessage: " + payload != null ?  payload : "");
                    if (payload.equals(Warning) && a == 0) {
                        showIntentActivityNotify();
                        a = 1;
                    }
                    mConnect.sendTextMessage("I am android client4567");
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

    private void initLocation() {
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setNeedAddress(true);
        mlocationClient.setLocationListener(this);
        //设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000 * 10 * 6 * 10);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                double longitude = amapLocation.getLongitude();//获取经度
                if (c == 1) {
                    saveString.createFiletthree(longitude + "@");
                }
                double latitude = amapLocation.getLatitude();//获取纬度
                if (c == 1) {
                    Toast.makeText(getApplicationContext(),"latitude"+latitude,Toast.LENGTH_SHORT).show();
                    saveString.createFiletthree(latitude + "@");
                }

                Const.Latitude = String.valueOf(latitude);
                Const.Longitude = String.valueOf(amapLocation.getLongitude());
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                 final String city = amapLocation.getCity();//城市信息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLeft.setText(city);
                    }
                });
                Const.city = amapLocation.getCity();
                Const.district = amapLocation.getDistrict();
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 显示通知栏点击跳转到指定Activity
     */
    public void showIntentActivityNotify() {
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
        int flagAutoCancel = Notification.FLAG_AUTO_CANCEL;//在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle("附近加油站")
                .setContentText("油量已不足，点击去加油站")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTicker("电量已充满");

        //点击的意图ACTION是跳转到Intent
        Intent resultIntent = new Intent(this, GaoDeActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    //    发送信息给服务器
    private void sendMessage() {
        String name = " hello master4567 ";
        if (name != null && name.length() != 0)
            mConnect.sendTextMessage(name);
        else
            Toast.makeText(getApplicationContext(), "不能为空", Toast.LENGTH_SHORT).show();

    }

    private void initView() {
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText("上海");
        imgLeft.setVisibility(View.VISIBLE);
        imgLeft.setImageResource(R.mipmap.navigation);
        title.setText("U行·天下");
        llRight.setVisibility(View.VISIBLE);
        imgRight.setVisibility(View.VISIBLE);
        fragments.add(new HomeFragment());
        fragments.add(new FaultPredictionFragment_change());
        fragments.add(new DrivingAssistantFragment());
        fragments.add(new MaintenanceAssistantFragment());
        fragments.add(new SettingFragment());
        tabAdapter = new FragmentTabAdapter(this, fragments, R.id.fragment_content, tabsRg);
    }

    @OnClick({R.id.ll_radio_home, R.id.radio_home, R.id.ico_radio_home, R.id.ll_radio_fault, R.id.radio_fault, R.id.ico_radio_fault,
            R.id.ll_radio_help, R.id.radio_help, R.id.ico_radio_help, R.id.ll_radio_user, R.id.radio_user, R.id.img_right,
            R.id.ico_radio_user, R.id.ll_left, R.id.ll_right, R.id.ico_radio_setting, R.id.radio_setting, R.id.ll_radio_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left:

                break;
            case R.id.ico_radio_home:
            case R.id.radio_home:
            case R.id.ll_radio_home:
                icoRadioHome.setImageResource(R.mipmap.index_footer_ico01a);
                radioHome.setChecked(true);
                tabAdapter.getRadioGroup(0);
                if (i != 1) {
                    select(i);
                }
                i = 1;
                break;
            case R.id.ico_radio_fault:
            case R.id.radio_fault:
            case R.id.ll_radio_fault:
                icoRadioFault.setImageResource(R.mipmap.index_footer_ico02a);
                tabAdapter.getRadioGroup(1);
                radioFault.setChecked(true);
                if (i != 2) {
                    select(i);
                }
                i = 2;
                break;
            case R.id.ico_radio_help:
            case R.id.radio_help:
            case R.id.ll_radio_help:
                icoRadioHelp.setImageResource(R.mipmap.index_footer_ico03a);
                tabAdapter.getRadioGroup(2);
                radioHelp.setChecked(true);
                if (i != 3) {
                    select(i);
                }
                i = 3;
                break;
            case R.id.ico_radio_user:
            case R.id.radio_user:
            case R.id.ll_radio_user:
//                Toast.makeText(context, "功能暂未开通", Toast.LENGTH_SHORT).show();
                icoRadioUser.setImageResource(R.mipmap.index_footer_ico04a);
                tabAdapter.getRadioGroup(3);
                radioUser.setChecked(true);
                if (i != 4) {
                    select(i);
                }
                i = 4;
                break;
            case R.id.ll_left:
                break;
            case R.id.ll_right:
            case R.id. img_right:
            /*    Intent intent = new Intent();
                intent.setClass(context, UserActivity.class);
                startActivity(intent);*/

                popwindon();
                popupWindow.showAtLocation(contentView,Gravity.BOTTOM,0,0);
                break;
            case R.id.ico_radio_setting:
            case R.id.radio_setting:
            case R.id.ll_radio_setting:
                icoRadioSetting.setImageResource(R.mipmap.index_footer_ico05a);
                tabAdapter.getRadioGroup(4);
                radioSetting.setChecked(true);
                if (i != 5) {
                    select(i);
                }
                i = 5;
                break;

        }
    }

    private void popwindon() {
        //加载弹出的布局
        contentView = LayoutInflater.from(TabMainActivity.this).inflate(R.layout.tab_main_pop, null);
        //绑定控件
        GridView gridView = (GridView) contentView.findViewById(R.id.gv);
        list = new ArrayList<>();
        for (int j = 0; j < kinds.length; j++) {
            list.add(kinds[j]);
        }
        //初始化适配器
        myGridAdapter = new MyGridAdapter(TabMainActivity.this, list,gridView);
        gridView.setSelector(new ColorDrawable(Color.WHITE));
        gridView.setAdapter(myGridAdapter);

        //gridview点击监听
        gridView.setOnItemClickListener(new ItemClickListener());
        //设置弹出的宽度和高度
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //获取焦点
        popupWindow.setFocusable(true);
        //注意，要是点击外部空白处弹框消息，那么必须给弹框设置一个背景，不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.MyPopupWindow_anim_style);
        //按下Android回退物理键popipwindow消失解决
        gridView.setOnKeyListener(new MyOnKeyListener());
    }

    //按下Android回退物理键popipwindow消失解决
    public class MyOnKeyListener implements View.OnKeyListener{
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return true;
                }
            }
            return false;
        }
    }

   //点击item的监听
    public class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            if (position == 0){
               c=1;
            }
            if (position == 1){
               c = 3;
            }
            if (position == 2){
                deleteDirection(file);
            }
            Log.e(TAG, "position: "+position);
            //背景选择
            myGridAdapter.setSeclection(position);
            //适配器通知数据改变
            myGridAdapter.notifyDataSetChanged();
        }
    }

    private boolean deleteDirection(File dir) {
        if (dir == null || !dir.exists() || dir.isFile()){
            return false;
        }
        for (File file : dir.listFiles()){
            if (file.isFile()){
                file.delete();
            }else if (file.isDirectory()){
                deleteDirection(file);//递归
            }
        }
        dir.delete();
        return true;
    }

    public void select(int i) {
        switch (i) {
            case 1:
                icoRadioHome.setImageResource(R.mipmap.index_footer_ico01);
                break;
            case 2:
                icoRadioFault.setImageResource(R.mipmap.index_footer_ico02);
                break;
            case 3:
                icoRadioHelp.setImageResource(R.mipmap.index_footer_ico03);
                break;
            case 4:
                icoRadioUser.setImageResource(R.mipmap.index_footer_ico04);
                break;
            case 5:
                icoRadioSetting.setImageResource(R.mipmap.index_footer_ico05);
                break;
            default:
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
//                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mConnect.disconnect();
        //Toast.makeText(getApplicationContext(),"onDestroy",Toast.LENGTH_SHORT).show();
        c = 0;
    }


}
