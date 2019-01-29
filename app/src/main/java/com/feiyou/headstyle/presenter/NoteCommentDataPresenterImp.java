package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.model.NoteCommentDataModelImp;
import com.feiyou.headstyle.view.NoteCommentDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class NoteCommentDataPresenterImp extends BasePresenterImp<NoteCommentDataView, NoteCommentRet> implements NoteCommentDataPresenter {
    private Context context = null;
    private NoteCommentDataModelImp noteDetailDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public NoteCommentDataPresenterImp(NoteCommentDataView view, Context context) {
        super(view);
        noteDetailDataModelImp = new NoteCommentDataModelImp(context);
    }

    @Override
    public void getNoteDetailData(int page, String msgId, int type) {
        noteDetailDataModelImp.getNoteCommentData(page, msgId, type, this);
    }
}
