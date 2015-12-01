package com.zhangling;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TeamFileCase {
	Worker worker;
	WebDriver driver;

	@Test
	@Parameters({ "upload", "createTeam" })
	public void upload(String fileName, String teamName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		worker.uploadCommon(fileName);
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

	/**
	 * 创建团队
	 * 
	 * @Test
	 * @Parameters({"createTeam" ) public void createTeam(String name) {
	 *                           Navigate.toTeamFile(driver);
	 *                           worker.createTeam(name); }
	 */
	/**
	 * 复制文件到当前（个人）文件，文件名称后+1
	 */
	@Test
	public void copyToMyFiles(String file, String teamName) {
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);

	}

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
	@Parameters({ "common" })
	public void common(String filename) {
		worker.common(filename);
	}

	@Test
	@Parameters({ "renaming" })
	public void renaming(String filename) {
		worker.renaming(filename);
	}

}
