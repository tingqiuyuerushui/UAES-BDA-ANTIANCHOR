package uaes.bda.antianchor.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
 * Created by lun.zhang on 4/26/2017.
 */

public class ActivityRegistNextStep extends Activity {
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
    @BindView(R.id.et_vehicle)
    EditText etVehicle;
    @BindView(R.id.et_car_id)
    EditText etCarId;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_step);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        title.setText("绑定车辆信息");
        imgLeft.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.ll_left, R.id.btn_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_left:
                setResult(1001);
                finish();
                break;
            case R.id.btn_finish:
                setResult(1001);
                finish();
                break;
        }
    }
}
