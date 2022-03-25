package com.juntai.wisdom.im.mine;


import com.juntai.disabled.basecomponent.base.BaseObserver;
import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.mvp.IModel;
import com.juntai.disabled.basecomponent.utils.RxScheduler;
import com.juntai.disabled.federation.R;
import com.juntai.wisdom.im.AppNetModule;
import com.juntai.wisdom.im.base.BaseAppPresent;
import com.juntai.wisdom.im.bean.CitysBean;
import com.juntai.wisdom.im.bean.MultipleItem;
import com.juntai.wisdom.im.bean.MyMenuBean;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * Describe:
 * Create by tobato
 * 2020/3/7
 * email:954101549@qq.com
 */
public class MyCenterPresent extends BaseAppPresent<IModel, MyCenterContract.ICenterView> implements MyCenterContract.ICenterPresent {

    @Override
    protected IModel createModel() {
        return null;
    }

    public void logout(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .logout(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void verifyAddFriend(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .verifyAddFriend(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void modifyFriendRemark(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .modifyFriendRemark(requestBody)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void getAllCitys(String keywords, String tag) {
        AppNetModule.createrRetrofit()
                .getAllCitys(keywords, "1", "65862700fcb4f4b4dcbe4e554e5bbe3d")
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<CitysBean>(getView()) {
                    @Override
                    public void onSuccess(CitysBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void modifyAccount(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .modifyAccount(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void getQRCode(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getQRCode(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }
    public void commitSuggestion(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .commitSuggestion(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    public void modifyUserInfo(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .modifyUserInfo(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    @Override
    public void initList() {
    }

    public List<MultipleItem> getMenuBeans() {
        List<MultipleItem> menuBeans = new ArrayList<>();
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_DIVIDER, ""));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_MYCENTER_MENUS, new MyMenuBean("隐私", 0, R.mipmap.menu_private, MyCenterContract.MENU_PRIVATE, true)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_MYCENTER_MENUS, new MyMenuBean("我的收藏", 0, R.mipmap.menu_collect, MyCenterContract.MENU_COLLECT, true)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_MYCENTER_MENUS, new MyMenuBean("修改密码", 0, R.mipmap.modify_pwd, MyCenterContract.MODIFY_PWD, true)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_MYCENTER_MENUS, new MyMenuBean("清理内存", 0, R.mipmap.clear_cache, MyCenterContract.SET_CLEAR_TAG, true)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_MYCENTER_MENUS, new MyMenuBean("检测更新", 0, R.mipmap.check_update, MyCenterContract.SET_UPDATE_TAG, true)));
        menuBeans.add(new MultipleItem(MultipleItem.ITEM_MYCENTER_MENUS, new MyMenuBean("关于我们", 0, R.mipmap.about_us, MyCenterContract.SET_ABOUT_TAG, true)));

        return menuBeans;
    }


    /**
     * @param body
     * @param tag
     */
    public void modifyGroupName(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .modifyGroupName(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }

    /**
     * @param body
     * @param tag
     */
    public void modifyNickNameOfGroup(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .modifyNickNameOfGroup(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<BaseResult>(getView()) {
                    @Override
                    public void onSuccess(BaseResult o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        if (getView() != null) {
                            getView().onError(tag, msg);
                        }
                    }
                });
    }


}
