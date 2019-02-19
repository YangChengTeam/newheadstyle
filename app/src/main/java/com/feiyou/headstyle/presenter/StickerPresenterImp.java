package com.feiyou.headstyle.presenter;

import android.content.Context;

import com.feiyou.headstyle.base.BasePresenterImp;
import com.feiyou.headstyle.bean.StickerInfoRet;
import com.feiyou.headstyle.bean.TopicInfoRet;
import com.feiyou.headstyle.model.StickerModelImp;
import com.feiyou.headstyle.model.TopicDataModelImp;
import com.feiyou.headstyle.view.StickerDataView;
import com.feiyou.headstyle.view.TopicDataView;

/**
 * Created by iflying on 2018/1/9.
 */

public class StickerPresenterImp extends BasePresenterImp<StickerDataView, StickerInfoRet> implements StickerPresenter {
    private Context context = null;
    private StickerModelImp stickerModelImp = null;

    /**
     * @param view 具体业务的视图接口对象
     * @descriptoin 构造方法
     */
    public StickerPresenterImp(StickerDataView view, Context context) {
        super(view);
        stickerModelImp = new StickerModelImp(context);
    }

    @Override
    public void getDataList() {
        stickerModelImp.getDataList(this);
    }
}
