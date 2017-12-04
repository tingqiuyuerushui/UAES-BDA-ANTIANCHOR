package uaes.bda.antianchor.demo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.MyTrafficStyle;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.SubPoiItem;
import com.amap.api.services.traffic.RoadTrafficQuery;
import com.amap.api.services.traffic.TrafficSearch;
import com.amap.api.services.traffic.TrafficStatusEvaluation;
import com.amap.api.services.traffic.TrafficStatusInfo;
import com.amap.api.services.traffic.TrafficStatusResult;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import overlay.PoiOverlay;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.adapter.MyListAdapter;
import uaes.bda.antianchor.demo.utils.saveString;

public class GaoDeActivity extends Activity implements LocationSource, AMapLocationListener,
        View.OnClickListener, PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter
        , AMap.OnMapClickListener, MyListAdapter.InnerItemOnclickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "911";
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private MapView mMapView;
    //声明AMapLocationClient类对象
    public AMapLocationClient mapLocationClient;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mapLocationClientOption;
    ////////
    private ProgressDialog progDialog = null;// 搜索时进度条
    private String keyWord;
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;//搜索
    private PoiSearch.SearchBound searchBound;
    private int currentPage;// 当前页面，从0开始计数
    private PoiResult poiResults; // poi返回的结果
    private String city = "上海";//搜索城市
    private LatLonPoint latLonPoint;
    private View view;
    private List<PoiItem> poiItems;
    private Marker mlastMarker;

    private LinearLayout mPoiDetail;
    private ListView listView;
    private UiSettings mUiSettings;
    private String address = "ws://139.224.8.68:5678/fuelfilling";
    public static WebSocketConnection mConnect = new WebSocketConnection();
    private Context mContext;
    private int b;
    private int a = 0;
    private int c = 0;
    private int d = 0;
    private static final int BAIDU_READ_PHONE_STATE = 100;

    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;
    /**
     * Notification构造器
     */
    NotificationCompat.Builder mBuilder;
    /**
     * Notification的ID
     */
    int notifyId = 100;
    private TrafficSearch trafficSearch;
    private String street;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gao_de);
        ButterKnife.bind(this);
        mMapView = findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mContext = this;
        mMapView.onCreate(savedInstanceState);
        showContacts();
    }


    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        // EventBus.getDefault().post(new MainMessage("123"));
        webSocket();
        setUp();
        button();
        finishsh();
        traffic(); //自定义实时交通图的颜色样式
        LocationMarker();//更改中心显示箭头
