package com.tairan.cloud.service.selenium;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class JsDemo {

	public static void main(String[] args) throws AWTException, IOException {
		System.getProperties().setProperty("webdriver.ie.driver", "C:\\Users\\hzmpj\\Desktop\\IEDriverServer.exe");
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		ieCapabilities.setJavascriptEnabled(true);
		ieCapabilities.setCapability("takesScreenshot", true);
		WebDriver webDriver = new InternetExplorerDriver(ieCapabilities);
		try {
			webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			webDriver.get("https://12329.lygzfgjj.com.cn/BSS_GR/Main/Main");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * js test
		 */
		// JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		// jse.executeScript("window.document.getElementById('LoginPassword').focus();");
		// WebElement idele = (WebElement)
		// jse.executeScript("window.document.getElementById('LoginPassword').focus();");
		JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		String data = "{ client_id: 'PCWeb04', usercode: '320705198505133513', password: '123', timestamp: timestamp, uniquecode: '' }";
		String ss=(String) jse.executeScript("alert('aa')");
		System.out.println(ss);
	}

}
