package uaes.bda.antianchor.demo.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public abstract class NetWorkHandler extends Handler {
	private Dialog dialog;
	private Context context;
	

	public NetWorkHandler(Context context, Dialog dialog) {
		super();
		this.context = context;
		this.dialog = dialog;
	}

	public abstract void successMessage();
	public abstract void errorMessage(Context context, Message msg);
	public abstract void netTimeOutMakeText();

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if(dialog != null && dialog.isShowing()){
			dialog.cancel();
			dialog.dismiss();
			dialog = null;
		}
		switch (msg.what) {
		case 1:
			successMessage();
			break;
			
		case 2:
			errorMessage(context, msg);
//			FinalToast.ErrorMessage(context, msg);
			break;
		case 3:
//			FinalToast.netTimeOutMakeText(context);
			netTimeOutMakeText();
			break;
		}
		super.handleMessage(msg);
	}
}
