package com.zhf.tkmapperstudy.help.util;


import com.alibaba.fastjson.JSON;
import com.zhf.tkmapperstudy.help.exception.CustomException;
import com.zhf.tkmapperstudy.help.result.JsonResult;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.util.CastUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpUtil {


    public static void throwCusException(String message) {
        throw new CustomException(JSON.toJSONString(JsonResult.errorMessage(message)));
    }


    /**
     * 判断对象为空
     *
     * @return 为空返回true，不为空返回false
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            List<Object> list = CastUtils.cast(obj);
            return list.size() == 0;
        }
        if ((obj instanceof String)) {
            return ((String) obj).trim().equals("");
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 跟据传入时间获取星期，格式：周一，周二...周日
     */
    public static String getWeek(LocalDateTime t) {
        int week = t.get(WeekFields.of(DayOfWeek.of(1), 1).dayOfWeek());
        String result = "";
        switch (week) {
            case 1:
                result = "周一";
                break;
            case 2:
                result = "周二";
                break;
            case 3:
                result = "周三";
                break;
            case 4:
                result = "周四";
                break;
            case 5:
                result = "周五";
                break;
            case 6:
                result = "周六";
                break;
            case 7:
                result = "周日";
                break;
            default:
                result = "参数错误";
        }
        return result;
    }

    /**
     * 转换LocalDateTime的toString格式
     */
    public static String lctToString(LocalDateTime l) {
        String s = l.toString();
        int i = s.indexOf(".");
        if (i > 0) {
            return s.replace("T", " ").substring(0, i);
        } else {
            return s.replace("T", " ");
        }
    }

    /**
     * String左对齐
     */
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * String右对齐
     */
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    /**
     * 获取Double保留两位小数
     */
    public static double getDoubleScale(double d) {
        return getDoubleScale(d, 2);
    }

    public static String toStringDoubleScale(double d) {
        return toStringDoubleScale(d, 2);
    }

    public static String toStringDoubleScale(double d, int scale) {
        return Double.toString(BigDecimal.valueOf(d).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    /**
     * 获取Double，自定义保留小数个数
     *
     * @param scale 设置需要保留的小数个数
     */
    public static double getDoubleScale(double d, int scale) {
        return BigDecimal.valueOf(d).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 验证手机号格式
     */
    public static boolean verifyPhone(String str) {
        String regex = "^(1[3-9]\\d{9}$)";
        if (!isEmpty(str)) {
            if (str.length() == 11) {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(str);
                return m.matches();
            }
        }
        return false;
    }

    /**
     * 手机号脱敏
     * xxx****xxxx
     * x: 手机号原数字
     * *: 要替换成的字符，可以是任意字符
     */
    public static String desensitizationPhone(String phone) {
        return desensitizationPhone(phone, "xxx****xxxx");
    }

    /**
     * 手机号脱敏
     *
     * @param phone    13465987562
     * @param template xxx****xxxx  x: 手机号原数字,*: 要替换成的字符，可以是任意字符
     */
    public static String desensitizationPhone(String phone, String template) {
        StringBuilder result = new StringBuilder();
        if (verifyPhone(phone)) {
            if (template.length() == 11) {
                char[] chars = template.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char aChar = chars[i];
                    if (aChar == 'x') {
                        result.append(phone.charAt(i));
                    } else {
                        result.append(aChar);
                    }
                }
            } else {
                System.out.println("模板格式有误");
            }
        } else {
            System.out.println("手机号格式不正确");
        }
        return result.toString();
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (isNotEmpty(files)) {
            for (File file : files) {
                //删除子文件
                if (file.isFile()) {
                    flag = deleteFile(file.getAbsolutePath());
                } //删除子目录
                else {
                    flag = deleteDirectory(file.getAbsolutePath());
                }
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        return dirFile.delete();
    }

    /**
     * 驼峰转下划线
     */
    public static String humpToLine(String str) {
        Pattern humpPattern = Pattern.compile("[A-Z]");
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString().toLowerCase();
    }

    /**
     * 下划线转驼峰
     */
    public static String lineToHump(String str) {
        if (!str.contains("_")) {
            return str;
        }
        Pattern linePattern = Pattern.compile("_(\\w)");
        Matcher matcher = linePattern.matcher(str.toLowerCase());
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 方法描述：获取'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_'内的随机字符串
     *
     * @param length 需要返回的随机字符串长度
     * @return 随机字符串
     */
    public static String getRandomChar(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_";
        return getRandomChar(length, str);
    }

    /**
     * 方法描述：获取自定义字符的随机字符串
     *
     * @param length 需要返回的随机字符串长度
     * @param str    自定义的字符
     * @return 随机字符串
     */
    public static String getRandomChar(int length, String str) {
        StringBuilder result = new StringBuilder();
        if (!isEmpty(str)) {
            Random random = new Random();
            int strLength = str.length();
            for (int i = 0; i < length; i++) {
                result.append(str.charAt(random.nextInt(strLength)));
            }
        }
        return result.toString();
    }

    public static String getParam(Object... param) {
        String result = "";
        if (isEmpty(param)) return result;
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < param.length; i++) {
            map.put("param" + i, param[i]);
        }
        if (map.size() > 0) {
            result = map.toString();
        }
        return result;
    }

    /**
     * 参数校验
     *
     * @return 有为空参数返回true, 否则返回false
     */
    public static boolean paramVerify(Object... params) {
        boolean flag = true;
        for (Object param : params) {
            if (isEmpty(param)) {
                flag = false;
            }
        }
        return !flag;
    }

    /**
     * BASE64加密加强版
     */
    public static String encryptBASE64Plus(String key) {
        Random random = new Random();
        int count = random.nextInt(5) + 1;
        int size = random.nextInt(3) + 1;
        int num = size + count + 1;
        for (int i = 0; i < num; i++) {
            key = encryptBASE64(key);
        }
        return count + "" + size + getRandomChar(num + size) + key;
    }

    public static String encryptBASE64(String key) {
        return (new BASE64Encoder()).encodeBuffer(key.getBytes()).replaceAll("\\p{C}", "");
    }

    /**
     * BASE64解密加强版
     */
    public static String decryptBASE64Plus(String key) {
        int count = Integer.parseInt(key.substring(0, 1));
        int size = Integer.parseInt(key.substring(1, 2));
        int num = size + count + 1;
        key = key.substring(num + size + 2);
        for (int i = 0; i < num; i++) {
            key = decryptBASE64(key);
        }
        return key;
    }

    public static String decryptBASE64(String key) {
        try {
            byte[] bytes = (new BASE64Decoder()).decodeBuffer(key);
            return new String(bytes);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 拷贝list对象里面的值
     */
    public static <E, C> List<C> copyListObjectValue(Collection<? extends E> c, Class<C> clazz) {
        List<C> resultList = new ArrayList<>();
        for (E var1 : c) {
            try {
                C result = clazz.newInstance();
                if (var1 != null && result != null) {
                    BeanUtils.copyProperties(var1, result);
                    resultList.add(result);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }

    /**
     * 下划线转驼峰首字母大小
     */
    public static String getFieldsUpper(String str) {
        String var1 = lineToHump(str);
        return var1.substring(0, 1).toUpperCase() + var1.substring(1);
    }

    /**
     * 截去最后一个字符
     */
    public static String substringLastOne(String str) {
        if (isNotEmpty(str)) {
            return str.substring(0, str.length() - 1);
        } else {
            return "";
        }
    }

    /**
     * 将多个集合整合成一个list集合
     */
    @SafeVarargs
    public static <E> List<E> addListAll(Collection<? extends E>... c) {
        List<E> lists = new ArrayList<>();
        for (Collection<? extends E> var1 : c) {
            lists.addAll(var1);
        }
        return lists;
    }

    /**
     *
     */
    public static String reflect(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(o) != null) list.add(field.getName() + "=" + JSON.toJSONString(field.get(o)));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return StringUtils.join(list, '&');
    }

    public static int getRandomInt(int s, int e) {
        if (s < e) {
            List<Integer> list = new ArrayList<>();
            for (int i = s; i < e; i++) {
                list.add(i);
            }
            Random random = new Random();
            return list.get(random.nextInt(list.size()));
        }
        return 0;
    }

    public static double getRandomDouble(int s, int e, int scale) {
        if (s < e) {
            List<Integer> list = new ArrayList<>();
            for (int i = s; i < e; i++) {
                list.add(i);
            }
            Random random = new Random();
            Integer var1 = list.get(random.nextInt(list.size()));
            return getDoubleScale(var1 + random.nextDouble(), scale);
        }
        return 0.0;
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param localDateTime
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        Date date = Date.from(zdt.toInstant());
        return date;
    }
}
