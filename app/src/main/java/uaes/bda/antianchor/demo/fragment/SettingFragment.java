package uaes.bda.antianchor.demo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.activity.FeatureListActivity;
import uaes.bda.antianchor.demo.activity.LowOilWarningSettingActivity;

/**
 * Created by lun.zhang on 10/20/2017.
 */

public class SettingFragment extends Fragment {
    Unbinder mUnbinder;
    @BindView(R.id.ll_low_oil_warning_setting)
    LinearLayout llLowOilWarningSetting;
    @BindView(R.id.ll_feature_list)
    LinearLayout llFeatureList;
    private Context context;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
    @OnClick({R.id.ll_low_oil_warning_setting, R.id.ll_feature_list})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_low_oil_warning_setting:
                intent.setClass(context, LowOilWarningSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_feature_list:
                intent.setClass(context, FeatureListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
