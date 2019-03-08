package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.activity.AddFriendsActivity;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.LoginDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/26.
 */
public class FollowFragment extends BaseFragment {

    @BindView(R.id.tv_follow_user)
    TextView mFollowTv;

    LoginDialog loginDialog;

    public static FollowFragment getInstance() {
        return new FollowFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_friends, null);
        ButterKnife.bind(this, root);
        return root;
    }

    public void initData() {
        loginDialog = new LoginDialog(getActivity(), R.style.login_dialog);
    }

    @OnClick(R.id.tv_follow_user)
    void followUser() {
        if (!App.getApp().isLogin) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
        startActivity(intent);
    }
}
