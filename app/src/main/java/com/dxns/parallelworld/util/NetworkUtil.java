package com.dxns.parallelworld.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


import com.dxns.parallelworld.core.ParallelwordApplacation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 13-8-23.
 */
public class NetworkUtil {
    public static int getConnectedType() {
        if (ParallelwordApplacation.get() != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) ParallelwordApplacation.get()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
    public static boolean isWifiConnecting() {
        try {
            WifiManager wifi = (WifiManager) ParallelwordApplacation.get().getSystemService(Context.WIFI_SERVICE);
            return wifi.isWifiEnabled()&& getConnectedType() == ConnectivityManager.TYPE_WIFI;//返回true时表示存在，
        } catch (Exception e) {
            Log.e("error", e.toString());
            return false;
        }
    }

    public static boolean isNetConnecting() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) ParallelwordApplacation.get()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("error", e.toString());
        }
        return false;
    }

    private static long parserNumber(String line) throws Exception {
        long ret = 0;
        String[] delim = line.split(" ");
        if (delim.length >= 1) {
            ret = Long.parseLong(delim[0]);
        }
        return ret;
    }

    public static long syncFetchReceivedBytes() {
        // TODO Auto-generated method stub
        ProcessBuilder cmd;
        long readBytes = 0;
        BufferedReader rd = null;
        try {
            String[] args = {"/system/bin/cat", "/proc/net/dev"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            rd = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line;
            int linecount = 0;
            while ((line = rd.readLine()) != null) {
                linecount++;
                if (line.contains("lan0") || line.contains("eth0")) {
                    String[] delim = line.split(":");
                    if (delim.length >= 2) {
                        readBytes = parserNumber(delim[1].trim());
                        break;
                    }
                }
            }
            rd.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return readBytes;
    }
}
