package com.zhangling;

import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class MyFileCase {
	Worker worker;
	WebDriver driver;

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
		worker = new Worker(url);
		driver = worker.driver;
		worker.loginRight(username, password);
	}

	/**
	 * 新建文件夹
	 */
	@Test
	public void newFolder() {
		Navigate.toMyFile(driver);
		worker.newFolder();
	}

	/**
	 * 新建同名文件夹，依赖newFolder方法
	 */
	@Test
	public void newFolderSame() {
		Navigate.toMyFile(driver);
		worker.newFolderSame();
	}

	@Test
	public void deleteFolder() {// 删除文件夾
		Navigate.toMyFile(driver);
		worker.deleteFolder();
	}

	@Test
	@Parameters({"upload"})
	public void upload(String uploadFile) {
		Navigate.toMyFile(driver);
		worker.uploadCommon(uploadFile);
	}
	

	@Test(dependsOnMethods={"upload"})
	@Parameters({"upload"})
	public void deleteFile(String delFile){
		Navigate.toMyFile(driver);
		worker.deleteFile(delFile);
	}

	/**
	 * 云盘分享
	 */
	@Test
	@Parameters({"cloudShare"})
	public void cloudShare(String file) {
		Navigate.toMyFile(driver);
		worker.cloudShare(file);
	}
	/**
	 * 链接分享
	 */
	@Test
	@Parameters({"linkShare"})
	public void linkShare(String file ) {
		Navigate.toMyFile(driver);
		worker.linkShare(file);	
	}
	/**
	 *在浏览器中打开链接分享 
	 */
	@Test(dependsOnMethods={"linkShare"})
	@Parameters({"openLinkShare"})
	public void openLinkShared(String file) {
		Navigate.toMyShares(driver);
		worker.openLinkShared(file);	
	}
	/**
	 * 创建团队
	 */
	@Test
	@Parameters({"createTeam"})
	public void createTeam(String name) {
		Navigate.toTeamFile(driver);
		worker.createTeam(name);
	}

	/**
	 * 复制文件到当前（个人）文件，文件名称后+1
	 */
	@Test(dependsOnMethods="createTeam")
	@Parameters({"copyToMyFiles"})
	public void copyToMyFiles(String file) {
		Navigate.toMyFile(driver);
		worker.copyToMyFiles(file);
	}
	
	/**
	 * 复制文件到团队文件
	 */
	@Test(dependsOnMethods={"createTeam"})
	@Parameters({"copyToTeamFile","createTeam"})
	public void copyToTeamFile(String file,String teamname) {
		Navigate.toMyFile(driver);
		worker.copyToTeamFile(file,teamname);
		Navigate.toMyFile(driver);
	}

	/**
	 * 复制文件到公司文件
	 */
	@Test
	@Parameters({"copyToCompanyFile"})
	public void copyToCompanyFile(String file) {
		Navigate.toMyFile(driver);
		worker.copyToCompanyFile(file);
		Navigate.toMyFile(driver);
	}

	/**
	 * 个人文件添加收藏
	 * @param filename
	 */
	@Test
	@Parameters("favoritesUploadFileName")
	public void favorites(String filename) {
		Navigate.toMyFile(driver);
		worker.favorites(filename);
	}

	/**
	 * 新建外链上传文件夹
	 * @param myExternalUpload
	 */
	@Test
	@Parameters({ "newExternalUpload" })
	public void newExternalUpload(String myExternalUpload) {
		Navigate.toMyFile(driver);
		worker.newExternalUpload(myExternalUpload);
	}

	/**
	 * 关闭外链上传文件夹，依赖newExternalUpload()
	 * @param myExternalUpload
	 */
	@Test(dependsOnMethods={"newExternalUpload"})
	@Parameters({"newExternalUpload"})
	public void closeExternalUpload(String myExternalUpload) {
		Navigate.toMyFile(driver);
		worker.closeExternalUpload(myExternalUpload);
	}
	
	/**
	 * 删除外链上传文件夹
	 * @param myExternalUpload
	 */
	@Test(dependsOnMethods={"closeExternalUpload"})
	@Parameters({"newExternalUpload"})
	public void deleteExternalUpload(String myExternalUpload){
		Navigate.toMyFile(driver);
		worker.deleteExternalUpload(myExternalUpload);		
	}
	
	/**
	 * 打标签
	 * @param filename
	 */
	@Test
	@Parameters({"tagging"})
	public void tagging(String filename){
		Navigate.toMyFile(driver);
		worker.tagging(filename);
	}
	
	/**
	 * 评论
	 * @param filename
	 */
	@Test
	@Parameters({"common"})
	public void comment(String filename){
		Navigate.toMyFile(driver);
		worker.comment(filename);
	}
	
	@Test
	@Parameters({"renaming"})
	public void renaming(String filename){
		Navigate.toMyFile(driver);
		worker.renaming(filename);		
	}
	
	@AfterClass
	public void logout(){
	Navigate.toMyFile(driver);
	worker.logout();
	}
	
	
}






