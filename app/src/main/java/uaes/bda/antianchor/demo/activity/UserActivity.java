package uaes.bda.antianchor.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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

public class UserActivity extends Activity {
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
    @BindView(R.id.img_user_head)
    ImageView imgUserHead;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.tv_reservation)
    TextView tvReservation;
    @BindView(R.id.ll_reseration)
    LinearLayout llReseration;
    @BindView(R.id.tv_handle_status)
    TextView tvHandleStatus;
    @BindView(R.id.ll_handle_status)
    LinearLayout llHandleStatus;
    @BindView(R.id.tv_my_car)
    TextView tvMyCar;
    @BindView(R.id.ll_my_car)
    LinearLayout llMyCar;
    @BindView(R.id.tv_history_record)
    TextView tvHistoryRecord;
    @BindView(R.id.ll_history_record)
    LinearLayout llHistoryRecord;
    @BindView(R.id.tv_add_car)
    TextView tvAddCar;
    @BindView(R.id.ll_add_car)
    LinearLayout llAddCar;
    @BindView(R.id.tv_music)
    TextView tvMusic;
    @BindView(R.id.textView13)
    TextView textView13;
    @BindView(R.id.img_music)
    ImageView imgMusic;
    @BindView(R.id.img_wifi)
    ImageView imgWifi;
    @BindView(R.id.tv_wifi)
    TextView tvWifi;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_my_setting)
    TextView tvMySetting;
    @BindView(R.id.tv_smart)
    TextView tvSmart;
    private Context context;
    private int IMGHEIGHT = 70, IMGWEIGHT = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        context = this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        imgLeft.setVisibility(View.VISIBLE);
        title.setText("个人信息");
        for (int i = 0; i < 3; i++) {
            setImgSize(i);
        }
    }

    private void setImgSize(int i) {
        Drawable drawable = null;
        switch (i) {
            case 0:
                drawable = getResources().getDrawable(R.mipmap.icon_update);
                drawable.setBounds(0, 0, IMGHEIGHT, IMGHEIGHT);
                tvUpdate.setCompoundDrawables(null, drawable, null, null);
                break;
            case 1:
                drawable = getResources().getDrawable(R.mipmap.icon_my_setting);
                drawable.setBounds(0, 0, IMGHEIGHT, IMGHEIGHT);
                tvMySetting.setCompoundDrawables(null, drawable, null, null);
                break;
            case 2:
                drawable = getResources().getDrawable(R.mipmap.icon_smart);
                drawable.setBounds(0, 0, IMGHEIGHT, IMGHEIGHT);
                tvSmart.setCompoundDrawables(null, drawable, null, null);
                break;

            default:
                break;
        }
    }

    @OnClick({R.id.ll_left, R.id.ll_reseration, R.id.ll_handle_status, R.id.ll_my_car, R.id.ll_history_record, R.id.ll_add_car})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_left:
                finish();
                break;
            case R.id.ll_reseration:
                break;
            case R.id.ll_handle_status:
                break;
            case R.id.ll_my_car:
                break;
            case R.id.ll_history_record:
                break;
            case R.id.ll_add_car:
                intent.setClass(context, ActivityUserCarInfo.class);
                startActivity(intent);
                break;
        }
    }
}
