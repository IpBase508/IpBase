package com.ygip.ipbase_android.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LockyLuo on 2017/10/6.
 */

public class NetworkAddressUtils {

    /**
     * 获取手机的MAC地址
     * @return
     */
    public static HashMap<String ,String> getMacM(){
        HashMap<String ,String> hashMap=new HashMap<>();

        try {
            Enumeration<NetworkInterface> networkInterfaces=NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements())
            {
                NetworkInterface networkInterface=networkInterfaces.nextElement();
                byte[] adress=networkInterface.getHardwareAddress();
                if (adress == null || adress.length == 0) {
                    continue;
                }
                StringBuffer stringBuffer=new StringBuffer();
                for (byte b:adress) {
                    stringBuffer.append(String.format("%02X:",b));
                }
                if (stringBuffer.length()>0){
                    stringBuffer.deleteCharAt(stringBuffer.length()-1);
                }
                String mac=stringBuffer.toString();
                hashMap.put(networkInterface.getName(),mac);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hashMap;
    }


    public static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    public static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * 获取手机的Mac地址，在Wifi未开启或者未连接的情况下也能获取手机Mac地址
     */
    public static String getMacAddress(Context context) {
        String macAddress = null;
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            macAddress = wifiInfo.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 获取手机的Ip地址
     */
    public static String getIpAddress(Context context) {
        String IpAddress = null;
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            IpAddress = intToIpAddress(wifiInfo.getIpAddress());
        }
        return IpAddress;
    }

    /**
     * 获取WifiInfo
     */
    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        if (null != wifiManager) {
            info = wifiManager.getConnectionInfo();
        }
        return info;
    }


    public static long ipAddressToint(String ip) {
        String[] items = ip.split("\\.");
        return Long.valueOf(items[0]) << 24
                | Long.valueOf(items[1]) << 16
                | Long.valueOf(items[2]) << 8
                | Long.valueOf(items[3]);
    }

    public static String intToIpAddress(long ipInt) {
        StringBuffer sb = new StringBuffer();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取当前可连接Wifi列表
     */
    public static List<?> getAvailableNetworks(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> wifiList = null;
        if (wifiManager != null) {
            wifiList = wifiManager.getScanResults();
        }
        return wifiList;
    }

    /**
     * 获取已连接的Wifi路由器的Mac地址
     */
    public static String getConnectedWifiMacAddress(Context context) {
        String connectedWifiMacAddress = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> wifiList;

        if (wifiManager != null) {
            wifiList = wifiManager.getScanResults();
            WifiInfo info = wifiManager.getConnectionInfo();
            if (wifiList != null && info != null) {
                for (int i = 0; i < wifiList.size(); i++) {
                    ScanResult result = wifiList.get(i);
                    if (info.getBSSID().equals(result.BSSID)) {
                        connectedWifiMacAddress = result.BSSID;
                    }
                }
            }
        }
        return connectedWifiMacAddress;
    }
}