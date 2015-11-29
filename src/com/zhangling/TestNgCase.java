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
	@Parameters({"upload"})
	public void upload(String uploadFile) {
		worker.uploadCommon(uploadFile);
	}
	

	@Test(dependsOnMethods={"upload"})
	@Parameters({"upload"})
	public void deleteFile(String delFile){
		worker.deleteFile(delFile);
	}

	/**
	 * 云盘分享
	 */
	@Test
	@Parameters({"cloudShare"})
	public void cloudShare(String file) {
		worker.cloudShare(file);
	}
	/**
	 * 链接分享
	 */
	@Test
	@Parameters({"linkShare"})
	public void linkShare(String file ) {
		worker.linkShare(file);	
	}
	/**
	 *在浏览器中打开链接分享 
	 */
	@Test(dependsOnMethods={"linkShare"})
	@Parameters({"openLinkShare"})
	public void openLinkShared(String file) {
		worker.openLinkShared(file);	
	}
	/**
	 * 创建团队
	 */
	@Test
	@Parameters({"createTeam"})
	public void createTeam(String name) {
		worker.createTeam(name);
	}

	/**
	 * 复制文件到当前（个人）文件，文件名称后+1
	 */
	@Test(dependsOnMethods="createTeam")
	@Parameters({"copyToMyFiles"})
	public void copyToMyFiles(String file) {
		worker.copyToMyFiles(file);
	}
	
	/**
	 * 复制文件到团队文件
	 */
	@Test
	@Parameters({"copyToTeamFile","createTeam"})
	public void copyToTeamFile(String file,String teamname) {
		worker.copyToTeamFile(file,teamname);
	}

	/**
	 * 复制文件到公司文件
	 */
	@Test
	@Parameters({"copyToCompanyFile"})
	public void copyToCompanyFile(String file) {
		worker.copyToCompanyFile(file);
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
	@Parameters({ "newExternalUpload" })
	public void newExternalUpload(String myExternalUpload) {
		worker.newExternalUpload(myExternalUpload);
	}

	/**
	 * 关闭外链上传文件夹，依赖newExternalUpload()
	 * @param myExternalUpload
	 */
	@Test(dependsOnMethods={"newExternalUpload"})
	@Parameters({"newExternalUpload"})
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

