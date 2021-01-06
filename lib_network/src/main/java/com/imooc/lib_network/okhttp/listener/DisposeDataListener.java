package com.imooc.lib_network.okhttp.listener;

/**
 * @author zengx
 * @description 业务逻辑层真正处理的地方，包括java层异常和业务层异常
 */
public interface DisposeDataListener {

    /**
     * 请求成功回调事件处理
     * @param responseObj  对象
     */
    void onSuccess(Object responseObj);

    /**
     * 请求失败回调事件处理
     * @param reasonObj  对象
     */
    void onFailure(Object reasonObj);

}
