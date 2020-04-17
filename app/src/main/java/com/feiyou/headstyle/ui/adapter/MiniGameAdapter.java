package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.cmcm.cmgame.gamedata.bean.GameInfo;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CategoryInfo;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.OpenGlUtils;

/**
 * Created by admin on 2018/1/8.
 */

public class MiniGameAdapter extends BaseQuickAdapter<GameInfo, BaseViewHolder> {

    private Context mContext;

    public MiniGameAdapter(Context context, List<GameInfo> datas) {
        super(R.layout.mini_game_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, GameInfo item) {
        helper.setText(R.id.tv_game_name, item.getName()).setText(R.id.tv_play_num, (int) OpenGlUtils.rnd(10000, 20000) + "人在玩");
        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        Glide.with(mContext).load(item.getIconUrl()).apply(options).into((ImageView) helper.getView(R.id.iv_game_cover));
    }
}