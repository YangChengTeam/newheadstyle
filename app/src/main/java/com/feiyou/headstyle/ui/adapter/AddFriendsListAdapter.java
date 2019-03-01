package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class AddFriendsListAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {

    private Context mContext;

    public AddFriendsListAdapter(Context context, List<UserInfo> datas) {
        super(R.layout.add_fiends_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final UserInfo item) {

        helper.setText(R.id.tv_user_id, "头像号：" + item.getId())
                .setText(R.id.tv_user_sign, item.getIntro())
                .setText(R.id.tv_user_nick_name, item.getNickname());

        RequestOptions options = new RequestOptions();
        options.override(SizeUtils.dp2px(60), SizeUtils.dp2px(60));
        options.transform(new GlideRoundTransform(mContext, 30));
        Glide.with(mContext).load(item.getUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

        helper.setBackgroundRes(R.id.layout_follow, item.isFollow() ? R.drawable.is_follow_bg : R.drawable.into_bg);
        helper.setTextColor(R.id.tv_follow_txt, ContextCompat.getColor(mContext, item.isFollow() ? R.color.black2 : R.color.tab_select_color));
        helper.setText(R.id.tv_follow_txt, item.isFollow() ? "已关注" : "+关注");

    }
}