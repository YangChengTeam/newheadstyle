package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestMsgInfo;
import com.feiyou.headstyle.ui.custom.BlurTransformation;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class TestChatImageListAdapter extends BaseQuickAdapter<TestMsgInfo, BaseViewHolder> {

    private Context mContext;

    int lastIndex = -1;

    public TestChatImageListAdapter(Context context, List<TestMsgInfo> datas) {
        super(R.layout.msg_image_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final TestMsgInfo item) {

        LinearLayout leftImageLayout = helper.itemView.findViewById(R.id.layout_left_img);

        switch (item.getType()) {
            case TestMsgInfo.TYPE_RECEIVED:
                helper.setVisible(R.id.left_layout, true);

                if (!StringUtils.isEmpty(item.getImgUrl())) {
                    leftImageLayout.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(item.getImgUrl()).into((ImageView) helper.itemView.findViewById(R.id.left_img));
                } else {
                    leftImageLayout.setVisibility(View.GONE);
                }

                if (!StringUtils.isEmpty(item.getResultImageUrl())) {
                    helper.setVisible(R.id.layout_left_result, true);
                    Glide.with(mContext).load(item.getResultImageUrl()).apply(RequestOptions.bitmapTransform(new BlurTransformation(15, 2))).into((ImageView) helper.itemView.findViewById(R.id.iv_left_result));
                    helper.addOnClickListener(R.id.layout_left_result);
                }

                helper.setVisible(R.id.right_layout, false);
                helper.setText(R.id.left_msg, item.getContent());
                break;
            case TestMsgInfo.TYPE_RECEIVED_IMAGE:

                break;
            case TestMsgInfo.TYPE_SENT:
                helper.setVisible(R.id.left_layout, false);
                helper.setVisible(R.id.right_layout, true);
                helper.setText(R.id.right_msg, item.getContent());

                RequestOptions options = new RequestOptions();
                options.transform(new GlideRoundTransform(mContext, 24));
                Glide.with(mContext).load(item.getImgUrl()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

                break;
            default:
                break;
        }

    }
}