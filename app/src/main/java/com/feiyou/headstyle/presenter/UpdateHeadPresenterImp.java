package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.PhotoWallRet;
import com.feiyou.headstyle.bean.UpdateHeadRet;
import com.feiyou.headstyle.model.DeletePhotoModelImp;
import com.feiyou.headstyle.model.UpdateHeadModelImp;
import com.feiyou.headstyle.model.UploadPhotoModelImp;
import com.feiyou.headstyle.view.PhotoWallDataView;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public class UpdateHeadPresenterImp extends BasePresenterImp<IBaseView, UpdateHeadRet> implements UpdateHeadPresenter {
    private Context context = null;
    private UpdateHeadModelImp uploadPhotoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public UpdateHeadPresenterImp(IBaseView view, Context context) {
        super(view);
        uploadPhotoModelImp = new UpdateHeadModelImp(context);
    }

    @Override
    public void updateHead(String userId, String filePath) {
        uploadPhotoModelImp.updateHead(userId, filePath, this);
    }
}
