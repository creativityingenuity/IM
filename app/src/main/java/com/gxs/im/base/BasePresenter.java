package com.gxs.im.base;

/**
 * Call:vipggxs@163.com
 * Created by YT
 */

public interface BasePresenter<T extends BaseView> {
    /**
     * 与view进行关联
     * @param view
     */
    void attachView(T view);

    /**
     * 与view解除关联
     */
    void detachView();
}
