package com.juntai.wisdom.im.chatlist.groupchat;

import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.mine.myinfo.BaseModifyActivity;

/**
 * @aouther tobato
 * @description 描述  修改群名称
 * @date 2022-01-16 9:10
 */
public class ModifyGroupNameActivity extends BaseModifyActivity {


    @Override
    protected String getWarnContent() {
        return "";
    }

    @Override
    protected String getTitleName() {
        return "修改群名称";
    }

    @Override
    protected void commit(String textViewValue) {
        mPresenter.modifyGroupName(getBaseBuilder().add("groupId", String.valueOf(baseId)).add("groupName", textViewValue).build(), AppHttpPath.MODIFY_GROUP_NAME);
    }


    @Override
    public void onSuccess(String tag, Object o) {
        super.onSuccess(tag, o);
        ToastUtils.toast(mContext, "更改成功");
        finish();

    }
}
