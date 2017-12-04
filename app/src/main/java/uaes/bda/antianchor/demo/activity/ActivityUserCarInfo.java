package uaes.bda.antianchor.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uaes.bda.antianchor.demo.R;

/**
 * Created by lun.zhang on 5/1/2017.
 */

public class ActivityUserCarInfo extends Activity {
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
    @BindView(R.id.et_brand)
    EditText etBrand;
    @BindView(R.id.et_models)
    EditText etModels;
    @BindView(R.id.et_car_system)
    EditText etCarSystem;
    @BindView(R.id.et_car_ID)
    EditText etCarID;
    @BindView(R.id.et_frame)
    EditText etFrame;
    @BindView(R.id.et_engine_number)
    EditText etEngineNumber;
    @BindView(R.id.checkBox2)
    CheckBox checkBox2;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car_info);
        context = this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        imgLeft.setVisibility(View.VISIBLE);
        title.setText("添加车辆信息");
        llRight.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("保存");
    }

    @OnClick({R.id.ll_left, R.id.ll_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_left:
                finish();
                break;
            case R.id.ll_right:

                break;
        }
    }
}
