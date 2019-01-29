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

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dingmouren.layoutmanagergroup.viewpager.OnViewPagerListener;
import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.VideoInfo;
import com.feiyou.headstyle.ui.activity.VideoShowActivity;
import com.feiyou.headstyle.ui.adapter.VideoListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/26.
 */
public class VideoFragment extends BaseFragment {

    @BindView(R.id.video_list)
    RecyclerView mVideoListView;

    VideoListAdapter videoListAdapter;

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

        List<VideoInfo> list = new ArrayList<>();
        list.add(new VideoInfo());
        list.add(new VideoInfo());
        list.add(new VideoInfo());
        list.add(new VideoInfo());
        list.add(new VideoInfo());
        list.add(new VideoInfo());

        videoListAdapter = new VideoListAdapter(getActivity(), list);
        mVideoListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mVideoListView.setAdapter(videoListAdapter);

        videoListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showVideo();
            }
        });
    }

    void showVideo() {
        Intent intent = new Intent(getActivity(), VideoShowActivity.class);
        startActivity(intent);
    }
}
