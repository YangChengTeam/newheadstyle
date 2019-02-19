package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.HeadInfo;
import com.feiyou.headstyle.ui.custom.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

public class HeadShowItemAdapter extends BaseAdapter {

    private static final String TAG = "HeadShowItemAdapter";

    private Context mContext;

    private List<HeadInfo> heads;

    private int ivWidth;

    private int ivHeight;

    private int showShape = 1; //展示的形状.1,正方形,2圆形

    public HeadShowItemAdapter(Context mContext, List<HeadInfo> heads, int shape) {
        super();
        this.mContext = mContext;
        this.heads = heads;
        ivWidth = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(66);
        this.showShape = shape;
    }

    public void setShowShape(int showShape) {
        this.showShape = showShape;
    }

    public List<HeadInfo> getHeads() {
        return heads;
    }

    public void setHeads(List<HeadInfo> heads) {
        this.heads = heads;
    }

    public void addItemData(HeadInfo imageUrl) {
        if (heads == null) {
            heads = new ArrayList<HeadInfo>();
        }
        heads.add(imageUrl);
    }

    public void addDatas(List<HeadInfo> images) {
        if (heads == null) {
            heads = new ArrayList<HeadInfo>();
        } else {
            heads.clear();
        }
        heads.addAll(images);
    }

    public void clear() {
        heads.clear();
    }

    public void remove(int index) {
        if (index > -1 && index < heads.size()) {
            heads.remove(index);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return heads != null ? heads.size() : 0;
    }

    @Override
    public Object getItem(int pos) {
        return heads.get(pos);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_new_item, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.mHeadShowImageView = (ImageView) convertView.findViewById(R.id.iv_show_head);
            holder.mHeadShowImageView.getLayoutParams().width = ivWidth;
            holder.mHeadShowImageView.getLayoutParams().height = ivWidth;
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions();
        options.transform(new GlideRoundTransform(mContext, SizeUtils.dp2px(117)));
        if (showShape == 1) {
            Glide.with(mContext).load(heads.get(position).getImgurl()).into(holder.mHeadShowImageView);
        } else {
            Glide.with(mContext).load(heads.get(position).getImgurl()).apply(options).into(holder.mHeadShowImageView);
        }

        return convertView;
    }


    private static class ViewHolder {
        ImageView mHeadShowImageView;
    }
}