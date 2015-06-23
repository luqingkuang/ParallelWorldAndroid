package com.dxns.parallelworld.data.utils;

/**
 * @author kingty
 * @title OkHttpUtils
 * @description
 * @modifier
 * @date
 * @since 15/6/23 下午4:14
 */
import com.dxns.parallelworld.core.AppConfig;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OkHttpClient自定义工具类
 */
public class OkHttpUtils {

    private static OkHttpClient singleton;

    public static OkHttpClient getInstance(Context context) {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    File cacheDir = new File(context.getCacheDir(), AppConfig.RESPONSE_CACHE);

                    singleton = new OkHttpClient();
                    try {
                        singleton.setCache(new Cache(cacheDir, AppConfig.RESPONSE_CACHE_SIZE));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    singleton.setConnectTimeout(AppConfig.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                    singleton.setReadTimeout(AppConfig.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
                }
            }
        }
        return singleton;
    }
}
