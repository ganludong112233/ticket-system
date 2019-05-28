package com.workorder.ticket.common.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;

/**
 * 加密工具
 * 
 * @author wzdong
 *
 */
@SuppressWarnings("restriction")
public class TokenEncryptUtil {
	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(TokenEncryptUtil.class);

	public static String secritKey = "HSH";
	// SecretKey 负责保存对称密钥
	private static SecretKeySpec keySpec;
	// Cipher负责完成加密或解密工作
	private static Cipher c;

	static {
		try {
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			// 实例化支持DES算法的密钥生成器(算法名称命名需按规定，否则抛出异常)
			byte[] raw = getRawKey(secritKey);
			keySpec = new SecretKeySpec(raw, "DES");
			// 生成Cipher对象,指定其支持的DES算法
			c = Cipher.getInstance("DES/ECB/NoPadding");
		} catch (Exception e) {

			logger.error("TokenEncryptUtil init fail! ERROR:{}", e.getMessage());
		}
	}

	private static byte[] getRawKey(String seed)
			throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG"); // 获得一个随机数，传入的参数为默认方式。
		sr.setSeed(seed.getBytes()); // 设置一个种子，这个种子一般是用户设定的密码。也可以是其它某个固定的字符串
		KeyGenerator keyGen = KeyGenerator.getInstance("DES"); // 获得一个key生成器（AES加密模式）
		keyGen.init(56, sr); // 设置密匙长度128位
		SecretKey key = keyGen.generateKey(); // 获得密匙
		byte[] raw = key.getEncoded(); // 返回密匙的byte数组供加解密使用
		return raw;
	}

	/**
	 * 对字符串加密
	 */
	public static String encrytor(String str) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
		c.init(Cipher.ENCRYPT_MODE, keySpec);
		byte[] src = str.getBytes();
		// 加密，结果保存进cipherByte
		byte[] cipherByte = c.doFinal(src);
		return BytesHexStrTranslate.bytesToHexFun3(cipherByte);
	}

	/**
	 * 对字符串解密
	 */
	public static String decryptor(String encStr) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		// 根据密钥，对Cipher对象进行初始化，DECRYPT_MODE表示加密模式
		c.init(Cipher.DECRYPT_MODE, keySpec);
		byte[] buff = BytesHexStrTranslate.toBytes(encStr);
		byte[] cipherByte = c.doFinal(buff);
		return new String(cipherByte);
	}

	public static void main(String[] args) throws Exception {
		StringBuilder tokenBuilder = new StringBuilder();
		tokenBuilder
				.append("2671")
				.append("_")
				.append("SCSXYHSH")
				.append("_")
				.append(DateUtils.format(new Date(),
						DateUtils.DATE_FORMAT_YYYYMMDDHHMMSS));

		String encontent = TokenEncryptUtil.encrytor(tokenBuilder.toString());
		String decontent = TokenEncryptUtil.decryptor(encontent);
		System.out.println("明文是:" + tokenBuilder.toString());
		System.out.println("加密后:" + encontent);
		System.out.println("解密后:" + new String(decontent));
		System.out.println("-------------");

		String oldEncContent = "1ea67b1a95ca406e4837a3026e23dacbefd9bd746bde073fe9243b8aacebbeed";
		String oldDecontent = TokenEncryptUtil.decryptor(oldEncContent);
		System.out.println("加密文本:" + oldEncContent);
		System.out.println("解密后:" + new String(oldDecontent));
	}

}
