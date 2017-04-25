package com.tairan.cloud.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

public class JsoupUtil {

	// input[name='__EVENTARGUMENT']
	public String value(String html, String select) {
		String value = "";
		try {
			Document doc = Jsoup.parse(html);
			value = doc.select(select).attr("value");
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).info("jsoup错误");
		}
		return value;
	}

	public Map<String, Object> nextText(String html, String tdsel, Map<String, String> strmap) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (Map.Entry<String, String> entry : strmap.entrySet()) {
				String value = entry.getValue();
				String key = entry.getKey();
				Document doc = Jsoup.parse(html);
				Elements tdEles = doc.select(tdsel);
				for (Element tdEle : tdEles) {
					String td = tdEle.text();
					if (td.contains(value)) {
						String content = tdEle.nextElementSibling().text().replaceAll("\\s", "");
						map.put(key, content);
						break;
					}
				}
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).info("jsoup错误");
		}
		return map;
	}

	private Map<String, Object> fundinfoJsoup(String html) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Document doc = Jsoup.parse(html);
			Elements tdEles = doc.select("div.bg_content table");
			for (Element tableEle : tdEles) {
				if (tableEle.text().contains("姓名")) {
					tdEles = tableEle.select("td");
					break;
				}
			}
			for (Element tdEle : tdEles) {
				String td = tdEle.text();
				if (td.contains("姓名")) {
					String fund_realname = tdEle.nextElementSibling().text();
					map.put("fund_realname", fund_realname);
				} else if (td.contains("身份证号")) {
					String fund_idcard = tdEle.nextElementSibling().text();
					map.put("fund_idcard", fund_idcard);
				} else if (td.contains("单位名称")) {
					String fund_company = tdEle.nextElementSibling().text();
					map.put("fund_company", fund_company);
				} else if (td.contains("开户日期")) {
					String fund_opendate = tdEle.nextElementSibling().text();
					map.put("fund_opendate", fund_opendate);
				} else if (td.contains("个人账户")) {
					String fund_code = tdEle.nextElementSibling().text();
					map.put("fund_code", fund_code);
				} else if (td.contains("单位账号")) {
					String fund_companyid = tdEle.nextElementSibling().text();
					map.put("fund_companyid", fund_companyid);
				} else if (td.contains("缴存基数")) {
					String fund_base = tdEle.nextElementSibling().text();
					map.put("fund_base", fund_base);
				} else if (td.contains("缴存比例")) {
					String fund_percentboth = tdEle.nextElementSibling().text();
					map.put("fund_percentboth", fund_percentboth);
				} else if (td.contains("当前住房公积金余额")) {
					String fund_balance = tdEle.nextElementSibling().text();
					map.put("fund_balance", fund_balance);
				} else if (td.contains("账户状态")) {
					String fund_status = tdEle.nextElementSibling().text();
					map.put("fund_status", fund_status);
				}
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(this.getClass()).info("jsoup错误");
		}
		return map;
	}

	private List<Map<String, Object>> fundPayJsoup(String html) {
		List<Map<String, Object>> payList = new ArrayList<Map<String, Object>>();
		Document doc = Jsoup.parse(html);
		Elements tableEles = doc.select("div.gjj_table table");
		if (!tableEles.isEmpty()) {
			Elements trEles = tableEles.first().select("tr");
			for (Element tableEle : tableEles) {
				if (tableEle.text().contains("发送日期")) {
					trEles = tableEle.select("tr");
					break;
				}
			}
			Elements tdTitleEles = null;
			int flag = 0;
			/** 得到表头 */
			for (int i = 0; i < trEles.size(); i++) {
				flag = i;
				String text = trEles.get(i).text();
				if (text.contains("发送日期")) {
					tdTitleEles = trEles.get(i).select("td");
					flag = i + 1;
					break;
				}
			}
			for (int i = flag; i < trEles.size(); i++) {
				Elements tdEles = trEles.get(i).select("td");
				if (tdEles.size() == tdTitleEles.size()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (int j = 0; j < tdEles.size(); j++) {
						String title = tdTitleEles.get(j).text();
						if (title.contains("发送日期")) {
							String fund_time = tdEles.get(j).text();
							map.put("fund_time", fund_time);
						} else if (title.contains("摘要")) {
							String fund_summary = tdEles.get(j).text();
							map.put("fund_summary", fund_summary);
						} else if (title.contains("支取金额")) {
							String fund_company = tdEles.get(j).text();
							map.put("fund_company", fund_company);
						} else if (title.contains("收入金额")) {
							String fund_amount = tdEles.get(j).text();
							map.put("fund_amount", fund_amount);
						}
					}
					payList.add(map);
				}
			}
		}
		return payList;
	}

}
