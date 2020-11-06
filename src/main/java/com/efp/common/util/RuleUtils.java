package com.efp.common.util;

public class RuleUtils {

	public static String convertRule(String rule) {
		if (rule == null) {
			return "";
		}
		int first = rule.indexOf("/");
		first = Math.max(first + 1, 0);
		int lastIndexOf = rule.lastIndexOf("/");
		lastIndexOf = Math.min(lastIndexOf, rule.length() -1);
		return rule.substring(first, lastIndexOf);
	}

	/**
	 * 判断字符串是否ip
	 * @param ipStr ip字符串
	 * @return boolean
	 */
	public static boolean isIp(String ipStr) {
		return ipStr.matches("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");
	}

	public static boolean isPort(String port) {
		return port.matches("");
	}
}
