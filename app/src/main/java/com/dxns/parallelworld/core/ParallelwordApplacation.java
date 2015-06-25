package com.dxns.parallelworld.core;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.dxns.parallelworld.util.DisplayAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tumblr.remember.Remember;

/**
 * 应用的applacation
 * @author kingty
 * @title ParallelwordApplacation
 * @description
 * @modifier
 * @date
 * @since 15/6/18 下午5:30
 */
public class ParallelwordApplacation extends android.support.multidex.MultiDexApplication {

    private static final String TAG  = "ParallelwordApplacation";
    private static ParallelwordApplacation application;
    private static PackageInfo packageInfo;
    public static ParallelwordApplacation get() {
        return application;
    }

    public static PackageInfo getPackageInfo() {
        return packageInfo;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        //初始化fresco图片库
        Fresco.initialize(getApplicationContext());
        //初始化Remember Reference库
        Remember.init(getApplicationContext(), "com.dxns.parallelworld");
        application = this;
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Database.get();
    }
}
