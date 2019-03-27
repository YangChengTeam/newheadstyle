package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.AddNoteRet;
import com.feiyou.headstyle.bean.CollectInfoRet;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.model.AddNoteModelImp;
import com.feiyou.headstyle.model.CollectDataModelImp;
import com.feiyou.headstyle.view.AddNoteView;
import com.feiyou.headstyle.view.CollectDataView;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public class AddNotePresenterImp extends BasePresenterImp<AddNoteView, AddNoteRet> implements AddNotePresenter {
    private Context context = null;
    private AddNoteModelImp addNoteModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public AddNotePresenterImp(AddNoteView view, Context context) {
        super(view);
        addNoteModelImp = new AddNoteModelImp(context);
    }

    @Override
    public void addNote(String uid, String content, String topicId, String friends,List<String> filePaths) {
        addNoteModelImp.addNote(uid,content,topicId,friends,filePaths,this);
    }
}
