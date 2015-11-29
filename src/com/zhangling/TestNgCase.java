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

	/**
	 * 个人文件添加收藏
	 * @param filename
	 */
	@Test
	@Parameters("favoritesUploadFileName")
	public void favorites(String filename) {
		worker.favorites(filename);
	}

	/**
	 * 新建外链上传文件夹
	 * @param myExternalUpload
	 */
	@Test
	@Parameters({ "externalUpload" })
	public void newExternalUpload(String myExternalUpload) {
		worker.newExternalUpload(myExternalUpload);
	}

	/**
	 * 关闭外链上传文件夹，依赖newExternalUpload()
	 * @param myExternalUpload
	 */
	@Test(dependsOnMethods={"newExternalUpload"})
	public void closeExternalUpload(String myExternalUpload) {
		worker.closeExternalUpload(myExternalUpload);
	

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

