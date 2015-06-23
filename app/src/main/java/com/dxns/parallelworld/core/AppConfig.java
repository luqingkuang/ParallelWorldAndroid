package com.dxns.parallelworld.core;

/**
 * APP常量及相关资源定义
 * @author kingty
 * @title AppConfig
 * @description
 * @modifier
 * @date
 * @since 15/6/18 下午5:34
 */
public class AppConfig {
    public static final String DATABASENAME = "parallelworld.db";
    public static final String BASEURL = "http://parallelworld.kingty.club";

    /**
     * 标记是否输出debug信息
     */
    public static boolean DEBUG = true;

    /**
     * okhttp相关
     */
    public static String  RESPONSE_CACHE = "http";
    public static long RESPONSE_CACHE_SIZE = 10*1024*1024;
    public static int HTTP_CONNECT_TIMEOUT = 20*1000;
    public static int HTTP_READ_TIMEOUT = 20*1000;


}
