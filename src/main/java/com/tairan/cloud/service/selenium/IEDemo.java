package com.tairan.cloud.service.selenium;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;

public class IEDemo {

	public static void keyPressString(Robot r, String str) {
		r.setAutoDelay(100);
		int key = KeyEvent.VK_1;
		r.keyPress(key);
		r.keyRelease(key);
	}

	public static void keyPress(Robot r) {
		int key = KeyEvent.VK_TAB;
		r.keyPress(key);
		r.keyRelease(key);
	}

	public static void keyPressString(String str) {
		try {
			Robot r;
			r = new Robot();
			for (char c : str.toCharArray()) {
				int key = KeyEvent.getExtendedKeyCodeForChar(c);
				r.keyPress(key);
				r.keyRelease(key);
			}
			r.delay(800);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static byte[] takeScreenshot(WebDriver driver) throws IOException {
		WebDriver augmentedDriver = new Augmenter().augment(driver);
		return ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.BYTES);
		// TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
		// return takesScreenshot.getScreenshotAs(OutputType.BYTES);
	}

	public static BufferedImage createElementImage(WebDriver driver, WebElement webElement) throws IOException {
		// 获得webElement的位置和大小。
		Point location = webElement.getLocation();
		Dimension size = webElement.getSize();
		// 创建全屏截图。
		BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(takeScreenshot(driver)));
		// 截取webElement所在位置的子图。
		System.out.println(location.getX() + ":" + location.getY() + ":" + size.getWidth() + ":" + size.getHeight());
		// 268:309:115:27
		BufferedImage croppedImage = originalImage.getSubimage(location.getX() + 15, location.getY() + 2,
				size.getWidth() - 15, size.getHeight() - 2);
		return croppedImage;
	}

	public static WinDef.HWND findHandle(String browserClassName, String alieditClassName) {
		WinDef.HWND browser = Win32Util.findHandleByClassName(browserClassName, 10, TimeUnit.SECONDS);
		return Win32Util.findHandleByClassName(browser, alieditClassName, 10, TimeUnit.SECONDS);
	}

	public static void main(String[] args) throws AWTException, IOException {
//		System.getProperties().setProperty("webdriver.ie.driver", "C:\\Users\\hzmpj\\Desktop\\IEDriverServer.exe");
//		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
//		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
//		ieCapabilities.setJavascriptEnabled(true);
//		ieCapabilities.setCapability("takesScreenshot", true);
//		// ieCapabilities.setCapability("webdriver.ie.driver", "C:\\Program
//		// Files\\Internet Explorer\\iexplore.exe");
//		WebDriver webDriver = new InternetExplorerDriver(ieCapabilities);
//		try {
//			// webDriver.manage().timeouts().implicitlyWait(1,
//			// TimeUnit.SECONDS);
//			webDriver.get("https://whgjj.hkbchina.com/portal/pc/login.html");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		// webDriver.findElement(By.id("LoginId")).sendKeys("13437199819");
		// String tt =
		// webDriver.findElement(By.id("LoginPassword")).getAttribute("outerHTML");
		// System.out.println(tt);
		// System.out.println("..................");
		// keyPressString("zhonghaijun");
		// webDriver.findElement(By.id("LoginPassword")).sendKeys("zhonghaijun");
		// webDriver.findElement(By.cssSelector("input#_vTokenName")).sendKeys("aaaa");

		// Actions action = new Actions(webDriver);
		// // 对该元素进行右击操作
		// action.click(webDriver.findElement(By.id("LoginId"))).perform();
		// Robot r = new Robot();
		// keyPress(r);
		// keyPressString(r,"1111");

		// 按ESC键返回，设置焦点成功
		// keyPressString("1111");

		// String html =
		// webDriver.findElement(By.xpath("/html")).getAttribute("outerHTML");
		// System.out.println(html);
		// webDriver.close();

		/**
		 * js test
		 */
		// WebElement idele = webDriver.findElement(By.id("LoginId"));

		// JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		// jse.executeScript("window.document.getElementById('LoginPassword').focus();");
		// keyPressString("aa");
		// WebElement idele = (WebElement)
		// jse.executeScript("window.document.getElementById('LoginPassword').focus();");
		// jse.executeScript("window.document.getElementById('LoginId').setAttribute('value','13437199819');");
		// jse.executeScript("window.document.getElementById('LoginPassword').setAttribute('value','zhonghaijun');");
		// jse.executeScript("window.document.getElementById('_vTokenName').focus();");
		// String name = "D:\\test\\code\\"+ System.currentTimeMillis() +
		// ".png";
		// BufferedImage inputbig = createElementImage(webDriver,
		// webDriver.findElement(By.id("_tokenImg")));
		// ImageIO.write(inputbig, "png", new File(name));
		// Scanner sc = new Scanner(System.in);
		// String code = sc.nextLine();
		// webDriver.findElement(By.cssSelector("input#_vTokenName")).sendKeys(code);
		// webDriver.findElement(By.id("LoginPassword")).sendKeys("zhonghaijun");
		// System.out.println(idele.getAttribute("outerHTML"));
		/**
		 * 输入
		 */
		// JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		// jse.executeScript("window.document.getElementById('LoginPassword').focus();");
		// keyPressString("aa");
		/**
		 * jna test
		 */
		String password = "your password";
		HWND alipayEdit = findHandle("Chrome_RenderWidgetHostHWND", "Edit");
		//assertThat("获取支付宝密码控件失败。", alipayEdit, notNullValue());
		boolean isSuccess = Win32Util.simulateCharInput(alipayEdit, password);
		//assertThat("输入支付宝密码["+ password +"]失败。", isSuccess, is(true));
		
	//	String html = webDriver.findElement(By.xpath("/html")).getAttribute("outerHTML");
		//System.out.println(html);
	}

}
