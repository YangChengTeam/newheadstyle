package com.feiyou.headstyle.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feiyou.headstyle.App;
import com.feiyou.headstyle.R;
import com.feiyou.headstyle.bean.ReportInfo;
import com.feiyou.headstyle.bean.ResultInfo;
import com.feiyou.headstyle.bean.UserInfo;
import com.feiyou.headstyle.common.Constants;
import com.feiyou.headstyle.presenter.ReportInfoPresenterImp;
import com.feiyou.headstyle.ui.adapter.ReportListAdapter;
import com.feiyou.headstyle.ui.base.BaseFragmentActivity;
import com.feiyou.headstyle.ui.custom.NormalDecoration;
import com.feiyou.headstyle.view.NoteTypeView;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by myflying on 2018/11/23.
 */
public class ReportInfoActivity extends BaseFragmentActivity implements NoteTypeView {

    @BindView(R.id.topbar)
    QMUITopBar mTopBar;

    ImageView mBackImageView;

    @BindView(R.id.report_list)
    RecyclerView mReportListView;

    @BindView(R.id.et_report_content)
    EditText mReportEditText;

    @BindView(R.id.btn_submit)
    Button mSubmitButton;

    ReportListAdapter reportListAdapter;

    private int lastIndex = -1;

    private ReportInfoPresenterImp reportInfoPresenterImp;

    private UserInfo userInfo;

    private String rid;

    private int reportType = 1;

    private ProgressDialog progressDialog = null;

    private Handler handler = new Handler();

    @Override
    protected int getContextViewId() {
        return R.layout.activity_report_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTopBar();
        initData();
    }

    private void initTopBar() {
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        View topSearchView = getLayoutInflater().inflate(R.layout.common_top_back, null);
        topSearchView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(48)));
        TextView titleTv = topSearchView.findViewById(R.id.tv_title);
        titleTv.setText("举报");

        mTopBar.setCenterView(topSearchView);
        mBackImageView = topSearchView.findViewById(R.id.iv_back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && !StringUtils.isEmpty(bundle.getString("rid"))) {
            rid = bundle.getString("rid");
        }
        if (bundle != null) {
            reportType = bundle.getInt("report_type", 1);
        }
        userInfo = App.getApp().getmUserInfo();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在操作");

        List<ReportInfo> list = new ArrayList<>();
        list.add(new ReportInfo("违规违法"));
        list.add(new ReportInfo("色情低俗"));
        list.add(new ReportInfo("辱骂我或者他人"));

        reportListAdapter = new ReportListAdapter(this, list);
        mReportListView.setLayoutManager(new LinearLayoutManager(this));
        mReportListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line_color), 1));
        mReportListView.setAdapter(reportListAdapter);

        reportListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == lastIndex) {
                    return;
                }
                reportListAdapter.getData().get(position).setSelected(true);
                if (lastIndex > -1) {
                    reportListAdapter.getData().get(lastIndex).setSelected(false);
                }
                lastIndex = position;
                reportListAdapter.notifyDataSetChanged();
            }
        });
        reportInfoPresenterImp = new ReportInfoPresenterImp(this, this);
    }

    @OnClick(R.id.btn_submit)
    void submit() {

        if (lastIndex == -1) {
            ToastUtils.showLong("请选择举报类别");
            return;
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        if (rid.equals("-1")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    ToastUtils.showLong("举报成功");
                    finish();
                }
            }, 1500);
        } else {
            reportInfoPresenterImp.takeReport(userInfo != null ? userInfo.getId() : "", rid, reportType, reportListAdapter.getData().get(lastIndex).getReportTypeName(), mReportEditText.getText().toString());
        }
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loadDataSuccess(ResultInfo tData) {
        Logger.i(JSON.toJSONString(tData));
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            ToastUtils.showLong("举报成功");
            finish();
        } else {
            ToastUtils.showLong("举报失败");
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
