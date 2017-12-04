package uaes.bda.antianchor.demo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.constant.Const;
import uaes.bda.antianchor.demo.customsizeview.Star;
import uaes.bda.antianchor.demo.utils.FinalToast;
import uaes.bda.antianchor.demo.utils.GetProgressDialog;
import uaes.bda.antianchor.demo.utils.MyUtil;
import uaes.bda.antianchor.demo.utils.NetworkUtil;
import uaes.bda.antianchor.demo.utils.OkHttp;
import uaes.bda.antianchor.demo.utils.TimePickerDialog;

/**
 * Created by lun.zhang on 10/17/2017.
 */

public class DoNoteActivity extends Activity {
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
    @BindView(R.id.et_oil_station)
    EditText etOilStation;
    @BindView(R.id.tv_data_select)
    TextView tvDataSelect;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.et_oil_price)
    EditText etOilPrice;
    @BindView(R.id.et_fule_charge)
    EditText etFuleCharge;
    @BindView(R.id.et_oil_total_money)
    EditText etOilTotalMoney;
    @BindView(R.id.star_hp)
    Star starHp;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.ll_oil_band)
    LinearLayout llOilBand;
    private TimePickerDialog mTimePickerDialog;
    private Context context;
    private ProgressDialog dialog;
    private String str89;
    private String str92;
    private String str95;
    private String str98;
    private String strBand;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (dialog != null && dialog.isShowing()) {
                dialog.cancel();
            }
            switch (msg.what) {
                // 处理UI更新等操作
                case 1:
                    setAdapter();
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

    private void setAdapter() {

    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donote);
        ButterKnife.bind(this);
        context = this;
        context.setTheme(R.style.AppTheme);
        dialog = GetProgressDialog.getProgressDialog(context);
        initView();
        getServiceData();
    }

    private void initView() {
        title.setText("记账");
        tvRight.setText("保存");
        imgLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvDataSelect.setText(getCurrentData());
        starHp.setMark(3.0f);
        final List<String> list = new ArrayList<String>();
        list.add("89#");
        list.add("92#");
        list.add("95#");
        list.add("98#");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view;
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setTextColor(Color.WHITE);
                strBand = list.get(position);
                switch (position) {
                    case 0:
                        etOilPrice.setText(str89);
                        break;
                    case 1:
                        etOilPrice.setText(str92);
                        break;
                    case 2:
                        etOilPrice.setText(str95);
                        break;
                    case 3:
                        etOilPrice.setText(str98);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        starHp.setStarChangeLister(new Star.OnStarChangeListener() {
            @Override
            public void onStarChange(Float mark) {
                    if (mark > 4) {
                        tvComment.setText("非常满意，无可挑剔");
                    } else if (mark <= 4 && mark >= 3) {
                        tvComment.setText("比较满意，仍可改善");
                    } else if (mark < 3 && mark >= 2) {
                        tvComment.setText("一般，还需要改善");
                    } else if (mark < 2 && mark >= 1) {
                        tvComment.setText("不满意，比较差");
                    } else {
                        tvComment.setText("非常不满意，各方面都需要改善");

                    }
            }
        });
        etOilTotalMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                //延迟800ms，如果不再输入字符，则执行该线程的run方法
                handler.postDelayed(delayRun, 1000);
            }
        });
    }

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            if (etOilPrice.getText().toString().length() > 0 && etOilTotalMoney.getText().toString().length() > 0) {
                float totalFuelVolume = Float.valueOf(etOilTotalMoney.getText().toString()) / Float.valueOf(etOilPrice.getText().toString());
                DecimalFormat decimalFormat = new DecimalFormat(".00");
                etFuleCharge.setText(decimalFormat.format(totalFuelVolume));
            }
        }
    };

    private void getServiceData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlGetOilPrice, addParams(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject jsonData = JSON.parseObject(result);
                if (jsonData.getString("status").equals("1")) {
                    JSONObject data = JSON.parseObject(jsonData.getString("data"));
                    str89 = data.getString("89#");
                    str92 = data.getString("92#");
                    str95 = data.getString("95#");
                    str98 = data.getString("98#");
                    MyUtil.sendHandleMsg(1, "数据获取成功", handler);

                }
                Log.e("result", result);
            }
        });
    }

    private Map<String, String> addParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("city", "shanghai");
        return params;
    }

    private void commitPostData() {
        if (NetworkUtil.isNetworkEnabled(context) == -1 || !NetworkUtil.isNetworkAvailable(context)) {
            handler.sendEmptyMessage(3);
            return;
        }
        dialog.show();
        OkHttp.postAsync(Const.UrlPostNoteData, addPostData(), new OkHttp.DataCallBack() {
            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("error result", e.toString());
                MyUtil.sendHandleMsg(2, e.toString(), handler);
            }

            @Override
            public void requestSuccess(String result) throws Exception {
                JSONObject jsonData = JSON.parseObject(result);
                if (jsonData.getString("status").equals("1")) {
                    MyUtil.sendHandleMsg(2, "保存成功", handler);
                    finish();

                }
                Log.e("result", result);
            }
        });
    }

    private Map<String, String> addPostData() {
        Map<String, String> params = new HashMap<String, String>();
        if (etOilStation.getText().toString().length() <= 0) {
            etOilStation.setText("未知");
        }
        if (strBand.length() > 0) {
            params.put("fuelType", strBand.substring(0, strBand.indexOf("#")));
        }
        if (etFuleCharge.getText().toString().isEmpty()) {
            params.put("fuelFillingVolume", "0");
        } else {
            params.put("fuelFillingVolume", etFuleCharge.getText().toString());
        }
        params.put("stationName", etOilStation.getText().toString());
        params.put("vin", "111111");
        params.put("fuelPrice", etOilPrice.getText().toString());
        params.put("fuelFillingDate", tvDataSelect.getText().toString());
        params.put("fuelFillingCost", etOilTotalMoney.getText().toString());
        return params;
    }

    private String getCurrentData() {
        Calendar now = Calendar.getInstance();
        int mMonth = now.get(Calendar.MONTH);
        int mDay = now.get(Calendar.DAY_OF_MONTH);
        String strMonth = ((mMonth + 1) < 10) ? ("0" + (mMonth + 1)) : ((mMonth + 1) + "");
        String strDay = (mDay < 10) ? ("0" + mDay) : (mDay + "");
        return now.get(Calendar.YEAR) + "-" + strMonth + "-" + strDay;

    }

    @OnClick({R.id.img_left, R.id.ll_left, R.id.tv_data_select, R.id.btn_commit, R.id.tv_right, R.id.ll_right,R.id.ll_oil_band})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_left:
            case R.id.ll_left:
                finish();
                break;
            case R.id.tv_data_select:
                mTimePickerDialog = new TimePickerDialog(DoNoteActivity.this, tvDataSelect);
                mTimePickerDialog.showDatePickerDialog();
                break;
            case R.id.btn_commit:
                if (etFuleCharge.getText().toString().isEmpty()) {
                    Toast.makeText(context, "油量不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                commitPostData();
                break;
            case R.id.ll_right:
            case R.id.tv_right:
                if (etFuleCharge.getText().toString().isEmpty()) {
                    Toast.makeText(context, "油量不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                commitPostData();
                break;
            case R.id.ll_oil_band:
                spinner.performClick();
                break;
        }
    }
}
