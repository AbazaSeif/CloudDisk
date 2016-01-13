package com.zhangling;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils {
	
	/**
	 * 等待时间为毫秒
	 * @param time
	 */
	public static void waitFor(int time) {
		try {
			Thread.currentThread().sleep(time);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	public static boolean isExists(WebDriver driver, By by) {
		try {
			ExpectedCondition<WebElement> presence = ExpectedConditions.presenceOfElementLocated(by); 
			new WebDriverWait(driver,1).until(presence);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static WebElement waitElementShow(WebDriver driver, final By by, int time) {
		WebElement e = (new WebDriverWait(driver, time/1000)).until(ExpectedConditions.presenceOfElementLocated(by));
		return e;
	}
	
	/**
	 * 等待直到元素不可见
	 */
	public static void waitUntilElementInvisiable(WebDriver driver,By path){
		ExpectedCondition<Boolean> presence = ExpectedConditions.invisibilityOfElementLocated(path); 
		new WebDriverWait(driver,20).until(presence);
	}
	
	/**
	 *等待直到元素可见 
	 */
	public static void waitUntilElementVisiable(WebDriver driver,By path){
		ExpectedCondition<WebElement> presence = ExpectedConditions.visibilityOfElementLocated(path); 
		new WebDriverWait(driver,20).until(presence);
	}
	
	public static String getToday(){
		Date date = Calendar.getInstance().getTime();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String str = format.format(date);
		return str;
	}
	
	public static void takeScreenShot(WebDriver driver)
	{
		String path = "D:/screenshot/"+System.currentTimeMillis()+".jpg";
		File a = new File(path);
		
		File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(file, a);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取到粘贴板上的内容
	 * @return
	 */
	public static String getClip(){
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		DataFlavor flavor = DataFlavor.stringFlavor;
		if (clipboard.isDataFlavorAvailable(flavor)) {
			try {
				return clipboard.getData(flavor).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
