package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.LoginRequest;
import com.feiyou.headstyle.bean.PhotoWallRet;
import com.feiyou.headstyle.bean.UserInfoRet;
import com.feiyou.headstyle.model.DeletePhotoModelImp;
import com.feiyou.headstyle.model.UploadPhotoModelImp;
import com.feiyou.headstyle.model.UserInfoModelImp;
import com.feiyou.headstyle.view.PhotoWallDataView;
import com.feiyou.headstyle.view.UserInfoView;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public class UploadPhotoPresenterImp extends BasePresenterImp<PhotoWallDataView, PhotoWallRet> implements UploadPhotoPresenter {
    private Context context = null;
    private UploadPhotoModelImp uploadPhotoModelImp = null;
    private DeletePhotoModelImp deletePhotoModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public UploadPhotoPresenterImp(PhotoWallDataView view, Context context) {
        super(view);
        uploadPhotoModelImp = new UploadPhotoModelImp(context);
        deletePhotoModelImp = new DeletePhotoModelImp(context);
    }

    @Override
    public void uploadPhotoWall(String uid, List<String> filePaths) {
        uploadPhotoModelImp.uploadPhotoWall(uid, filePaths, this);
    }

    @Override
    public void deletePhoto(String uid, String photos) {
        deletePhotoModelImp.deletePhoto(uid, photos, this);
    }
}
