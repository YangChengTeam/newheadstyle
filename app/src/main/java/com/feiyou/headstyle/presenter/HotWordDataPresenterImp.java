package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.LetterInfoRet;
import com.feiyou.headstyle.bean.SearchHotWordRet;
import com.feiyou.headstyle.model.HotWordDataModelImp;
import com.feiyou.headstyle.model.LetterDataModelImp;
import com.feiyou.headstyle.view.HotWordDataView;
import com.feiyou.headstyle.view.LetterDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class HotWordDataPresenterImp extends BasePresenterImp<IBaseView, SearchHotWordRet> implements HotWordDataPresenter {
    private Context context = null;
    private HotWordDataModelImp hotWordDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public HotWordDataPresenterImp(IBaseView view, Context context) {
        super(view);
        hotWordDataModelImp = new HotWordDataModelImp(context);
    }

    @Override
    public void getTagData() {
        hotWordDataModelImp.getTagData(this);
    }
}
