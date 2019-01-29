package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.WordTypeRet;
import com.feiyou.headstyle.model.WordTypeModelImp;
import com.feiyou.headstyle.view.LetterDataView;
import com.feiyou.headstyle.view.WordTypeView;

/**
 * Created by iflying on 2018/1/9.
 */

public class WordTypePresenterImp extends BasePresenterImp<WordTypeView, WordTypeRet> implements WordTypePresenter {
    private Context context = null;
    private WordTypeModelImp wordTypeModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public WordTypePresenterImp(WordTypeView view, Context context) {
        super(view);
        wordTypeModelImp = new WordTypeModelImp(context);
    }

    @Override
    public void getWordTypeByPage(int page) {
        wordTypeModelImp.getWordTypeByPage(page, this);
    }
}
