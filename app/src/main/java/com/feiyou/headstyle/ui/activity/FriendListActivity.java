package com.feiyou.headstyle.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FriendsGroup;
import com.feiyou.headstyle.bean.FriendsGroupRet;
import com.feiyou.headstyle.bean.FriendsInfo;
import com.feiyou.headstyle.bean.MessageEvent;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.FriendsDataPresenterImp;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.view.FriendsDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.wang.avi.AVLoadingIndicatorView;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by myflying on 2019/1/24.
 */
public class FriendListActivity extends BaseFragmentActivity implements FriendsDataView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    @BindView(R.id.friends_list)
    SwipeMenuRecyclerView mFriendsListView;

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    ImageView mBackImageView;

    TextView mConfigTextView;

    private GroupAdapter gAdapter;

    private FriendsDataPresenterImp friendsDataPresenterImp;

    private List<FriendsGroup> friendsGroups;

    RequestOptions requestOptions;

    private ArrayList<String> friendIds;

    private ArrayList<String> friendNames;

    @Override
    protected int getContextViewId() {
        return R.layout.activity_friends;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        View topFriendView = getLayoutInflater().inflate(R.layout.common_top_config, null);
        topFriendView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        mTopBar.setCenterView(topFriendView);
        mBackImageView = topFriendView.findViewById(R.id.iv_back);
        mConfigTextView = topFriendView.findViewById(R.id.tv_config);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        mConfigTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < gAdapter.getmListItems().size(); i++) {
                    if (gAdapter.getmListItems().get(i).isSelected) {
                        friendIds.add(gAdapter.getmListItems().get(i).id + "");
                        friendNames.add(gAdapter.getmListItems().get(i).cname);
                    }
                }

                if (friendIds == null || friendIds.size() == 0) {
                    ToastUtils.showLong("请选择好友");
                    return;
                }

                Intent intent = new Intent();
                intent.putStringArrayListExtra("friend_ids", friendIds);
                intent.putStringArrayListExtra("friend_names", friendNames);
                setResult(PushNoteActivity.RESULT_FRIEND_CODE, intent);

                MessageEvent friendInfosEvent = new MessageEvent("friend_ids");
                Logger.i("choose friends--->" + JSON.toJSONString(friendIds));
                friendInfosEvent.setFriendIds(JSON.toJSONString(friendIds));
                friendInfosEvent.setFriendNames(JSON.toJSONString(friendNames));
                EventBus.getDefault().post(friendInfosEvent);
                finish();
            }
        });
    }

    public void initData() {
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.head_def);
        requestOptions.transform(new GlideRoundTransform(this, 5));
        requestOptions.override(SizeUtils.dp2px(50), SizeUtils.dp2px(50));

        friendIds = new ArrayList<>();
        friendNames = new ArrayList<>();

        mFriendsListView.setNestedScrollingEnabled(false);
        mFriendsListView.setLayoutManager(new LinearLayoutManager(this));
        //mFriendsListView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.line_color)));

        mFriendsListView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                if (gAdapter != null && gAdapter.getmListItems().get(position).id > -1) {
                    Logger.i("country info--->" + gAdapter.getmListItems().get(position).id + "---" + gAdapter.getmListItems().get(position).cname);

                    boolean isFlag = !gAdapter.getmListItems().get(position).isSelected;

                    gAdapter.getmListItems().get(position).setSelected(isFlag);
                    gAdapter.notifyDataSetChanged();
                }
            }
        });

        friendsDataPresenterImp = new FriendsDataPresenterImp(this, this);
        friendsDataPresenterImp.getFriendsByUserId(App.getApp().getmUserInfo() != null ? App.getApp().getmUserInfo().getId() : "");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        avi.hide();

    }

    @Override
    public void loadDataSuccess(FriendsGroupRet tData) {
        Logger.i(JSONObject.toJSONString(tData));
        avi.hide();
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            friendsGroups = tData.getData();
            if (friendsGroups != null && friendsGroups.size() > 0) {
                mFriendsListView.setVisibility(View.VISIBLE);
                mNoDataLayout.setVisibility(View.GONE);
                gAdapter = new GroupAdapter();
                mFriendsListView.setAdapter(gAdapter);
                gAdapter.setListItems(friendsGroups);
            } else {
                mNoDataLayout.setVisibility(View.VISIBLE);
                mFriendsListView.setVisibility(View.GONE);
            }
        } else {
            avi.hide();
            mNoDataLayout.setVisibility(View.VISIBLE);
            mFriendsListView.setVisibility(View.GONE);
            ToastUtils.showLong(StringUtils.isEmpty(tData.getMsg()) ? "操作错误" : tData.getMsg());
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        avi.hide();
        mNoDataLayout.setVisibility(View.VISIBLE);
        mFriendsListView.setVisibility(View.GONE);
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

        static final int VIEW_TYPE_NON_STICKY = R.layout.friend_item_group;
        static final int VIEW_TYPE_NON_STICKY_SELECTED = R.layout.friend_group_item_selected;
        static final int VIEW_TYPE_STICKY = R.layout.friend_group_item_normal;

        private List<ListItem> mListItems = new ArrayList<>();

        @Override
        public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(viewType, parent, false);
            return new GroupViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GroupViewHolder holder, int position) {
            holder.bind(mListItems.get(position));
        }

        @Override
        public int getItemViewType(int position) {
            if (mListItems.get(position) instanceof StickyListItem) {
                return VIEW_TYPE_STICKY;
            }

            if (mListItems.get(position).isSelected) {
                return VIEW_TYPE_NON_STICKY_SELECTED;
            } else {
                return VIEW_TYPE_NON_STICKY;
            }
        }

        @Override
        public int getItemCount() {
            return mListItems.size();
        }

        public List<ListItem> getmListItems() {
            return mListItems;
        }

        void setListItems(List<FriendsGroup> wrappers) {
            mListItems.clear();

            for (int i = 0; i < wrappers.size(); i++) {
                FriendsGroup countryWrapper = wrappers.get(i);
                for (int j = 0; j < countryWrapper.getList().size(); j++) {
                    FriendsInfo friendsInfo = countryWrapper.getList().get(j);
                    ListItem item = new ListItem(friendsInfo.getId(), friendsInfo.getNickname(), friendsInfo.getUserimg());
                    mListItems.add(item);
                }
            }

            //在特定位置增加分组索引
            StickyListItem firstSticky = new StickyListItem(-1, wrappers.get(0).getName(), "");
            mListItems.add(0, firstSticky);

            int tempIndex = 0;
            for (int m = 1; m < wrappers.size(); m++) {
                FriendsGroup countryWrapper = wrappers.get(m);
                tempIndex += wrappers.get(m - 1).getList().size() + 1;
                Logger.i("temp index--->" + tempIndex);
                StickyListItem stickyListItem = new StickyListItem(-1, countryWrapper.getName(), "");
                mListItems.add(tempIndex, stickyListItem);
            }

            notifyDataSetChanged();
        }
    }

    private class GroupViewHolder extends RecyclerView.ViewHolder {
        //private TextView userIdTv;
        private TextView userNickNameTv;
        private ImageView userHeadIv;

        GroupViewHolder(View itemView) {
            super(itemView);
            userNickNameTv = itemView.findViewById(R.id.tv_user_nick_name);
            //userIdTv = itemView.findViewById(R.id.tv_user_id);
            userHeadIv = itemView.findViewById(R.id.iv_group_item_img);
        }

        void bind(ListItem item) {
            if (item.id > -1) {
                //userIdTv.setVisibility(View.VISIBLE);
                userHeadIv.setVisibility(View.VISIBLE);
                //userIdTv.setText(item.id + "");
                userNickNameTv.setText(item.cname);
                Glide.with(FriendListActivity.this).load(item.userHeadUrl).apply(requestOptions).into(userHeadIv);
            } else {
                //userIdTv.setVisibility(View.INVISIBLE);
                userHeadIv.setVisibility(View.GONE);
                userNickNameTv.setText(item.cname);
            }

        }
    }

    private static class ListItem {
        private int id;
        private String cname;
        private String userHeadUrl;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        ListItem(int id, String name, String userHeadUrl) {
            this.id = id;
            this.cname = name;
            this.userHeadUrl = userHeadUrl;
        }
    }

    private static class StickyListItem extends ListItem {
        StickyListItem(int id, String text, String headUrl) {
            super(id, text, headUrl);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popBackStack();
    }
}
