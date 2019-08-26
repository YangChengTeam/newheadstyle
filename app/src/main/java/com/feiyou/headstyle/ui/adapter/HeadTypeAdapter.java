package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.CategoryInfo;
import com.feiyou.headstyle.bean.HeadType;
import com.feiyou.headstyle.bean.WordInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class HeadTypeAdapter extends BaseQuickAdapter<CategoryInfo, BaseViewHolder> {

    private Context mContext;

    public HeadTypeAdapter(Context context, List<CategoryInfo> datas) {
        super(R.layout.head_type_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CategoryInfo item) {
//        LinearLayout typeLayout = helper.getView(R.id.layout_type);
//        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() / 4, ViewGroup.LayoutParams.WRAP_CONTENT);
//        typeLayout.setLayoutParams(params);

        helper.setText(R.id.iv_type_name, item.getTagsname());
        RequestOptions options = new RequestOptions().skipMemoryCache(true);
        Glide.with(mContext).load(item.getTagsimg()).apply(options).into((ImageView) helper.getView(R.id.iv_type_img));
    }
}