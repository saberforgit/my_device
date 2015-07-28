package location.xiaofeng.com.mydevice;

import java.util.Map;

/**
 * Created by S on 2015/7/27.
 */
public class DeviceInfo {

    private String cpuName ;
    private String softWateVersion;
    private String systemVersion;
    private String phoneModle;
    private String manufacturer;
    private boolean isRoot;
    private double totalRom;
    private double availRom;
    private double availRam;
    private Map batteryInfo ;
    private int[] registers;

    public DeviceInfo() {
    }

    public int[] getRegisters() {
        return registers;
    }

    public void setRegisters(int[] registers) {
        this.registers = registers;
    }

    public String getSoftWateVersion() {
        return softWateVersion;
    }

    public void setSoftWateVersion(String softWateVersion) {
        this.softWateVersion = softWateVersion;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Map getBatteryInfo() {
        return batteryInfo;
    }

    public void setBatteryInfo(Map batteryInfo) {
        this.batteryInfo = batteryInfo;
    }

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public String getPhoneModle() {
        return phoneModle;
    }

    public void setPhoneModle(String phoneModle) {
        this.phoneModle = phoneModle;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public double getTotalRom() {
        return totalRom;
    }

    public void setTotalRom(double totalRom) {
        this.totalRom = totalRom;
    }

    public double getAvailRom() {
        return availRom;
    }

    public void setAvailRom(double availRom) {
        this.availRom = availRom;
    }

    public double getAvailRam() {
        return availRam;
    }

    public void setAvailRam(double availRam) {
        this.availRam = availRam;
    }
}

