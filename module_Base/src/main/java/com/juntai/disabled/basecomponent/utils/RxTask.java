package com.juntai.disabled.basecomponent.utils;

/**
 * Author:wang_sir
 * Time:2019/2/12 17:50
 * Description:This is RxTask
 */
public abstract class RxTask<T> {

    private T t;


    public abstract T doOnIoThread();

    public abstract void doOnUIThread(T t);

}
