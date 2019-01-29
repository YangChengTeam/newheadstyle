package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.NoteInfoRet;
import com.feiyou.headstyle.bean.TopicInfoRet;
import com.feiyou.headstyle.model.NoteDataModelImp;
import com.feiyou.headstyle.model.TopicDataModelImp;
import com.feiyou.headstyle.view.NoteDataView;
import com.feiyou.headstyle.view.TopicDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class NoteDataPresenterImp extends BasePresenterImp<NoteDataView, NoteInfoRet> implements NoteDataPresenter {
    private Context context = null;
    private NoteDataModelImp noteDataModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public NoteDataPresenterImp(NoteDataView view, Context context) {
        super(view);
        noteDataModelImp = new NoteDataModelImp(context);
    }

    @Override
    public void getNoteData(int page, int type, String userid) {
        noteDataModelImp.getNoteData(page, type, userid, this);
    }
}
