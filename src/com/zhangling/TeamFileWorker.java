
package com.zhangling;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import autoitx4java.AutoItX;

import com.jacob.com.LibraryLoader;

public class TeamFileWorker {

	WebDriver driver;
	
	public TeamFileWorker(String url) {
		File firepath = new File("lib/firepath-0.9.7.1-fx.xpi"); 
		File firebug = new File("lib/firebug-2.0.13-fx.xpi"); 
		FirefoxProfile profile = new FirefoxProfile(); 
		try {
			profile.addExtension(firebug);
			profile.setPreference("extensions.firebug.currentVersion", "2.0.13");//设置firebug 版本
			profile.addExtension(firepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		profile.setPreference("browser.startup.homepage", "about:blank");
		profile.setPreference("startup.homepage_welcome_url.additional", "");
		
		/*profile.setPreference("browser.download.folderList", 2);//0桌面;1默认;2指定目录
		profile.setPreference("browser.download.dir", "d:\\");//下载到指定目录
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/pdf");//多个用逗号分开
*/		
		/*ProfilesIni allProfiles = new ProfilesIni();  
		FirefoxProfile profile = allProfiles.getProfile("default");*/  
		
		driver = new FirefoxDriver(profile);
		driver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		driver.navigate().to(url);
	}
	
	public void enterMyTeam(String teamName){
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
	}
	
	public void closeGroup(){
		By openClose = By.xpath("//nav[@id='sidebar']/span[text()='团队文件']/label/i[@id='group_open_close']");
		if(!driver.findElement(openClose).getAttribute("style").equals("")){
			driver.findElement(openClose).click();
		}
	}

	public void loginRight(String username, String password) {
		driver.findElement(By.className("user")).sendKeys(username);
		driver.findElement(By.className("locked")).sendKeys(password);
		WebElement checkbox = driver.findElement(By.className("remb_name"));// 判断记住我是否勾选
		if (!checkbox.isSelected()) {
			checkbox.click();
		}
		driver.findElement(By.className("btn_login")).click();
		Utils.waitElementShow(driver, By.xpath("//span[text()='张苓']"), 10);
		boolean exists = Utils.isExists(driver, By.xpath("//span[text()='张苓']"));
		if (!exists) {
			Assert.fail("登陆文档云不成功");
			System.out.println("登陆文档云失败");
		}
		System.out.println("登陆文档云成功");
	}

	public String newFolder() {
		Utils.isExists(driver, By.xpath("//div[@id='dsdsf']"));
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='new_build']")).click();
		String folderName = "folder" + System.currentTimeMillis();
		WebElement element = driver.findElement(By.xpath("//input[@name='folderName']"));	
		element.click();
		element.sendKeys(folderName);
		Utils.waitFor(2000);
		WebElement confirm = driver.findElement(By.className("confirmNewFolder"));
		confirm.click();
		Boolean success = Utils.isExists(driver, By.xpath("//a[@title='"+folderName+"']"));
		if (!success) {
			Assert.fail("新建文件夹失败");
			System.out.println("新建文件夹失败");
		}
		System.out.println("新建文件夹成功");
		return folderName;
	}

	/**
	 * 新建同名文件夹，获取到所有文件夹的名称后，获取第一个文件夹名称
	 */
	public void newFolderSame() {
		
		List<WebElement> lis = driver.findElements(By.xpath("//img[contains(@src,'floder_defult.png')]/following-sibling::div"));
		String existsFolderName = lis.get(0).getAttribute("data-name");

		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='new_build']")).click();
		driver.findElement(By.name("folderName")).sendKeys(existsFolderName);
		driver.findElement(By.className("confirmNewFolder")).click();
		WebElement txt = driver.findElement(By.xpath(".//*[@id='page_message_warning']/span"));
		Assert.assertEquals(txt.getText(), "文件夹名称重复，请重新输入");
		Utils.waitElementShow(driver, By.xpath("//button[text()='取消']"), 5);
		driver.findElement(By.xpath("//button[text()='取消']")).click();
		System.out.println("新建重名文件夹用例执行成功");
	}

