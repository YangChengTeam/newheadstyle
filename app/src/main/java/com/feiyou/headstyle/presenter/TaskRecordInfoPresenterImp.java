package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.SignDoneInfoRet;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.model.SignDoneInfoModelImp;
import com.feiyou.headstyle.model.TaskRecordInfoModelImp;

/**
 * Created by iflying on 2018/1/9.
 */

public class TaskRecordInfoPresenterImp extends BasePresenterImp<IBaseView, TaskRecordInfoRet> implements TaskRecordInfoPresenter {
    private Context context = null;
    private TaskRecordInfoModelImp taskRecordInfoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public TaskRecordInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        taskRecordInfoModelImp = new TaskRecordInfoModelImp(context);
    }

    @Override
    public void addTaskRecord(String uid, String openid, String taskId, int goldNum, double cash, int status, String recordId) {
        taskRecordInfoModelImp.addTaskRecord(uid, openid, taskId, goldNum, cash, status, recordId, this);
    }

    @Override
    public void addHomeTaskRecord(String uid, String openid, String imei, double cash, int type) {
        taskRecordInfoModelImp.addHomeTaskRecord(uid, openid, imei, cash, type, this);
    }
}
