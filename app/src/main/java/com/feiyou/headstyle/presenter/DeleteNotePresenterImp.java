package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.FollowInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.model.DeleteNoteModelImp;
import com.feiyou.headstyle.model.FollowModelImp;

/**
 * Created by iflying on 2018/1/9.
 */

public class DeleteNotePresenterImp extends BasePresenterImp<IBaseView, ResultInfo> implements DeleteNotePresenter {
    private Context context = null;
    private DeleteNoteModelImp deleteNoteModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public DeleteNotePresenterImp(IBaseView view, Context context) {
        super(view);
        deleteNoteModelImp = new DeleteNoteModelImp(context);
    }

    @Override
    public void deleteNote(String userId, String msgId) {
        deleteNoteModelImp.deleteNote(userId, msgId, this);
    }
}
