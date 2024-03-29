package com.smile.studio.libsmilestudio.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.io.File;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("WrongConstant")
public class AndroidDeviceInfo {

    public static String platform(Context context) {
        if (isEmulator())
            return "Emulator";
        if (isHdmiSwitchSet())
            return "Box";
        if (isDirectToTV(context))
            return "TV";
        if (isTablet(context)) {
            return "Tablet";
        } else {
            return "Phone";
        }
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static boolean isHdmiSwitchSet() {
        // The file '/sys/devices/virtual/switch/hdmi/state' holds an int -- if it's 1 then an HDMI device is connected.
        // An alternative file to check is '/sys/class/switch/hdmi/state' which exists instead on certain devices.
        File switchFile = new File("/sys/devices/virtual/switch/hdmi/state");
        if (!switchFile.exists()) {
            switchFile = new File("/sys/class/switch/hdmi/state");
        }
        try {
            Scanner switchFileScanner = new Scanner(switchFile);
            int switchValue = switchFileScanner.nextInt();
            switchFileScanner.close();
            return switchValue > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDirectToTV(Context context) {
        return (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEVISION)
                || context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK));
    }

    public static boolean isTablet(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE;
    }

    public static void swipeToUnlock(Window w) {
        w.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        w.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        w.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    @SuppressLint("MissingPermission")
    public static void Vibrate(Context context, int time) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    /**
     * ANDROID_ID là một chuỗi số 64-bit (như là một chuỗi hex) được ngẫu nhiên
     * được tạo ra khi người dùng đầu tiên thiết lập các thiết bị và phải duy
     * trì liên tục cho các đời của thiết bị của người dùng. Các giá trị có thể
     * thay đổi nếu một thiết lập lại nhà máy được thực hiện trên các thiết bị.
     * Xem thêm thông tin trong lớp Secure#ANDROID_ID
     *
     * @param context
     * @return Trả về một chuỗi số 64-bit (như là một chuỗi hex). Trả về chuỗi
     * rỗng nếu ID thiết bị không có sẵn.
     */
    public static String getAndroidID(Context context) {
        String str = "";
        try {
            if ((str = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID)) != null) {
                return str;
            }
        } catch (Exception e) {
            Debug.e(e.toString());
        }
        return "";
    }

    public static String getAndroidName() {
        return Build.MODEL;
    }

    public static int getAndroidSDKInt() {
        return Build.VERSION.SDK_INT;
    }

    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getAppVersionName(Context context) {
        String versionName = null;
        PackageInfo pInfo;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * Lấy tên nhà mạng SIM số
     *
     * @param context
     * @return
     */
    @SuppressWarnings("WrongConstant")
    public static String getCarrierName(Context context) {
        TelephonyManager localTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            return localTelephonyManager.getNetworkOperatorName();
        }
        return "";
    }

    /**
     * Trả về ID thiết bị duy nhất, ví dụ như số IMEI cho GSM và MEID hoặc ESN
     * cho điện thoại CDMA
     *
     * @param context
     * @param defaultValues
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getDeviceID(Context context, String defaultValues) {
        TelephonyManager manager;
        try {
            if (((manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)) != null)
                    && (!TextUtils.isEmpty(manager.getDeviceId()))) {
                return manager.getDeviceId();
            }
        } catch (Exception e) {
            Debug.e(e.getMessage());
        }
        return defaultValues;
    }

    /**
     * Hàm yêu cầu lấy thông tin về danh sách các email google của người dùng.
     *
     * @param context
     * @param type    Chuỗi ký tự chứa các loại tài khoản sẽ trả về, null để lấy tất
     *                cả các tài khoản. Ví dụ: type = "com.google"
     * @return
     */
    public static String[] getEmail(Context context, String type) {
        String[] arrayOfString = new String[0];
        Account[] accounts;
        try {
            if (((accounts = AccountManager.get(context).getAccountsByType(type)) != null) && (accounts.length > 0)) {
                arrayOfString = new String[accounts.length];
                int k = 0;
                Account account;
                int j = accounts.length;
                for (int i = 0; i < j; i++) {
                    account = accounts[i];
                    arrayOfString[(k++)] = (account == null ? "" : account.name);
                }
            }
        } catch (Exception e) {
            Debug.e(e.toString());
        }
        return arrayOfString;
    }

    /**
     * Manufacturer là thông tin mà bạn có thể xem Nhà sản xuất của sản phẩm /
     * phần cứng.
     *
     * @return
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * Model là thông tin mà bạn có thể xem về cách thiết kế sản phẩm.
     *
     * @return
     */
    public static String getModel() {
        return Build.MODEL;
    }

    public static String getSerialNumber() {
        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals("")) serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals("")) serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals("")) serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals("")) serialNumber = Build.SERIAL;

            // If none of the methods above worked
            if (serialNumber.equals("")) serialNumber = null;
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }

        return serialNumber;
    }

    public static String getIPMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            Debug.e("Lỗi: " + e.getMessage());
        }
        return "";
    }

    /**
     * Hàm yêu cầu lấy thông tin về cách kết nối mạng (Internet) của thiết bị là
     * dùng theo phương thức kết nối nào, ví như: Wifi, Wimax, 3G...
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getNetworkType(Context context) {
        String str = "UNKNOWN";
        NetworkInfo networkinfo = null;
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == 0) {
            try {
                if (((networkinfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo()) != null) && (networkinfo.isConnectedOrConnecting())) {
                    int i = networkinfo.getType();
                    if (i == ConnectivityManager.TYPE_WIFI) {
                        str = "WIFI";
                    } else if (i == ConnectivityManager.TYPE_WIMAX) {
                        str = "WIMAX";
                    } else if ((i == ConnectivityManager.TYPE_MOBILE) || (i == ConnectivityManager.TYPE_MOBILE_DUN)
                            || (i == ConnectivityManager.TYPE_MOBILE_HIPRI)
                            || (i == ConnectivityManager.TYPE_MOBILE_MMS)
                            || (i == ConnectivityManager.TYPE_MOBILE_SUPL)) {
                        str = networkinfo.getSubtypeName();
                    } else {
                        str = networkinfo.getTypeName();
                    }
                }
            } catch (Exception e) {
                Debug.e(e.toString());
            }
        }
        return str;
    }

    /**
     * Hàm yêu cầu lấy thông tin về chuỗi số điện thoại của người sử dụng, chẳng
     * hạn như số MSISDN cho một điện thoại GSM.
     *
     * @param context
     * @return Trả về chuỗi số điện thoại của người sử dụng ví như số MSISDN cho
     * một điện thoại GSM. Ngược lại trả về null nếu nó không có sẵn.
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = null;
        String str = null;
        try {
            if ((tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)) != null) {
                str = tm.getLine1Number();
            }
        } catch (Exception localException) {
            str = str.substring(2);
        }
        return str;
    }

    /**
     * Hàm yêu cầu lấy thông tin về chiều dài và chiều cao màn hình của thiết
     * bị.
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Point getScreenSize(Context context) {
        Point localPoint = new Point();
        Display display = null;
        try {
            display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            if (Build.VERSION.SDK_INT >= 13) {
                display.getSize(localPoint);
            } else {
                localPoint.set(display.getWidth(), display.getHeight());
            }
        } catch (Exception e) {
            Debug.e(e.toString());
        }
        return localPoint;
    }

}
