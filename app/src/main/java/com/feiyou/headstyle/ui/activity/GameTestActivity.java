package com.feiyou.headstyle.ui.activity;


import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.cmcm.cmgame.CmGameSdk;
import com.cmcm.cmgame.GameView;
import com.cmcm.cmgame.IAppCallback;
import com.cmcm.cmgame.IGameAccountCallback;
import com.cmcm.cmgame.IGameAdCallback;
import com.cmcm.cmgame.IGamePlayTimeCallback;
import com.cmcm.cmgame.activity.H5GameActivity;
import com.cmcm.cmgame.gamedata.CmGameAppInfo;
import com.cmcm.cmgame.view.CmGameTopView;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.base.IBaseView;
import com.feiyou.headstyle.bean.GameGoldInfoRet;
import com.feiyou.headstyle.bean.PlayGameInfo;
import com.feiyou.headstyle.bean.SeeVideoInfo;
import com.feiyou.headstyle.bean.TaskRecordInfoRet;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.GameGoldPresenterImp;
import com.feiyou.headstyle.presenter.TaskRecordInfoPresenterImp;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.GameProfitDialog;
import com.feiyou.headstyle.ui.custom.NotEnoughDialog;
import com.feiyou.headstyle.ui.custom.SimpleRoundProgress;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import butterknife.BindView;


public class GameTestActivity extends BaseFragmentActivity implements IAppCallback, IGamePlayTimeCallback, IGameAdCallback, IGameAccountCallback, Application.ActivityLifecycleCallbacks, IBaseView, GameProfitDialog.GameSeeVideoListener {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    ImageView closeImage;

    LinearLayout mCurrentProfitLayout;

    TextView mShowGoldTv;

    SimpleRoundProgress simpleRoundProgress;

    private CountDownTimer mCountDownTimer;

    private static final long MAX_TIME = 17 * 1000;

    private long curTime = 0;

    private boolean isPause = false;

    TextView mProfitNum;

    float lastX = 0;
    float lastY = 0;

    float currentX = 0;
    float currentY = 0;

    boolean isMove = false;

    boolean isRestart = false;

    private int isPlaySecond;

    private ImageView mGamePointIv;

    private GameProfitDialog gameProfitDialog;

    private PlayGameInfo playGameInfo;

    TaskRecordInfoPresenterImp taskRecordInfoPresenterImp;

    GameGoldPresenterImp gameGoldPresenterImp;

    private String taskId = "";

    private String recordId = "";

    private UserInfo userInfo;

    private boolean isStartTask;

    private SeeVideoInfo gameSeeVideoInfo;

    private boolean seeVideoIsFinish;

    private int playGameGoldNumber;

    private NotEnoughDialog notEnoughDialog;

    private int currentGameCount;

    Handler mHandler = new Handler();

    //玩游戏总共的时间,单位:S
    private int totalSecond;

    //最后一次玩的游戏的ID
    private String lastGameId = "";

    private boolean gameProfitIsFinish;//当日的游戏收益是否达到了上线

    private boolean videoTaskIsFinish;//当日看视频获得收益是否达到了上线

    private int currentTodayGold;//当天获得的金币总数

    private int todayVideoGold;//当天看视频获得的金币数

