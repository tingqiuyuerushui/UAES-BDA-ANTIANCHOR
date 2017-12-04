package uaes.bda.antianchor.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uaes.bda.antianchor.demo.R;
import uaes.bda.antianchor.demo.fragment.AccumulatedOilRecords;
import uaes.bda.antianchor.demo.fragment.HistoryOfOil;
import uaes.bda.antianchor.demo.fragment.MyFragmentPagerAdapter;
import uaes.bda.antianchor.demo.fragment.SingleRefuelingRecord;
import uaes.bda.antianchor.demo.fragment.UseOilHelperFragment;
import uaes.bda.antianchor.demo.view.NoScrollViewPager;

/**
 * Created by lun.zhang on 10/18/2017.
 */

public class OilHelperActivity extends FragmentActivity {
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
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oil_helper);
        ButterKnife.bind(this);
        context = this;
        context.setTheme(R.style.AppTheme);
        initView();
    }

    private void initView() {
        title.setText("用油详情");
        imgLeft.setVisibility(View.VISIBLE);
        List<Fragment> fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new UseOilHelperFragment());
        fragmentList.add(new SingleRefuelingRecord());
        fragmentList.add(new AccumulatedOilRecords());
        fragmentList.add(new HistoryOfOil());
        List<String> titleList = new ArrayList<String>();
        titleList.add("用油助手");
        titleList.add("单次加油");
        titleList.add("累计用油");
        titleList.add("用油统计");
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentList, titleList, 4);
        viewPager.setNoScroll(true);//设置viewpager禁止滑动
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager, true);
    }

    @OnClick({R.id.img_left, R.id.ll_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_left:
            case R.id.ll_left:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("oilHelperActivity","执行了，，");
    }
}
