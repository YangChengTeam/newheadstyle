package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ForecastInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.model.AddNoteModelImp;
import com.feiyou.headstyle.model.ForecastModelImp;
import com.feiyou.headstyle.view.ForecastView;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public class ForecastPresenterImp extends BasePresenterImp<ForecastView, ForecastInfoRet> implements ForecastPresenter {
    private Context context = null;
    private ForecastModelImp forecastModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public ForecastPresenterImp(ForecastView view, Context context) {
        super(view);
        forecastModelImp = new ForecastModelImp(context);
    }

    @Override
    public void getForecastData(String star, String day) {
        forecastModelImp.getForecastData(star, day, this);
    }
}
