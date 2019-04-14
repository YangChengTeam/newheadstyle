package com.feiyou.headstyle.ui.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.HeadInfoRet;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.SearchHotWord;
import com.feiyou.headstyle.bean.SearchHotWordRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.HeadListDataPresenterImp;
import com.feiyou.headstyle.presenter.HotWordDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.SearchHistoryAdapter;
import com.feiyou.headstyle.ui.adapter.SearchHotWordAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.view.HotWordDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class SearchActivity extends BaseFragmentActivity implements HotWordDataView, ConfigDialog.ConfigListener {

    @BindView(R.id.layout_top)
    LinearLayout mTopLayout;

    @BindView(R.id.et_key_word)
    EditText mHotWordEditText;

    @BindView(R.id.hot_search_word_list)
    RecyclerView mSearchHotListView;

    @BindView(R.id.hot_search_history_list)
    RecyclerView mHistoryListView;

    @BindView(R.id.search_result_list)
    RecyclerView mSearchResultListView;

    @BindView(R.id.layout_hot)
    LinearLayout mHotLayout;

    @BindView(R.id.layout_result)
    LinearLayout mResultLayout;

    @BindView(R.id.layout_search_no_data)
    LinearLayout mNoDataLayout;

    @BindView(R.id.tv_cancel)
    TextView mCancelTextView;

    @BindView(R.id.layout_clear)
    LinearLayout mClearLayout;

    SearchHotWordAdapter searchHotWordAdapter;

    SearchHistoryAdapter searchHistoryAdapter;

    HeadInfoAdapter headInfoAdapter;

    HotWordDataPresenterImp hotWordDataPresenterImp;

    HeadListDataPresenterImp headListDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private List<String> historySearchList;

    ConfigDialog configDialog;

    private String mKeyWord;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void initData() {
        configDialog = new ConfigDialog(this, R.style.login_dialog, 1, "确认清除吗?", "请你确认是否清除搜索记录？");
        configDialog.setConfigListener(this);

        initProgress("搜索中");

        mHotWordEditText.setOnEditorActionListener(new EditorActionListener());

        //热门词
        mSearchHotListView.setLayoutManager(new GridLayoutManager(this, 2));
        searchHotWordAdapter = new SearchHotWordAdapter(this, null);
        mSearchHotListView.setAdapter(searchHotWordAdapter);
        searchHotWordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startSearch(searchHotWordAdapter.getData().get(position).getName());
            }
        });

        //搜索记录
        mHistoryListView.setLayoutManager(new GridLayoutManager(this, 3));
        searchHistoryAdapter = new SearchHistoryAdapter(this, null);
        mHistoryListView.setAdapter(searchHistoryAdapter);

        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startSearch(searchHistoryAdapter.getData().get(position));
            }
        });

        //搜索结果
        List<HeadInfo> headInfoList = new ArrayList<>();
        for (int m = 0; m < 60; m++) {
            headInfoList.add(new HeadInfo());
        }
        headInfoAdapter = new HeadInfoAdapter(this, headInfoList);
        mSearchResultListView.setLayoutManager(new GridLayoutManager(this, 3));
        mSearchResultListView.setAdapter(headInfoAdapter);

        headInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                headListDataPresenterImp.getSearchList(currentPage, StringUtils.isEmpty(mKeyWord) ? "" : mKeyWord, "");
            }
        }, mSearchResultListView);

        headInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int jumpPage = position / pageSize;
                int jumpPosition = position % pageSize;

                Logger.i("jumpPage page--->" + jumpPage + "---jumpPosition--->" + jumpPosition);

                Intent intent = new Intent(SearchActivity.this, HeadShowActivity.class);
                intent.putExtra("from_type", 4);
                intent.putExtra("key_word", mKeyWord);
                intent.putExtra("jump_page", jumpPage + 1);
                intent.putExtra("jump_position", jumpPosition);
                startActivity(intent);
            }
        });

        //请求数据
        headListDataPresenterImp = new HeadListDataPresenterImp(this, this);
        hotWordDataPresenterImp = new HotWordDataPresenterImp(this, this);
        hotWordDataPresenterImp.getTagData();
    }

    @Override
    public void onResume() {
        super.onResume();

        String tempHistory = SPUtils.getInstance().getString(Constants.SEARCH_HISTORY);

        if (!StringUtils.isEmpty(tempHistory)) {
            historySearchList = JSON.parseObject(tempHistory, new TypeReference<List<String>>() {
            });
        } else {
            historySearchList = new ArrayList<>();
        }
        searchHistoryAdapter.setNewData(historySearchList);
    }

    public void addSearchKey(String keyWord) {
        mKeyWord = keyWord;
        if (historySearchList != null) {
            if (!historySearchList.contains(keyWord)) {
                historySearchList.add(keyWord);
                SPUtils.getInstance().put(Constants.SEARCH_HISTORY, JSON.toJSONString(historySearchList));
                searchHistoryAdapter.addData(keyWord);
            }
        }
    }

    private class EditorActionListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (textView.getId()) {
                case R.id.et_key_word:
                    if (i == EditorInfo.IME_ACTION_SEARCH) {
                        if (StringUtils.isEmpty(textView.getText())) {
                            ToastUtils.showLong("请输入关键词后搜索");
                            break;
                        }
                        mKeyWord = textView.getText().toString();

                        startSearch(mKeyWord);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    public void startSearch(String keyWord) {
        currentPage = 1;
        showDialog();
        addSearchKey(keyWord);
        mHotWordEditText.setText(keyWord);
        headListDataPresenterImp.getSearchList(currentPage, keyWord, "");
        KeyboardUtils.hideSoftInput(SearchActivity.this);
    }

    @OnClick(R.id.tv_cancel)
    public void cancelSearch() {
        if (StringUtils.isEmpty(mHotWordEditText.getText())) {
            finish();
        } else {
            mHotWordEditText.setText("");
            mHotLayout.setVisibility(View.VISIBLE);
            mResultLayout.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.GONE);
            onResume();
        }
    }

    @OnClick(R.id.layout_clear)
    public void clearHistory() {
        if (searchHistoryAdapter != null && searchHistoryAdapter.getData().size() > 0) {
            if (configDialog != null && !configDialog.isShowing()) {
                configDialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (StringUtils.isEmpty(mHotWordEditText.getText())) {
            popBackStack();
        } else {
            mHotWordEditText.setText("");
            mHotLayout.setVisibility(View.VISIBLE);
            mResultLayout.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.GONE);
            onResume();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        dismissDialog();
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i(JSONObject.toJSONString(tData));
        dismissDialog();

        if (tData.getCode() == Constants.SUCCESS && tData != null) {

            if (tData instanceof SearchHotWordRet) {
                searchHotWordAdapter.addData(((SearchHotWordRet) tData).getData());
            }
            if (!StringUtils.isEmpty(mHotWordEditText.getText())) {
                mHotWordEditText.setSelection(mHotWordEditText.getText().length());
            }
            if (tData instanceof HeadInfoRet) {
                if (((HeadInfoRet) tData).getData() != null && ((HeadInfoRet) tData).getData().size() > 0) {
                    mHotLayout.setVisibility(View.GONE);
                    mResultLayout.setVisibility(View.VISIBLE);
                    mNoDataLayout.setVisibility(View.GONE);
                    if (currentPage == 1) {
                        headInfoAdapter.setNewData(((HeadInfoRet) tData).getData());
                    } else {
                        headInfoAdapter.addData(((HeadInfoRet) tData).getData());
                    }

                    if (((HeadInfoRet) tData).getData().size() == pageSize) {
                        headInfoAdapter.loadMoreComplete();
                    } else {
                        headInfoAdapter.loadMoreEnd(true);
                    }
                } else {
                    mHotLayout.setVisibility(View.GONE);
                    mResultLayout.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            headInfoAdapter.loadMoreEnd(true);
            if (currentPage == 1) {
                mHotLayout.setVisibility(View.GONE);
                mResultLayout.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        headInfoAdapter.loadMoreEnd(true);
    }

    @Override
    public void config() {
        SPUtils.getInstance().put(Constants.SEARCH_HISTORY, "");
        searchHistoryAdapter.setNewData(null);
    }

    @Override
    public void cancel() {
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }
}
