package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MyCommentRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.MyCommentPresenterImp;
import com.feiyou.headstyle.presenter.VideoInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.VideoShowActivity;
import com.feiyou.headstyle.ui.adapter.MyCommentAdapter;
import com.feiyou.headstyle.ui.adapter.VideoListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.MyCommentView;
import com.feiyou.headstyle.view.VideoInfoView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2018/11/26.
 */
public class MyCommentFragment extends BaseFragment implements MyCommentView {

    @BindView(R.id.my_comment_list)
    RecyclerView mCommentListView;

    MyCommentAdapter myCommentAdapter;

    private MyCommentPresenterImp myCommentPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    public static MyCommentFragment getInstance() {
        return new MyCommentFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_comment, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {

        myCommentPresenterImp = new MyCommentPresenterImp(this, getActivity());

        myCommentAdapter = new MyCommentAdapter(getActivity(), null);
        mCommentListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentListView.setAdapter(myCommentAdapter);

        myCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Logger.i("my comment pos--->" + position);

            }
        });

        myCommentPresenterImp.getMyCommentList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 1,currentPage);
        myCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                myCommentPresenterImp.getMyCommentList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 1,currentPage);
            }
        }, mCommentListView);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(MyCommentRet tData) {
        Logger.i(JSONObject.toJSONString(tData));
        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            if (currentPage == 1) {
                myCommentAdapter.setNewData(tData.getData());
            } else {
                myCommentAdapter.addData(tData.getData());
            }

            if (tData.getData().size() == pageSize) {
                myCommentAdapter.loadMoreComplete();
            } else {
                myCommentAdapter.loadMoreEnd();
            }

        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
