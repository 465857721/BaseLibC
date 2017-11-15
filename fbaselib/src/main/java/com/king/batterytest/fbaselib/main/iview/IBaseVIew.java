package com.king.batterytest.fbaselib.main.iview;

/**
 * Created by zhoukang on 2017/5/15.
 */

public interface IBaseVIew {

    void showLoading();//展示加载框

    void showLoading(boolean can);// 是否能用户取消

    void onTimeOut();

    void dismissLoading();//取消加载框展示

    void onNetError();// 网络错误

    void onError(String s);// 接口 返回非 0

    void onFormJsonError();

    void onNoPermission();

    String getToken();

    String getJd();

    String getWd();

}
