package com.dxns.parallelworld.data.utils;

/**
 * @author kingty
 * @title GosnUtils
 * @description
 * @modifier
 * @date
 * @since 15/6/23 下午4:19
 */
import com.dxns.parallelworld.data.annotation.ParamName;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;


import java.lang.reflect.Field;
import java.util.Date;

/**
 * 自定义的Gson
 */
public class GosnUtils {

    public static Gson newInstance() {
        GsonBuilder builder = new GsonBuilder();

        builder.setFieldNamingStrategy(new AnnotateNaming());


        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)        //设置把所有的json都转换成小写
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        return builder.create();
    }

    /**
     * 注解对需要网络传输bean进行标注
     */
    private static class AnnotateNaming implements FieldNamingStrategy {

        @Override
        public String translateName(Field field) {
            ParamName a = field.getAnnotation(ParamName.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }
}