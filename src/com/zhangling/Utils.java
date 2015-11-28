package com.zhangling;

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
			driver.findElement(by);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static WebElement waitElementShow(WebDriver driver, final By by, int time) {
		WebElement e = (new WebDriverWait(driver, time)).until(new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				return d.findElement(by);
			}
		});
		return e;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
