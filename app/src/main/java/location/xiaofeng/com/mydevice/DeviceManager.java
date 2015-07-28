package location.xiaofeng.com.mydevice;

import android.app.Activity;
import android.os.Handler;

/**
 * Created by S on 2015/7/27.
 */
public class DeviceManager {


    public static final int BATTERY_INFO =1;
    public static final int AvailRamMemory_INFO = 2;


    private DeviceReceiver deviceReceiver;
    private DeviceGetter deviceGetter;
    private DeviceInfo deviceInfo;

    /**
     * 获取设备获取器
     * @param handler
     * @return
     */
    public DeviceGetter getDeviceGetterInstance(Handler handler){
        return new DeviceGetter(handler);
    }

    /**
     * 获取设备注册器
     * @param handler
     * @return
     */
    public  DeviceReceiver getDeviceReceiverInstance(Handler handler){
        return new DeviceReceiver(handler);
    }
    /**
     * 获取设备信息管理器
     * @return
     */
    public  DeviceInfo getDeviceInfoInstance(){
        if(deviceInfo==null){
            synchronized(DeviceManager.class){
                if(deviceInfo==null){
                    deviceInfo=new DeviceInfo();
                }
            }
        }
        return deviceInfo;
    }

}
