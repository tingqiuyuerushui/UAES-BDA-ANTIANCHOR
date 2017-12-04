package uaes.bda.antianchor.demo.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private int count;

    public MyFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList,
                                  List<String> titleList, int count) {
        super(fragmentManager);

        this.fragmentList = fragmentList;
        this.titleList = titleList;
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("DerekDick", "MyFragmentPagerAdapter.getItem(" + position + ")");
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        Log.d("DerekDick", "MyFragmentPagerAdapter.getCount()");
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("DerekDick", "MyFragmentPagerAdapter.getPageTitle(" + position + ")");
        return titleList.get(position);
    }
}
