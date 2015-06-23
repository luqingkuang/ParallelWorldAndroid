package com.dxns.parallelworld.data.api;

import android.content.Context;

import com.dxns.parallelworld.core.AppConfig;
import com.dxns.parallelworld.data.exception.MyErrorHandler;
import com.dxns.parallelworld.data.utils.GosnUtils;
import com.dxns.parallelworld.data.utils.MyRequestInterceptor;
import com.dxns.parallelworld.data.utils.OkHttpUtils;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * @author kingty
 * @title Service
 * @description
 * @modifier
 * @date
 * @since 15/6/17 下午11:08
 */
public class Api {


    public  static<T> T createApi(Context context,Class<T> clazz){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(AppConfig.BASEURL)
                .setRequestInterceptor(MyRequestInterceptor.get())
                .setConverter(new GsonConverter(GosnUtils.newInstance()))
                .setClient(new OkClient(OkHttpUtils.getInstance(context)))
                .setErrorHandler(new MyErrorHandler())
                .setLogLevel(AppConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .build();
        return restAdapter.create(clazz);
    }
}
