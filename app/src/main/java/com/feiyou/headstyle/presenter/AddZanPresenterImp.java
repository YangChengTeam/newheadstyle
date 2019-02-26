package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.ZanResultRet;
import com.feiyou.headstyle.model.AddNoteModelImp;
import com.feiyou.headstyle.model.AddZanModelImp;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public class AddZanPresenterImp extends BasePresenterImp<IBaseView, ZanResultRet> implements AddZanPresenter {
    private Context context = null;
    private AddZanModelImp addZanModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public AddZanPresenterImp(IBaseView view, Context context) {
        super(view);
        addZanModelImp = new AddZanModelImp(context);
    }

    @Override
    public void addZan(int type, String userId, String messageId, String commentId, String repeatId,int modelType) {
        addZanModelImp.addZan(type, userId, messageId, commentId, repeatId, modelType,this);
    }
}
