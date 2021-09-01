package com.zhf.tkmapperstudy.help.util;

public class StaticUtil {

    public static ThreadLocal<String> permissions = new ThreadLocal<>();

    public static String dataSource = "mysql";

    public static String activeProfiles = "dev";
}
