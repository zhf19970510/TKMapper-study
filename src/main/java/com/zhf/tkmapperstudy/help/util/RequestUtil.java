package com.zhf.tkmapperstudy.help.util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class RequestUtil {

    private static final String FILE_URL_IP_BLACKLIST = "/usr/local/ipInfo/";
    // 记录每秒某一IP或者用户ID的请求数量，定期清理（秒）
    private static Map<String, Integer> requestMap = new HashMap<>();
    // 记录每分钟某一IP的请求数量，定期清理（分钟）
    private static Map<String, Integer> loginMap = new HashMap<>();
    // 记录被限制访问请求IP的限制开始时间
    private static final Map<String, Long> limitMap = new HashMap<>();
    // 记录被限制访问请求IP被限制的次数
    private static final Map<String, Integer> ipBlacklistMap = new HashMap<>();
    // 记录黑名单IP
    private static final Set<String> ipBlacklist = new HashSet<>();
    // 记录白名单IP
    private static final Set<String> ipWhitelist = new HashSet<>();
    private static String fileTxtBlacklist;
    private static String fileTxtWhitelist;
    private static String fileTxtBlacklistNumber;

    /**
     * 加载数据
     *
     * @param objectName 项目名称
     */
    public static void start(String objectName) {
        // 拼接记录文件路径
        String path = FILE_URL_IP_BLACKLIST + objectName;
        fileTxtBlacklist = path + "/ipBlacklist.txt";
        fileTxtWhitelist = path + "/ipWhitelist.txt";
        fileTxtBlacklistNumber = path + "/ipBlacklistNumber.txt";
        // 定时清理记录的请求数
        Thread qpsThread = new Thread(new qpsRunnable());
        qpsThread.start();
        Thread loginThread = new Thread(new loginRunnable());
        loginThread.start();
        // 创建目录
        File file = new File(path);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }
        // 加载ip黑名单
        loadIpBlacklist();
        // 加载ip白名单
        loadIpWhitelist();
        // 加载达到限制条件的ip以及次数
        loadIpBlacklistNumber();
//        System.out.println("RequestUtil load the success!");
    }

    /**
     * 访问请求qps限制
     */
    public static boolean insertRequest(String id, int qps) {
        boolean flag = false;
        Integer integer = requestMap.get(id);
        if (integer == null) {
            requestMap.put(id, 1);
            flag = true;
        } else {
            if (integer < qps) {
                requestMap.put(id, integer + 1);
                flag = true;
            }
        }
        return !flag;
    }

    /**
     * 访问请求次数限制
     *
     * @param ip    请求ip
     * @param num   每分钟限制请求次数
     * @param times 达到请求次数限制访问时间(分钟)
     * @return 返回时间戳，没达到限制返回null
     */
    public static Long loginRequestLimit(String ip, int num, int times) {
        Long timestamp = getLoginLimit(ip, times);
        if (timestamp != null) {
            return timestamp;
        }
        Integer integer = loginMap.get(ip);
        if (integer == null) {
            loginMap.put(ip, 1);
        } else {
            if (integer < num) {
                loginMap.put(ip, integer + 1);
            } else {
                timestamp = System.currentTimeMillis() / 1000L;
                limitMap.put(ip, timestamp);
                insertIpBlacklistMap(ip, true);
                insertIpBlacklistNumber(ip);
            }
        }
        return timestamp;
    }


    /**
     * 验证此ip是否在黑名单中
     */
    public static boolean isIpBlacklist(String ip) {
        return ipBlacklist.contains(ip);
    }

    /**
     * 验证此ip是否在白名单中
     */
    public static boolean isIpWhitelist(String ip) {
        return ipWhitelist.contains(ip);
    }

    public static void addIpWhitelist(List<String> ips) {
        StringBuilder sb = new StringBuilder();
        for (String ip : ips) {
            boolean add = ipWhitelist.add(ip);
            if (add) {
                sb.append(ip).append("\t");
            }
        }
        if (!sb.toString().isEmpty()) {
            File fileTxt = new File(fileTxtWhitelist);
            outputTxt(fileTxt, sb, true);
        }
    }

    /**
     * 解除白名单ip
     */
    public static void deleteWhiteList(List<String> ips) {
        boolean flag = false;
        for (String ip : ips) {
            flag = ipWhitelist.remove(ip);
        }
        if (flag) {
            List<String> list = new ArrayList<>(ipWhitelist);
            File fileTxt = new File(fileTxtWhitelist);
            updateTxt(list, fileTxt);
        }
    }

    /**
     * 解除黑名单ip
     */
    public static void deleteIpBlacklistByIp(List<String> ips) {
        boolean flag = false;
        Integer num = null;
        for (String ip : ips) {
            flag = ipBlacklist.remove(ip);
            num = ipBlacklistMap.remove(ip);
        }
        if (flag) {
            List<String> list = new ArrayList<>(ipBlacklist);
            File fileTxt = new File(fileTxtBlacklist);
            updateTxt(list, fileTxt);
        }
        if (num != null) {
            Map<String, Integer> map = new HashMap<>(ipBlacklistMap);
            updateTxt(map);
        }
    }

    /**
     * 获取ip黑名单
     */
    public static String getIpBlacklist() {
        return ipBlacklist.toString();
    }

    /**
     * 获取请求ip
     */
    public static String getIpAddr(final HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


    /**
     * 查看是否受限制
     *
     * @param ip    请求ip
     * @param times 达到请求次数限制访问时间(分钟)
     */
    private static Long getLoginLimit(String ip, int times) {
        Long timestamp = limitMap.get(ip);
        if (timestamp == null) {
            return timestamp;
        } else {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.ofHours(8));
            Duration duration = Duration.between(now, localDateTime);
            long minutes = duration.toMinutes();//相差的分钟数
            if (Math.abs(minutes) < times) {
                return timestamp;
            } else {
                limitMap.remove(ip);
            }
        }
        return null;
    }

    /**
     * 更新txt文件
     */
    private static void updateTxt(List<String> list, File fileTxt) {
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s).append("\t");
        }
        outputTxt(fileTxt, result, false);
    }

    private static void updateTxt(Map<String, Integer> map) {
        File fileTxt = new File(fileTxtBlacklistNumber);
        StringBuilder result = new StringBuilder();
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Integer value = entry.getValue();
                String key = entry.getKey();
                for (int i = 0; i < value; i++) {
                    result.append(key).append("\t");
                }
            }
        }
        outputTxt(fileTxt, result, false);

    }

    private synchronized static void outputTxt(File fileTxt, StringBuilder result, boolean append) {
        String str = result.toString();
        OutputStream out = null;
        try {
            out = new FileOutputStream(fileTxt, append);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 将字符串转成字节数组
        byte[] b = str.getBytes();
        try {
            // 将byte数组写入到文件之中
            out.write(b);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 添加达到限制条件的ip次数,达到三次将封掉此IP
     */
    private static void insertIpBlacklistMap(String ip, boolean flag) {
        Integer integer = ipBlacklistMap.get(ip);
        if (integer == null) {
            ipBlacklistMap.put(ip, 1);
        } else {
            if (flag) {
                if (integer < 2) {
                    ipBlacklistMap.put(ip, integer + 1);
                } else {
                    insertIpBlacklist(ip);
                    limitMap.remove(ip);
                }
            } else {
                ipBlacklistMap.put(ip, integer + 1);
            }

        }
    }

    /**
     * 加载达到限制条件的ip以及次数
     */
    private static void loadIpBlacklistNumber() {
        File fileTxt = new File(fileTxtBlacklistNumber);
        boolean flag = isFlag(fileTxt);
        if (flag) {
            String trim = getTxt(fileTxt);
            String[] split = trim.split("\t");
            for (String ip : split) {
                ip = ip.trim();
                if (ip.length() > 0) {
                    insertIpBlacklistMap(ip, false);
                }
            }
        }
    }

    /**
     * 获取txt文件内容
     */
    private static String getTxt(File fileTxt) {
        StringBuilder result = new StringBuilder();
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(fileTxt));
            String s = null;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                //拼接字符串
                result.append(System.lineSeparator()).append(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString().trim();
    }

    /**
     * 加载ip黑名单
     */
    private static void loadIpBlacklist() {
        addCache(fileTxtBlacklist, ipBlacklist);
    }

    /**
     * 加载ip白名单
     */
    private static void loadIpWhitelist() {
        addCache(fileTxtWhitelist, ipWhitelist);
    }

    private static void addCache(String fileTxtWhitelist, Set<String> ipList) {
        File fileTxt = new File(fileTxtWhitelist);
        boolean flag = isFlag(fileTxt);
        if (flag) {
            String trim = getTxt(fileTxt);
            String[] split = trim.split("\t");
            for (String ip : split) {
                ip = ip.trim();
                if (ip.length() > 0) {
                    ipList.add(ip);
                }
            }
        }
    }

    private static boolean isFlag(File fileTxt) {
        boolean flag = true;
        if (!fileTxt.exists()) {
            try {
                boolean newFile = fileTxt.createNewFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            flag = false;
        }
        return flag;
    }

    /**
     * 添加达到限制条件的ip
     */
    private static void insertIpBlacklistNumber(String ip) {
        File fileTxt = new File(fileTxtBlacklistNumber);
        StringBuilder sb = new StringBuilder(ip);
        outputTxt(fileTxt, sb, true);
    }

    /**
     * 添加ip黑名单
     */
    private static void insertIpBlacklist(String ip) {
        File fileTxt = new File(fileTxtBlacklist);
        StringBuilder sb = new StringBuilder(ip);
        outputTxt(fileTxt, sb, true);
        ipBlacklist.add(ip);
    }


    /**
     * 定时清理qps记录
     */
    static class qpsRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    requestMap = new HashMap<>();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 定时清理登录请求次数记录
     */
    static class loginRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(60000);
                    loginMap = new HashMap<>();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


}
