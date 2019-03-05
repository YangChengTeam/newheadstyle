package com.feiyou.headstyle.ui.fragment.sub;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MyCommentRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.MyCommentPresenterImp;
import com.feiyou.headstyle.ui.adapter.MyNoticeAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.MyCommentView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2018/11/26.
 */
public class NoticeFragment extends BaseFragment implements MyCommentView {

    @BindView(R.id.my_notice_list)
    RecyclerView mNoticeListView;

    MyNoticeAdapter noticeAdapter;

    private MyCommentPresenterImp myCommentPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    public static NoticeFragment getInstance() {
        return new NoticeFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_notice, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {

        myCommentPresenterImp = new MyCommentPresenterImp(this, getActivity());

        noticeAdapter = new MyNoticeAdapter(getActivity(), null);
        mNoticeListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNoticeListView.setAdapter(noticeAdapter);

        noticeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.i("my comment pos--->" + position);
            }
        });

        myCommentPresenterImp.getMyCommentList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 3, currentPage);
        noticeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                myCommentPresenterImp.getMyCommentList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 3, currentPage);
            }
        }, mNoticeListView);
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
                noticeAdapter.setNewData(tData.getData());
            } else {
                noticeAdapter.addData(tData.getData());
            }

            if (tData.getData().size() == pageSize) {
                noticeAdapter.loadMoreComplete();
            } else {
                noticeAdapter.loadMoreEnd();
            }

        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
