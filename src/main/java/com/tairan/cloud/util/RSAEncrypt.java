package com.tairan.cloud.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RSA 加密解密
 * 
 * @author hzzlj
 *
 */
public class RSAEncrypt {

	public static final Logger LOGGER = LoggerFactory.getLogger(RSAEncrypt.class);

	/**
	 * 字节数据转字符串专用集合
	 */
	/**
	 * 随机生成密钥对
	 */
	public static void genKeyPair(String path) {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 初始化密钥对生成器，密钥大小为96-1024位
		keyPairGen.initialize(1024, new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 得到公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		try {
			// 得到公钥字符串
			String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
			// 得到私钥字符串
			String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
			// 将密钥对写入到文件
			FileWriter pubfw = new FileWriter(path + "/publicKey.keystore");
			FileWriter prifw = new FileWriter(path + "/privateKey.keystore");
			BufferedWriter pubbw = new BufferedWriter(pubfw);
			BufferedWriter pribw = new BufferedWriter(prifw);
			pubbw.write(publicKeyString);
			pribw.write(privateKeyString);
			pubbw.flush();
			pubbw.close();
			pubfw.close();
			pribw.flush();
			pribw.close();
			prifw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static String loadPublicKeyByFile(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "/publicKey.keystore"));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) {
		try {
			byte[] buffer = Base64.decodeBase64(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public static String loadPrivateKeyByFile(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + "/privateKey.keystore"));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从字符串中加载私钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) {
		try {
			byte[] buffer = Base64.decodeBase64(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 私钥加密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) {
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);

			Provider DEFAULT_PROVIDER = new BouncyCastleProvider();
			Cipher ci = Cipher.getInstance("RSA", DEFAULT_PROVIDER);

			byte[] output = ci.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 私钥解密过程
	 * 
	 * @param privateKey
	 *            私钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) {
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 公钥解密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param cipherData
	 *            密文数据
	 * @return 明文
	 * @throws Exception
	 *             解密过程中的异常信息
	 */
	public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) {
		Cipher cipher = null;
		try {
			// 使用默认RSA
			cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data
	 *            输入数据
	 * @return 十六进制内容
	 */

	public static String bytesToHexString(byte[] b) {
		String a = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			a = a + hex;
		}
		return a;
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的私钥对象。
	 * 
	 * @param modulus
	 *            系数。
	 * @param privateExponent
	 *            专用指数。
	 * @return RSA专用私钥对象。
	 */
	public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
		if (StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPrivateExponent)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(
						"hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
			}
			return null;
		}
		byte[] modulus = null;
		byte[] privateExponent = null;
		try {
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
		} catch (DecoderException ex) {
			LOGGER.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
		}
		if (modulus != null && privateExponent != null) {
			return generateRSAPrivateKey(modulus, privateExponent);
		}
		return null;
	}

	/**
	 * 根据给定的系数和专用指数构造一个RSA专用的私钥对象。
	 * 
	 * @param modulus
	 *            系数。
	 * @param privateExponent
	 *            专用指数。
	 * @return RSA专用私钥对象。
	 */
	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
				new BigInteger(privateExponent));
		try {
			KeyFactory keyFactory = null;
			try {
				keyFactory = KeyFactory.getInstance("RSA");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
			return key;
		} catch (InvalidKeySpecException ex) {
			LOGGER.error("RSAPrivateKeySpec is unavailable.", ex);
		} catch (NullPointerException ex) {
			LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
		}
		return null;
	}

	/**
	 * 根据给定的16进制系数和专用指数字符串构造一个RSA专用的公钥对象。
	 * 
	 * @param modulus
	 *            系数。
	 * @param publicExponent
	 *            专用指数。
	 * @return RSA专用公钥对象。
	 */
	public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent) {
		if (StringUtils.isBlank(hexModulus) || StringUtils.isBlank(hexPublicExponent)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
			}
			return null;
		}
		byte[] modulus = null;
		byte[] publicExponent = null;
		try {
			modulus = Hex.decodeHex(hexModulus.toCharArray());
			publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
		} catch (DecoderException ex) {
			LOGGER.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
		}
		if (modulus != null && publicExponent != null) {
			return generateRSAPublicKey(modulus, publicExponent);
		}
		return null;
	}

