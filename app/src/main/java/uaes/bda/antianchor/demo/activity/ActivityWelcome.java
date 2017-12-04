/**
 * 
 */
package uaes.bda.antianchor.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.TabMainActivity;


/**
 * @author 张伦
 * @Description: TODO
 * @ClassName: ActivityWelcome
 * @date 2017/2/16 10:23:12
 */
public class ActivityWelcome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//取消状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ActivityWelcome.this, TabMainActivity.class);
				startActivity(intent);
				finish();
			}
		}, 2500);
	}
}
