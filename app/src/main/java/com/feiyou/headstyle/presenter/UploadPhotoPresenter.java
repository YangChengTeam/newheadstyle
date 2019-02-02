package com.feiyou.headstyle.presenter;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface UploadPhotoPresenter {
    void uploadPhotoWall(String uid, List<String> filePaths);

    void deletePhoto(String uid, String photos);
}
