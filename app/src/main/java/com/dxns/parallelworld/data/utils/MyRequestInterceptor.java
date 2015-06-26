package com.dxns.parallelworld.data.utils;

import android.widget.Toast;

import com.dxns.parallelworld.core.AppConfig;
import com.dxns.parallelworld.core.Database;
import com.dxns.parallelworld.core.ParallelwordApplacation;
import com.dxns.parallelworld.data.service.URL;
import com.dxns.parallelworld.util.MD5Utils;
import com.dxns.parallelworld.util.ToastUtils;
import com.dxns.parallelworld.view.navigation.Navigator;
import com.tumblr.remember.Remember;

import retrofit.RequestInterceptor;
import retrofit.client.Request;

/**
 * @author kingty
 * @title MyInterceptor
 * @description 拦截URL请求，添加公共参数
 * @modifier
 * @date
 * @since 15/6/23 下午4:47
 */
public class MyRequestInterceptor {






    public static RequestInterceptor get(){
        return new RequestInterceptor() {
            /**
             * 不需要拦截的列表
             */
            String[] unInterceptUrls = {
                    URL.REGISTER,
                    URL.LOGIN

            };
            /**
             * 是否是
             * @param url
             * @return
             */
            private boolean isInterceptUrl(String url){
                for(String s:unInterceptUrls){
                    if(s.equals(url)){
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void intercept(RequestFacade requestFacade) {
                String userId = Remember.getString(AppConfig.USERID, "");
                String token = Remember.getString(AppConfig.TOKEN,"");
                if(userId.equals("")||token.equals("")){
                    if(isInterceptUrl(requestFacade.getBaseUrl())){
                        ToastUtils.show("请登录", Toast.LENGTH_SHORT);
                        Navigator.ToLoginActivity();
                    }else {
                        requestFacade.addQueryParam("v", ParallelwordApplacation.getPackageInfo().versionName);//版本号
                        requestFacade.addQueryParam("device", "android");//设备类型
                    }



                }else {
                    long timestampStr = System.currentTimeMillis();
                    String SignStr = requestFacade.getBaseUrl()+"&token="+token+"&timestamp="+timestampStr;
                    String sign = MD5Utils.generate(SignStr);//MD5加盐加密，签名串

                    requestFacade.addQueryParam("v", ParallelwordApplacation.getPackageInfo().versionName);//版本号
                    requestFacade.addQueryParam("device", "android");//设备类型
                    requestFacade.addQueryParam("userId", userId);//用户ID
                    requestFacade.addQueryParam("sign", sign);//签名串
                    requestFacade.addQueryParam("timestamp", timestampStr+"");//时间戳
                }
            }
        };
    }
}
