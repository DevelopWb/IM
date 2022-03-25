package com.juntai.disabled.basecomponent.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

/**
 *
 */
public abstract class BaseLazyFragment extends BaseFragment {

    /**
     * Fragment 处理懒加载，为了防止 setUserVisibleHint 进入多次导致数据重复加载
     */

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && mRootView != null  ) {
            lazyLoad();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isVisible() && mRootView != null ) {
            lazyLoad();
        } else {
            lazyloadGone();
            super.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @UiThread
    protected abstract void lazyLoad();
    protected abstract void lazyloadGone();

}
