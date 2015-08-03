package location.xiaofeng.com.mydevice;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.UUID;

/**
 * Created by S on 2015/7/20.
 */
public class DeviceGetter {

    private int[] s ;
    Handler handler ;
    BroadcastReceiver batteryReceiver;
    DeviceInfo deviceInfo;
    public DeviceGetter(Handler handler) {
        this.handler = handler;
        initData();
    }

    public void initData(){
        batteryReceiver = MainActivity.getDeviceManager().getDeviceReceiverInstance(handler).getBatteryReceiver();
        deviceInfo =MainActivity.getDeviceManager().getDeviceInfoInstance();
    }
    //手机型号
    public String getPhoneModel() {
        String model = Build.MODEL;
        deviceInfo.setPhoneModle(model);
        return model;
    }

    //手机厂商
    public String getManufacturer() {
        String manufacturer = Build.MANUFACTURER;
        deviceInfo.setManufacturer(manufacturer);
        return manufacturer;
    }

    /* 获取CPU名字 */
    public  String getCpuName() {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("/proc/cpuinfo");
            br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            deviceInfo.setCpuName(array[1]);
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fr != null)
                try {
                    fr.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (br != null)
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
    *Rom存储
    *返回字节
     */
    public long getAvailableRomMemroy() {
        long[] romInfo = new long[2];
        //Total rom memory
        romInfo[0] = getTotalInternalMemorySize();

        //Available rom memory
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfo[1] = blockSize * availableBlocks;
        deviceInfo.setAvailRom(romInfo[1]);
        return romInfo[1];
    }

    /**
     * 获取总内存
     * @return
     * 返回字节
     */
    public long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long totalMemory = totalBlocks * blockSize;
        return totalMemory;
    }
    public String[] getVersion(){
        String[] version={"null","null","null","null"};
        String str1 = "/proc/version";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            version[0]=arrayOfString[2];//KernelVersion
            localBufferedReader.close();
        } catch (IOException e) {
        }
        version[1] = Build.VERSION.RELEASE;// firmware version
        version[2]=Build.MODEL;//model
        version[3]=Build.DISPLAY;//system version
        deviceInfo.setSoftWateVersion(version[1]);
        deviceInfo.setSystemVersion(version[3]);
        return version;
    }

    /**
     * 获取android当前运行内存大小
     */
    public void getAvailRamMemory(final Context mContext) {

        new Thread(){
            @Override
            public void run() {
                ActivityManager am = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                am.getMemoryInfo(mi);
                //mi.availMem; 当前系统的可用内存
                Log.e("dsafas", String.valueOf(mi.availMem));
                Message message = new Message();
                message.what=DeviceManager.AvailRamMemory_INFO;
                message.obj = String.valueOf(mi.availMem);
                        handler.sendMessage(message);
            }
        }.start();

    }
    /**
    获取屏幕分辨率
     */
    public int[] getDisplayMetrics(Activity activity){
        int[] str = new int[2];
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();
        str[0]=screenHeight;
        str[1] = screenWidth;
        return str;
    }
    /*
    有无摄像头
     */
    public boolean getCamera(){
        int x = Camera.getNumberOfCameras();
        if(x>0){
            return true;
        }else{
            return  false;
        }
    }
    /*
    蓝牙mac地址
     */
    public String getBooleanMac(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.getAddress();
    }
    /*
    获取wifi mac
     */
    public String getLocalMacAddress(BaseDeviceActivity activity) {
                 WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
                 WifiInfo info = wifi.getConnectionInfo();
                 return info.getMacAddress();
             }
    /*
    sd卡剩余空间
     */
    public long getAvailaleSize() {

        File path = Environment.getExternalStorageDirectory(); //取得sdcard文件路径
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }
    /*
    总sd卡大小
     */
    public long getAllSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getBlockCount();
        return availableBlocks * blockSize;
    }
    /*
    获取sd卡序列号
     */
    public String getSDSerial(){
        String str1 = null;
        Object localOb;
        try
        {
            str1 = "/sys/block/mmcblk2/device/";
            localOb = new FileReader(str1 + "serial"); // 串号/序列号
            String sd_serial = new BufferedReader((Reader)localOb).readLine();
            System.out.println("serial: " + sd_serial);
            return  sd_serial;
        }catch(Exception e1)
        {
            System.out.println(e1.getMessage());
            return  null;
        }
    }
/*
获取电池信息
 */
    public void getBatteryInfo(Activity activity){

    }

    /**
     * Description : 获取开机的时间
     *
     *@return String 秒数
     *
     */
    public static long getUpTime(Activity context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("这是存储文件的名字",       Context.MODE_PRIVATE);

        long seconds= sharedPreferences.getLong("存储时间的key", new Date().getTime());
        return seconds;
    }
    /*
    获取IEMI
     */
    public String getIEMI(BaseDeviceActivity activity){
        String imei =((TelephonyManager)activity.getSystemService(Activity.TELEPHONY_SERVICE)).getDeviceId();
        return imei;
    }
    /*
    获取UDid
     */
    protected String getUdid (BaseDeviceActivity activity){
        final TelephonyManager tm = (TelephonyManager)activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }
    /**
     * 判断当前手机是否有ROOT权限
     * @return
     */
    public boolean isRoot(){
        boolean bool = false;

        try{
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
                bool = false;
            } else {
                bool = true;
            }
            Log.d("root", "bool = " + bool);
        } catch (Exception e) {

        }
        return bool;
    }
    /*
    获取屏幕状态
     */
    public boolean getScreenLock(Activity activity){
        KeyguardManager mKeyguardManager = (KeyguardManager)activity.getSystemService(Context.KEYGUARD_SERVICE);
        boolean flag = mKeyguardManager.inKeyguardRestrictedInputMode();
        return flag;
    }

    /**
     * Role:获取当前设置的电话号码
     * <BR>Date:2012-3-12
     * <BR>@author CODYY)peijiangping
     */
    public String getNativePhoneNumber(Activity ac) {
        TelephonyManager telephonyManager = (TelephonyManager)ac
                    .getSystemService(Context.TELEPHONY_SERVICE);
        String NativePhoneNumber=null;
        NativePhoneNumber=telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     * 需要加入权限<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/> <BR>
     * Date:2012-3-12 <BR>
     *
     * @author CODYY)peijiangping
     */
    public String getProvidersName(Activity ac) {
        TelephonyManager telephonyManager = (TelephonyManager)ac
                .getSystemService(Context.TELEPHONY_SERVICE);
        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        }
        return ProvidersName;
    }

    /*
    获取imsi
     */
    public  String getImsi(BaseDeviceActivity activity){
        TelephonyManager mTelephonyMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        return imsi;
    }
    /*
    判断手机是否漫游
     */
    public boolean getPhoneRoamState(BaseDeviceActivity activity){
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
        return false;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
        return true;
        }
        return  false;

    }


}

