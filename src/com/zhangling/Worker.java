package com.zhangling;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Worker {

	WebDriver driver;

	public Worker(String url, String username, String password) {
		driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.manage().window().maximize();
		driver.navigate().to(url);
	}

	public void loginRight(String username, String password) {
		driver.findElement(By.className("user")).sendKeys(username);
		driver.findElement(By.className("locked")).sendKeys(password);
		WebElement checkbox = driver.findElement(By.className("remb_name"));// 判断记住我是否勾选
		if (!checkbox.isSelected()) {
			checkbox.click();
		}
		driver.findElement(By.className("btn_login")).click();
		Utils.waitElementShow(driver, By.xpath("//span[text()='张小胖']"), 10);
		boolean exists = Utils.isExists(driver, By.xpath("//span[text()='张小胖']"));
		if (!exists) {
			Assert.fail("登陆不成功");
		}
		System.out.println("登陆文档云+成功");

	}

	public void newFolder() {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		driver.findElement(By.id("new_build")).click();
		WebElement element = driver.findElement(By.name("folderName"));
		String folderName = "folder" + System.currentTimeMillis();
		element.sendKeys(folderName);
		WebElement confirm = driver.findElement(By.className("confirmNewFolder"));
		confirm.click();
		Boolean success = Utils.isExists(driver, By.xpath("//img[contains(@src,'floder_defult.png')]/parent::li[@data-name='" + folderName + "']"));
		if (!success) {
			Assert.fail("新建文件夹失败");
		}
		System.out.println("新建文件夹成功");
	}

	/**
	 * 新建同名文件夹，获取到所有文件夹的名称后，获取第一个文件夹名称
	 */
	public void newFolderSame() {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		List<WebElement> lis = driver.findElements(By.xpath("//img[contains(@src,'floder_defult.png')]/parent::li"));
		String existsFolderName = lis.get(0).getAttribute("data-name");

		driver.findElement(By.id("new_build")).click();
		driver.findElement(By.name("folderName")).sendKeys(existsFolderName);
		driver.findElement(By.className("confirmNewFolder")).click();
		WebElement txt = driver.findElement(By.xpath(".//*[@id='page_message_warning']/span"));
		Assert.assertEquals(txt.getText(), "文件夹名称重复，请重新输入");
		Utils.waitElementShow(driver, By.xpath("//button[text()='取消']"), 5);
		driver.findElement(By.xpath("//button[text()='取消']")).click();
		System.out.println("新建重名文件夹用例执行成功");
	}

	/*
	 * // 再次新建同名文件夹 driver.findElement(By.id("new_build")).click();
	 * driver.findElement(By.name("folderName")).sendKeys(folderName);
	 * driver.findElement(By.className("confirmNewFolder")).click(); WebElement
	 * txt =
	 * driver.findElement(By.xpath(".//*[@id='page_message_warning']/span"));
	 * Utils.waitFor(3000); Assert.assertEquals(txt.getText(), "文件夹名称重复，请重新输入");
	 * driver.findElement(By.className("dialog_close")).click();
	 */
	@Test
	public void deleteFolder() {// 删除文件夾
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		List<WebElement> lis = driver.findElements(By.xpath("//img[contains(@src,'floder_defult.png')]/parent::li"));
		String existsFolderName = lis.get(0).getAttribute("data-name");
		driver.findElement(By.xpath("//ul[@data-name='" + existsFolderName + "']/child::li[1]/input")).click();
		driver.findElement(By.id("delete")).click();
		driver.findElement(By.className("btn_primary_large")).click();
		driver.findElement(By.id("delete")).click();
		driver.findElement(By.className("btn_primary_large")).click();
		Utils.waitElementShow(driver, By.xpath("//span[text()='操作完成']"), 10);
		Boolean folderName = Utils.isExists(driver, By.xpath("//a[@data-name='111' and @title='111']"));
		if (!folderName) {
			System.out.println("删除文件夹成功");
		} else {
			System.out.println("删除文件夹失败");
		}
	}

}
