package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.AddCollectionRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.model.AddCollectionModelImp;
import com.feiyou.headstyle.model.AddNoteModelImp;
import com.feiyou.headstyle.view.AddCollectionView;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public class AddCollectionPresenterImp extends BasePresenterImp<IBaseView, AddCollectionRet> implements AddCollectionPresenter {
    private Context context = null;
    private AddCollectionModelImp addCollectionModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public AddCollectionPresenterImp(IBaseView view, Context context) {
        super(view);
        addCollectionModelImp = new AddCollectionModelImp(context);
    }

    @Override
    public void addCollection(String uid, String imgId) {
        addCollectionModelImp.addCollection(uid, imgId, this);
    }
}
