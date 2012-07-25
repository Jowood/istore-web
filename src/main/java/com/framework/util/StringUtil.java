package com.framework.util;

import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class StringUtil {
    
    public final static String[] nationArray = {
            "汉族", "蒙古族", "回族", "藏族", "维族",
            "苗族", "彝族", "壮族", "布依族", "朝鲜族",
            "满族", "侗族", "瑶族", "白族", "土家族",
            "哈尼族", "哈萨克族", "傣族", "黎族", "傈傈族",
            "佤族", "畲族", "高山族", "拉祜族", "水族",
            "东乡族", "纳西族", "景颇族", "柯尔克孜族", "土族",
            "达斡尔族", "仫佬族", "羌族", "布朗族", "撒拉族",
            "毛难族", "仡佬族", "锡伯族", "阿昌族", "普米族",
            "塔吉克族", "怒族", "乌孜别克族", "俄罗斯族", "鄂温克族",
            "崩龙族", "保安族", "裕固族", "京族", "塔塔尔族",
            "独龙族", "鄂伦春族", "赫哲族", "门巴族", "珞巴族",
            "基诺族", "外国血统中国籍", "其他"};

    public static String[] provinceArray = {
            "北京", "天津", "河北", "山西", "内蒙古",
            "辽宁", "吉林", "黑龙江", "上海",
            " 江苏", "浙江", "安徽", "福建",
            "江西", "山东", "河南", "湖北", "湖南",
            "广东", "广西", "海南", "重庆", "四川",
            "贵州", "云南", "西藏", "陕西", "甘肃",
            "青海", "宁夏", "新疆",
            "香港", "澳门", "台湾", "其它"};

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    private final static char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String resultString) { 
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = CodeUtil.byteToHex((md.digest(resultString
                    .getBytes())));
        } catch (Exception ex) {

        }
        return resultString;
    }

    /**
     * 获得0-9的随机数
     *
     * @param length
     * @return String
     */
    public static String getRandomNumber(int length) {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < length; i++) {
            buffer.append(random.nextInt(10));
        }
        return buffer.toString();
    }

    /**
     * 获得0-9的随机数 长度默认为10
     *
     * @return String
     */
    public static String getRandomNumber() {
        return getRandomNumber(10);
    }

    /**
     * 获得0-9,a-z,A-Z范围的随机数
     *
     * @param length 随机数长度
     * @return String
     */

    public static String getRandomChar(int length) {

        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(36)]);
        }
        return buffer.toString();
    }

    /**
     * 获得10位随机字符
     *
     * @return String
     */
    public static String getRandomChar() {
        return getRandomChar(10);
    }

    /**
     * 获得主键
     *
     * @return String
     */
    public static String getPrimaryKey() {
        Date now = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateformat.format(now) + getRandomChar(16);
    }

    /**
     * 获得主键 按索引
     *
     * @return String
     */
    public static String getPrimaryKey(int index) {
        Date now = new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
        String indexAppendZero = NumberUtil.appendZero(index, 6);
        return dateformat.format(now) + indexAppendZero
                + getRandomChar(16 - indexAppendZero.length());
    }

    /**
     * 判断字符是否为空
     *
     * @param input 某字符串
     * @return 包含则返回true，否则返回false
     */
    public static boolean isEmpty(String input) {
        return input == null || input.length() == 0;
    }

    /**
     * 判断字符是否为空
     *
     * @param obj 某字符串
     * @return 包含则返回true，否则返回false
     */
    public static boolean isEmpty(Object obj) {
        return obj == null || obj.toString().length() == 0;
    }

    /**
     * 判断字符是否为空
     *
     * @param lst 某字符串
     * @return 包含则返回true，否则返回false
     */
    public static boolean isEmpty(List lst) {
        return lst == null || lst.size() == 0;
    }

    /**
     * 将对象转换成字符串输出
     *
     * @param obj 某字对象
     * @return
     */
    public static String objToStr(Object obj) {
        if (isEmpty(obj)) {
            return "";
        } else {
            return obj.toString().trim();
        }
    }

    /**
     * 将对象转换成字符串输出(如果为空则用指定值替换)
     *
     * @param obj      某对象
     * @param paddingV 替换字符串
     * @return
     */
    public static String objToStr(Object obj, String paddingV) {
        if (isEmpty(obj)) {
            return paddingV;
        } else {
            return obj.toString().trim();
        }
    }


    private static final String PATTERN_LINE_START = "^";

    private static final String PATTERN_LINE_END = "$";

    private static final char[] META_CHARACTERS = {'$', '^', '[', ']', '(', ')',
            '{', '}', '|', '+', '.', '\\'};

    /**
     * 正则表达式匹配
     * The function is based on regex.
     *
     * @param pattern
     * @param str
     * @return
     */
    public static boolean regexMatch(String pattern, String str) {
        String result = PATTERN_LINE_START;
        char[] chars = pattern.toCharArray();
        for (char ch : chars) {
            if (Arrays.binarySearch(META_CHARACTERS, ch) >= 0) {
                result += "\\" + ch;
                continue;
            }
            switch (ch) {
                case '*':
                    result += ".*";
                    break;
                case '?':
                    result += ".{0,1}";
                    break;
                default:
                    result += ch;
            }
        }
        result += PATTERN_LINE_END;
        return Pattern.matches(result, str);
    }

    public static String convertBLOBtoString(java.sql.Blob BlobContent) {
        byte[] base64;
        String newStr = ""; //返回字符串

        try {
            base64 = org.apache.commons.io.IOUtils.toByteArray(BlobContent.getBinaryStream());
            newStr = new BASE64Encoder().encodeBuffer(base64);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newStr;
    }


    //得到真实IP地址

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String toString(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }
}
