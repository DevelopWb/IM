package com.juntai.wisdom.im.mine;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.juntai.disabled.basecomponent.utils.ActivityManagerTool;
import com.juntai.disabled.basecomponent.utils.DialogUtil;
import com.juntai.disabled.basecomponent.utils.FileCacheUtils;
import com.juntai.disabled.basecomponent.utils.ImageLoadUtil;
import com.juntai.disabled.basecomponent.utils.RxScheduler;
import com.juntai.disabled.basecomponent.utils.RxTask;
import com.juntai.disabled.basecomponent.utils.ToastUtils;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppHttpPath;
import com.juntai.wisdom.im.base.BaseAppFragment;
import com.juntai.wisdom.im.bean.ContactBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.MyMenuBean;
import com.juntai.wisdom.im.bean.UserBean;
import com.juntai.wisdom.im.entrance.LoginActivity;
import com.juntai.wisdom.im.entrance.main.MainActivity;
import com.juntai.wisdom.im.mine.myCollect.MyCollectActivity;
import com.juntai.wisdom.im.mine.myinfo.MyInformationActivity;
import com.juntai.wisdom.im.mine.secret.SecrecyActivity;
import com.juntai.wisdom.im.utils.HawkProperty;
import com.juntai.wisdom.im.utils.UrlFormatUtil;
import com.juntai.wisdom.im.utils.UserInfoManager;
import com.orhanobut.hawk.Hawk;

/**
 * @aouther tobato
 * @description 描述
 * @date 2021/4/17 16:12
 */
public class MyCenterFragment extends BaseAppFragment<MyCenterPresent> implements MyCenterContract.ICenterView, View.OnClickListener {

    private UserBean userBean;
    MyMenuAdapter myMenuAdapter;
    private String headUrl = "";

