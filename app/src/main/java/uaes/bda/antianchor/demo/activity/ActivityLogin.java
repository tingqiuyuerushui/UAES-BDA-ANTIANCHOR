/**
 *
 */
package uaes.bda.antianchor.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.TabMainActivity;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MySharedData;

/**
 * @author 张伦
 * @Description: TODO
 * @ClassName: ActivityLogin
 * @date 2017/2/16 1:25:51
 */
public class ActivityLogin extends Activity {

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
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.checkBox)
    CheckBox checkBox;
    private Context context;
    private ProgressDialog dialog = null;
    private String strUserName, strPassword;
    private String desc, result;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog.isShowing() && dialog != null) {
                dialog.cancel();
            }
            switch (msg.what) {
                // 处理UI更新等操作
                case 1:
                    Intent intent = new Intent();
                    intent.setClass(context, TabMainActivity.class);
                    startActivity(intent);
                    MySharedData.sharedata_WriteString(context, "user_name", etUsername.getText().toString());
                    MySharedData.sharedata_WriteString(context, "password", etPassword.getText().toString());
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 2:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    FinalToast.netTimeOutMakeText(context);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bg);
        ButterKnife.bind(this);
        context = this;
        dialog = GetProgressDialog.getProgressDialogMessage(context, "正在登陆……");
        strUserName = MySharedData.sharedata_ReadString(this, "user_name");
        strPassword = MySharedData.sharedata_ReadString(this, "password");

        if (strUserName.length() > 0) {
            etUsername.setText(strUserName);
            etPassword.setText(strPassword);
        }
        initView();
    }

    private void initView() {
        llRight.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        title.setTextColor(getResources().getColor(R.color.main_color));
        tvRight.setTextColor(getResources().getColor(R.color.main_color));
        title.setText("登录");
        tvRight.setText("注册");
        checkBox.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_login, R.id.title, R.id.ll_right})
    void OnMyClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (btnLogin.getText().toString().equals("下一步")) {
                    Intent intent = new Intent();
                    intent.setClass(context, ActivityRegistNextStep.class);
                    startActivityForResult(intent, 1001);
//                    Toast.makeText(context,"正在开发中",Toast.LENGTH_SHORT).show();
                } else {
                    strUserName = etUsername.getText().toString();
                    strPassword = etPassword.getText().toString();
                    nativeLogin(strUserName, strPassword);
                }
                break;
            case R.id.title:

                break;
            case R.id.ll_right:
                displayView();
                break;
            default:
                break;
        }
    }

    private void displayView() {
        if (title.getText().toString().equals("登录")) {
            title.setText("注册");
            tvRight.setText("登录");
            etUsername.setText("");
            etPassword.setText("");
            etUsername.setHint("请输入注册账号");
            etPassword.setHint("请输入注册密码");
            tvForgetPassword.setVisibility(View.GONE);
            btnLogin.setText("下一步");
        } else if (title.getText().toString().equals("注册")) {
            title.setText("登录");
            tvRight.setText("注册");
            etUsername.setText("");
            etPassword.setText("");
            etUsername.setHint("请输入账号");
            etPassword.setHint("请输入密码");
            tvForgetPassword.setVisibility(View.VISIBLE);
            btnLogin.setText("登录");
        }
    }

    private void nativeLogin(String userName, String passWord) {
        if (userName.equals("admin") && passWord.equals("123")) {
            Message msg = new Message();
            msg.obj = "登录成功";
            msg.what = 1;
            handler.sendMessage(msg);
        } else if (userName.equals("guest") && passWord.equals("123")) {
            Message msg = new Message();
            msg.obj = "登录成功";
            msg.what = 1;
            handler.sendMessage(msg);
        } else {
            Toast.makeText(context, "账号或密码错误", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            if (title.getText().toString().equals("注册")) {
                title.setText("登录");
                tvRight.setText("注册");
                etUsername.setText("");
                etPassword.setText("");
                etUsername.setHint("请输入账号");
                etPassword.setHint("请输入密码");
                btnLogin.setText("登录");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    //	private void Login(){
//		if (NetworkUtil.isNetworkEnabled(context) == -1) {
//			handler.sendEmptyMessage(3);
//			return;
//		}
//		dialog.show();
//		TaskExecutor.executeTask(new Runnable() {
//
//			@Override
//			public void run() {
//				String jsonData = getWebServerInfo();
//				try {
//					JSONObject jsonObj = new JSONObject(jsonData);
//					desc = jsonObj.getString("desc");
//					result = jsonObj.getString("result");
//					Message msg = new Message();
//					msg.obj = desc;
//					if(desc.equals("认证通过")){
//						msg.what = 1;
//						msg.obj = "登录成功";
//					}else{
//						msg.obj = "账号或密码错误";
//						msg.what = 2;
//					}
//					handler.sendMessage(msg);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

}
