package com.zhangling;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TeamFileCase {
	TeamFileWorker worker;
	@BeforeClass
	@Parameters({"url","username","password"})
	public void initBrowser(String url,String username,String password){		
		worker = new TeamFileWorker(url);
		worker.loginRight(username, password);
	}
	
	@BeforeMethod
	@Parameters({"createTeam"})
	public void enterMyTeam(String teamName){
		worker.enterMyTeam(teamName);
	}
	
	@Test
	public void newFolder(){
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
	
	@Test
	@Parameters({"download"})
	public void download(String fileName){
		worker.download(fileName);
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
	@Parameters({"teamFileCloudShare"})
	public void teamFileCloudShare(String file) {
		worker.teamFileCloudShare(file);
	}
	/**
	 * 链接分享
	 */
	@Test
	@Parameters({"teamFileLinkShare"})
	public void teamFileLinkShare(String file ) {
		worker.teamFileLinkShare(file);	
	}
	/**
	 *在浏览器中打开链接分享 
	 */
	@Test(dependsOnMethods={"teamFileLinkShare"})
	@Parameters({"teamFileLinkShare"})
	public void openLinkShared(String file) {
		worker.openLinkShared(file);	
	}

	/**
	 * 复制文件到个人文件
	 */
	@Test
	@Parameters({"TeamFileCopyToMyFile"})
	public void teamFilCopyToMyFiles(String file) {
		worker.teamFilCopyToMyFiles(file);
	}
	
	/**
	 * 复制文件到团队文件,文件名称后+1
	 */
	@Test
	@Parameters({"teamFileCopyToTeamFile","createTeam"})
	public void teamFileCopyToTeamFile(String file,String teamname) {
		worker.teamFileCopyToTeamFile(file, teamname);
	}

	/**
	 * 复制文件到公司文件
	 */
	@Test
	@Parameters({"copyToCompanyFile"})
	public void teamFileCopyToCompanyFile(String file) {
		worker.teamFileCopyToCompanyFile(file);
	}

	/**
	 * 团队文件添加收藏
	 * @param filename
	 */
	@Test
	@Parameters("teamFile_favorites")
	public void favorites(String filename) {
		worker.favorites(filename);
	}
	
	/**
	 * 打标签
	 * @param filename
	 */
	@Test
	@Parameters({"tagging"})
	public void tagging(String filename){
		worker.tagging(filename);
	}
	
	/**
	 * 评论
	 * @param filename
	 */
	@Test
	@Parameters({"common"})
	public void comment(String filename){
		worker.comment(filename);
	}
	
	@Test
	@Parameters({"renaming"})
	public void renaming(String filename){
		worker.renaming(filename);		
	}
	/**
	 * 团队文件中将文件移动到文件夹
	 * @param fileName
	 */
	@Test
	@Parameters({"moveFileToFolder"})
	public void moveFileToFolder(String fileName){
		worker.moveFileToFolder(fileName);
	}
	
	@Test
	@Parameters({"moveToCurrentDir","createTeam"})
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
