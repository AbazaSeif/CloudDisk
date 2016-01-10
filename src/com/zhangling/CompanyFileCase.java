package com.zhangling;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CompanyFileCase {
	CompanyFileWorker worker;
	WebDriver driver;
	
	@BeforeClass
	@Parameters({"url","username","password"})
	public void initBrowser(String url,String username,String password){		
		worker = new CompanyFileWorker(url);
		driver = worker.driver;
		worker.loginRight(username, password);
	}
	
	@BeforeMethod
	@Parameters({"enterCompanyTeam"})
	public void enterMyTeam(String teamName){
		worker.enterCompanyTeam(teamName);
	}
	
	@Test
	public void companyFlie_newFolder(){
		worker.newFolder();		
	}
	
	/**
	 * 新建同名文件夹，依赖newFolder方法
	 */
	@Test
	public void companyFlie_newFolderSame() {
		worker.newFolderSame();
	}

	@Test
	public void companyFlie_deleteFolder() {// 删除文件夾
		worker.deleteFolder();
	}

	@Test
	@Parameters({"upload"})
	public void companyFlie_upload(String uploadFile) {
		worker.uploadCommon(uploadFile);
	}
	
	@Test
	@Parameters({"download"})
	public void companyFlie_download(String fileName){
		worker.download(fileName);
	}
	
	@Test
	@Parameters({"upload"})
	public void companyFlie_deleteFile(String delFile){
		worker.deleteFile(delFile);
	}

	/**
	 * 云盘分享
	 */
	@Test
	@Parameters({"companyFileCloudShare"})
	public void companyFile_CloudShare(String file) {
		worker.companyFileCloudShare(file);
	}
	/**
	 * 链接分享
	 */
	@Test
	@Parameters({"companyFileLinkShare"})
	public void companyFileLinkShare(String file) {
		worker.companyFilelinkShare(file);	
	}
	/**
	 *在浏览器中打开链接分享 
	 */
	@Test
	@Parameters({"companyFileLinkShare"})
	public void companyFileOpenLinkShare(String file) {
		worker.openLinkShared(file);	
	}

	/**
	 * 复制文件到个人文件
	 */
	@Test
	@Parameters({"copyToMyFiles"})
	public void companyFileCopyToMyFiles(String file) {
		worker.companyFileCopyToMyFiles(file);
	}
	
	/**
	 * 复制文件到团队文件,文件名称后+1
	 */
	@Test
	@Parameters({"companyFileCopyToTeamFile","createTeam"})
	public void companyFileCopyToTeamFile(String file,String teamname) {
		Navigate.toCompanyFile(driver);
		worker.companyFileCopyToTeamFile(file, teamname);
	}

	/**
	 * 复制文件到公司文件
	 */
	@Test
	@Parameters({"companyFileCopyToCompanyFile"})
	public void companyFileCopyToCompanyFile(String file) {
		worker.companyFileCopyToCompanyFile(file);
	}

	/**
	 * 团队文件添加收藏
	 * @param filename
	 */
	@Test
	@Parameters("favoritesUploadFileName")
	public void companyFlieFavorites(String filename) {
		worker.favorites(filename);
	}
	
	/**
	 * 打标签
	 * @param filename
	 */
	@Test
	@Parameters({"tagging"})
	public void companyFlieTagging(String filename){
		worker.tagging(filename);
	}
	
	/**
	 * 评论
	 * @param filename
	 */
	@Test
	@Parameters({"common"})
	public void companyFlieComment(String filename){
		worker.comment(filename);
	}
	
	@Test
	@Parameters({"renaming"})
	public void companyFlieRenaming(String filename){
		worker.renaming(filename);		
	}
	
	@Test
	@Parameters({"moveFileToFolder"})
	public void moveFileToFolder(String fileName){
		worker.moveFileToFolder(fileName);
	}
	
	@Test
	@Parameters({"moveToCurrentDir","teamName"})
	public void moveToCurrentDir(String fileName,String teamName){
		worker.moveToCurrentDir(fileName, teamName);
	}
	
	@Test
	@Parameters({"lock"})
	public void lock(String fileName){
		worker.lock(fileName);
	}
	
	@Test(dependsOnMethods = {"lock"})
	@Parameters({"lock"})
	public void unlock(String fileName){
		worker.unlock(fileName);
	}
		
	@AfterClass
	public void logout(){
		worker.logout();
	}
	
	
	
	
	
	
}