	/**
	 * 根据给定的系数和专用指数构造一个RSA专用的公钥对象。
	 * 
	 * @param modulus
	 *            系数。
	 * @param publicExponent
	 *            专用指数。
	 * @return RSA专用公钥对象。
	 */
	public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
		} catch (InvalidKeySpecException ex) {
			LOGGER.error("RSAPublicKeySpec is unavailable.", ex);
		} catch (NullPointerException ex) {
			LOGGER.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
		}
		return null;
	}

	/**
	 * 使用指定的公钥加密数据。
	 * 
	 * @param publicKey
	 *            给定的公钥。
	 * @param data
	 *            要加密的数据。
	 * @return 加密后的数据。
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) throws Exception {
		Provider DEFAULT_PROVIDER = new BouncyCastleProvider();
		Cipher ci = Cipher.getInstance("RSA", DEFAULT_PROVIDER);
		ci.init(Cipher.ENCRYPT_MODE, publicKey);
		return ci.doFinal(data);
	}

	/**
	 * 公钥加密过程
	 * 
	 * @param publicKey
	 *            公钥
	 * @param plainTextData
	 *            明文数据
	 * @return
	 * @throws Exception
	 *             加密过程中的异常信息
	 */
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) {
		Cipher cipher = null;
		try {
			// 使用默认RSA
			// cipher = Cipher.getInstance("RSA");
			cipher = Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 使用给定的公钥加密给定的字符串。
	 * <p />
	 * 若 {@code publicKey} 为 {@code null}，或者 {@code plaintext} 为 {@code null}
	 * 则返回 {@code
	 * null}。
	 * 
	 * @param publicKey
	 *            给定的公钥。
	 * @param plaintext
	 *            字符串。
	 * @return 给定字符串的密文。
	 */
	public static String encryptString(PublicKey publicKey, String plaintext) {
		if (publicKey == null || plaintext == null) {
			return null;
		}
		byte[] data = plaintext.getBytes();
		try {
			byte[] en_data = encrypt(publicKey, data);
			return new String(Hex.encodeHex(en_data));
		} catch (Exception ex) {
			LOGGER.error(ex.getCause().getMessage());
		}
		return null;
	}

	/** 算法名称 */
	private static final String ALGORITHOM = "RSA";
	/** 保存生成的密钥对的文件名称。 */
	private static final String RSA_PAIR_FILENAME = "/__RSA_PAIR.txt";
	/** 密钥大小 */
	private static final int KEY_SIZE = 1024;
	/** 默认的安全服务提供者 */
	private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

	private static KeyPairGenerator keyPairGen = null;
	private static KeyFactory keyFactory = null;
	/** 缓存的密钥对。 */
	private static KeyPair oneKeyPair = null;

	private static File rsaPairFile = null;

	static {
		try {
			keyPairGen = KeyPairGenerator.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
			keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
		} catch (NoSuchAlgorithmException ex) {
			LOGGER.error(ex.getMessage());
		}
		// rsaPairFile = new File(getRSAPairFilePath());
	}

	/**
	 * 返回已初始化的默认的公钥。
	 * 
	 * @throws IOException
	 */

	// public static RSAPublicKey getDefaultPublicKey() {
	//
	// KeyPair keyPair = getKeyPair();
	//
	// if (keyPair != null) {
	//
	// return (RSAPublicKey) keyPair.getPublic();
	//
	// }
	//
	// return null;
	//
	// }
	//
	// /**
	// * 135 返回RSA密钥对。 136
	// */
	//
	// public static KeyPair getKeyPair() {
	//
	// // 首先判断是否需要重新生成新的密钥对文件
	//
	// if (isCreateKeyPairFile()) {
	//
	// // 直接强制生成密钥对文件，并存入缓存。
	//
	// return generateKeyPair();
	//
	// }
	//
	// if (oneKeyPair != null) {
	//
	// return oneKeyPair;
	//
	// }
	//
	// return readKeyPair();
	//
	// }

	public static void main(String[] args) throws IOException {
		/**
		 * 生成密钥对
		 */
		// RSAEncrypt.genKeyPair("D:");
		/**
		 * 公钥加密
		 */
		String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdm//J+YtPaTn3c/WkqomF907MHbLi28xb4xHgWrqC10NoOI/xgeejBEJ38ms2APzalRGdgST3V8ThXyHPM44dof//DhPJ8oj5dLVssqxn2bxw70J7hZW4I7rIGiLtEFDW4xjKet4q2HSgE2lpg4HP37clCGRbRqvSQGHSn2kFMQIDAQAB";
		// String key =
		// "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJwVoMigOTKBH60fnu3FUoz5uaQf8HUx0+4fxkfzFXH09TRFTYpX77WecZ7OJRr2KosMJOGcrPiJQcwEW6beJbFHgCOO9KxP8Xbl7N9FtbBcgHhorXs1uzVfgoNtpkcCG4CkXkLdvVgiTme3XGZXew37cwKvs0hbFV237sbyxvfPAgMBAAECgYBuMgLSwfO/yVPQyTtOSPpwFzrOOdJtGyGxw3Mchu0ECFo5bhhBbLYK9nLnGEkwEM2WX5uGQTQJP/ZZEFaYp8vZko2MX7pfA81b3mP/MeU4qJIZkeGrCClYqUWuGxdHE/x1CvqWpcj5I4z3GL3dtZJYqiI29doUNVio7bb1D+V6iQJBAN2VqESAwMQslk7Z4AdzaeuZEkPonYk0tELzRPXb81T+3KICbzptYIRQOpgUAvT9I7o1Z0qTavxQa/qK8cgtpzUCQQC0U6Umo9d18gQmtq2RU7TF4myW6oommbLl+0P5cf/TAOSohg46mA+VJpnu3tRsmZ9QqryWFw/Fr9LnMP4Q7c9zAkAXnQM5iZ8BSN8buAwvvxTijoVrxzZCkONE3zfSG4Zq4F4Sxb+kslM+xm34FunZwcA078v6HougJ9HZ+USa2IKhAkBwr5ySquwnvlctwhZwrHhY+IeXP3WZbY7H/N6bcMvHFdXOIgeeQgvqzrZ++SyRuOY/yJIU0NCAXsFSy+DYv2NVAkBzbBd5dUFiH67FgKuwLtGx4XNsIhyPbnG3gTQfFxu5U4a23LkP7ryqN22+bFzoA4znWlkmlCtcNQO0sMcAxBoa";
		RSAPublicKey publicKey = RSAEncrypt.loadPublicKeyByStr(pubKey);

		String password = RSAEncrypt.bytesToHexString(
				RSAEncrypt.encrypt(publicKey, FileUtils.getContent("D:\\Program Files\\Redis\\dump.rdb")));
		System.out.println(password);
		/**
		 * 私钥解密
		 */
		// RSAPrivateKey a = RSAEncrypt.loadPrivateKeyByStr(key);
		// byte[] data = RSAEncrypt.decrypt(a,
		// RSAEncrypt.hexStringToBytes(password));
		// System.out.println(new String(data));

		/**
		 * 测试1
		 */
		// 00d492084c3cf4a893d23cb3864602aca8c00f18e948530553d07d49f996172ec809e38faa4a3deb36084f4378759bf7bd3c5486ab1b3a24e700fb282bc0db35e9
		// RSAPublicKey publicKey = getRSAPublidKey(
		// "00d492084c3cf4a893d23cb3864602aca8c00f18e948530553d07d49f996172ec809e38faa4a3deb36084f4378759bf7bd3c5486ab1b3a24e700fb282bc0db35e9",
		// "010001");
		//// String password =
		// RSAEncrypt.bytesToHexString(RSAEncrypt.encrypt(publicKey,
		// "830408wl".getBytes()));
		//// System.out.println(password);
		// String password = encryptString(publicKey, "830408wl");
		// System.out.println(password);
		/**
		 * 测试2
		 */

	}
}