    private TextView mStatusTopTitle;
    private ImageView mHeadImage;
    private TextView mNickname;
    /**
     * 18763739973
     */
    private TextView mTelNumber;
    private RecyclerView mMenuRecycler;
    /**
     * 退出账号
     */
    private TextView mLoginOut;
    private int imUnReadCount;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_my_center;
    }

    @Override
    protected void initView() {
//        mStatusTopTitle = getView(R.id.status_top_title);
        mHeadImage = getView(R.id.headImage);
        mHeadImage.setOnClickListener(this);
        mNickname = getView(R.id.nickname);
        mNickname.setAlpha(0.3f);
        mTelNumber = getView(R.id.tel_number);
        mTelNumber.setVisibility(View.GONE);
        mMenuRecycler = getView(R.id.menu_recycler);
        mLoginOut = getView(R.id.login_out);
        mLoginOut.setOnClickListener(this);
        ConstraintLayout mheadLayoutCl = getView(R.id.head_layout_cl);
        mheadLayoutCl.setOnClickListener(this);
        myMenuAdapter = new MyMenuAdapter(mPresenter.getMenuBeans());
        getBaseActivity().initRecyclerview(mMenuRecycler, myMenuAdapter, LinearLayoutManager.VERTICAL);
//        mStatusTopTitle.setText("个人中心");

        myMenuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultipleItem multipleItem = (MultipleItem) adapter.getData().get(position);
                switch (multipleItem.getItemType()) {
                    case MultipleItem.ITEM_MYCENTER_MENUS:
                        MyMenuBean item = (MyMenuBean) multipleItem.getObject();
                        switch (item.getTag()) {
                            case MyCenterContract.MODIFY_PWD:
                                startActivity(new Intent(mContext, ModifyPwdActivity.class));
                                break;
                            case MyCenterContract.SET_ABOUT_TAG:
                                // 关于我们
                                startActivity(new Intent(mContext, AboutActivity.class));
                                break;

                            case MyCenterContract.SET_CLEAR_TAG:
                                // 清理缓存
                                getBaseActivity().setAlertDialogHeightWidth(DialogUtil.getMessageDialog(mContext, "将清理掉应用本地的图片和视频缓存文件",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                RxScheduler.doTask(getBaseAppActivity(), new RxTask<String>() {
                                                    @Override
                                                    public String doOnIoThread() {
                                                        FileCacheUtils.clearAll(mContext.getApplicationContext());
                                                        return "清理成功";
                                                    }

                                                    @Override
                                                    public void doOnUIThread(String s) {
                                                        ToastUtils.success(mContext.getApplicationContext(), s);
                                                    }
                                                });
                                            }
                                        }).show(), -1, 0);
                                break;
                            case MyCenterContract.SET_UPDATE_TAG:
                                //检查更新
                                getBaseAppActivity().update(true);
                                break;
                            case MyCenterContract.MENU_PRIVATE:
                                //隐私
                                startActivity(new Intent(mContext, SecrecyActivity.class));
                                break;
                            case MyCenterContract.MENU_COLLECT:
                                // :  2022-01-25     我的收藏

                                startActivity(new Intent(mContext, MyCollectActivity.class));

                                break;
                            default:
                                ToastUtils.toast(mContext, "暂未开放");
                                break;
                        }
                        break;
                    default:
                        break;
                }


            }
        });
    }

    @Override
    protected void initData() {
        mPresenter.initList();
        mHeadImage.setImageResource(R.mipmap.default_user_head_icon);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfoManager.isLogin()) {
            mLoginOut.setVisibility(View.VISIBLE);
            mPresenter.getUserInfo(((MainActivity) getActivity()).getBaseBuilder()
                    .add("toUserId", String.valueOf(UserInfoManager.getUserId()))
                    .build(), MyCenterContract.USER_DATA_TAG);
        } else {
            mLoginOut.setVisibility(View.GONE);
        }
    }


    @Override
    protected void lazyLoad() {
        super.lazyLoad();
    }

    @Override
    protected MyCenterPresent createPresenter() {
        return new MyCenterPresent();
    }


    @Override
    public void onClick(View v) {
        if (!UserInfoManager.isLogin()) {
//            MyApp.goLogin();
            return;
        }
        switch (v.getId()) {
            case R.id.head_layout_cl:
                //个人信息
                startActivity(new Intent(mContext, MyInformationActivity.class));
                break;
            case R.id.login_out:
                //退出登录
                AlertDialog alertDialog = DialogUtil.getMessageDialog(mContext, "是否退出登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.logout(getBaseAppActivity().getBaseBuilder().build(), AppHttpPath.LOGOUT);
                        dialog.dismiss();
                    }
                }).show();
                getBaseActivity().setAlertDialogHeightWidth(alertDialog, -1, 0);
                break;
        }
    }

    @Override
    public void onSuccess(String tag, Object o) {
        switch (tag) {
            case MyCenterContract.USER_DATA_TAG:
                userBean = (UserBean) o;
                ContactBean dataBean = userBean.getData();
                if (dataBean != null) {
                    mLoginOut.setVisibility(View.VISIBLE);
                    mNickname.setText(dataBean.getNickname());
                    mNickname.setAlpha(0.8f);
                    mTelNumber.setText("超视距号:" + userBean.getData().getAccountNumber());
                    mTelNumber.setVisibility(View.VISIBLE);
                    if (!headUrl.equals(userBean.getData().getHeadPortrait())) {
                        headUrl = userBean.getData().getHeadPortrait();
                        ImageLoadUtil.loadCirImgNoCrash(mContext.getApplicationContext(), UrlFormatUtil.getImageThumUrl(headUrl), mHeadImage, R.mipmap.default_user_head_icon, R.mipmap.default_user_head_icon);
                    }
                    Hawk.put(HawkProperty.SP_KEY_USER, userBean);
                }
                break;
            case AppHttpPath.LOGOUT:
                ToastUtils.success(mContext, "退出成功");
                UserInfoManager.clearUserData();//清理数据
                ActivityManagerTool.getInstance().finishApp();
                HawkProperty.clearRedPoint(mContext.getApplicationContext());
                startActivity(new Intent(mContext, LoginActivity.class));
                //重置界面
//                mNickname.setText("点击登录");
//                mNickname.setAlpha(0.3f);
//                mTelNumber.setVisibility(View.GONE);
//                mLoginOut.setVisibility(View.GONE);
//                mPresenter.initList();
//                headUrl = "";
//                mHeadImage.setImageResource(R.mipmap-xxhdpi.default_user_head_icon);
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(String tag, Object o) {
        ToastUtils.error(mContext, String.valueOf(o));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
