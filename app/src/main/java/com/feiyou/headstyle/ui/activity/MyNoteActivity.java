package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.AddZanPresenterImp;
import com.feiyou.headstyle.presenter.DeleteNotePresenterImp;
import com.feiyou.headstyle.presenter.NoteDataPresenterImp;
import com.feiyou.headstyle.ui.adapter.MoreHeadTypeAdapter;
import com.feiyou.headstyle.ui.adapter.NoteInfoAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.ConfigDialog;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.NoteDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class MyNoteActivity extends BaseFragmentActivity implements NoteDataView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, ConfigDialog.ConfigListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.my_note_list)
    RecyclerView mNoteListView;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.layout_no_data)
    LinearLayout noDataLayout;

    ImageView mBackImageView;

    NoteInfoAdapter noteInfoAdapter;

    private NoteDataPresenterImp noteDataPresenterImp;

    private int currentPage = 1;

    private int pageSize = 30;

    BottomSheetDialog bottomSheetDialog;

    LinearLayout mDeleteLayout;

    LinearLayout mCancelLayout;

    private UserInfo userInfo;

    ConfigDialog configDialog;

    private ProgressDialog progressDialog = null;

    DeleteNotePresenterImp deleteNotePresenterImp;

    private int currentItemPos = -1;

    private boolean isMakeDelete;

    private AddZanPresenterImp addZanPresenterImp;

    BottomSheetDialog shareDialog;

    private ShareAction shareAction;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_my_note;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("我的发帖");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        mRefreshLayout.setOnRefreshListener(this);
        //设置进度View样式的大小，只有两个值DEFAULT和LARGE
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewOffset(true, -0, 200);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        mRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

        //设置触发刷新的距离
        mRefreshLayout.setDistanceToTriggerSync(200);
        //如果child是自己自定义的view，可以通过这个回调，告诉mSwipeRefreshLayoutchild是否可以滑动
        mRefreshLayout.setOnChildScrollUpCallback(null);

        configDialog = new ConfigDialog(this, R.style.login_dialog, 1, "确认删除吗?", "请你确认是否删除当前帖子?");
        configDialog.setConfigListener(this);

        userInfo = App.getApp().getmUserInfo();

        if (shareAction == null) {
            shareAction = new ShareAction(this);
            shareAction.setCallback(shareListener);//回调监听器
        }

        shareDialog = new BottomSheetDialog(this);
        View shareView = LayoutInflater.from(this).inflate(R.layout.share_dialog_view, null);
        ImageView mCloseImageView = shareView.findViewById(R.id.iv_close_share);
        LinearLayout weixinLayout = shareView.findViewById(R.id.layout_weixin);
        LinearLayout circleLayout = shareView.findViewById(R.id.layout_circle);
        LinearLayout qqLayout = shareView.findViewById(R.id.layout_qq_friends);
        LinearLayout qzoneLayout = shareView.findViewById(R.id.layout_qzone);
        weixinLayout.setOnClickListener(this);
        circleLayout.setOnClickListener(this);
        qqLayout.setOnClickListener(this);
        qzoneLayout.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
        shareDialog.setContentView(shareView);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在删除");

        bottomSheetDialog = new BottomSheetDialog(this);
        View deleteDialogView = LayoutInflater.from(this).inflate(R.layout.note_delete_dialog, null);
        mDeleteLayout = deleteDialogView.findViewById(R.id.layout_delete);
        mCancelLayout = deleteDialogView.findViewById(R.id.layout_cancel);
        bottomSheetDialog.setContentView(deleteDialogView);
        mDeleteLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);

        noteDataPresenterImp = new NoteDataPresenterImp(this, this);
        deleteNotePresenterImp = new DeleteNotePresenterImp(this, this);
        noteInfoAdapter = new NoteInfoAdapter(this, null, 2);
        mNoteListView.setLayoutManager(new LinearLayoutManager(this));
        addZanPresenterImp = new AddZanPresenterImp(this, this);
        mNoteListView.setAdapter(noteInfoAdapter);
        //mNoteListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), SizeUtils.dp2px(8)));
        noteInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyNoteActivity.this, CommunityArticleActivity.class);
                intent.putExtra("msg_id", noteInfoAdapter.getData().get(position).getId());
                startActivity(intent);
            }
        });

        noteInfoAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                noteDataPresenterImp.getMyNoteList(currentPage, userInfo != null ? userInfo.getId() : "");
            }
        }, mNoteListView);

        noteInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentItemPos = position;

                if (view.getId() == R.id.layout_operation) {
                    if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
                        bottomSheetDialog.show();
                    }
                }
                if (view.getId() == R.id.iv_user_head) {
                    Intent intent = new Intent(MyNoteActivity.this, UserInfoActivity.class);
                    intent.putExtra("user_id", userInfo != null ? userInfo.getId() : "");
                    startActivity(intent);
                }

                if (view.getId() == R.id.layout_item_zan) {
                    String messageId = noteInfoAdapter.getData().get(position).getId();
                    addZanPresenterImp.addZan(1, App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "", noteInfoAdapter.getData().get(position).getUserId(), messageId, "", "", 1);
                }

                //分享
                if (view.getId() == R.id.layout_note_share) {
                    if (shareDialog != null && !shareDialog.isShowing()) {
                        shareDialog.show();
                    }
                }
            }
        });

        mNoteListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                mRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        noteDataPresenterImp.getMyNoteList(currentPage, userInfo != null ? userInfo.getId() : "");
    }

    @OnClick(R.id.tv_send_note)
    void sendContent() {
        Intent intent = new Intent(this, PushNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            noDataLayout.setVisibility(View.GONE);
            mNoteListView.setVisibility(View.VISIBLE);

            if (configDialog != null && configDialog.isShowing()) {
                configDialog.dismiss();
            }

            if (tData instanceof NoteInfoRet) {
                if (((NoteInfoRet) tData).getData() != null && ((NoteInfoRet) tData).getData().size() > 0) {
                    if (currentPage == 1) {
                        noteInfoAdapter.setNewData(((NoteInfoRet) tData).getData());
                    } else {
                        noteInfoAdapter.addData(((NoteInfoRet) tData).getData());
                    }

                    if (((NoteInfoRet) tData).getData().size() == pageSize) {
                        noteInfoAdapter.loadMoreComplete();
                    } else {
                        noteInfoAdapter.loadMoreEnd(true);
                    }
                } else {
                    noDataLayout.setVisibility(View.VISIBLE);
                    mNoteListView.setVisibility(View.GONE);
                }
            }

            if (tData instanceof ZanResultRet) {
                int tempNum = noteInfoAdapter.getData().get(currentItemPos).getZanNum();
                if (((ZanResultRet) tData).getData().getIsZan() == 0) {
                    tempNum = tempNum - 1;
                } else {
                    tempNum = tempNum + 1;
                }

                noteInfoAdapter.getData().get(currentItemPos).setZanNum(tempNum);
                noteInfoAdapter.getData().get(currentItemPos).setIsZan(((ZanResultRet) tData).getData().getIsZan());
                noteInfoAdapter.notifyDataSetChanged();
            }

            if (isMakeDelete) {
                ToastUtils.showLong("删除成功");
                currentPage = 1;
                isMakeDelete = false;
                noteDataPresenterImp.getMyNoteList(currentPage, userInfo != null ? userInfo.getId() : "");
            }

        } else {
            noDataLayout.setVisibility(View.VISIBLE);
            mNoteListView.setVisibility(View.GONE);
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mRefreshLayout.setRefreshing(false);
        noDataLayout.setVisibility(View.VISIBLE);
        mNoteListView.setVisibility(View.GONE);

        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        currentPage = 1;
        noteDataPresenterImp.getMyNoteList(currentPage, userInfo != null ? userInfo.getId() : "");
    }

    @Override
    public void onClick(View view) {

        if (shareDialog != null && shareDialog.isShowing()) {
            shareDialog.dismiss();
        }

        String shareContent = "快来试试炫酷的头像吧";
        UMImage image = new UMImage(this, R.drawable.app_share);
        if (currentItemPos > -1 && noteInfoAdapter.getData() != null) {
            NoteInfo tempNoteInfo = noteInfoAdapter.getData().get(currentItemPos);
            shareContent = StringUtils.isEmpty(tempNoteInfo.getContent()) ? "快来试试炫酷的头像吧" : tempNoteInfo.getContent();
            if (tempNoteInfo.getImageArr() != null && tempNoteInfo.getImageArr().length > 0) {
                image = new UMImage(this, tempNoteInfo.getImageArr()[0]);
                image.compressStyle = UMImage.CompressStyle.QUALITY;
            }
        }

        UMWeb web = new UMWeb("http://gx.qqtn.com");
        if (shareAction != null) {
            web.setTitle(shareContent);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(shareContent);//描述
        }

        switch (view.getId()) {
            case R.id.layout_cancel:
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }
                break;
            case R.id.layout_delete:
                if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
                    bottomSheetDialog.dismiss();
                }

                if (configDialog != null && !configDialog.isShowing()) {
                    configDialog.show();
                }
                break;
            case R.id.iv_close_share:
                dismissShareView();
                break;
            case R.id.layout_weixin:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN).share();
                break;
            case R.id.layout_circle:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).share();
                break;
            case R.id.layout_qq:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QQ).share();
                break;
            case R.id.layout_qzone:
                shareAction.withMedia(web).setPlatform(SHARE_MEDIA.QZONE).share();
                break;
            default:
                break;
        }
    }

    @Override
    public void config() {
        isMakeDelete = true;
        deleteNotePresenterImp.deleteNote(userInfo != null ? userInfo.getId() : "", noteInfoAdapter.getData().get(currentItemPos).getId());
    }

    @Override
    public void cancel() {
        if (configDialog != null && configDialog.isShowing()) {
            configDialog.dismiss();
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(MyNoteActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            dismissShareView();
            Toast.makeText(MyNoteActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(MyNoteActivity.this, "取消分享", Toast.LENGTH_LONG).show();
        }
    };


    /**
     * 关闭分享窗口
     */
    public void dismissShareView() {
        if (bottomSheetDialog != null && bottomSheetDialog.isShowing()) {
            bottomSheetDialog.dismiss();
        }
    }
}
