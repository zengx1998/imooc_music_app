package com.imooc.lib_network.okhttp.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 *
 * @author zengx
 */
public class CommonRequest {

    public static Request createPostRequest(String url, RequestParams params) {
        return createPostRequest(url, params, null);
    }

    /**
     * 带请求头的 post 请求
     * @param url  网络地址
     * @param params 参数
     * @param headers  请求头
     * @return request
     */
    public static Request createPostRequest(String url,RequestParams params,RequestParams headers){
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if ( params != null){
            for (Map.Entry<String,String> entry : params.urlParams.entrySet()){
                //参数遍历
                mFormBodyBuild.add(entry.getKey(),entry.getValue());
            }
        }
        Headers.Builder mHeadersBuild = new Headers.Builder();
        if (headers != null){
            for (Map.Entry<String,String> entry : headers.urlParams.entrySet()){
                //请求头遍历
                mHeadersBuild.add(entry.getKey(),entry.getValue());
            }
        }

        FormBody mFormBody = mFormBodyBuild.build();
        Headers mHeader = mHeadersBuild.build();
        Request request = new Request.Builder()
                .url(url)
                .headers(mHeader)
                .post(mFormBody)
                .build();
        return request;
    }

    public static Request createGetRequest(String url, RequestParams params) {
        return createGetRequest(url, params, null);
    }

    /**
     * 带请求头的 get 请求
     * @param url  网络地址
     * @param params 参数
     * @param headers  请求头
     * @return request
     */
    public static Request createGetRequest(String url,RequestParams params,RequestParams headers){
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), entry.getValue());
            }
        }
        Headers mHeader = mHeaderBuild.build();
        return new Request.Builder().
                url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .headers(mHeader)
                .build();
    }


    /**
     * 文件上传请求
     */
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    public static Request createMultiPostRequest(String url, RequestParams params) {
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                } else if (entry.getValue() instanceof String) {

                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBody.build()).build();
    }
}
