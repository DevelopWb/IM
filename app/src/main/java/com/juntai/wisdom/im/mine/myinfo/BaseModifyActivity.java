package com.juntai.wisdom.im.mine.myinfo;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.mvp.BasePresenter;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.base.BaseAppActivity;
import com.juntai.wisdom.im.mine.MyCenterContract;
import com.juntai.wisdom.im.mine.MyCenterPresent;
import com.juntai.wisdom.im.utils.UserInfoManager;

/**
 * @aouther tobato
 * @description 描述
 * @date 2021-11-13 15:37
 */
public abstract class BaseModifyActivity extends BaseAppActivity<MyCenterPresent> implements MyCenterContract.ICenterView {

    protected EditText mNickNameEt;
    public static String DEFAULT_HINT = "defaulthint";
    public int baseId;
    public String baseString;


    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
    }

    @Override
    public int getLayoutView() {
        return R.layout.activity_modify_nick_name;
    }

    @Override
    public void initView() {
        setTitleName(getTitleName());
        getTitleRightTv().setText("保存");
        getTitleRightTv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(getTextViewValue(mNickNameEt))) {
                    ToastUtils.toast(mContext,"不能为空");
                    return;
                }
                commit(getTextViewValue(mNickNameEt));
            }
        });

        mNickNameEt = (EditText) findViewById(R.id.nick_name_et);
        TextView mWarnTv = (TextView) findViewById(R.id.warn_tv);
        mWarnTv.setText(getWarnContent());
        if (getIntent() != null) {
            String defaultContent = getIntent().getStringExtra(DEFAULT_HINT);
            baseId = getIntent().getIntExtra(BASE_ID,0);
            baseString = getIntent().getStringExtra(BASE_STRING);
            mNickNameEt.setHint(defaultContent);
        }

    }

    protected abstract String getWarnContent();

    protected abstract String getTitleName();


    protected abstract void commit(String textViewValue);

    @Override
    public void initData() {

    }


    @Override
    public void onSuccess(String tag, Object o) {

    }

}
