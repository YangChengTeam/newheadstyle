package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.TestInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class TestInfoAdapter extends BaseQuickAdapter<TestInfo, BaseViewHolder> {

    private Context mContext;

    public TestInfoAdapter(Context context, List<TestInfo> datas) {
        super(R.layout.test_info_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TestInfo item) {
        helper.setText(R.id.tv_test_title, item.getTestTitle())
                .setText(R.id.tv_test_content, item.getTestSubTitle());

        Glide.with(mContext).load(item.getTestThumb()).into((ImageView) helper.itemView.findViewById(R.id.iv_test_thumb));
    }
}