package com.juntai.disabled.basecomponent.mvp;

/**
 * @Author: tobato
 * @Description: 作用描述
 * @CreateDate: 2020/3/3 9:46
 */
public interface IViewExpand<T> {

    /**
     *   成功后
     * @param tag  标识
     * @param t  实体
     */
    void   onSuccess(String tag, T t);

    /**
     * 错误后
     * @param tag  标识
     * @param t
     */
    void   onError(String tag, T t);
}
