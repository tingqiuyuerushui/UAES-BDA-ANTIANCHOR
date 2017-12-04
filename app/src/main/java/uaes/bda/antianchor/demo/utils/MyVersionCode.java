package uaes.bda.antianchor.demo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;


public class MyVersionCode {

	public static int getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			int version = info.versionCode;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	public static float getVersionName(Context context){
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			float versionName = Float.parseFloat(info.versionName);
			return versionName;
		} catch (Exception e) {
			Log.e("getVerNameException", e.toString());
			return 0;
		}
	}
}
