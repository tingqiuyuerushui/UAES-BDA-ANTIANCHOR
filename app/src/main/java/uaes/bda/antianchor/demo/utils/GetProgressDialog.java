package uaes.bda.antianchor.demo.utils;


import android.app.ProgressDialog;
import android.content.Context;

public class GetProgressDialog {
	private static ProgressDialog dialogDefaultMessage = null;
	private static ProgressDialog dialogSetMessage = null;
	public static ProgressDialog getProgressDialog(Context context, String msg){
		//		if(dialogSetMessage == null){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(msg);
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		/*}else{
			dialogSetMessage = new ProgressDialog(context);
			dialogSetMessage.setMessage(msg);
		}*/
		return dialog;
	}
	public static ProgressDialog getProgressDialog(Context context){
		//		if(dialogDefaultMessage == null){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage("正在获取，请等待..");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		//		}
		return dialog;
	}
	public static ProgressDialog getProgressDialogMessage(Context context, String message){
		//		if(dialogDefaultMessage == null){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(message);
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		//		}
		return dialog;
	}
	public static ProgressDialog getUpLoadProgress(Context context){
		//		if(dialogDefaultMessage == null){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage("正在上传，请稍后..");
		dialog.setIndeterminate(true);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		//		}
		return dialog;
	}
	public static void cancel(ProgressDialog dialog){
		if(dialog != null && dialog.isShowing()){
			dialog.cancel();
			dialog = null;
		}
	}
}
