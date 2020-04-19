package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
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
                .setText(R.id.tv_user_sign, StringUtils.isEmpty(item.getIntro()) ? "该用户比较懒~" : item.getIntro())
                .setText(R.id.tv_user_nick_name, item.getNickname());

        RequestOptions options = new RequestOptions();
        options.override(SizeUtils.dp2px(60), SizeUtils.dp2px(60));
        options.transform(new GlideRoundTransform(mContext, 30));
        Glide.with(mContext).load(item.getUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

        if (item.getSex() > 0) {
            helper.setVisible(R.id.iv_sex, true);
            Glide.with(mContext).load(item.getSex() == 1 ? R.mipmap.sex_boy : R.mipmap.sex_girl).into((ImageView) helper.getView(R.id.iv_sex));
        } else {
            helper.setVisible(R.id.iv_sex, false);
        }
        helper.addOnClickListener(R.id.layout_follow).addOnClickListener(R.id.iv_user_head);
    }
}