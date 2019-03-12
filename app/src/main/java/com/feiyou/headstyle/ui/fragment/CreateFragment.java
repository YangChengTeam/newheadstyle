package com.feiyou.headstyle.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.base.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by myflying on 2019/3/12.
 */
public class CreateFragment extends BaseFragment {
    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create, null);
        ButterKnife.bind(this, root);
        return root;
    }
}
