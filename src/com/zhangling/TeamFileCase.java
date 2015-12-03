package com.zhangling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TeamFileCase {
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
	
	@Test
	@Parameters({"createTeam","upload"})
	public void upload(String teamName,String fileName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver,teamName);
		worker.uploadCommon(fileName,"//div[@id='TeamFiles']//span[@id='upload']");
	}

	@Test
	public void newFolder(String teamName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.newFolder();
	}

	@Test
	public void newFolderSame(String teamName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.newFolderSame();
	}

	@Test
	public void deleteFolder(String teamName) {// 删除文件夾
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.deleteFolder();
	}

	@Test(dependsOnMethods = { "upload" })
	@Parameters({ "upload" })
	public void deleteFile(String delFile, String teamName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.deleteFile(delFile);
	}

	/**
	 * 云盘分享
	 */
	@Test
	@Parameters({ "cloudShare" })
	public void cloudShare(String file, String teamName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.cloudShare(file);
	}

	/**
	 * 链接分享
	 */
	@Test
	@Parameters({ "linkShare" })
	public void linkShare(String file, String teamName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.linkShare(file);
	}

	/**
	 * 在浏览器中打开链接分享
	 */
	@Test(dependsOnMethods = { "linkShare" })
	@Parameters({ "openLinkShare" })
	public void openLinkShared(String file, String teamName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.openLinkShared(file);
	}

	
	/*@Test
	@Parameters({"teamFileToMyFile","upload"})
	public void teamFileToMyFiles(String fileName, String teamName) {
		worker.teamFileToMyFile(teamName, fileName);		

	}*/

	/**
	 * 复制文件到团队文件
	 */
	@Test
	@Parameters({ "copyToTeamFile", "createTeam" })
	public void copyToTeamFile(String file, String teamname) {
		worker.copyToTeamFile(file, teamname);
	}

	/**
	 * 复制文件到公司文件
	 */
	@Test
	@Parameters({ "copyToCompanyFile" })
	public void copyToCompanyFile(String file) {
		worker.copyToCompanyFile(file);
	}

	/**
	 * 个人文件添加收藏
	 * 
	 * @param filename
	 */
	@Test
	@Parameters("favoritesUploadFileName")
	public void favorites(String filename) {
		worker.favorites(filename);
	}

	/**
	 * 新建外链上传文件夹
	 * 
	 * @param myExternalUpload
	 */
	@Test
	@Parameters({ "newExternalUpload" })
	public void newExternalUpload(String myExternalUpload) {
		worker.newExternalUpload(myExternalUpload);
	}

	/**
	 * 关闭外链上传文件夹，依赖newExternalUpload()
	 * 
	 * @param myExternalUpload
	 */
	@Test(dependsOnMethods = { "newExternalUpload" })
	@Parameters({ "newExternalUpload" })
	public void closeExternalUpload(String myExternalUpload) {
		worker.closeExternalUpload(myExternalUpload);
	}

	/**
	 * 删除外链上传文件夹
	 * 
	 * @param myExternalUpload
	 */
	@Test
	@Parameters({ "newExternalUpload" })
	public void deleteExternalUpload(String myExternalUpload) {
		worker.deleteExternalUpload(myExternalUpload);
	}

	/**
	 * 打标签
	 * 
	 * @param filename
	 */
	@Test
	@Parameters({ "tagging" })
	public void tagging(String filename) {
		worker.tagging(filename);
	}

	/**
	 * 评论
	 * 
	 * @param filename
	 */
	@Test
	@Parameters({ "comment" })
	public void comment(String filename) {
		worker.comment(filename);
	}

	@Test
	@Parameters({ "renaming" })
	public void renaming(String filename) {
		worker.renaming(filename);
	}

	public void teamFileToMyFile(String teamName,String fileName){
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.uploadCommon(fileName);
		driver.findElement(By.xpath("//ul[@data-name='"+fileName+"']/child::li[1]/input")).click();
		driver.findElement(By.id("copy")).click();
		driver.findElement(By.id("copy-fileTree-holder_1_span")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Utils.waitFor(2000);
		Navigate.toMyFile(driver);
		Boolean copyFileExists = Utils.isExists(driver, By.xpath("//a[@data-name='"+fileName+"']"));		 
		if (!copyFileExists) {
			System.out.println("团队文件复制到个人文件失败");
			Assert.fail("团队文件复制到个人文件失败");
		}else{
			System.out.println("团队文件复制到个人文件成功");
		}
		
	}
}
