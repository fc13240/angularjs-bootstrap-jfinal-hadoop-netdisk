package com.hadoop.gy404.tools;
import java.security.MessageDigest;


public class MD5Util {

	public final static Object MD5String(Object object){
		String s1 = calc(object+"gy404fc");//密码因子，增加破解难度
		String s2 = calc(s1);
		return s2;
	}
	
	
	public final static String calc(String ss) {

		String s = ss == null ? "" : ss;

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String args[])
	{
		System.out.println(MD5String("123456"));
	}
}
