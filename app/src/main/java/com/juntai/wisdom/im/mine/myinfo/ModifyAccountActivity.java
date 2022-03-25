package com.juntai.wisdom.im.mine.myinfo;

import android.content.Intent;

import com.juntai.disabled.basecomponent.utils.ActivityManagerTool;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.entrance.LoginActivity;

/**
 * @aouther tobato
 * @description 描述  修改账户
 * @date 2021-11-13 15:38
 */
public class ModifyAccountActivity extends BaseModifyActivity {

    @Override
    protected String getWarnContent() {
        return getString(R.string.warn_mofify_account);
    }


    @Override
    protected String getTitleName() {
        return "更改超视距号";
    }

    @Override
    protected void commit(String textViewValue) {
//  更改超视距号
        mPresenter.modifyAccount(getBaseBuilder().add("accountNumber", textViewValue).build(), AppHttpPath.MODIFY_USER_ACCOUNT);
    }

    @Override
    public void onSuccess(String tag, Object o) {
        ActivityManagerTool.getInstance().finishApp();
        startActivity(new Intent(mContext, LoginActivity.class));
    }
}
