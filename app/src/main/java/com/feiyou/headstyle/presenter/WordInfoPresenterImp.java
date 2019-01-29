package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.WordInfoRet;
import com.feiyou.headstyle.bean.WordTypeRet;
import com.feiyou.headstyle.model.WordInfoModelImp;
import com.feiyou.headstyle.model.WordTypeModelImp;
import com.feiyou.headstyle.view.WordInfoView;
import com.feiyou.headstyle.view.WordTypeView;

/**
 * Created by iflying on 2018/1/9.
 */

public class WordInfoPresenterImp extends BasePresenterImp<WordInfoView, WordInfoRet> implements WordInfoPresenter {
    private Context context = null;
    private WordInfoModelImp wordInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public WordInfoPresenterImp(WordInfoView view, Context context) {
        super(view);
        wordInfoModelImp = new WordInfoModelImp(context);
    }

    @Override
    public void getWordInfoByType(String cid) {
        wordInfoModelImp.getWordInfoByType(cid, this);
    }
}
