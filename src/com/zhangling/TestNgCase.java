package com.zhangling;

import java.util.ArrayList;

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
	@Test
	public void newFolderSame() {
		worker.newFolderSame();
	}

	@Test
	public void deleteFolder() {// 删除文件夾
		worker.deleteFolder();
	}

	@Test
	public void upload() {
		worker.uploadCommon("1.txt");
	}

	@Test(dependsOnMethods={"upload"})
	public void deleteFile(){
		worker.deleteFile();
	}

	/**
	 * 云盘分享
	 */
	@Test
	public void cloudShare() {
		worker.cloudShare();
	}
	/**
	 * 链接分享
	 */
	@Test
	public void linkShare() {
		worker.linkShare();	
	}
	/**
	 *在浏览器中打开链接分享 
	 */
	@Test(dependsOnMethods={"linkShare"})
	public void openLinkShared() {
		worker.openLinkShared();	
	}
	/**
	 * 创建团队
	 */
	@Test
	public void createTeam() {
		worker.createTeam();
	}

	/**
	 * 复制文件到当前（个人）文件，文件名称后+1
	 */
	@Test
	public void copyToMyFiles() {
		worker.copyToMyFiles();
	}
	
	/**
	 * 复制文件到团队文件
	 */
	@Test
	public void copyToTeamFile() {
		worker.copyToTeamFile();
	}

	/**
	 * 复制文件到公司文件
	 */
	@Test
	public void copyToCompanyFile() {
		worker.copyToCompanyFile();
	}

	@Test
	@Parameters("favoritesUploadFileName")
	public void favorites(String filename) {
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();
		worker.uploadCommon(filename);
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

