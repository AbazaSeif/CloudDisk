package com.zhangling;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import autoitx4java.AutoItX;

import com.jacob.com.LibraryLoader;

public class TestNgCase {

	WebDriver driver;
	Worker worker;

	/**
	 * 初始化浏览器,登陆文档云+
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	@BeforeClass
	@Parameters({ "url", "username", "password" })
	public void initBrowser(String url, String username, String password) {
		worker = new Worker(url, username, password);
		worker.loginRight(username, password);
	}

	/**
	 * 新建文件夹
	 */
	@Test
	public void newFolder() {
		worker.newFolder();
	}

	/**
	 * 新建同名文件夹，依赖newFolder方法
	 */
	@Test(dependsOnMethods={"newFolder"})
	public void newFolderSame() {
		worker.newFolderSame();
	}

	@Test(dependsOnMethods={"newFolderSame"})
	public void deleteFolder() {// 删除文件夾

		worker.deleteFolder();
	}

	@Test
	public void upload() {
		this.uploadCommon("1.txt");
	}

	public void uploadCommon(String fileName) {
		File file = new File("lib/jacob-1.18-x64.dll");
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());

		File file1 = new File("D:\\upload\\" + fileName);
		System.out.println(file1);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driver.findElement(By.id("upload")).click();
		driver.findElement(By.xpath(".//*[@id='uploader_browse']/span")).click();
		AutoItX x = new AutoItX();
		String open = "文件上传";
		x.winActivate(open);
		try {
			Thread.sleep(1000);
			x.controlFocus(open, "", "Edit1");
			Thread.sleep(1000);
			x.ControlSetText(open, "", "Edit1", file1.getAbsolutePath());
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
		x.controlClick(open, "", "Button1");

		driver.findElement(By.xpath("//a[@id='uploader_start']/child::span")).click();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			WebElement element = driver.findElement(By.xpath("//li[@data-name='" + fileName + "']"));
		} catch (Exception e) {
			Assert.fail("上传失败");
		}
	}

	@Test
	public void cloudShare() {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		this.uploadCommon("2.txt");
		driver.findElement(By.xpath("//ul[@data-name='2.txt']/li[1]/input")).click();
		WebElement element = driver.findElement(By.id("share"));
		element.click();
		driver.findElement(By.id("cloudShare")).click();
		driver.findElement(By.id("s2id_autogen2")).sendKeys("jenny");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement selector = driver.findElement(By.xpath("//div[@title='张小胖(jenny)']/parent::*/parent::*"));
		selector.click();
		// selector.sendKeys(Keys.ENTER);
		driver.findElement(By.xpath(".//*[@id='modal_fileshare']/div[2]/div/div[2]/form/div[9]/button[1]")).click();
		driver.findElement(By.xpath("//a[@lang='My_Share']/i")).click();
		driver.findElement(By.xpath("//span[text()='已发分享']")).click();
		try {
			driver.findElement(By.xpath("//a[@data-name='2.txt']"));
		} catch (Exception e) {
			Assert.fail("云盘分享失败");
		}

	}

	@Test
	public void linkShare() {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		this.uploadCommon("3.txt");
		driver.findElement(By.xpath("//ul[@data-name='3.txt']/li[1]/input")).click();
		WebElement element = driver.findElement(By.id("share"));
		element.click();
		driver.findElement(By.id("linkShare")).click();
		driver.findElement(By.xpath("//button[text()='创建分享链接']")).click();
		Utils.waitFor(3000);
		driver.findElement(By.xpath("//button[text()='复制链接和提取码']")).click();

		WebElement password = driver.findElement(By.xpath("//label[text()='提取码']/following-sibling::*"));
		String code = password.getText();
		String link = driver.findElement(By.xpath("//label[text()='链接地址']/following-sibling::span")).getAttribute("title");

		driver.findElement(By.xpath("//button[text()='关闭']")).click();
		driver.findElement(By.xpath("//a[@lang='My_Share']/i")).click();
		driver.findElement(By.xpath("//span[text()='已发分享']")).click();
		try {
			driver.findElement(By.xpath("//a[@data-name='3.txt']"));
		} catch (Exception e) {
			Assert.fail("链接分享失败");
		}

		driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(0)).navigate().to(link);
		driver.findElement(By.xpath("//input[@placeholder='请输入提取码']")).sendKeys(code);
		driver.findElement(By.id("team-check-submit")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean exist = Utils.isExists(driver, By.xpath("//a[text()='3.txt' and @code]"));
		if (!exist) {
			Assert.fail("打开分享链接不成功");
		}

	}

	@Test
	public void copyToMyFiles() {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		this.uploadCommon("4.txt");
		driver.findElement(By.xpath("//ul[@data-name='4.txt']/child::*/input")).click();
		driver.findElement(By.id("copy")).click();
		driver.findElement(By.id("copy-fileTree-holder_1_span")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Utils.waitFor(2000);
		boolean alarm = Utils.isExists(driver, By.xpath("//span[text()='操作完成']"));

		if (!alarm) {
			Assert.fail("复制到个人文件失败");
		}
		try {
			driver.findElement(By.xpath("//a[@data-name='4(1).txt']"));
		} catch (Exception e) {
			Assert.fail("同目录下复制失败");
		}
	}

	@Test
	public void createTeam() {
		driver.findElement(By.className("headermenu_ico_team")).click();
		Utils.waitFor(3000);
		driver.findElement(By.id("btn_newTeam")).click();
		driver.findElement(By.name("new_team_name")).sendKeys("myTeam");
		driver.findElement(By.xpath("//i[@class='ico_location']")).click();
		driver.findElement(By.id("userGroupTree_1_span")).click();
		driver.findElement(By.xpath("//button[text()='下一步']")).click();
		Utils.waitFor(1500);
		String path = "//input[contains(@id,'s2id_autogen')]";
		driver.findElement(By.xpath(path)).click();
		Utils.waitElementShow(driver, By.xpath("//input[contains(@id,'s2id_autogen')]"), 2);
		driver.findElement(By.xpath(path)).click();
		driver.findElement(By.xpath(path)).sendKeys("jenny01");
		Utils.waitFor(3000);
		driver.findElement(By.xpath("//div[@title='张小娜(jenny01)']")).click();
		driver.findElement(By.xpath("//button[text()='创建团队']")).click();
		try {
			driver.findElement(By.xpath("//span[text()='myTeam']"));
		} catch (Exception e) {
			Assert.fail("创建团队失败");
		}
	}

	@Test
	public void copyToTeamfile() {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		this.uploadCommon("5.txt");
		driver.findElement(By.xpath("//ul[@data-name='5.txt']/child::*/input")).click();
		driver.findElement(By.id("copy")).click();
		driver.findElement(By.id("copy-teamTree-holder_1_switch")).click(); // +号
		driver.findElement(By.xpath("//span[text()='myTeam']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		driver.findElement(By.className("headermenu_ico_team")).click();
		driver.findElement(By.xpath("//a[@data-name='myTeam']")).click();
		Boolean file = Utils.isExists(driver, By.xpath("//a[@data-name='5.txt']"));
		if (!file) {
			Assert.fail("复制到团队文件失败");
		}
	}

	@Test
	public void copyAToCompanyfile() {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		this.uploadCommon("6.docx");
		driver.findElement(By.xpath("//ul[@data-name='6.docx']/child::*/input")).click();
		driver.findElement(By.id("copy")).click();
		driver.findElement(By.id("copy-compTree-holder_1_switch")).click();
		driver.findElement(By.xpath("//span[text()='companyTeam']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		driver.findElement(By.className("headermenu_ico_companyfile")).click();
		Boolean file = Utils.isExists(driver, By.xpath("//a[@data-name='6.docx']"));
		if (!file) {
			Assert.fail("复制到公司文件失败");
		}
	}

	@Test
	@Parameters("favoritesUploadFileName")
	public void favorites(String filename) {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		this.uploadCommon(filename);
		WebElement txt = driver.findElement(By.xpath("//li[@data-name='7.pdf']"));
		Actions action = new Actions(driver);
		action.moveToElement(txt).build().perform();
		Utils.waitFor(5000);
		driver.findElement(By.xpath("//li[@data-name='" + filename + "']/following-sibling::li[@class='filebtns']//a[@title='更多']")).click();
		driver.findElement(By.xpath("//a[text()='添加收藏']")).click();
		driver.findElement(By.xpath("//span[text()='我的收藏']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		driver.findElement(By.xpath("//span[text()='收藏夹']")).click();
		Boolean favorite = Utils.isExists(driver, By.xpath("//a[@title='7.pdf']"));
		if (!favorite) {
			AssertJUnit.fail("文件收藏失败");
		}

	}

	@Test
	@Parameters({ "myExternalUpload" })
	public void newBuildExternalUpload(String myExternalUpload) {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();// 点击个人文件
		driver.findElement(By.xpath("//span[text()='外链上传']")).click();// 点击外链上传
		Utils.waitElementShow(driver, By.xpath("//span[text()='外链上传']"), 3);
		driver.findElement(By.xpath("//div[@id='ExternalUpload']//span[@id='new_build']")).click();// 点击新建
		WebElement element = driver.findElement(By.name("fileName"));
		element.sendKeys(myExternalUpload);// 在文本框中输入文件名称
		Boolean label = Utils.isExists(driver, By.xpath("//label[text()='永不过期']"));
		if (!label) {
			WebElement date = driver.findElement(By.name("creatStartTime"));
			date.sendKeys(Utils.getToday());
		}// 判断是否存在永不过期。
		Utils.waitElementShow(driver, By.xpath("//span[text()='确定']"), 10);
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		driver.findElement(By.xpath("//h4[text()='外链上传文件夹属性']/preceding-sibling::button")).click();
		Boolean folder = Utils.isExists(driver, By.xpath("//a[@title='myExternalUpload']"));
		if (!folder) {
			Assert.fail("新建外链上传文件夹失败");
		}
	}

	@Test
	@Parameters({ "y7techfile" })
	public void closeExternalUpload(String y7techfile) {
		this.newBuildExternalUpload(y7techfile);
		WebElement ul = driver.findElement(By.xpath("//ul[@data-name='订单地方']"));
		Actions action = new Actions(driver);
		action.moveToElement(ul).build().perform();

	}
}

/*
 * @AfterMethod public void logout() {
 * driver.findElement(By.className("caret")).click();
 * driver.findElement(By.xpath(".//*[@id='loginOut']/span")).click(); WebElement
 * login = driver.findElement(By.className("btn_login")); boolean success
 * =login.isDisplayed(); Assert.assertEquals(success,true); }
 * 
 * @AfterClass public void afterClass() { }
 * 
 * @BeforeTest public void beforeTest() { }
 */

