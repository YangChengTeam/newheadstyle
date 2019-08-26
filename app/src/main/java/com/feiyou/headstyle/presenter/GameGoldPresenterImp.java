package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.GameGoldInfoRet;
import com.feiyou.headstyle.bean.GoldDetailRet;
import com.feiyou.headstyle.model.GameGoldModelImp;
import com.feiyou.headstyle.model.GoldDetailModelImp;
import com.feiyou.headstyle.view.GoldDetailView;

/**
 * Created by iflying on 2018/1/9.
 */

public class GameGoldPresenterImp extends BasePresenterImp<IBaseView, GameGoldInfoRet> implements GameGoldPresenter {
    private Context context = null;
    private GameGoldModelImp gameGoldModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public GameGoldPresenterImp(IBaseView view, Context context) {
        super(view);
        gameGoldModelImp = new GameGoldModelImp(context);
    }

    @Override
    public void gameGold(String uid, String openId) {
        gameGoldModelImp.gameGold(uid, openId, this);
    }
}
