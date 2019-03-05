package com.feiyou.headstyle.ui.fragment.sub;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.MyAtMessageRet;
import com.feiyou.headstyle.bean.MyCommentRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.MyAtMessagePresenterImp;
import com.feiyou.headstyle.presenter.MyCommentPresenterImp;
import com.feiyou.headstyle.ui.adapter.MyAtMessageAdapter;
import com.feiyou.headstyle.ui.adapter.MyCommentAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.MyAtMessageView;
import com.feiyou.headstyle.view.MyCommentView;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by myflying on 2018/11/26.
 */
public class MyAtMessageFragment extends BaseFragment implements MyAtMessageView {

    @BindView(R.id.my_at_message_list)
    RecyclerView mAtMessageListView;

    MyAtMessageAdapter myAtMessageAdapter;

    private MyAtMessagePresenterImp myAtMessagePresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    public static MyAtMessageFragment getInstance() {
        return new MyAtMessageFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_at_message, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {

        myAtMessagePresenterImp = new MyAtMessagePresenterImp(this, getActivity());

        myAtMessageAdapter = new MyAtMessageAdapter(getActivity(), null);
        mAtMessageListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAtMessageListView.setAdapter(myAtMessageAdapter);

        myAtMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Logger.i("my comment pos--->" + position);
            }
        });

        myAtMessagePresenterImp.getMyAtMessageList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 1, currentPage);
        myAtMessageAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                myAtMessagePresenterImp.getMyAtMessageList(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", 1, currentPage);
            }
        }, mAtMessageListView);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(MyAtMessageRet tData) {
        Logger.i(JSONObject.toJSONString(tData));
        if (tData != null && tData.getCode() == Constants.SUCCESS) {

            if (currentPage == 1) {
                myAtMessageAdapter.setNewData(tData.getData());
            } else {
                myAtMessageAdapter.addData(tData.getData());
            }

            if (tData.getData().size() == pageSize) {
                myAtMessageAdapter.loadMoreComplete();
            } else {
                myAtMessageAdapter.loadMoreEnd();
            }

        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
