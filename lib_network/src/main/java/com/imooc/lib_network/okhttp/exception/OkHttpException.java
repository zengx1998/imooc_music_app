package com.imooc.lib_network.okhttp.exception;

/**
 * @author zengx
 * 自定义异常类,返回ecode,emsg到业务层
 */
public class OkHttpException extends Exception {

    /*
    序列化的标记，如果没有，可能会序列化失败
    **/
    private static final long serialVersionUID = 1L;

    /**
     * the server return code
     */
    private int ecode;

    /**
     * the server return error message
     */
    private Object emsg;

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public Object getEmsg() {
        return emsg;
    }
}