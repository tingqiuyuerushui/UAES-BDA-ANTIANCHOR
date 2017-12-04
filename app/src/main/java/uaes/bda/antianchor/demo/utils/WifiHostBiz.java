/**
 * 
 */
package uaes.bda.antianchor.demo.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @author 张伦
 * @Description: TODO
 * @ClassName: WifiHostBiz
 * @date 2017年2月6日 上午11:20:56
 */
public class WifiHostBiz {
	private final String TAG = "WifiHostBiz";
	private WifiManager wifiManager;
	private String WIFI_HOST_SSID = "freeWifi";
	private String WIFI_HOST_PRESHARED_KEY = "12345678";// 密码必须大于8位数
	private String APSSID = "";
	private String APPWD = "";
	public WifiHostBiz(Context context, String APSSID, String APPWD) {
		super();  
		this.APSSID = APSSID;
		this.APPWD = APPWD;
		//获取wifi管理服务  
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}  

	/**判断热点开启状态*/  
	public boolean isWifiApEnabled() {  
		return getWifiApState() == WIFI_AP_STATE.WIFI_AP_STATE_ENABLED;  
	}  
	/** 
	 * check whether wifi hotspot on or off 
	 */  
	public boolean isApOn() {  
		try {  
			Method method = wifiManager.getClass().getDeclaredMethod("isWifiApEnabled");
			method.setAccessible(true);  
			return (Boolean) method.invoke(wifiManager);
		}  
		catch (Throwable ignored) {}
		return false;  
	}  
	private WIFI_AP_STATE getWifiApState(){  
		int tmp;  
		try {  
			Method method = wifiManager.getClass().getMethod("getWifiApState");
			tmp = ((Integer) method.invoke(wifiManager));
			// Fix for Android 4  
			if (tmp > 10) {  
				tmp = tmp - 10;  
			}  
			return WIFI_AP_STATE.class.getEnumConstants()[tmp];  
		} catch (Exception e) {
			// TODO Auto-generated catch block  
			e.printStackTrace();  
			return WIFI_AP_STATE.WIFI_AP_STATE_FAILED;  
		}  
	}  

	public enum WIFI_AP_STATE {  
		WIFI_AP_STATE_DISABLING, WIFI_AP_STATE_DISABLED, WIFI_AP_STATE_ENABLING,  WIFI_AP_STATE_ENABLED, WIFI_AP_STATE_FAILED  
	}  

	/** 
	 * wifi热点开关 
	 * @param enabled   true：打开  false：关闭 
	 * @return  true：成功  false：失败 
	 */  
	public boolean setWifiApEnabled(boolean enabled) {  
		System.out.println(TAG + ":开启热点");
		if (enabled) { // disable WiFi in any case  
			//wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi  
			wifiManager.setWifiEnabled(false);  
			System.out.println(TAG + ":关闭wifi");
		}else{  
			//	            wifiManager.setWifiEnabled(true);  
		}  
		try {  
			//热点的配置类  
			WifiConfiguration apConfig = new WifiConfiguration();
			//配置热点的名称(可以在名字后面加点随机数什么的)  
			apConfig.SSID = APSSID;  
			//配置热点的密码  
			apConfig.preSharedKey = APPWD;  
			//安全：WPA2_PSK  
			apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			//通过反射调用设置热点  
			Method method = wifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			//返回热点打开状态  
			return (Boolean) method.invoke(wifiManager, apConfig, enabled);
		} catch (Exception e) {
			e.printStackTrace();
			return false;  
		}  
	} 
	//获取WLAN SSID
	public String getWifiApSSID() {
		Method method = null;
		String SSID = null;
		try {
			method = wifiManager.getClass().getMethod("getWifiApConfiguration");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			WifiConfiguration apConfig = (WifiConfiguration) method.invoke(wifiManager);
			SSID = apConfig.SSID;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i(TAG, "getWifiApSSID -> " + SSID);
		return SSID;
	}
	//获取WLAN　密码
	public String getWifiApSharedKey() {
		Method method = null;
		String SharedKey = null;
		try {
			method = wifiManager.getClass().getMethod("getWifiApConfiguration");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			WifiConfiguration apConfig = (WifiConfiguration) method.invoke(wifiManager);
			SharedKey = apConfig.preSharedKey;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SharedKey;
	}
	//获取链接到当前热点的设备IP：

	@SuppressWarnings({ "rawtypes", "resource", "unchecked" })
	private ArrayList getConnectedHotIP() {
		ArrayList connectedIP = new ArrayList();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"/proc/net/arp"));
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.split(" +");
				if (splitted != null && splitted.length >= 4) {
					String ip = splitted[0];
					connectedIP.add(ip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connectedIP;
	}
	//输出链接到当前设备的IP地址
	@SuppressWarnings("rawtypes")
	public String printHotIp() {

		ArrayList connectedIP = getConnectedHotIP();
		StringBuilder resultList = new StringBuilder();
		for ( int i = 0;i < connectedIP.size();i++) {
			resultList.append(connectedIP.get(i));
//			resultList.append("\n");
		}
		System.out.print(resultList);
		Log.d(TAG,"---->>heww resultList="+resultList);
		return resultList.toString();
	}
}