    @Override
    protected int getContextViewId() {
        return R.layout.activity_classify;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
        this.getApplication().registerActivityLifecycleCallbacks(this);

    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View aboutView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        aboutView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = aboutView.findViewById(R.id.tv_title);
        titleTv.setText("游戏赚钱");

        mTopBar.setCenterView(aboutView);
        mBackImageView = aboutView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
            userInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
            });
        }

        playGameInfo = (PlayGameInfo) getIntent().getSerializableExtra("play_game_info");
        gameSeeVideoInfo = (SeeVideoInfo) getIntent().getSerializableExtra("game_see_video");

        taskRecordInfoPresenterImp = new TaskRecordInfoPresenterImp(this, this);
        gameGoldPresenterImp = new GameGoldPresenterImp(this, this);

        Logger.i("play game info--->" + JSON.toJSONString(playGameInfo));

        // 目前只支持anrdoid 5.0或以上
        if (Build.VERSION.SDK_INT < 21) {
            Toast.makeText(this, "不支持低版本，仅支持android 5.0或以上版本!", Toast.LENGTH_LONG).show();
        }

        // 把默认游戏中心view添加进媒体指定界面
        GameView gameTabsClassifyView = (GameView) findViewById(R.id.gameView);
        gameTabsClassifyView.inflate(this);

        // 初始化小游戏 sdk 的账号数据，用于存储游戏内部的用户数据，
        // 为避免数据异常，这个方法建议在小游戏列表页面展现前（可以是二级页面）才调用
        CmGameSdk.initCmGameAccount();

        /////////// ///////////////////////////////////////////
        /// 如下为可选功能，如没必要，不要使用
        /// 如下为可选功能
        /// 如下为可选功能

        // 默认游戏中心页面，点击游戏试，触发回调
        CmGameSdk.setGameClickCallback(this);

        // 点击游戏右上角或物理返回键，退出游戏时触发回调，并返回游戏时长
        CmGameSdk.setGamePlayTimeCallback(this);

        // 游戏内增加自定义view，提供产品多样性
        //initMoveViewSwitch();
        initMoveView();

        // 所有广告类型的展示和点击事件回调，仅供参考，数据以广告后台为准
        // 建议不要使用，有阻塞行为会导致程序无法正常使用
        // CmGameSdk.setGameAdCallback(this);

        // 账号信息变化时触发回调，若需要支持APP卸载后游戏信息不丢失，需要注册该回调
        CmGameSdk.setGameAccountCallback(this);

        if (userInfo != null) {
            gameGoldPresenterImp.gameGold(userInfo.getId(), userInfo.getOpenid());
        }

        notEnoughDialog = new NotEnoughDialog(this, R.style.login_dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("onresume--->");
//        if (mCountDownTimer != null) {
//            mCountDownTimer.cancel();
//            isPause = false;
//            curTime = 0;
//        }
    }

    @Override
    public void gameClickCallback(String gameName, String gameID) {
        Log.d("cmgamesdk_Main2Activity", gameID + "----" + gameName);
        curTime = 0;
        isPause = false;
        mGamePointIv.setVisibility(View.GONE);
        simpleRoundProgress.setProgress(0);
        currentGameCount = 0;

        if (!StringUtils.isEmpty(lastGameId) && lastGameId.equals(gameID)) {
            playGameGoldNumber = 0;
        }

        lastGameId = gameID;
    }

    /**
     * @param playTimeInSeconds 玩游戏时长，单位为秒
     */
    @Override
    public void gamePlayTimeCallback(String gameId, int playTimeInSeconds) {
        Log.d("cmgamesdk_Main2Activity", "play game ：" + gameId + "playTimeInSeconds : " + playTimeInSeconds);

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        totalSecond += playTimeInSeconds;
        //经过1.5s之后检测，判断是否打开过新的游戏
        checkTotalInfo(gameId);
    }

    public void checkTotalInfo(String logoutGameId) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (totalSecond < 20) {
                    if (notEnoughDialog != null && !notEnoughDialog.isShowing()) {
                        notEnoughDialog.show();
                    }
                } else {
                    if (lastGameId.equals(logoutGameId)) {
                        gameProfitDialog = new GameProfitDialog(GameTestActivity.this, R.style.login_dialog);
                        gameProfitDialog.setGameSeeVideoListener(GameTestActivity.this);
                        gameProfitDialog.show();
                        gameProfitDialog.setTitleValue("本次游戏收益", playGameGoldNumber);
                        lastGameId = "";
                        playGameGoldNumber = 0;
                        totalSecond = 0;
                    }
                }

                curTime = 0;
                isPause = false;
                mGamePointIv.setVisibility(View.GONE);
                simpleRoundProgress.setProgress(0);
                isStartTask = false;
            }
        }, 600);
    }


    /**
     * 广告曝光/点击回调
     *
     * @param gameId   游戏Id
     * @param adType   广告类型：1：激励视频广告；2：Banner广告；3：原生Banner广告；4：全屏视频广告；5：插屏广告；6：开屏大卡广告
     * @param adAction 广告操作：1：曝光；2：点击；3：关闭；4：跳过
     */
    @Override
    public void onGameAdAction(String gameId, int adType, int adAction) {
        Log.d("cmgamesdk_Main2Activity", "onGameAdAction gameId: " + gameId + " adType: " + adType + " adAction: " + adAction);
    }


    /**
     * 游戏账号信息回调，需要接入方保存，下次进入或卸载重装后设置给SDK使用，可以支持APP卸载后，游戏信息不丢失
     *
     * @param token     用户token
     */
    @Override
    public void onGameAccount(String token) {
        //Log.d("cmgamesdk_Main2Activity", "onGameAccount uid: " + uid + " token: " + token + " gameToken: " + gameToken);
//        SPUtils.getInstance().put("game_uid", uid);
//        SPUtils.getInstance().put(uid + "token", token);
//        SPUtils.getInstance().put(uid + "game_token", gameToken);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        CmGameSdk.removeGameClickCallback();
        CmGameSdk.setMoveView(null);
        CmGameSdk.removeGamePlayTimeCallback();
        CmGameSdk.removeGameAdCallback();
        CmGameSdk.removeGameAccountCallback();

        if (closeImage != null) {
            closeImage.clearAnimation();
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    public void initCountDownTimer(long millisInFuture) {

        Logger.i("init count down--->" + millisInFuture / 1000);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        mCountDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                curTime = millisUntilFinished;

                int surplusSecond = (int) (millisUntilFinished / 1000);

                isPlaySecond = 18 - surplusSecond;

                Logger.i("count down surplusSecond--->" + surplusSecond + "---isRestart--->" + isRestart);

                //每隔2s检测一次是否点击了屏幕
                if ((MAX_TIME - surplusSecond) % 3 == 0 && !isRestart) {
                    if (currentX == lastX && currentY == lastY) {
                        Logger.i("count down point is equals");
                        isMove = false;
                        isPause = true;
                        mCountDownTimer.cancel();
                    }
                }

                if (isRestart) {
                    isRestart = false;
                }

                int progress = (int) ((double) (100 / 18) * isPlaySecond);

                simpleRoundProgress.setProgress(progress > 100 ? 100:progress);
                Logger.i("倒计时--->" + surplusSecond + "s");
                isPause = false;
            }

            public void onFinish() {
                curTime = 0;
                isPause = false;
                mGamePointIv.setVisibility(View.VISIBLE);
                simpleRoundProgress.setProgress(100);
                //倒计时完成，执行任务结束
                addTaskEnd();
            }
        };
    }

    public void addTaskStart() {
        if (userInfo != null && playGameInfo != null) {
            recordId = "";
            Logger.i("task start--->");
            taskId = playGameInfo.getId() + "";
            taskRecordInfoPresenterImp.addTaskRecord(userInfo.getId(), userInfo.getOpenid(), taskId, playGameInfo.getGoldnum(), 0, 0, "0");
        }
    }

    public void addTaskEnd() {
        if (userInfo != null && playGameInfo != null) {
            Logger.i("task end--->");
            taskId = playGameInfo.getId() + "";
            taskRecordInfoPresenterImp.addTaskRecord(userInfo.getId(), userInfo.getOpenid(), taskId, playGameInfo.getGoldnum(), 0, 1, recordId);
        }
    }

    // 请确保在游戏界面展示前传入目标View，并在豹趣游戏模块关闭后控制自己View的回收避免泄漏
    private void initMoveView() {
        final View view = LayoutInflater.from(this).inflate(R.layout.test_move_layout, null);

        LinearLayout profitLayout = view.findViewById(R.id.layout_profit);
        closeImage = view.findViewById(R.id.iv_close);
        mCurrentProfitLayout = view.findViewById(R.id.layout_current_profit);
        mShowGoldTv = view.findViewById(R.id.tv_show_gold);

        Animation startRotate = AnimationUtils.loadAnimation(GameTestActivity.this, R.anim.rotate_start_anim);
        Animation stopRotate = AnimationUtils.loadAnimation(GameTestActivity.this, R.anim.rotate_stop_anim);
        simpleRoundProgress = view.findViewById(R.id.round_progress);
        mGamePointIv = view.findViewById(R.id.iv_game_point);

        mProfitNum = view.findViewById(R.id.tv_game_profit_num);

        //不能在根布局设置点击事件，否则影响拖动，我们会进行点击回调，在回调中处理即可
        CmGameTopView cmGameTopView = new CmGameTopView(view, new CmGameTopView.CmViewClickCallback() {
            @Override
            public void onClick(View v) {

                if (mGamePointIv.getVisibility() == View.VISIBLE) {
                    if (gameProfitDialog != null && !gameProfitDialog.isShowing()) {
                        gameProfitDialog.show();
                        gameProfitDialog.setTitleValue("游戏收益", currentGameCount);
                        mGamePointIv.setVisibility(View.GONE);
                        simpleRoundProgress.setProgress(0);
                        mProfitNum.setText("+" + currentTodayGold);
                        currentGameCount = 0;
                    }
                    return;
                }

                if (!isPause) {
                    if (mCountDownTimer != null) {
                        isPause = true;
                        mCountDownTimer.cancel();
                    }
                } else {
                    if (curTime != 0) {
                        //将上次当前剩余时间作为新的时长
                        initCountDownTimer(curTime);
                        mCountDownTimer.start();
                        isRestart = true;
                        isPause = false;
                    }
                }

                //Toast.makeText(GameTestActivity.this, "这里被点击了", Toast.LENGTH_SHORT).show();
                if (profitLayout.getVisibility() == View.INVISIBLE) {
                    closeImage.setImageResource(R.mipmap.time_close);
                    profitLayout.setVisibility(View.VISIBLE);

                    startRotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    closeImage.startAnimation(startRotate);
                } else {
                    profitLayout.setVisibility(View.INVISIBLE);

                    stopRotate.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            closeImage.setImageResource(R.mipmap.time_gift);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    closeImage.startAnimation(stopRotate);
                }
            }
        });

        //目前不支持多个可点击View，可以设置类似菜单格式，在顶层View点击后再进行展示，菜单内的点击自己控制，但是记得用完后隐藏
        view.findViewById(R.id.layout_profit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.layout_profit).setVisibility(View.INVISIBLE);
            }
        });

        //控件初始位置位于游戏界面右上角,距离顶部100dp，右边距10dp，这里可以进行设定布局格式，参数必须为FrameLayout.LayoutParam
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.END;
        layoutParams.rightMargin = 30; //注意这里是px，记得dp转化
        layoutParams.topMargin = 300;
        cmGameTopView.setLayoutParams(layoutParams);

        // 控件是否可滑动，默认可滑动
        cmGameTopView.setMoveEnable(true);
        // 顶层View是否需要等到游戏加载成功再显示，默认是
        cmGameTopView.setNeedShowAfterGameShow(true);

        //设置屏幕事件监听
        cmGameTopView.setScreenCallback(new CmGameTopView.ScreenEventCallback() {
            @Override
            public void onDrag(MotionEvent event) {
                //TopView被拖拽时候的回调
                Log.d("cmgamesdk_Main2Activity", "控件拖拽：" + event.getAction() + ":" + event.getX() + "," + event.getY());
            }

            @Override
            public void onScreenTouch(MotionEvent event) {
                //游戏界面被触摸时候的回调
                Log.d("cmgamesdk_Main2Activity", "屏幕点击：" + event.getAction() + ":" + event.getX() + "," + event.getY());

                if (gameProfitIsFinish) {
                    return;
                }

                currentX = event.getX();
                currentY = event.getY();

                if (currentX == lastX && currentY == lastY) {
                    isMove = false;
                } else {
                    isMove = true;
                    isPause = false;
                    if (curTime != 0) {
                        //将上次当前剩余时间作为新的时长
                        initCountDownTimer(curTime);
                        mCountDownTimer.start();
                        isRestart = true;
                    } else {
                        initCountDownTimer(MAX_TIME);
                        mCountDownTimer.start();
                        //开始一次任务
                        if (!isStartTask) {
                            isStartTask = true;
                            addTaskStart();
                        }
                    }
                }

                lastX = event.getX();
                lastY = event.getY();
            }

            @Override
            public void onViewVisible() {

            }
        });

        CmGameSdk.setMoveView(cmGameTopView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        popBackStack();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof H5GameActivity) {
            gameProfitDialog = new GameProfitDialog(activity, R.style.login_dialog);
            gameProfitDialog.setGameSeeVideoListener(this);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(Object tData) {
        Logger.i("game test data--->" + JSON.toJSONString(tData));
        if (tData instanceof TaskRecordInfoRet) {
            if (((TaskRecordInfoRet) tData).getCode() == Constants.SUCCESS) {
                if (StringUtils.isEmpty(recordId)) {
                    if (((TaskRecordInfoRet) tData).getData() != null) {
                        recordId = ((TaskRecordInfoRet) tData).getData().getInfoid();
                    }
                    Logger.i("recordId--->" + recordId);
                } else {
                    if (((TaskRecordInfoRet) tData).getData() != null) {
                        Logger.i("任务完成recordId--->" + recordId);
                        isStartTask = false;
                        if (taskId.equals("16")) {
                            playGameGoldNumber += playGameInfo.getGoldnum();
                            currentGameCount += playGameInfo.getGoldnum();
                            currentTodayGold += playGameInfo.getGoldnum();
                            mProfitNum.setText("+" + currentTodayGold);
                            mShowGoldTv.setText("+" + playGameInfo.getGoldnum());
                            mCurrentProfitLayout.setVisibility(View.VISIBLE);
                            closeImage.setVisibility(View.GONE);
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mCurrentProfitLayout.setVisibility(View.GONE);
                                    closeImage.setVisibility(View.VISIBLE);
                                }
                            }, 2500);

                            //判断当日游戏收益是否到了上限
                            if (playGameInfo != null && currentTodayGold >= playGameInfo.getGoldtotal()) {
                                gameProfitIsFinish = true;
                                Logger.i("gold is finish--->" + gameProfitIsFinish);
                            }
                        }
                        if (taskId.equals("17") && gameSeeVideoInfo != null && gameSeeVideoInfo.getId() == 17 && seeVideoIsFinish) {
                            todayVideoGold += gameSeeVideoInfo.getGoldnum();
                            if (gameSeeVideoInfo != null && todayVideoGold >= gameSeeVideoInfo.getGoldtotal()) {
                                videoTaskIsFinish = true;
                            }
                            ToastUtils.showLong("领取成功 +" + gameSeeVideoInfo.getGoldnum() + "金币");
                        }
                    }
                }
            } else {
                //finish();
                Logger.i("task error--->");
            }
        }
        if (tData instanceof GameGoldInfoRet) {
            if (((GameGoldInfoRet) tData).getCode() == Constants.SUCCESS) {
                currentTodayGold = ((GameGoldInfoRet) tData).getData().getGoldnum();
                todayVideoGold = ((GameGoldInfoRet) tData).getData().getTodayVideoNum();

                mProfitNum.setText("+" + currentTodayGold);

                if (playGameInfo != null && currentTodayGold >= playGameInfo.getGoldtotal()) {
                    gameProfitIsFinish = true;
                    Logger.i("load gold is finish--->" + gameProfitIsFinish);
                }

                if (gameSeeVideoInfo != null && todayVideoGold >= gameSeeVideoInfo.getGoldtotal()) {
                    videoTaskIsFinish = true;
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    public void startVideo() {
        if (userInfo != null && gameSeeVideoInfo != null) {
            Logger.i("game see video task start--->");
            taskId = gameSeeVideoInfo.getId() + "";
            recordId = "";
            seeVideoIsFinish = false;
            taskRecordInfoPresenterImp.addTaskRecord(userInfo.getId(), userInfo.getOpenid(), taskId, gameSeeVideoInfo.getGoldnum(), 0, 0, "0");
        }
    }

    @Override
    public void endVideo() {
        if (userInfo != null && gameSeeVideoInfo != null) {
            Logger.i("game see video task end--->");
            taskId = gameSeeVideoInfo.getId() + "";
            seeVideoIsFinish = true;
            taskRecordInfoPresenterImp.addTaskRecord(userInfo.getId(), userInfo.getOpenid(), taskId, gameSeeVideoInfo.getGoldnum(), 0, 1, recordId);
        }
    }
}
