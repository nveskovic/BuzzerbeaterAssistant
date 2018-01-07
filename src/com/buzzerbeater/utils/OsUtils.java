package com.buzzerbeater.utils;

public class OsUtils {

    public enum OsType {
        WINDOWS, MAC, LINUX, OTHER
    };
    private static final String osName = System.getProperty("os.name").toLowerCase();

    private static boolean isWindows() {
        return (osName.indexOf("win") >= 0);
    }

    private static boolean isMac() {
        return (osName.indexOf("mac") >= 0);
    }

    private static boolean isUnix() {
        return (osName.indexOf("nux") >= 0);
    }

    public static OsType getOsType() {
        if(isUnix()) return OsType.LINUX;
        if(isWindows()) return OsType.WINDOWS;
        if(isMac()) return OsType.MAC;
        return OsType.OTHER;
    }
}
