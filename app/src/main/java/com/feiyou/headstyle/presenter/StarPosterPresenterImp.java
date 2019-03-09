package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ForecastInfoRet;
import com.feiyou.headstyle.bean.StarPosterRet;
import com.feiyou.headstyle.model.ForecastModelImp;
import com.feiyou.headstyle.model.PosterModelImp;
import com.feiyou.headstyle.view.ForecastView;
import com.feiyou.headstyle.view.StarPosterView;

/**
 * Created by iflying on 2018/1/9.
 */

public class StarPosterPresenterImp extends BasePresenterImp<IBaseView, StarPosterRet> implements StarPosterPresenter {
    private Context context = null;
    private PosterModelImp posterModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public StarPosterPresenterImp(IBaseView view, Context context) {
        super(view);
        posterModelImp = new PosterModelImp(context);
    }

    @Override
    public void createPoster(String nickname, String headimg, String uniqid) {
        posterModelImp.createPoster(nickname, headimg, uniqid, this);
    }
}
