package com.tairan.cloud.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import com.tairan.cloud.common.ErrorCode;
import com.tairan.cloud.common.FundException;

public class Utils {

	synchronized private static boolean isImageFromBase64(String base64Str) {
		boolean flag = false;
		try {
			Base64 base64 = new Base64();
			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(base64.decode(base64Str)));
			if (null == bufImg) {
				return flag;
			}
			flag = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return flag;
	}
	
	public void cookie(String html) {
		String cookieName = getMatcherStr(html, "(?<=cookie\\|)(.*?)(?=\\|path)");
		String cookieValue = getMatcherStr(html, "(?<=path\\|)(.*?)(?=')").replace('|', '/');
		// ant_stream_522475cb1bfd1=1482492814/2784874171;
		// ASP.NET_SessionId=5e3tkp55t4yvxv55qabpzr55;
		// __CSRFCOOKIE=0c227eb7-d769-41f5-a25a-c2680eb29f41;
		// _gscu_689646994=82463365j4xqbh20;
		// _gscs_689646994=82463365wrfirf20|pv:1; _gscbrs_689646994=1;
		// bow_stream_522475cb1bfd1=13
		CookieStore cookieStore = new BasicCookieStore();
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
		BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
		cookie.setDomain(".fushun.gov.cn");// 设置跨域
		cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "true");
		cookieStore.addCookie(cookie);
		localContext.setCookieStore(cookieStore);

		html = (String) HttpUtils.excuteGet(localContext, "url", new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return EntityUtils.toString(response.getEntity());
			}
		});
	}

	/**
	 * js 换行正则 换行的正则 javascript>[\\s\\S]{1,10}alert\\(.*?\\)
	 * 
	 * @param html
	 * @return
	 */
	public Map<String, String> params(String html) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String poolSelect = getMatcherStr(html, "poolSelect = \\{[\\s\\S]*?\\};");
			String[] poolSelectlist = poolSelect.split("\n");
			for (int i = 0; i < poolSelectlist.length; i++) {
				String element = poolSelectlist[i];
				if (element.contains(":")) {
					String[] elementlist = element.split(":");
					if (elementlist.length == 2) {
						String str1 = elementlist[0];
						str1 = getMatcherStr(str1, "\".*?\"|'.*?'").replaceAll("\"|'", "");
						String str2 = elementlist[1];
						str2 = getMatcherStr(str2, "\".*?\"|'.*?'").replaceAll("\"|'", "");
						System.out.println(str1 + "   " + str2);
						map.put(str1, str2);
					}
				}
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).info("正则错误错误");
		}
		return map;
	}

	/**
	 * 将时间戳转化为时间
	 */
	public String stampToDate(String s) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long lt = new Long(s);
		Date date = new Date(lt);
		String res = format.format(date);
		return res;
	}

	public Map<String, String> acquireCode(HttpClient client) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE%20d%20MMM%20yyyy%20HH:mm:ss%20'GMT+0800'%20", Locale.US);
		String ran = sdf.format(new Date()) + "(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)";
		String url = "http://www.hsgjj.com/jcaptcha?onlynum=true&radom=" + ran;
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "image/webp,image/*,*/*;q=0.8");
		byte[] image = HttpUtils.excuteGetWrappedByteArray(client, url, headers);
		/** 存储图片用于测试 */
		try {
			FileUtils.writeByteArrayToFile(new File("D:\\test\\code\\" + System.currentTimeMillis() + ".png"), image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	//	InputStream in = new ByteArrayInputStream(image);
		//String json = aspriseOCRService.aspriseOCR(client, in, "0");
//		if (StringUtils.isNotBlank(json)) {
//			JSONObject obj = JSON.parseObject(json);
//			map.put("code", obj.getString("data"));
//			map.put("imageid", obj.getString("id"));
//		}
//		Base64 base64 = new Base64();
//		map.put("image", base64.encodeAsString(image));
//		return map;
	}

	/**
	 * 随机生成多位随机数
	 */
	// RandomStringUtils.randomNumeric(18)
	/**
	 * unicode转中文工具类
	 * 
	 * @param unicode
	 * @return
	 */
	public String unicodeTozwe(String unicode) {
		/**
		 * StringEscapeUtils还有其他的方法可以尝试
		 */
		// StringEscapeUtils.unescapeXml("&#39564;&#35777;&#30721;&#26377;&#35823;")
		return StringEscapeUtils.unescapeJava(unicode);
	}

	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
			System.out.println("MD5(" + sourceStr + ",32) = " + result);
			System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result;
	}

	public String encode(String str, String bm) {
		try {
			String enstr = URLEncoder.encode(str, bm);
			return enstr;
		} catch (UnsupportedEncodingException e) {
			throw new FundException(ErrorCode.UNKNOWN_ERROR.getCode(),
					ErrorCode.UNKNOWN_ERROR.getMessage() + "[URLEncoder.encode错误]");
		}
	}

	public String decode(String str, String bm) {
		try {
			String enstr = URLDecoder.decode(str, bm);
			return enstr;
		} catch (UnsupportedEncodingException e) {
			throw new FundException(ErrorCode.UNKNOWN_ERROR.getCode(),
					ErrorCode.UNKNOWN_ERROR.getMessage() + "[URLDecoder.decode错误]");
		}
	}

	// alert\\(.*?\\)
	public String getMatcherStr(String msg, String regex) {
		try {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(msg);
			if (m.find()) {
				return m.group();
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).info("正则错误错误");
		}
		return null;
	}

	// alert\\( \\)
	public String getMatcherStr(String msg, String regex1, String regex2) {
		try {
			Pattern p = Pattern.compile(regex1 + ".*?" + regex2);
			Matcher m = p.matcher(msg);
			if (m.find()) {
				return m.group().replaceAll(regex1 + "|" + regex2, "");
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).info("正则错误错误");
		}
		return null;
	}

}
