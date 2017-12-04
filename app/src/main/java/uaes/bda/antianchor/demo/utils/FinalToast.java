package uaes.bda.antianchor.demo.utils;

import android.content.Context;
import android.os.Message;
import android.widget.Toast;

public  class FinalToast {
	public static void netTimeOutMakeText(Context context){
		Toast.makeText(context, "网络链接失败请检查网络", 1000).show();
	}
	
	public static void ErrorMessage(Context context, Message msg){
		Toast.makeText(context, msg.obj.toString(), 1000).show();
	}
	public static void ErrorContext(Context context, String msg){
		Toast.makeText(context, msg, 1000).show();
	}
}
