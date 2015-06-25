package com.dxns.parallelworld.data.utils;

import android.widget.Toast;

import com.dxns.parallelworld.core.AppConfig;
import com.dxns.parallelworld.core.Database;
import com.dxns.parallelworld.core.ParallelwordApplacation;
import com.dxns.parallelworld.util.ToastUtils;

import retrofit.RequestInterceptor;
import retrofit.client.Request;

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
            public void intercept(RequestFacade requestFacade) {
                String userId = Database.getSharedPreferences().getString(AppConfig.USERID,"");
                String token = Database.getSharedPreferences().getString(AppConfig.TOKEN,"");
                if(userId.equals("")||token.equals("")){
                    ToastUtils.show("请登录", Toast.LENGTH_SHORT);
                    //此处跳转登录界面
                }else {

                    requestFacade.addQueryParam("v", ParallelwordApplacation.getPackageInfo().versionName);
                    requestFacade.addQueryParam("device", "android");
                    requestFacade.addQueryParam("userId", userId);

                }


                /**
                 * 测试拿到注解URL
                 *
                 */
                    String url = requestFacade.getBaseUrl();

                    ToastUtils.show(url, Toast.LENGTH_SHORT);
                    System.out.print(url);

            }
        };
    }
}
