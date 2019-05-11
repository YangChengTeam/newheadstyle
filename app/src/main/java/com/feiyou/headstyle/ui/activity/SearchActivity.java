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
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PathUtils;
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
import com.feiyou.headstyle.presenter.RecordInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.HeadInfoAdapter;
import com.feiyou.headstyle.ui.adapter.SearchHistoryAdapter;
import com.feiyou.headstyle.ui.adapter.SearchHotWordAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.OpenDialog;
import com.feiyou.headstyle.view.HotWordDataView;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;


public class SearchActivity extends BaseFragmentActivity implements HotWordDataView, ConfigDialog.ConfigListener, OpenDialog.ConfigListener {

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

    private RecordInfoPresenterImp recordInfoPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    private List<String> historySearchList;

    ConfigDialog configDialog;

    private String mKeyWord;

    OpenDialog openDialog;

    BaseDownloadTask task;

    private int openType = 1; //下载APP, 2 打开小程序

    private SearchHotWord searchHotWord;

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
        openDialog = new OpenDialog(this, R.style.login_dialog);
        openDialog.setConfigListener(this);

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
                searchHotWord = searchHotWordAdapter.getData().get(position);
                switch (searchHotWord.getType()) {
                    case 1:
                        startSearch(searchHotWordAdapter.getData().get(position).getName());
                        break;
                    case 2:
                        if (task != null && task.isRunning()) {
                            Toasty.normal(SearchActivity.this, "正在下载打开请稍后...").show();
                        } else {
                            openType = 1;
                            if (NetworkUtils.isMobileData()) {
                                openDialog.setTitle("温馨提示");
                                openDialog.setContent("当前是移动网络，是否继续下载？");
                            } else {
                                openDialog.setTitle("打开提示");
                                openDialog.setContent("即将下载" + searchHotWord.getName());
                            }
                            openDialog.show();
                        }
                        break;
                    case 3:
                        openType = 2;
                        openDialog.setTitle("打开提示");
                        openDialog.setContent("即将打开\"" + searchHotWord.getName() + "\"小程序");
                        openDialog.show();
                        break;
                    case 4:
                        addRecord(searchHotWord.getAdId());
                        Intent intent = new Intent(SearchActivity.this, AdActivity.class);
                        intent.putExtra("open_url", searchHotWord.getJumpPath());
                        intent.putExtra("ad_title", searchHotWord.getName());
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
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
        recordInfoPresenterImp = new RecordInfoPresenterImp(this, this);
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
        historySearchList.clear();
        SPUtils.getInstance().put(Constants.SEARCH_HISTORY, "");
        searchHistoryAdapter.setNewData(null);
    }

    @Override
    public void cancel() {
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }

    @Override
    public void openConfig() {
        if (openType == 1) {
            downAppFile(searchHotWord.getJumpPath());
        }

        if (openType == 2) {
            String appId = "wxd1112ca9a216aeda"; // 填应用AppId
            IWXAPI api = WXAPIFactory.createWXAPI(this, appId);
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = searchHotWord.getOriginId(); // 填小程序原始id
            req.path = searchHotWord.getJumpPath(); //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
            api.sendReq(req);
        }

    }

    @Override
    public void openCancel() {
        if (openDialog != null && openDialog.isShowing()) {
            openDialog.dismiss();
        }
    }

    public void downAppFile(String downUrl) {
        final String filePath = PathUtils.getExternalAppFilesPath() + "/temp_app.apk";
        Logger.i("down app path --->" + filePath);

        task = FileDownloader.getImpl().create(downUrl)
                .setPath(filePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Toasty.normal(SearchActivity.this, "正在下载打开请稍后...").show();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        ToastUtils.showLong("下载完成");
                        //install(filePath);
                        AppUtils.installApp(filePath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

        task.start();
    }

    public void addRecord(String aid) {
        recordInfoPresenterImp.adClickInfo(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", aid);
    }
}
