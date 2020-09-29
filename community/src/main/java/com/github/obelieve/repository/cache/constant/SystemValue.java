package com.github.obelieve.repository.cache.constant;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.github.obelieve.utils.LanguageUtil;
import com.github.obelieve.utils.TelephoneUtil;
import com.zxy.frame.utils.SPUtil;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.TimeZone;


/**
 * Created by zhangzhiqiang_dian91 on 2015/11/25.
 */
public class SystemValue {

    /****
     * 1 未知 2 2g 3 3g,引入通用平台代码
     *****/
    private static final String MOBILE_UNKNOWN = "unkown";
    private static final String MOBILE_2G = "2G";
    private static final String MOBILE_3G = "3G";
    private static final String MOBILE_4G = "4G";
    private static final String MOBILE_WIFI = "wifi";
    // 程序一启动就初始化的全局变量
    public static String firmwareVersion = "";
    public static int versionCode = 1;
    public static String versionName = "";
    public static String packageName = "";
    public static String chl = "";
    public static String imei = "";
    public static String mac = "";
    public static String imsi = "";
    public static String nt = "0";
    public static String abi = "";
    public static String cid = ""; //友盟推送cid
    public static String system = "0"; //手机系统
    public static String model = "";
    public static String resolution = "";
    public static String deviceId = "";
    public static String brand = "";
    public static int sdk = 7;
    public static String timezone = "";// 时区
    public static String language = "";
    public static String locale = "";
    public static String client_ip = "";
    public static int[] resolutionXY = new int[2];
    //时间戳 单位：秒
    public static long ts;

    public static String token;
    public static String pushToken;
    public static String uploadToken;

    public static float sysDensity = 0;
    public static int sysDensityDpi = 0;
    public static int sysWidth = 0;
    public static int sysHeight = 0;

    public static String mUcToken;

    public static String imid;
    public static String imtoken;

    /**
     * 程序一启动就初始化的全局变量,如imei imsi ...
     *
     * @param ctx
     */
    public static void init(Context ctx) {
        try {
            DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();

            sysDensity = metrics.density;
            sysDensityDpi = metrics.densityDpi;
            sysWidth = Math.min(metrics.widthPixels, metrics.heightPixels);
            sysHeight = Math.max(metrics.widthPixels, metrics.heightPixels);

            pushToken = SPUtil.getInstance().getString(PreferenceConst.PUSH_TOKEN);
            uploadToken = SPUtil.getInstance().getString(PreferenceConst.PUSH_TOKEN);

            cid = SPUtil.getInstance().getString(PreferenceConst.CID_PUSH);
            firmwareVersion = TelephoneUtil.getFirmWareVersion();
            resolution = TelephoneUtil.getScreenResolution(ctx);
            resolutionXY = TelephoneUtil.getScreenResolutionXY(ctx);
            versionCode = TelephoneUtil.getVersionCode(ctx);
            versionName = TelephoneUtil.getVersionName(ctx);
            packageName = ctx.getPackageName();
//            chl = UMUtils.getChannelByXML(ctx);
            mac = TelephoneUtil.getWifiMacAddress(ctx);

            /*
			 * 影响较大，暂时先不改
			 */
            if (mac != null && mac.length() > 0) {
                mac = mac.replace("-", "");
                mac = mac.replace(":", "");
            }
            deviceId = TelephoneUtil.getDeviceId(ctx);
            //imsi = TelephoneUtil.getIMSI(ctx);
            abi = TelephoneUtil.getCPUABI();
            sdk = TelephoneUtil.getApiLevel();
            nt = getNT(ctx);
            model = TelephoneUtil.getMachineName();
            brand = TelephoneUtil.getBrandName();
            ts = System.currentTimeMillis() / 1000;

            // 时区 TODO时区编码
            TimeZone tz = TimeZone.getDefault();
            timezone = tz.getDisplayName(false, TimeZone.SHORT);
            language = LanguageUtil.getInstance().getCurrentLanguage(ctx);

            boolean isFirst = SPUtil.getInstance().getBoolean(PreferenceConst.KEY_FIRST_ENTER, true);
            if (isFirst){
                locale = ctx.getResources().getConfiguration().locale.getCountry();
                SPUtil.getInstance().putString(PreferenceConst.KEY_LOCALE, locale);
            }else {
                locale = SPUtil.getInstance().getString(PreferenceConst.KEY_LOCALE, ctx.getResources().getConfiguration().locale.getCountry());
            }

            client_ip = getLocalIp();
            imei = TelephoneUtil.getIMEI(ctx);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getLocalIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements())
            {
                // 得到每一个网络接口绑定的所有ip
                NetworkInterface nif = en.nextElement();
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements())
                {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress()) {
                        ipaddress = ip.getHostAddress();
                        return ipaddress;
                    }
                }
            }
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        return ipaddress;

    }


    /**
     * 取设备GPU
     *
     * @return
     */
    public static String getGPU() {
        return "";
    }

    /**
     * 取网络类型
     *
     * @return
     */
    static String getNT(Context context) {
        //1:运营商,2:wifi,其它默认99
        try {
            if (context == null) {
                return "99";
            }
            ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            if (networkInfo == null) {
                // 无网络
                return "99";
            }
            if (!networkInfo.isConnected()) {
                // 未连接也认为是无网络
                return "99";
            }
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return "2";
            }
            if (type == ConnectivityManager.TYPE_MOBILE) {
                String mobileSubtype = mobileNetworkType(context);
                return "1";
            }else {
                return "99";
            }
        } catch (Exception e) {

        }
        return "99";
    }

    public static boolean isWap(Context context) {
        try {
            if (context == null) {
                return false;
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                return false;
            }
            NetworkInfo mobNetInfo = connectivityManager.getActiveNetworkInfo();
            if (mobNetInfo == null || "wifi".equals(mobNetInfo.getTypeName().toLowerCase())) {
                return false;
            }

            if (mobNetInfo.getExtraInfo().contains("wap")) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    private static String mobileNetworkType(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return MOBILE_UNKNOWN;
        }
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return MOBILE_2G; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return MOBILE_2G; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return MOBILE_2G; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return MOBILE_3G; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return MOBILE_3G; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return MOBILE_2G; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return MOBILE_3G; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return MOBILE_3G; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return MOBILE_3G; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return MOBILE_3G; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return MOBILE_3G; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return MOBILE_3G; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return MOBILE_3G; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return MOBILE_2G; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return MOBILE_4G; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return MOBILE_UNKNOWN;
            default:
                return MOBILE_UNKNOWN;
        }
    }
}
