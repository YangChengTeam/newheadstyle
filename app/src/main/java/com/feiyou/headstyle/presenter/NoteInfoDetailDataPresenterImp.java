package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.NoteInfoDetailRet;
import com.feiyou.headstyle.model.NoteInfoDetailDataModelImp;
import com.feiyou.headstyle.view.NoteInfoDetailDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class NoteInfoDetailDataPresenterImp extends BasePresenterImp<NoteInfoDetailDataView, NoteInfoDetailRet> implements NoteInfoDetailDataPresenter {
    private Context context = null;
    private NoteInfoDetailDataModelImp noteInfoDetailDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public NoteInfoDetailDataPresenterImp(NoteInfoDetailDataView view, Context context) {
        super(view);
        noteInfoDetailDataModelImp = new NoteInfoDetailDataModelImp(context);
    }

    @Override
    public void getNoteInfoDetailData(String msgId) {
        noteInfoDetailDataModelImp.getNoteInfoDetailData(msgId, this);
    }
}
