package uaes.bda.antianchor.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import uaes.bda.antianchor.demo.R;

/**
 * Created by lun.zhang on 4/19/2017.
 */

public class UserFragment extends Fragment {
    private Context context;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        view = inflater.inflate(R.layout.activity_user, container, false);
        ButterKnife.bind(this,view);
        return view;
    }
}
