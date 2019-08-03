package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.EveryDayHbWrapper;
import com.feiyou.headstyle.bean.ExchangeInfo;
import com.feiyou.headstyle.bean.ReceiveUserInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 2018/1/8.
 */

public class ReceiveUserListAdapter extends BaseQuickAdapter<ReceiveUserInfo, BaseViewHolder> {

    private Context mContext;

    public ReceiveUserListAdapter(Context context, List<ReceiveUserInfo> datas) {
        super(R.layout.receive_user_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, ReceiveUserInfo item) {

        SimpleDateFormat tempSdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String tempTime = TimeUtils.date2String(TimeUtils.millis2Date(item.getAddtime() * 1000), tempSdf);

        helper.setText(R.id.tv_user_name, item.getNickname())
                .setText(R.id.tv_receive_date, tempTime)
                .setText(R.id.tv_receive_money, item.getCash() + "元");

        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, 6));

        Glide.with(mContext).load(item.getUserimg()).apply(options).into((ImageView) helper.getView(R.id.iv_receive_user_head));
    }
}