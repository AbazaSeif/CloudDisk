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
	@Test(dependsOnMethods={"createTeam"})
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
	
	/**
	 * 删除外链上传文件夹
	 * @param myExternalUpload
	 */
	@Test(dependsOnMethods={"closeExternalUpload"})
	@Parameters({"newExternalUpload"})
	public void deleteExternalUpload(String myExternalUpload){
		worker.deleteExternalUpload(myExternalUpload);		
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
	
	@AfterClass
	public void logout(){
	worker.logout();
	}
	
	
	
	
	
	
}
