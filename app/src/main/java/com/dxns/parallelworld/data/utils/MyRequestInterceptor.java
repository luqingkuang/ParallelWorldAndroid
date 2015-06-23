package com.dxns.parallelworld.data.utils;

import android.widget.Toast;

import com.dxns.parallelworld.core.Database;
import com.dxns.parallelworld.core.ParallelwordApplacation;
import com.dxns.parallelworld.util.ToastUtils;

import retrofit.RequestInterceptor;

/**
 * @author kingty
 * @title MyInterceptor
 * @description
 * @modifier
 * @date
 * @since 15/6/23 下午4:47
 */
public class MyRequestInterceptor {

    public static RequestInterceptor get(){
        return new RequestInterceptor() {

            @Override
            public void intercept(RequestFacade request) {
                String userId = Database.getSharedPreferences().getString("userId","");

                if(userId.equals("")){
                    ToastUtils.show("请登录", Toast.LENGTH_SHORT);
                    //此处跳转登录界面
                }else {
                    request.addQueryParam("v", ParallelwordApplacation.getPackageInfo().versionName);
                    request.addQueryParam("device", "android");
                    request.addQueryParam("userId", userId);
                }

            }
        };
    }
}
