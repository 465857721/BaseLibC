package com.wetime.fanc;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.king.batterytest.fbaselib.utils.MLoggerInterceptor;
import com.king.batterytest.fbaselib.utils.SharePreferenceUtil;
import com.king.batterytest.fbaselib.utils.SignCheck;
import com.king.batterytest.fbaselib.utils.Tools;
import com.king.batterytest.fbaselib.view.MyMaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class FApp extends Application {
    private List<Activity> oList;
    private SharePreferenceUtil spu;
    private static FApp instance;
    public static IWXAPI mWxApi;

    @Override
    public void onCreate() {
        super.onCreate();
        registToWX();
        ArrayList<String> keys = new ArrayList<>();
        if (isd(this)) {
            keys.add("41:C2:55:46:96:1E:86:A8:FC:21:77:2C:77:37:6C:C9:30:41:C9:FA");//test
        }




        boolean checked = false;
        for (String s : keys) {
            SignCheck signCheck = new SignCheck(this, s);
            if (signCheck.check()) {
                checked = true;
            }

        }
        if (!checked) {
            System.exit(0);
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new MLoggerInterceptor("http", true))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)

                .build();

        OkHttpUtils.initClient(okHttpClient);
        oList = new ArrayList<>();
        spu = Tools.getSpu(this);
        instance = this;


    }

    public static boolean isd(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    static {

        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new MyMaterialHeader(context);
            }
        });

        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {

                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    public static FApp getInstance() {
        return instance;
    }


    public void addActivity(Activity activity) {

        if (!oList.contains(activity)) {
            oList.add(activity);
        }
    }

    private void registToWX() {

        mWxApi = WXAPIFactory.createWXAPI(this, "wx2fbcb61b6e5b1384", true);

        mWxApi.registerApp("wx2fbcb61b6e5b1384");
    }


    public void removeActivity(Activity activity) {

        if (oList.contains(activity)) {
            oList.remove(activity);
            activity.finish();
        }
    }


    public void removeALLActivity() {

        for (Activity activity : oList) {
            activity.finish();
        }
    }
}

