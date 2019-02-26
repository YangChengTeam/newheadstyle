package com.feiyou.headstyle.ui.fragment.sub;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.VideoInfo;
import com.feiyou.headstyle.bean.VideoInfoRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.VideoInfoPresenterImp;
import com.feiyou.headstyle.ui.activity.VideoShowActivity;
import com.feiyou.headstyle.ui.adapter.VideoListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;
import com.feiyou.headstyle.view.VideoInfoView;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/26.
 */
public class VideoFragment extends BaseFragment implements VideoInfoView {

    @BindView(R.id.video_list)
    RecyclerView mVideoListView;

    VideoListAdapter videoListAdapter;

    private VideoInfoPresenterImp videoInfoPresenterImp;

    private int randomPage;

    private int currentPage;

    private int pageSize = 30;

    private int clickPage;

    public static VideoFragment getInstance() {
        return new VideoFragment();
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_tab_video, null);
        ButterKnife.bind(this, root);
        initViews();
        return root;
    }

    public void initViews() {

        videoInfoPresenterImp = new VideoInfoPresenterImp(this, getActivity());

        videoListAdapter = new VideoListAdapter(getActivity(), null);
        mVideoListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mVideoListView.setAdapter(videoListAdapter);

        videoListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Logger.i("video_id--->" + videoListAdapter.getData().get(position).getId());

                int jumpPage = randomPage + position / pageSize;

                int jumpPosition = position % pageSize;

                Intent intent = new Intent(getActivity(), VideoShowActivity.class);
                intent.putExtra("jump_page", jumpPage);
                intent.putExtra("jump_position", jumpPosition);
                startActivity(intent);
            }
        });

        videoInfoPresenterImp.getDataList(1);
        videoListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                videoInfoPresenterImp.getDataList(1);
            }
        }, mVideoListView);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i(JSONObject.toJSONString(tData));
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData instanceof VideoInfoRet) {
                if (currentPage == 0) {
                    randomPage = ((VideoInfoRet) tData).getData().getPage();
                    currentPage = ((VideoInfoRet) tData).getData().getPage();

                    if (((VideoInfoRet) tData).getData().getList() != null) {
                        videoListAdapter.setNewData(((VideoInfoRet) tData).getData().getList());
                    }
                } else {
                    if (((VideoInfoRet) tData).getData().getList() != null) {
                        videoListAdapter.addData(((VideoInfoRet) tData).getData().getList());
                    }
                }

                if (((VideoInfoRet) tData).getData().getList().size() == pageSize) {
                    videoListAdapter.loadMoreComplete();
                } else {
                    videoListAdapter.loadMoreEnd();
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