//        search("加油站", 10000);
       // title();
        Traffic();

    }

    private void addMarkers(double a, double b) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(a, b), 14));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(a, b));
        markerOptions.title("当前经度: " + a + "    纬度:" + b);
        markerOptions.visible(true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.poi_marker_pressed));
        markerOptions.icon(bitmapDescriptor);
        aMap.addMarker(markerOptions);


    }

    private void finishsh() {
        ImageView IM = findViewById(R.id.gaode_back);
        IM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void button() {
        mPoiDetail = findViewById(R.id.poi_detail);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        TextView tv1 = findViewById(R.id.tv1);
        TextView tv2 = findViewById(R.id.tv2);
        TextView tv3 = findViewById(R.id.tv3);
        aMap.clear();
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.clear();
                search("加油站", 10000);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.clear();
                showProgressDialog();
                for (int i = 0; i <= 0; i++) {
                    addMarkers(31.215888, 121.7029689);
                    addMarkers(31.225888, 121.6929681);
                    addMarkers(31.235888, 121.6829682);
                    addMarkers(31.255888, 121.6629684);
                    addMarkers(31.265888, 121.6529685);
                    addMarkers(31.285888, 121.6329687);
                    addMarkers(31.295888, 121.6229688);
                    addMarkers(31.245888, 121.6729683);
                    addMarkers(31.275888, 121.6429686);
                }
                // mPoiDetail.setVisibility(View.VISIBLE);
                dissmissProgressDialog();
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aMap.clear();
                showProgressDialog();
                for (int i = 0; i <= 0; i++) {
                  //  addMarkers(31.215888, 121.7029689);
                    addMarkers(31.225888, 121.6129681);
                    addMarkers(31.235888, 121.6229682);
                    addMarkers(31.255888, 121.6329684);
                    addMarkers(31.265888, 121.6429685);
                    addMarkers(31.285888, 121.6529687);
                    addMarkers(31.295888, 121.6629688);
                 //   addMarkers(31.245888, 121.6729683);
                 //   addMarkers(31.275888, 121.6829686);
                }
              //  search("北汽4s店", 300000);
                dissmissProgressDialog();
            }
        });
    }

    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    init();
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void showContacts() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED

                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED

                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                ) {
            Toast.makeText(getApplicationContext(), "没有权限,请手动开启定位权限", Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(GaoDeActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, BAIDU_READ_PHONE_STATE);
        } else {
            init();
        }
    }

    private void setUp() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(this);
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mUiSettings
                .setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

    }

    private void traffic() {
        //自定义实时交通图的颜色样式
        MyTrafficStyle myTrafficStyle = new MyTrafficStyle();
        myTrafficStyle.setSeriousCongestedColor(0xff92000a);
        myTrafficStyle.setCongestedColor(0xffea0312);
        myTrafficStyle.setSlowColor(0xffff7508);
        myTrafficStyle.setSmoothColor(0xff00a209);
        aMap.setMyTrafficStyle(myTrafficStyle);
        aMap.setTrafficEnabled(true);//实时交通图
        aMap.showBuildings(true);// 显示3D 楼块
        aMap.showMapText(true);// 显示底图文字
        // int congestedColor = aMap.getMyTrafficStyle().getCongestedColor();

    }

    private void Traffic() {
        trafficSearch = new TrafficSearch(this);
        trafficSearch.setTrafficSearchListener(new TrafficSearch.OnTrafficSearchListener() {
            @Override
            public void onRoadTrafficSearched(TrafficStatusResult trafficStatusResult, int errorcode) {
                // Log.e(TAG, "onRoadTrafficSearched: "+123456 );
                // Log.e(TAG, "errorcode: " + errorcode );
                if (trafficStatusResult == null && errorcode != 1000) {
                    //Log.e(TAG, "onRoadTrafficSearched: "+"nullnullnullnullnullnullnullnull" );
                    saveString.createFilettfore(null + "" + "@");
                    saveString.createFilettfore(null + "" + "@" + "\r\n");
                } else {
                    trafficSearch(trafficStatusResult);
                }
            }
        });//此处设置this，因为实现类继承了TrafficSearchListener，也可以构造内部类实现。
    }

    private void trafficSearch(TrafficStatusResult trafficStatusResult) {
        //此为TrafficSearch.OnTrafficSearchListener监听器中的回调方法
        //  int describeContents = trafficStatusResult.describeContents();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        // saveString.createFile("日期: " + date + "@");
        // saveString.createFiletthree("日期: " + date + "@");

        String format = df.format(date);//定位时间

        //saveString.createFile("定位时间: " + format + "@");
        // saveString.createFiletthree("定位时间: " + format + "@");

        String description = trafficStatusResult.getDescription();//返回路况综合情况描述
        Toast.makeText(getApplicationContext(), description == null ? "null" : description, Toast.LENGTH_SHORT).show();
        // Log.e(TAG, "demo: "+description );
        saveString.createFilettfore(description + "@");

        // saveString.createFile("返回路况综合情况描述: " + description + "@");
        // saveString.createFiletthree("返回路况综合情况描述: " + description + "@");

        TrafficStatusEvaluation evaluation = trafficStatusResult.getEvaluation();//包含对路况的评价，拥堵占比等

        int evaluation_describeContents = evaluation.describeContents();
        //saveString.createFile("evaluation_describeContents: " + evaluation_describeContents + "@");
        //saveString.createFiletthree("evaluation_describeContents: " + evaluation_describeContents + "@");

        String blocked = evaluation.getBlocked();
        //saveString.createFile("blocked: " + blocked + "@");
        //saveString.createFiletthree("blocked: " + blocked + "@");

        String congested = evaluation.getCongested();

        //saveString.createFile("congested: " + congested + "@");
        // saveString.createFiletthree("congested: " + congested + "@");
        String description1 = evaluation.getDescription();

        saveString.createFilettfore(description1 + "@" + "\r\n");

        // saveString.createFile("description1: " + description1 + "@");
        //saveString.createFiletthree("description1: " + description1 + "@");

        String expedite = evaluation.getExpedite();
        //  saveString.createFile("expedite: " + expedite + "@");
        // saveString.createFiletthree("expedite: " + expedite + "@");

        String status = evaluation.getStatus();

        // saveString.createFile("status: " + status + "@");
        // saveString.createFiletthree("status: " + status + "@");
        String unknown = evaluation.getUnknown();

        // saveString.createFile("unknown: " + unknown + "@");
        //saveString.createFiletthree("unknown: " + unknown + "@");
//道路列表，包含道路坐标点集以及各道路路况信息
        List<TrafficStatusInfo> roads = trafficStatusResult.getRoads();
        for (int i = 0; i < roads.size(); i++) {
            int angle = roads.get(i).getAngle();
            // saveString.createFile("angle: " + angle + "@");
            // saveString.createFiletthree("angle: " + angle + "@");

            int roads_get_describeContents = roads.get(i).describeContents();

            // saveString.createFile("roads_get_describeContents: " + roads_get_describeContents + "@");
            // saveString.createFiletthree("roads_get_describeContents: " + roads_get_describeContents + "@");
            List<LatLonPoint> coordinates = roads.get(i).getCoordinates();
        /*    for (int j = 0; j < coordinates.size(); j++) {
                LatLonPoint copy = coordinates.get(j).copy();
                Log.e("345", "copy: " + copy);
            }*/
            long l = System.currentTimeMillis();
            String direction = roads.get(i).getDirection();

            // saveString.createFile("direction: " + direction + "@");
            //  saveString.createFiletthree("direction: " + direction + "@");
            String lcodes = roads.get(i).getLcodes();

            //  saveString.createFile("lcodes: " + lcodes + "@");
            //   saveString.createFiletthree("lcodes: " + lcodes + "@");
            String name = roads.get(i).getName();

            // saveString.createFile("name: " + name + "@");
            // saveString.createFiletthree("name: " + name + "@");
            float speed = roads.get(i).getSpeed();

            // saveString.createFile("speed: " + speed + "@");
            //saveString.createFiletthree("speed: " + speed + "@");
            String status1 = roads.get(i).getStatus();

            // saveString.createFile("status1: " + status1 + "@" + "\r\n");
            //  saveString.createFiletthree("status1: " + status1 + "@" + "\r\n");
            // a = 2;
        }
    }

    private void LocationMarker() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();   //设置自定义定位蓝点
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.navi_map_gps_locked));
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        // mMapView.onPause();
        webSocket();
        Log.e("132", "onPause: " + 2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
        Log.e("132", "onResume: " + 3);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapLocationClient.stopLocation();
        Log.e("132", "onStop: " + 4);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();

        if (mapLocationClient != null) {
            mapLocationClient.onDestroy();
        }

        c = 0;
    }

    private void webSocket() {
        Log.e(TAG, "we connect... 5678");
        try {
            mConnect.connect(address, new WebSocketHandler() {
                @Override
                public void onOpen() {
                    super.onOpen();
                    Log.i(TAG, "Status:Connect to " + address);
                    sendMessage();
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
//                    Log.e(TAG, "onTextMessage: " + payload != null ? payload : "");
                    //Filling
                    if (!payload.equals("Filling") && b == 0 && !payload.equals("Normal")) {

                        Toast.makeText(mContext, payload + "", Toast.LENGTH_SHORT).show();
                        PopWindow();
                        showIntentActivityNotify();
                        b = 1;
                    }
                    mConnect.sendTextMessage("I am android client5678");
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

    public void showIntentActivityNotify() {
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
        int flagAutoCancel = Notification.FLAG_AUTO_CANCEL;//在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle("油量已加满")
                .setContentText("油量已加满，点击查看详细信息")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setTicker("油量已加满、");

        //点击的意图ACTION是跳转到Intent
        Intent resultIntent = new Intent(this, DoNoteActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    //    发送信息给服务器
    private void sendMessage() {
        String name = " hello master5678 ";
        if (name != null && name.length() != 0)
            mConnect.sendTextMessage(name);
        else
            Toast.makeText(getApplicationContext(), "不能为空", Toast.LENGTH_SHORT).show();

    }

    private void PopWindow() {

        View view = LayoutInflater.from(mContext).inflate(R.layout.route_popwindow_layout, null);
        //设置屏幕的高度和宽度
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final PopupWindow pop = new PopupWindow(view, dm.widthPixels * 4 / 5, dm.heightPixels * 3 / 10);
        //如果不设置背景颜色的话，无法是pop dimiss掉。
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_background));
        pop.setOutsideTouchable(true);
        //点击空白处时，隐藏掉pop窗口
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.MyPopupWindow_anim_style);



    // * 设置popwindow的弹出的位置. *
    // 1：首先要判断是否有navigation bar。如果有的的话，要把他们的高度给加起来。 * *
    // 2：showAtLocation（）；是pop相对于屏幕而言的。 * *
    // 3：如果是 pop.showAsDropDown();则是相对于你要点击的view的位置。设置的坐标。

        if (checkDeviceHasNavigationBar2(this)) {
            int heigth_tobottom = 100 + getNavigationBarHeight();
            pop.showAtLocation(mMapView, Gravity.BOTTOM, 0, heigth_tobottom);
        } else {
            pop.showAtLocation(mMapView, Gravity.BOTTOM, 0, 100);
        }
        //设置 背景的颜色为 0.5f 的透明度
        backgroundAlpha(0.5f);

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //当popwindow消失的时候，恢复背景的颜色。
                backgroundAlpha(1.0f);
            }

        });

        Button btItem2;
        btItem2 = view.findViewById(R.id.bt_item2);
        btItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DoNoteActivity.class);
                startActivity(intent);
                pop.dismiss();
            }
        });
    }

    private static boolean checkDeviceHasNavigationBar2(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    private int getNavigationBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    private void backgroundAlpha(float v) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = v; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {

                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                demo(aMapLocation);
                StringBuilder stringBuilder = new StringBuilder();
                //定位成功回调信息，设置相关消息
                int type = aMapLocation.getLocationType();
                String address = aMapLocation.getAddress();
                stringBuilder.append(type + address);
                city = aMapLocation.getCity();

                //街道信息
                street = aMapLocation.getStreet();
                RoadTrafficQuery roadTrafficQuery = new RoadTrafficQuery(street, "310000", TrafficSearch.ROAD_LEVEL_NORMAL_WAY);
                trafficSearch.loadTrafficByRoadAsyn(roadTrafficQuery);

                //获得小点
                latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                if (latLonPoint == null) {
                    latLonPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                } else {
                    latLonPoint.setLatitude(aMapLocation.getLatitude());
                    latLonPoint.setLongitude(aMapLocation.getLongitude());
                }

                if (d == 0)
                    search("加油站", 10000);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见下方错误码表。
            }
        }
    }

    private void demo(AMapLocation aMapLocation) {
        int locationType = aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
        // saveString.createFile("获取当前定位结果来源，如网络定位结果，详见定位类型表: " + locationType + "@");
        //  saveString.createFiletwo("获取当前定位结果来源，如网络定位结果，详见定位类型表two: " + locationType + "@");
        double altitude = aMapLocation.getAltitude();//获取海拔高度信息
        //  saveString.createFile("获取海拔高度信息: " + altitude + "@");
        // saveString.createFiletwo("获取海拔高度信息: " + altitude + "@");
        float speed = aMapLocation.getSpeed();//速度  单位：米/秒
        //  saveString.createFile("速度: " + speed + "米/秒" + "@");
        // saveString.createFiletwo("速度: " + speed + "米/秒" + "@");
        float bearing = aMapLocation.getBearing();//获取方向角信息
        //  saveString.createFile("获取方向角信息: " + bearing + "@");
        // saveString.createFiletwo("获取方向角信息: " + bearing + "@");
        String buildingId = aMapLocation.getBuildingId();//获取室内定位建筑物Id
        // saveString.createFile("室内定位建筑物Id: " + buildingId + "@");
        // saveString.createFiletwo("室内定位建筑物Id: " + buildingId + "@");
        String floor = aMapLocation.getFloor();//获取室内定位楼层
        //saveString.createFile("获取室内定位楼层: " + floor + "@");
        // saveString.createFiletwo("获取室内定位楼层: " + floor + "@");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(aMapLocation.getTime());
        // saveString.createFile("日期: " + date + "@");
        // saveString.createFiletwo("日期: " + date + "@");
        String format = df.format(date);//定位时间

        saveString.createFilettfore(format + "@");

        //获取经度
        double longitude = aMapLocation.getLongitude();
        // Log.e(TAG, "demo: "+longitude );
        saveString.createFilettfore(longitude + "@");
     /*   if (c == 1){
            saveString.createFiletthree(longitude + "@");
        }*/
        //获取纬度
        double latitude = aMapLocation.getLatitude();
  /*      if (c == 1){
            saveString.createFiletthree(latitude + "@");
        }*/
        // points.add(new LatLng(longitude,latitude));
        // Log.e(TAG, "demo: "+latitude );
        saveString.createFilettfore(latitude + "@");

        // saveString.createFile("获取纬度: " + latitude + "@");
        // saveString.createFiletwo("获取纬度: " + latitude + "@");

        // saveString.createFile("获取经度: " + longitude + "@");
        // saveString.createFiletwo("获取经度: " + longitude + "@");
        float accuracy = aMapLocation.getAccuracy();//获取精度信息
        // saveString.createFile("获取精度信息: " + accuracy + "@");
        // saveString.createFiletwo("获取精度信息: " + accuracy + "@");

        // saveString.createFile("定位时间: " + format + "@");
        //  saveString.createFiletwo("定位时间: " + format + "@");
        String address = aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        // saveString.createFile("地址: " + address + "@");
        // saveString.createFiletwo("地址: " + address + "@");
        String country = aMapLocation.getCountry();//国家信息
        // saveString.createFile("国家信息: " + country + "@");
        //saveString.createFiletwo("国家信息: " + country + "@");
        String province = aMapLocation.getProvince();//省信息
        // saveString.createFile("省信息: " + province + "@");
        // saveString.createFiletwo("省信息: " + province + "@");
        String city = aMapLocation.getCity();//城市信息
        // saveString.createFile("城市信息: " + city + "@");
        // saveString.createFiletwo("城市信息: " + city + "@");
        String district = aMapLocation.getDistrict();//城区信息
        // saveString.createFile("城区信息: " + district + "@");
        // saveString.createFiletwo("城区信息: " + district + "@");
        String street = aMapLocation.getStreet();//街道信息
        // saveString.createFile("街道信息: " + street + "@");
        // saveString.createFiletwo("街道信息: " + street + "@");
        String streetNum = aMapLocation.getStreetNum();//街道门牌号信息
        // saveString.createFile("街道门牌号信息: " + streetNum + "@");
        // saveString.createFiletwo("街道门牌号信息: " + streetNum + "@");
        String cityCode = aMapLocation.getCityCode();//城市编码
        // saveString.createFile("城市编码: " + cityCode + "@");
        // saveString.createFiletwo("城市编码: " + cityCode + "@");
        String adCode = aMapLocation.getAdCode();//地区编码
        //  saveString.createFile("地区编码: " + adCode + "@");
        //  saveString.createFiletwo("地区编码: " + adCode + "@");
//  aMapLocation.getAOIName();//获取当前定位点的AOI信息
        String aoiName = aMapLocation.getAoiName();
        // saveString.createFile("获取当前定位点的AOI信息: " + aoiName + "@");
        // saveString.createFiletwo("获取当前定位点的AOI信息: " + aoiName + "@");
        int gpsAccuracyStatus = aMapLocation.getGpsAccuracyStatus();//获取GPS当前状态，返回值可参考AMapLocation类提供的常量
        // saveString.createFile("设备当前 GPS 状态: " + gpsAccuracyStatus + "@");
        // saveString.createFiletwo("设备当前 GPS 状态: " + gpsAccuracyStatus + "@");
        String locationDetail = aMapLocation.getLocationDetail();//获取定位结果来源
        // saveString.createFile("定位来源: " + locationDetail + "@");
        //saveString.createFiletwo("定位来源: " + locationDetail + "@");
        String locationDetail1 = aMapLocation.getLocationDetail();//定位信息描述
        // saveString.createFile("定位信息描述: " + locationDetail1 + "@");
        // saveString.createFiletwo("定位来源: " + locationDetail + "@");
        String errorInfo = aMapLocation.getErrorInfo();//定位出现异常的描述
        //  saveString.createFile("定位错误信息描述: " + errorInfo + "@");
        //  saveString.createFiletwo("定位错误信息描述: " + errorInfo + "@");
        int errorCode = aMapLocation.getErrorCode();//定位出现异常时的编码
        //  saveString.createFile("定位错误码: " + errorCode + "@");
        //  saveString.createFiletwo("定位错误码: " + errorCode + "@" + "\r\n");

    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mapLocationClient == null) {
            //初始化AMapLocationClient，并绑定监听
            mapLocationClient = new AMapLocationClient(getApplicationContext());

            //初始化定位参数
            mapLocationClientOption = new AMapLocationClientOption();
            //设置定位精度
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //是否返回地址信息
            mapLocationClientOption.setNeedAddress(true);
            //是否只定位一次
            mapLocationClientOption.setOnceLocation(false);
            //设置是否强制刷新WIFI，默认为强制刷新
            mapLocationClientOption.setWifiActiveScan(true);
            //是否允许模拟位置
            mapLocationClientOption.setMockEnable(false);
            //定位时间间隔
            mapLocationClientOption.setInterval(2000);
            //给定位客户端对象设置定位参数
            mapLocationClient.setLocationOption(mapLocationClientOption);
            //绑定监听
            mapLocationClient.setLocationListener(this);
            //开启定位
            mapLocationClient.startLocation();
        }

    }

    //停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mapLocationClient != null) {
            mapLocationClient.stopLocation();
            mapLocationClient.onDestroy();
        }
        mapLocationClient = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("=========", "横屏");
        } else {
            Log.d("=========", "竖屏");
        }
    }

    //点击搜索按钮
    private void search(String keyWord, int distance) {
        //  keyWord = "加油站";
        //  keyWord = "北汽4S店";

        doSearchQuery(keyWord, distance);
    }

    //搜索操作
    private void doSearchQuery(String keyWord, int distance) {

        searchQuery(keyWord);
        //点附近2000米内的搜索结果
        if (latLonPoint != null) {
            searchBound = new PoiSearch.SearchBound(latLonPoint, distance);
            poiSearch.setBound(searchBound);
        } else {
            searchBound = new PoiSearch.SearchBound(latLonPoint, distance);
        }
        poiSearch.searchPOIAsyn();//开始搜索
        d = 1;
    }

    private void searchQuery(String keyWord) {
        showProgressDialog();
        currentPage = 0;
        //第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyWord, "", city);
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);//设置回调数据的监听器
    }

    // 显示进度框
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n");
        progDialog.show();
    }

    //隐藏进度框
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
   @OnClick({/*R.id.img_left, R.id.img_right*/})
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.img_right:

                break;
            case R.id.img_left:
                finish();
                break;
        }*/
    }





    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(GaoDeActivity.this, infomation, Toast.LENGTH_SHORT).show();

    }

    // POI信息查询回调方法
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        dissmissProgressDialog();// 隐藏对话框
        if (i == 1000) {
            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                if (poiResult.getQuery().equals(query)) {// 是否是同一条
                    poiResults = poiResult;
                    // 取得搜索到的poiitems有多少页
                    // 取得第一页的poiitem数据，页数从数字0开始
                    poiItems = poiResult.getPois();
                    listview();

                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        //根据给定的参数来构造一个PoiOverlay的新对象。通过此构造函数创建Poi图层。
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        //将PoiOverlay从地图中移除。去掉PoiOverlay上所有的Marker。
                        poiOverlay.removeFromMap();
                        //将PoiOverlay加入到地图中。添加Marker到地图中。
                        poiOverlay.addToMap();//将marker绘制到地图上
                        //移动镜头到当前的视角。
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        Toast.makeText(GaoDeActivity.this, "未找到结果", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(GaoDeActivity.this, "该距离内没有找到结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            //    Log.i("---", "查询结果:" + i);
            Toast.makeText(GaoDeActivity.this, "异常代码---" + i, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getObject() != null) {
            jumpPoint(marker);// marker点击时跳动一下
            marker.showInfoWindow();
        }
        return false;
    }

    //marker点击时跳动一下
    public void jumpPoint(final Marker marker) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    // 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());
        }
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);
        TextView title = view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());

        Log.e("888", "getInfoWindow: " + marker.getZIndex() + ":   " + marker.getAlpha() + marker.getAnchorV() + ":   "
                + marker.getAnchorU() + marker.getDisplayLevel() + ":   " + marker.getGeoPoint() + marker.getPosition() + ":   " + marker.getPeriod()
        );
        ImageButton button = view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //   mPoiDetail.setVisibility(View.GONE);
    }

    private void listview() {
        listView = findViewById(R.id.listview);
        listView.setDividerHeight(1);
        MyListAdapter myListAdapter = new MyListAdapter(poiItems, getApplicationContext());
        myListAdapter.setOnInnerItemOnclickListener(this);
        listView.setAdapter(myListAdapter);
      //  listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(mContext, "整体item" + position, Toast.LENGTH_SHORT).show();
        PoiItem poiItem = poiItems.get(position);
        List<SubPoiItem> subPois = poiItem.getSubPois();
        LatLonPoint latLonPoint = subPois.get(0).getLatLonPoint();
        double latitude = latLonPoint.getLatitude();
        double longitude = latLonPoint.getLongitude();
        Log.e(TAG, "latLonPoint.getLatitude(): " + latitude);
        Log.e(TAG, "latLonPoint.getLongitude(): " + longitude);
 /*       for (int i = 0; i < subPois.size(); i++) {
            Log.e(TAG, "subPois.size(): "+subPois.size());
            LatLonPoint latLonPoint = subPois.get(i).getLatLonPoint();
            Log.e(TAG, "latLonPoint.getLatitude(): "+latLonPoint.getLatitude());
            Log.e(TAG, "latLonPoint.getLongitude(): "+latLonPoint.getLongitude() );
        }*/
        double latitude2 = this.latLonPoint.getLatitude();
        double longitude2 = this.latLonPoint.getLongitude();
        LatLng latLng = new LatLng(latitude2, longitude2);
        MarkerOptions draggable = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .position(latLng)
                .draggable(true);
        aMap.addMarker(draggable);
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.item_address:
                //   Toast.makeText(mContext, "局部item" + position, Toast.LENGTH_SHORT).show();

                break;
            case R.id.item_name:
                //   Toast.makeText(mContext, "局部item" + position, Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
