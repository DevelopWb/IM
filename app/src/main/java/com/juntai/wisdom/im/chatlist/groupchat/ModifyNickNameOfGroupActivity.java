package com.juntai.wisdom.im.chatlist.groupchat;

import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.mine.myinfo.BaseModifyActivity;

/**
 * @aouther tobato
 * @description 描述  修改群昵称
 * @date 2022-01-16 9:13
 */
public class ModifyNickNameOfGroupActivity extends BaseModifyActivity {

    @Override
    protected String getWarnContent() {
        return null;
    }

    @Override
    protected String getTitleName() {
        return "修改我这群里的昵称";
    }

    @Override
    protected void commit(String textViewValue) {

        mPresenter.modifyNickNameOfGroup(getBaseBuilder().add("groupUuid", baseString).add("userNickname", textViewValue).build(), AppHttpPath.MODIFY_GROUP_NAME);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        ToastUtils.toast(mContext, "更改成功");
        finish();

    }
}
