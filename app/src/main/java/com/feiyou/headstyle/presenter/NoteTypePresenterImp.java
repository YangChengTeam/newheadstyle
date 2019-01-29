package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.NoteTypeRet;
import com.feiyou.headstyle.bean.TopicInfoRet;
import com.feiyou.headstyle.model.NoteTypeModelImp;
import com.feiyou.headstyle.model.TopicDataModelImp;
import com.feiyou.headstyle.view.NoteTypeView;
import com.feiyou.headstyle.view.TopicDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class NoteTypePresenterImp extends BasePresenterImp<NoteTypeView, NoteTypeRet> implements NoteTypePresenter {
    private Context context = null;
    private NoteTypeModelImp noteTypeModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public NoteTypePresenterImp(NoteTypeView view, Context context) {
        super(view);
        noteTypeModelImp = new NoteTypeModelImp(context);
    }

    @Override
    public void getNoteTypeData(String topicId, int page, int type, String userId) {
        noteTypeModelImp.getNoteTypeData(topicId, page, type, userId, this);
    }
}
