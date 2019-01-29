package com.feiyou.headstyle.ui.fragment.sub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by myflying on 2018/11/26.
 */
public class FollowFragment extends BaseFragment {

    public static FollowFragment getInstance() {
        return new FollowFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab_follow, null);
        ButterKnife.bind(this, root);
        return root;
    }
}
