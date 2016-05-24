package com.demo.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class PassUtil {

	public static String buyerPassEncoder(String password, Integer version) {
		String md5 = null;
		if (version >= 2) {
			md5 = password;
		} else {
			String passBase64 = Base64.encodeBase64String(password.getBytes());
			int len = passBase64.length();
			char[] res = new char[len];
			for (int i = 0; i < len; i++) {
				char chr = passBase64.charAt(i);
				char add = (char) (i % 3);
				res[i] = (char) (chr + add);
			}
			md5 = DigestUtils.md5Hex(String.valueOf(res) + "koudai_gouwu_is_success_for_ever");
		}

		String md5_head = md5.substring(0, 24);
		String md5_new = DigestUtils.md5Hex(md5 + "<UDC><CommonSaltForPass>2015-03-02</CommonSaltForPass></UDC>");
		String md5_new_tail = md5_new.substring(md5_new.length() - 8);
		return md5_head.concat(md5_new_tail);
	}

	public static String sellerPassEncoder(String password, Integer version) {
		String md5 = null;
		if (version >= 2) {
			md5 = password.toLowerCase();
		} else {
			md5 = DigestUtils.md5Hex("_Wedian&&Is##Wonderful**@~0_".concat(password));
		}
		String md5_head = md5.substring(0, 24);
		String md5_new = DigestUtils.md5Hex(md5.concat("<UDC><CommonSaltForPass>2015-03-02</CommonSaltForPass></UDC>"));
		String md5_new_tail = md5_new.substring(md5_new.length() - 8);
		return md5_head.concat(md5_new_tail);
	}
}