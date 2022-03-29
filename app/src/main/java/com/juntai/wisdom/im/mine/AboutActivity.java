package com.juntai.wisdom.im.mine;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.juntai.disabled.basecomponent.base.BaseActivity;
import com.juntai.disabled.basecomponent.base.WebViewActivity;
import com.juntai.disabled.basecomponent.utils.BaseAppUtils;
import com.juntai.disabled.federation.R;

/**
 * @aouther Ma
 * @date 2019/3/17
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 当前版本:V1.0
     */
    private TextView mVersionText;
    /**
     * ©2020 君泰科技
     */
    private TextView mBottomTv;
    /**
     * 《用户协议》
     */
    private TextView mUserXieyi;
    /**
     * 《隐私协议》
     */
    private TextView mSecretXieyi;

    @Override
    public int getLayoutView() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setTitleName("关于我们");
        mVersionText = findViewById(R.id.version_text);
        mVersionText.setText("当前版本：V" + BaseAppUtils.getVersionName(mContext));
        mVersionText = (TextView) findViewById(R.id.version_text);
        mBottomTv = (TextView) findViewById(R.id.bottom_tv);
        mUserXieyi = (TextView) findViewById(R.id.user_xieyi);
        mUserXieyi.setOnClickListener(this);
        mSecretXieyi = (TextView) findViewById(R.id.secret_xieyi);
        mSecretXieyi.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_xieyi:
                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", getString(R.string.user_xieyi_url)));
                break;
            case R.id.secret_xieyi:
                startActivity(new Intent(mContext, WebViewActivity.class).putExtra("url", getString(R.string.secret_xieyi_url)));
                break;
            default:
                break;
        }
    }
}
