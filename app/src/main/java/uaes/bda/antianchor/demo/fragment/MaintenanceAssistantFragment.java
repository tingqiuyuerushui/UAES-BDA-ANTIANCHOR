package uaes.bda.antianchor.demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.activity.OilStatusActivity;

import uaes.bda.antianchor.demo.activity.SparkingPlugsActivity;
import uaes.bda.antianchor.demo.utils.FinalToast;

/**
 * Created by lun.zhang on 4/23/2017.
 */

public class MaintenanceAssistantFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.re_spark_plug)
    RelativeLayout reSparkPlug;
    @BindView(R.id.re_oil)
    RelativeLayout reOil;
    private Context context;
    private View view;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_maintenance_assistant, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.re_spark_plug, R.id.re_oil})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.re_spark_plug:
                intent.setClass(context, SparkingPlugsActivity.class);
                startActivity(intent);
                break;
            case R.id.re_oil:
               intent.setClass(context, OilStatusActivity.class);

                startActivity(intent);
                break;
        }
    }
}
