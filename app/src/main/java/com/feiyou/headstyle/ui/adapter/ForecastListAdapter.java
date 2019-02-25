package com.feiyou.headstyle.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ForecastInfo;
import com.feiyou.headstyle.bean.StarInfo;

import java.util.List;

/**
 * Created by admin on 2018/1/8.
 */

public class ForecastListAdapter extends BaseQuickAdapter<ForecastInfo.ForecastSubInfo, BaseViewHolder> {

    private Context mContext;

    private Integer[] forecastImages = {R.mipmap.forecast1, R.mipmap.forecast2, R.mipmap.forecast3, R.mipmap.forecast4, R.mipmap.forecast5, R.mipmap.forecast6};

    private Integer[] colors = {R.color.forecast1_color, R.color.test_bg_txt_color, R.color.forecast3_color, R.color.forecast4_color, R.color.forecast5_color, R.color.forecast6_color};

    public ForecastListAdapter(Context context, List<ForecastInfo.ForecastSubInfo> datas) {
        super(R.layout.forecast_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final ForecastInfo.ForecastSubInfo item) {
        helper.setText(R.id.tv_forecast_content, item.getValue())
                .setText(R.id.tv_forecast_title, item.getName());

        if (helper.getAdapterPosition() < forecastImages.length) {
            helper.setTextColor(R.id.tv_forecast_title, ContextCompat.getColor(mContext, colors[helper.getAdapterPosition()]));
            Glide.with(mContext).load(forecastImages[helper.getAdapterPosition()]).into((ImageView) helper.itemView.findViewById(R.id.iv_forecast_img));
        }
    }
}