package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.NoteCommentRet;
import com.feiyou.headstyle.bean.NoteSubCommentRet;
import com.feiyou.headstyle.model.NoteCommentDataModelImp;
import com.feiyou.headstyle.model.NoteSubCommentDataModelImp;

/**
 * Created by iflying on 2018/1/9.
 */

public class NoteSubCommentDataPresenterImp extends BasePresenterImp<IBaseView, NoteSubCommentRet> implements NoteSubCommentDataPresenter {
    private Context context = null;
    private NoteSubCommentDataModelImp noteSubCommentDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public NoteSubCommentDataPresenterImp(IBaseView view, Context context) {
        super(view);
        noteSubCommentDataModelImp = new NoteSubCommentDataModelImp(context);
    }

    @Override
    public void getNoteSubCommentData(int page, String commentId) {
        noteSubCommentDataModelImp.getNoteSubCommentData(page, commentId, this);
    }
}
