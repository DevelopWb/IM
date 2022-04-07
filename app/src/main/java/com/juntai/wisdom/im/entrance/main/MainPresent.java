package com.juntai.wisdom.im.entrance.main;


import com.juntai.disabled.basecomponent.base.BaseObserver;
import com.juntai.disabled.basecomponent.base.BaseResult;
import com.juntai.disabled.basecomponent.mvp.IModel;
import com.juntai.disabled.basecomponent.utils.RxScheduler;
import com.juntai.wisdom.im.AppNetModule;
import com.juntai.wisdom.im.base.BaseAppPresent;
import com.juntai.wisdom.im.bean.ContactInfoBean;
import com.juntai.wisdom.im.bean.ContactListBean;
import com.juntai.wisdom.im.bean.GroupListBean;
import com.juntai.wisdom.im.bean.NewFriendsBean;

import okhttp3.RequestBody;

/**
 * Describe:首页present
 * Create by tobato
 * 2020-8-8
 * email:954101549@qq.com
 */
public class MainPresent extends BaseAppPresent<IModel, MainContract.IBaseView> implements MainContract.IMainPagePresent {

    @Override
    protected IModel createModel() {
        return null;
    }


    public void getContactList(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .getFriendList(requestBody)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<ContactListBean>(null) {
                    @Override
                    public void onSuccess(ContactListBean o) {
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
    public void matchMobileContacts(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .matchMobileContacts(requestBody)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<ContactListBean>(getView()) {
                    @Override
                    public void onSuccess(ContactListBean o) {
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
    public void getGroupList(RequestBody body, String tag) {
        AppNetModule.createrRetrofit()
                .getGroupList(body)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<GroupListBean>(null) {
                    @Override
                    public void onSuccess(GroupListBean o) {
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

    public void searchFriends(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .searchFriends(requestBody)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<ContactInfoBean>(getView()) {
                    @Override
                    public void onSuccess(ContactInfoBean o) {
                        if (getView() != null) {
                            getView().onSuccess(tag, o.getData());
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

    public void applyAddFriend(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .applyAddFriend(requestBody)
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
    public void getUnreadFriendApply(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .getUnreadFriendApply(requestBody)
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



    public void agreeFriendsApply(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .agreeFriendsApply(requestBody)
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

    public void newFriendList(RequestBody requestBody, String tag) {
        AppNetModule.createrRetrofit()
                .newFriendList(requestBody)
                .compose(RxScheduler.ObsIoMain(getView()))
                .subscribe(new BaseObserver<NewFriendsBean>(null) {
                    @Override
                    public void onSuccess(NewFriendsBean o) {
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