	/**
	 * 删除列表中的第一个文件夹
	 */
	public void deleteFolder() {
		List<WebElement> lis = driver.findElements(By.xpath("//img[contains(@src,'floder_defult.png')]/parent::li/parent::ul"));
		String existsFolderName = lis.get(0).getAttribute("data-name");
		driver.findElement(By.xpath("//ul[@data-name='" + existsFolderName + "']/child::li[1]/input")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='delete']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Utils.waitFor(3000);
		Boolean folderName = Utils.isExists(driver, By.xpath("//a[@data-name='" + existsFolderName + "' and @title='" + existsFolderName + "']"));
		if (!folderName) {
			System.out.println("删除文件夹成功");
		} else {
			System.out.println("删除文件夹失败");
			Assert.fail("删除文件夹失败");
		}
	}

	/**
	 * 上传文件1.txt
	 * 
	 * @param fileName
	 */
	public void uploadCommon(String fileName) {
		File file = new File("lib/jacob-1.18-x64.dll");// 新建文件指向字符串指向的路径
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());// 注册此文件
		File file1 = new File("D:\\upload\\" + fileName);

		By upload = By.xpath("//div[@id='TeamFiles']//span[@id='upload']");
		Utils.waitElementShow(driver, upload, 5000);
		driver.findElement(upload).click();
		driver.findElement(By.xpath("//a[@id='uploader_browse']/span")).click();
		AutoItX x = new AutoItX();
		String uploadWin = "文件上传";
		x.winActivate(uploadWin);
		try {
			Thread.sleep(200);
			x.controlFocus(uploadWin, "", "Edit1");// 定位到文件名输入框
			Thread.sleep(200);
			x.ControlSetText(uploadWin, "", "Edit1", file1.getAbsolutePath());// 在输入框中输入文件名称（包含路径）
			Thread.sleep(200);
		} catch (InterruptedException e) {

		}
		x.controlClick(uploadWin, "", "Button1");// 点击“打开”

