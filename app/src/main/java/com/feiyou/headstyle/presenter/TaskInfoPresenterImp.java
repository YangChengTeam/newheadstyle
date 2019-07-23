package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.TaskInfoRet;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.model.TaskInfoModelImp;
import com.feiyou.headstyle.model.TaskRecordInfoModelImp;
import com.feiyou.headstyle.view.TaskInfoView;

/**
 * Created by iflying on 2018/1/9.
 */

public class TaskInfoPresenterImp extends BasePresenterImp<IBaseView, TaskInfoRet> implements TaskInfoPresenter {
    private Context context = null;
    private TaskInfoModelImp taskInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public TaskInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        taskInfoModelImp = new TaskInfoModelImp(context);
    }

    @Override
    public void taskList(String uid) {
        taskInfoModelImp.taskList(uid, this);
    }
}
