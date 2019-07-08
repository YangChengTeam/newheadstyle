package com.feiyou.headstyle.ui.fragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.ui.activity.HeadEditActivity;
import com.feiyou.headstyle.ui.adapter.GoodsListAdapter;
import com.feiyou.headstyle.ui.adapter.SignInListAdapter;
import com.feiyou.headstyle.ui.adapter.TaskListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.ui.custom.Glide4Engine;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by myflying on 2019/3/12.
 */
public class Create1Fragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.goods_list_view)
    RecyclerView mGoodListView;

    RecyclerView mSingInListView;

    RecyclerView mTaskListView;

    ImageView mSignInIv;

    GoodsListAdapter goodsListAdapter;

    SignInListAdapter signInListAdapter;

    TaskListAdapter taskListAdapter;

    View topView;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create1, null);
        ButterKnife.bind(this, root);
        initView();
        initData();
        return root;
    }

    public void initView() {
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
        View barView = new View(getActivity());
        barView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight() + SizeUtils.dp2px(6)));
        mTopBar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        mTopBar.setCenterView(barView);

        topView = LayoutInflater.from(getActivity()).inflate(R.layout.welfare_top_view, null);
        mSingInListView = topView.findViewById(R.id.sign_in_list_view);
        mTaskListView = topView.findViewById(R.id.task_list_view);
        mSignInIv = topView.findViewById(R.id.iv_sign_in);
        Glide.with(getActivity()).load(R.drawable.sign_in).into(mSignInIv);
    }

    public void initData() {
        goodsListAdapter = new GoodsListAdapter(getActivity(), null);
        mGoodListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mGoodListView.setAdapter(goodsListAdapter);
        goodsListAdapter.addHeaderView(topView);

        List<String> signList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            signList.add(i + "");
        }

        signInListAdapter = new SignInListAdapter(getActivity(), signList);
        mSingInListView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mSingInListView.setAdapter(signInListAdapter);

        List<String> taskList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            taskList.add(i + "");
        }

        taskListAdapter = new TaskListAdapter(getActivity(), taskList);
        mTaskListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskListView.setAdapter(taskListAdapter);
    }
}
