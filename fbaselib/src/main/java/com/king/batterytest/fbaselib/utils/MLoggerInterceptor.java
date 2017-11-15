package com.king.batterytest.fbaselib.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by zhoukang on 2017/5/17.
 */

public class MLoggerInterceptor implements Interceptor {

    private String tag;
    private boolean showResponse;

    public MLoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = "OkHttpUtils";
        }

        this.showResponse = showResponse;
        this.tag = tag;
    }


    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        this.logForRequest(request);
        Response response = chain.proceed(request);
        return this.logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            Response.Builder e = response.newBuilder();
            Response clone = e.build();
            if (this.showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
//                        Log.e(this.tag, "responseBody\'s contentType : " + mediaType.toString());
                        if (this.isText(mediaType)) {
                            String resp = body.string();

                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        }
                    }
                }
            }

        } catch (Exception var7) {

        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            if (this.showResponse) {
                RequestBody requestBody = request.body();
                String e = request.url().toString();
                Headers headers = request.headers();

                if (request.method().equals("POST")) {
//                    Log.e(this.tag, "contentType : " + request.body().contentType());

                    if (requestBody instanceof FormBody) {
                        for (int i = 0; i < ((FormBody) requestBody).size(); i++) {}
                    } else {
                        //buffer流
                        if (isText(request.body().contentType())) {
                            Buffer buffer = new Buffer();
                            requestBody.writeTo(buffer);
                            String oldParamsJson = buffer.readUtf8();}
                    }
                }
            }
        } catch (Exception var6) {

        }

    }

    private boolean isText(MediaType mediaType) {
        return mediaType.type() != null
                && mediaType.type().equals("text") ? true : mediaType.subtype() != null
                && (mediaType.subtype().equals("json")
                || mediaType.subtype().equals("xml")
                || mediaType.subtype().equals("html")
                || mediaType.subtype().equals("x-www-form-urlencoded")
                || mediaType.subtype().equals("webviewhtml"));
    }

}