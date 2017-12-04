package uaes.bda.antianchor.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;

/**
 * Created by lun.zhang on 10/20/2017.
 */

public class LowOilWarningSettingActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
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
    @BindView(R.id.tv_setting_value)
    TextView tvSettingValue;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private Context context;
    private ProgressDialog dialog;
    private String percent = "30";
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                // 处理UI更新等操作
                case 1:
                    break;
                case 2:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 3:
                    FinalToast.netTimeOutMakeText(context);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_low_oil_warning_setting);
        ButterKnife.bind(this);
        context = this;
        context.setTheme(R.style.AppTheme);
        dialog = GetProgressDialog.getProgressDialog(context);
        initView();
    }

    private void initView() {
        title.setText("低油量预警值设置");
        imgLeft.setVisibility(View.VISIBLE);
        seekBar.setOnSeekBarChangeListener(this);
    }

    private void commitPostData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlPostLowOilWarningData, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                dialog.cancel();
                if (result.length() > 0) {
                    JSONObject jsonData = JSON.parseObject(result);
                    if (jsonData.getString("status").equals("1")) {
                        MyUtil.sendHandleMsg(2, "数据提交成功", handler);
                    }
                    Log.e("result", result);

                }
            }
        });
    }

    private Map<String, String> addParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("percent", percent);
        return params;
    }

    @OnClick({R.id.img_left, R.id.tv_left, R.id.tv_more, R.id.ll_left, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_left:
            case R.id.tv_left:
            case R.id.ll_left:
                finish();
                break;
            case R.id.btn_commit:
                commitPostData();
                break;
            case R.id.tv_more:
                Intent intent = new Intent();
                intent.setClass(context, DoNoteActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("seekBar", "seekid:" + seekBar.getId() + ", progess" + progress);
        percent = progress + "";
        tvSettingValue.setText(progress + "%");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
