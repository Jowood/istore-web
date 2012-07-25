package com.framework.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * 数字操作功能类. <p/> 最后更新：2003-09-01
 * 
 * @author 姜敏
 */
public class NumberUtil {
	/**
	 * 检查字符串中是否是Float型数字
	 * 
	 * @param input
	 *            输入字符串
	 * @return 如果包含非Float型字符则返回false
	 */
	public static boolean isFloat(String input) {
		if (StringUtil.isEmpty(input)) {
			return false;
		}
		try {
			Float.parseFloat(input);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 检查字符串中是否是Double型数字
	 * 
	 * @param input
	 *            输入字符串
	 * @return 如果包含非Double型字符则返回false
	 */
	public static boolean isDouble(String input) {
		if (StringUtil.isEmpty(input)) {
			return false;
		}

		try {
			Double.parseDouble(input);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 检查字串中是否全部是数字字符
	 * 
	 * @param input
	 *            输入字符串
	 * @return 如果包含非数字字符则返回false
	 */
	public static boolean isDigital(String input) {
		if (StringUtil.isEmpty(input)) {
			return false;
		}
		for (int i = 0; i < input.length(); i++) {
			if (!Character.isDigit(input.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否是int型
	 * 
	 * @param input
	 *            String
	 * @return boolean
	 */
	public static boolean isInt(String input) {
		if (StringUtil.isEmpty(input)) {
			return false;
		}

		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * 在数字前面补零. <p/> 在数值前面补零，整个字符串达到固定长度，主要用于银行帐号，单据编号等
	 * 
	 * @param num
	 *            转换的数值
	 * @param length
	 *            使整个串达到的长度
	 * @return 数值前面补零的固定长度的字符串
	 */
	public static String appendZero(int num, int length) {

		StringBuilder pattern = new StringBuilder();
		for (int i = 0; i < length; i++) {
			pattern.append("0");
		}
		DecimalFormat df = new DecimalFormat(pattern.toString());
		return df.format(num);
	}

	/**
	 * 格式化数值
	 * 
	 * @param num
	 *            待格式化实型数值
	 * @param pattern
	 *            格式样式
	 * @return 符合格式的字符串
	 */
	public static String numberFormat(Number num, String pattern) {
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(num);
	}

	/**
	 * 格式化为百分数
	 * 
	 * @param num
	 * @param digit
	 * @return String
	 */
	public static String percentFormat(double num, int digit) {
		NumberFormat format = NumberFormat.getPercentInstance();
		format.setMaximumFractionDigits(digit);
		return format.format(num);
	}

	/**
	 * 判断奇数
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isOdd(int input) {
		return input % 2 != 0;
	}

	/**
	 * 判断偶数
	 * 
	 * @param input
	 * @return isEven
	 */
	public static boolean isEven(int input) {
		return input % 2 == 0;
	}

    public static void main(String[] args){
        System.out.println(NumberUtil.isDigital(""));
    }
}
