package uaes.bda.antianchor.demo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.activity.BatteryHelperActivity;
import uaes.bda.antianchor.demo.activity.SparkingPlugsActivity;
import uaes.bda.antianchor.demo.bean.SparkingWebSocket;
import uaes.bda.antianchor.demo.constant.Const;

/**
 * Created by lun.zhang on 11/14/2017.
 */

public class SparkingWebSocketService extends Service {
    private WebSocketConnection sparkingConnect;
    final Intent intent = new Intent();
    Context context;
    NotificationCompat.Builder mBuilder;
    int notifyId = 104;
    public NotificationManager mNotificationManager;
    private Gson gson = new Gson();
    private int a = 0 ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sparkingConnect = new WebSocketConnection();
        connectWebSocket(sparkingConnect);
        intent.setAction(SparkingPlugsActivity.ACTION_UPDATEUI);

    }

    private void connectWebSocket(WebSocketConnection webSocketConnection) {

        try {
            webSocketConnection.connect(Const.UrlWebSocketGetSparkingStatus, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    super.onOpen();
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                }

                @Override
                public void onTextMessage(final String payload) {
                    super.onTextMessage(payload);
                    SparkingWebSocket sws = gson.fromJson(payload, SparkingWebSocket.class);
                    CreateNotification();
                    List<String> fault = sws.getFault();
                    for (int i = 0; i < fault.size(); i++) {
                        String s = fault.get(i);
                        if (s != null){
                            CreateNotification();
                        }
                    }
                    intent.putExtra("payload", payload);
                    sendBroadcast(intent);
                    Log.e("WebSocketMsgService:", payload);



                }
            });

        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
    //创建通知
    public void CreateNotification() {
        mBuilder.setAutoCancel(true)//点击后让通知将消失
                .setContentTitle("火花塞详情")
                .setContentText("火花塞有问题，点击查看")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_MAX)
                .setTicker("警告");
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        mNotificationManager.notify(notifyId,notification);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        //点击的意图ACTION是跳转到Intent
        Intent resultIntent = new Intent(this, SparkingPlugsActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
