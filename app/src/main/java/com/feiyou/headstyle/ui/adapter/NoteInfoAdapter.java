package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.bean.NoteInfo;
import com.feiyou.headstyle.ui.activity.ShowImageListActivity;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;
import com.feiyou.headstyle.utils.MyTimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class NoteInfoAdapter extends BaseQuickAdapter<NoteInfo, BaseViewHolder> {

    private Context mContext;

    private int showType = 1; //1.普通类别,2.个人操作列表

    public NoteInfoAdapter(Context context, List<NoteInfo> datas, int type) {
        super(R.layout.note_item, datas);
        this.mContext = context;
        this.showType = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final NoteInfo item) {


        Date currentDate = TimeUtils.millis2Date(item.getAddTime() != null ? item.getAddTime() * 1000 : 0);
        String tempDateStr = MyTimeUtil.isOutMouth(currentDate) ? TimeUtils.millis2String(item.getAddTime() != null ? item.getAddTime() * 1000 : 0) : MyTimeUtil.getTimeFormatText(currentDate);

        helper.setText(R.id.tv_nick_name, item.getNickname())
                .setText(R.id.tv_topic_name, item.getName())
                .setText(R.id.tv_note_date, tempDateStr)
                .setText(R.id.tv_message_count, item.getCommentNum() + "")
                .setText(R.id.tv_zan_count, item.getZanNum() + "");


        TextView contentTv = helper.getView(R.id.tv_note_content);
        contentTv.setText(Html.fromHtml(StringUtils.isEmpty(item.getContent()) ? "" : item.getContent()));

        TextView isZanTv = helper.itemView.findViewById(R.id.tv_zan_count);
        Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.is_zan);
        Drawable notZan = ContextCompat.getDrawable(mContext, R.mipmap.note_zan);
        if (item.getIsZan() == 0) {
            isZanTv.setCompoundDrawablesWithIntrinsicBounds(notZan, null, null, null);
        } else {
            isZanTv.setCompoundDrawablesWithIntrinsicBounds(isZan, null, null, null);
        }

        if (showType == 1) {
            helper.setVisible(R.id.layout_follow, true);
            helper.setVisible(R.id.layout_operation, false);
            helper.addOnClickListener(R.id.layout_follow);
        } else {
            helper.setVisible(R.id.layout_follow, false);
            helper.setVisible(R.id.layout_operation, true);
            helper.addOnClickListener(R.id.layout_operation);
        }

        helper.setBackgroundRes(R.id.layout_follow, item.getIsGuan() == 0 ? R.drawable.into_bg : R.drawable.is_follow_bg);
        helper.setTextColor(R.id.tv_follow_txt, ContextCompat.getColor(mContext, item.getIsGuan() == 0 ? R.color.tab_select_color : R.color.is_follow_color));
        helper.setText(R.id.tv_follow_txt, item.getIsGuan() == 0 ? "+关注" : "已关注");

        helper.addOnClickListener(R.id.layout_item_zan).addOnClickListener(R.id.iv_user_head);

        RequestOptions options = new RequestOptions();
        options.override(SizeUtils.dp2px(42), SizeUtils.dp2px(42));
        options.transform(new GlideRoundTransform(mContext, 21));
        //options.placeholder(R.mipmap.head_def);
        Glide.with(mContext).load(item.getUserimg()).apply(options).into((ImageView) helper.itemView.findViewById(R.id.iv_user_head));

        List<HeadInfo> headInfos = new ArrayList<>();
        String[] tempImg = item.getImageArr();
        final ArrayList<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < tempImg.length; i++) {
            HeadInfo headInfo = new HeadInfo();
            headInfo.setImgurl(tempImg[i]);
            headInfos.add(headInfo);
            imageUrls.add(tempImg[i]);
        }

        CommunityItemAdapter communityItemAdapter = new CommunityItemAdapter(mContext, headInfos, showType);
        RecyclerView noteImageListView = helper.itemView.findViewById(R.id.note_img_list);
        noteImageListView.setLayoutManager(new GridLayoutManager(mContext, 3));
        noteImageListView.setAdapter(communityItemAdapter);

        communityItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, ShowImageListActivity.class);
                intent.putExtra("image_index", position);
                intent.putStringArrayListExtra("image_list", imageUrls);
                mContext.startActivity(intent);
            }
        });

    }
}