		driver.findElement(By.xpath("//a[@id='uploader_start' and not(contains(@class,'ui-state-disabled'))]")).click();//可点击状态
		Utils.waitUntilElementInvisiable(driver, By.xpath("//a[@id='uploader_start' and contains(@class,'ui-state-disabled')]"));//不可点击状态
		driver.findElement(By.xpath("//button[@title='关闭']")).click();
		Boolean uploadedfile = Utils.isExists(driver, By.xpath("//a[@data-name='" + fileName + "']"));
		if (! uploadedfile) {
			System.out.println("文件上传失败");
			Assert.fail("文件上传失败");
		} 

	}
	
	public void download(String fileName){
		Navigate.toMyFile(driver);
		driver.findElement(By.className("selectAll")).click();
		driver.findElement(By.id("downloadZip")).click();
	}
	

	/**
	 * 删除文件1.txt，需要依赖uploadCommon（）
	 */
	public void deleteFile(String deleteFile) {
		driver.findElement(By.xpath("//ul[@data-name='"+deleteFile+"']//input")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='delete']")).click();
		driver.findElement(By.className("btn_primary_large")).click();
		Utils.waitFor(3000);
		Boolean folderName = Utils.isExists(driver, By.xpath("//a[@data-name='"+deleteFile+"' and @title='"+deleteFile+"']"));
		if (!folderName) {
			System.out.println("删除文件成功");
		} else {
			System.out.println("删除文件失败");
			Assert.fail("删除文件失败");
		}
	}

	public void teamFileCloudShare(String shareFile) {
		uploadCommon(shareFile);
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+shareFile+"']/li/input")).click();
		WebElement element = driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='share']"));
		element.click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//a[@id='cloudShare']")).click();
		driver.findElement(By.id("s2id_autogen2")).sendKeys("jenny");
		Utils.waitElementShow(driver, By.xpath("//div[@title='张苓(jenny)']"), 5);
		//driver.findElement(By.xpath("//div[@title='张小一(jenny)']")).sendKeys(Keys.ENTER);
		new Actions(driver).click(driver.findElement(By.xpath("//div[@title='张苓(jenny)']"))).perform();
		driver.findElement(By.xpath("//button[text()='确定分享']")).click();
		Navigate.toMyShares(driver);
		driver.findElement(By.xpath("//span[text()='已发分享']")).click();

		Boolean sendShare = Utils.isExists(driver, By.xpath("//a[@data-name='"+shareFile+"']"));
		if (sendShare) {
			System.out.println("云盘分享文件成功");
		} else {
			System.out.println("云盘分享文件失败");
			Assert.fail("云盘分享文件失败");
		}
	}

	public void teamFileLinkShare(String shareFile) {
		uploadCommon(shareFile);
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+shareFile+"']/li/input")).click();
		WebElement element = driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='share']"));
		element.click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//a[@id='linkShare']")).click();
		driver.findElement(By.xpath("//button[text()='创建分享链接']")).click();
		driver.findElement(By.xpath("//button[text()='关闭']")).click();
		Navigate.toMyShares(driver);
		Boolean sentShare = Utils.isExists(driver, By.xpath("//a[@data-name='"+shareFile+"']"));
		if (sentShare) {
			System.out.println("链接分享成功");
		} else {
			System.out.println("链接分享失败");
			Assert.fail("链接分享失败");
		}
	}

	public void openLinkShared(String shareFile) {
		Navigate.toMyShares(driver);
		driver.findElement(By.xpath("//span[contains(text(),'已发分享')]")).click();
		Utils.waitFor(3000);
		driver.findElement(By.xpath("//div[@id='SendedShare']//a[@title='"+shareFile+"' and @data-name='"+shareFile+"']/ancestor::li[@class='filename_noico']/following-sibling::li[2]/span/div/embed")).click();
		String linkAndCode = Utils.getClip();
		String code = linkAndCode.substring(linkAndCode.lastIndexOf(":") + 1);// 截取链接中最后一个：后面的内容
		((JavascriptExecutor) driver).executeScript("window.open('" + linkAndCode + "')");
		
		String mainHandle = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		String secondHandle = "";
		for(String handle:handles){
			if(!handle.equals(mainHandle)){
				secondHandle = handle;
			}
		}
		driver.switchTo().window(secondHandle);
		driver.findElement(By.id("linkcode")).sendKeys(code);
		driver.findElement(By.xpath(".//a[contains(text(),'提取')]")).click();
		boolean exist = Utils.isExists(driver, By.xpath("//a[text()='"+shareFile+"' and @code]"));
		if (!exist) {
			 Assert.fail("打开分享链接不成功"); 
		}
	}
	
	
	public void teamFilCopyToMyFiles(String file) {
		
		uploadCommon(file);
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+file+"']/child::*/input")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='copy']")).click();
		driver.findElement(By.id("copy-fileTree-holder_1_span")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Utils.waitFor(2000);
		Navigate.toMyFile(driver);
		Boolean filename = Utils.isExists(driver, By.xpath("//div[@id='Home']//a[@data-name='"+file+"']"));
		if (!filename) {
			Assert.fail("复制到团队文件失败");
			System.out.println("复制到团队文件失败");
		}else{
			System.out.println("复制到团队文件成功");
		}
	}
	
	public void teamFileCopyToTeamFile(String file,String teamName) {		
		uploadCommon(file);
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+file+"']/child::*/input")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='copy']")).click();
		driver.findElement(By.xpath("//div[@class='modal-dialog']//span[@id='copy-teamTree-holder_1_switch']")).click(); // +号
		driver.findElement(By.xpath("//div[@class='modal-dialog']//span[text()='"+teamName+"']")).click();
		driver.findElement(By.xpath("//div[@class='modal-dialog']//span[text()='确定']")).click();
		Utils.waitFor(3000);
		String name = file.substring(0, file.indexOf("."));
		String prefix = file.substring(file.indexOf("."));
		Boolean filename = Utils.isExists(driver, By.xpath("//div[@id='TeamFiles']//a[@data-name='"+name+"(1)"+prefix+"']"));
		if (!filename) {
			System.out.println("复制到团队文件失败");
			Assert.fail("复制到团队文件失败");
		}else{
			System.out.println("复制到团队文件成功");
		}
	}
	
	public void teamFileCopyToCompanyFile(String filename) {
		uploadCommon(filename);
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+filename+"']/child::*/input")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='copy']")).click();
		driver.findElement(By.id("copy-compTree-holder_1_switch")).click();
		driver.findElement(By.xpath("//div[@class='modal-dialog']//a[@title='companyTeam']//span[text()='companyTeam']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Navigate.toCompanyFile(driver);
		Boolean file = Utils.isExists(driver, By.xpath("//a[@data-name='"+filename+"']"));
		if (!file) {
			Assert.fail("复制到公司文件失败");
			System.out.println("复制到公司文件失败");
		}else{
			System.out.println("复制到公司文件成功");
		}			
	}
	
	
	public void favorites(String filename) {
		uploadCommon(filename);
		WebElement txt = driver.findElement(By.xpath("//div[@id='TeamFiles']//a[@data-name='"+filename+"']"));
		Actions action = new Actions(driver);
		action.moveToElement(txt).build().perform();
		Utils.takeScreenShot(driver);
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+filename+"']//li[@class='filebtns']//a[@title='更多']")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+filename+"']//li[@class='filebtns']//a[@title='添加收藏']")).click();
		driver.findElement(By.xpath("//span[text()='我的收藏']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Navigate.toMyFile(driver);
		driver.findElement(By.xpath("//span[text()='收藏夹']")).click();
		Boolean favorite = Utils.isExists(driver, By.xpath("//a[@title='"+filename+"']"));
		if (!favorite) {
			Assert.fail("文件收藏失败");
			System.out.println("文件收藏失败");
		}else{
			System.out.println("文件收藏成功");			
		}
	}
	
	public void tagging(String file){
		uploadCommon(file);
		WebElement ele = driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+file+"']/li[2]"));
		Actions act = new Actions(driver);
		act.moveToElement(ele).build().perform();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+file+"']//a[@title='打标签']")).click();
		WebElement tag = driver.findElement(By.className("js_tag_name"));
		String tagname = "tag"+System.currentTimeMillis();
		tag.sendKeys(tagname);
		driver.findElement(By.xpath("//a[text()='贴上']")).click();
		driver.findElement(By.xpath("//span[text()='提交']")).click();
		Utils.waitFor(3000);
		act.moveToElement(ele).build().perform();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+file+"']//a[@title='打标签']")).click();
		Utils.waitFor(3000);
		Boolean isexist = Utils.isExists(driver, By.xpath("//span[text()='"+tagname+"']"));
		if(!isexist){
			System.out.println("打标签失败");
			Assert.fail("打标签失败");
		}else{
			System.out.println("打标签成功");
		}
		driver.findElement(By.xpath("//div[@id='dialog_add_tags']//span[text()='关闭']")).click();		
	}
	
	public void comment(String fileName){
		uploadCommon(fileName);
		WebElement file = driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@class='list-item' and @data-name='"+fileName+"']"));
		Actions act = new Actions(driver);
		act.moveToElement(file).build().perform();
		Utils.waitFor(3000);
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']/li[@class='filebtns']/a[@title='评论']")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//textarea[@id='commentTextarea']")).sendKeys("中国共产党万岁");
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[text()='评论']")).click();
		Boolean success = Utils.isExists(driver, By.xpath("//div[@id='TeamFiles']//div[text()='中国共产党万岁']"));
		if(success){
			System.out.println("评论成功");
		}else{
			System.out.println("评论失败");
			Assert.fail("评论失败");
		}		
		act.moveToElement(file).build().perform();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//a[@title='评论']")).click();
	}
	
	public void renaming(String fileName){
		uploadCommon(fileName);
		WebElement ele = driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@class='list-item' and @data-name='"+fileName+"']"));
		Actions act = new Actions(driver);
		act.moveToElement(ele).build().perform();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//li[@class='filebtns']//a[@title='更多']")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//a[text()='重命名']")).click();
		Utils.waitFor(3000);
		WebElement element = driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//input[@class='rename_txt']"));
		element.clear();
		element.sendKeys("pass");
		
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//button[@class='btn_gray_ico js_sub_rename']")).click();
		driver.switchTo().alert().accept();
		Boolean success = Utils.isExists(driver, By.xpath("//div[@id='TeamFiles']//ul[@data-name='pass']//a[text()='pass']"));
		if(!success){
			System.out.println("修改文件名失败");
			Assert.fail("修改文件名失败");
		}else{
			System.out.println("修改文件名成功");
		}
	}

	public void moveFileToFolder(String fileName){
		uploadCommon(fileName);
		String folderName = newFolder();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//li[1]//input")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='move']")).click();
		By add = By.xpath("//div[@class='modal-dialog']//span[@id='copy-teamTree-holder_1_switch']");
		Utils.waitElementShow(driver, add, 6000);
		driver.findElement(add).click();
		driver.findElement(By.xpath("//div[@class='modal-dialog']//span[text()='"+folderName+"']")).click();
		driver.findElement(By.xpath("//div[@class='modal-dialog']//span[text()='确定']")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//a[@title='"+folderName+"']")).click();
		Boolean ele= Utils.isExists(driver, By.xpath("//div[@id='TeamFiles']//a[text()='"+fileName+"']"));
		if(!ele){
			System.out.println("移动到文件夹失败");
			Assert.fail("移动到文件夹失败");
		}else{
			System.out.println("移动到文件夹成功");
		}
	}
	
	public void moveToCurrentDir(String fileName,String teamName){
		uploadCommon(fileName);
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[ @data-name='"+fileName+"']//li[1]//input")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//span[@id='move']")).click();
		driver.findElement(By.xpath("//span[text()='"+teamName+"']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Boolean ele = Utils.isExists(driver, By.xpath("//span[text()='不能移到相同目录']"));
		if(!ele){
			System.out.println("移动到当前目录失败");
			Assert.fail("移动到当前目录失败");
		}else{
			System.out.println("移动到当前目录成功");		
		}		
	}
	
	public void lock(String fileName){
		uploadCommon(fileName);
		WebElement ele = driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@class='list-item' and @data-name='"+fileName+"']"));
		Actions act = new Actions(driver);
		act.moveToElement(ele).build().perform();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//li[@class='filebtns']//a[@title='更多']")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//a[text()='锁定']")).click();
		Utils.waitFor(3000);
		Boolean mark = Utils.isExists(driver, By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//i[@class='main_ico_my_lock']"));
		if(!mark){
			System.out.println("锁定文件失败");
			Assert.fail("锁定文件失败");
		}else{
			System.out.println("锁定文件成功");
		}
	}
	
	public void unlock(String fileName){
		WebElement ele = driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@class='list-item' and @data-name='"+fileName+"']"));
		Actions act = new Actions(driver);
		act.moveToElement(ele).build().perform();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//li[@class='filebtns']//a[@title='更多']")).click();
		driver.findElement(By.xpath("//div[@id='TeamFiles']//a[text()='解锁']")).click();
		Utils.waitFor(3000);
		Boolean mark = Utils.isExists(driver, By.xpath("//div[@id='TeamFiles']//ul[@data-name='"+fileName+"']//i[@class='main_ico_my_lock']"));
		if(!mark){
			System.out.println("文件解锁成功");
			
		}else{
			System.out.println("文件解锁失败");
			Assert.fail("文件解锁失败");
		}
	}
	
	public void logout(){
		driver.findElement(By.xpath("//b[@class='caret']")).click();
		driver.findElement(By.xpath("//span[text()='退出系统']")).click();
		Utils.waitFor(3000);
		Boolean button = Utils.isExists(driver, By.xpath("//button[text()='登 录']"));
		if(button){
			System.out.println("退出系统成功");
		}else{
			Assert.fail("退出系统失败");
			System.out.println("退出系统失败");
		}
	}
	
	
		

}
