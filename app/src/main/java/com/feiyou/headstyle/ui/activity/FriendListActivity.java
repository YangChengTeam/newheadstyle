package com.feiyou.headstyle.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.FriendsGroup;
import com.feiyou.headstyle.bean.FriendsGroupRet;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.FriendsDataPresenterImp;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.utils.StatusBarUtil;
import com.feiyou.headstyle.view.FriendsDataView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

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

    ImageView mBackImageView;

    private GroupAdapter gAdapter;

    private FriendsDataPresenterImp friendsDataPresenterImp;

    private List<FriendsGroup> friendsGroups;

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
        mTopBar.setTitle(getResources().getString(R.string.app_name));
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_config, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48));
        params.setMargins(0, StatusBarUtil.getStatusBarHeight(this), 0, 0);
        mTopBar.setCenterView(topSearchView);
        //mTopBar.setLayoutParams(params);

        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });

        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    public void initData() {
        mFriendsListView.setNestedScrollingEnabled(false);
        mFriendsListView.setLayoutManager(new LinearLayoutManager(this));
        mFriendsListView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.line_color)));

        mFriendsListView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

                if (gAdapter != null && !gAdapter.getmListItems().get(position).id.equals("-1")) {
//                    Logger.i("country info--->" + gAdapter.getmListItems().get(position).id + "---" + gAdapter.getmListItems().get(position).cname);
//                    if (lastCountryPosition == position) {
//                        return;
//                    }
//                    if (lastCountryPosition > -1) {
//                        gAdapter.getmListItems().get(lastCountryPosition).setSelected(false);
//                    }
//                    gAdapter.getmListItems().get(position).setSelected(true);
//                    queryCountryId = gAdapter.getmListItems().get(position).id;
//                    gAdapter.notifyDataSetChanged();
//                    lastCountryPosition = position;
                }
            }
        });

        friendsDataPresenterImp = new FriendsDataPresenterImp(this, this);
        friendsDataPresenterImp.getFriendsByUserId("1");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(FriendsGroupRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            friendsGroups = tData.getData();
            gAdapter = new GroupAdapter();
            mFriendsListView.setAdapter(gAdapter);
            gAdapter.setListItems(friendsGroups);
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    private static class GroupAdapter extends RecyclerView.Adapter<GroupViewHolder> {

        static final int VIEW_TYPE_NON_STICKY = R.layout.item_menu_main;
        static final int VIEW_TYPE_NON_STICKY_SELECTED = R.layout.item_menu_main_selected;
        static final int VIEW_TYPE_STICKY = R.layout.item_menu_sticky;

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
                    ListItem item = new ListItem(countryWrapper.getList().get(j).getId(), countryWrapper.getList().get(j).getNickname());
                    mListItems.add(item);
                }
            }

            //在特定位置增加分组索引
            StickyListItem firstSticky = new StickyListItem("-1", wrappers.get(0).getName());
            mListItems.add(0, firstSticky);

            int tempIndex = 0;
            for (int m = 1; m < wrappers.size(); m++) {
                FriendsGroup countryWrapper = wrappers.get(m);
                tempIndex += wrappers.get(m - 1).getList().size() + 1;
                Logger.i("temp index--->" + tempIndex);
                StickyListItem stickyListItem = new StickyListItem("-1", countryWrapper.getName());
                mListItems.add(tempIndex, stickyListItem);
            }

            notifyDataSetChanged();
        }
    }

    private static class GroupViewHolder extends RecyclerView.ViewHolder {
        private TextView text;

        GroupViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.tv_title);
        }

        void bind(ListItem item) {
            text.setText(item.cname);
        }
    }

    private static class ListItem {
        private String id;
        private String cname;

        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        ListItem(String id, String name) {
            this.id = id;
            this.cname = name;
        }
    }

    private static class StickyListItem extends ListItem {
        StickyListItem(String id, String text) {
            super(id, text);
        }
    }

}
