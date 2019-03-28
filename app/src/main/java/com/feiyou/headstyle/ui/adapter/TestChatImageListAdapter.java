package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.TestMsgInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

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

        LinearLayout leftImageLayout = helper.getView(R.id.layout_left_img);
        LinearLayout leftTxtLayout = helper.getView(R.id.layout_left_text);

        switch (item.getType()) {
            case TestMsgInfo.TYPE_RECEIVED:
                helper.setVisible(R.id.left_layout, true);
                helper.setVisible(R.id.right_layout, false);
                leftTxtLayout.setVisibility(View.VISIBLE);

                if (!StringUtils.isEmpty(item.getImgUrl())) {
                    leftImageLayout.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(item.getImgUrl()).into((ImageView) helper.itemView.findViewById(R.id.left_img));
                } else {
                    leftImageLayout.setVisibility(View.GONE);
                }

                if (!StringUtils.isEmpty(item.getResultImageUrl())) {
                    helper.setVisible(R.id.layout_left_result, true);
                    leftTxtLayout.setVisibility(View.GONE);
                    RequestOptions options = new RequestOptions();
                    options.transform(new BlurTransformation(15, 1));
                    Glide.with(mContext).load(item.getResultImageUrl()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_left_result));
                    helper.addOnClickListener(R.id.layout_left_result);
                }

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