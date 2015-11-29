package com.zhangling;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import autoitx4java.AutoItX;

import com.jacob.com.LibraryLoader;

public class Worker {

	WebDriver driver;

	public Worker(String url, String username, String password) {
		driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.manage().window().maximize();
		driver.navigate().to(url);
	}

	public void loginRight(String username, String password) {
		driver.findElement(By.className("user")).sendKeys(username);
		driver.findElement(By.className("locked")).sendKeys(password);
		WebElement checkbox = driver.findElement(By.className("remb_name"));// 判断记住我是否勾选
		if (!checkbox.isSelected()) {
			checkbox.click();
		}
		driver.findElement(By.className("btn_login")).click();
		Utils.waitElementShow(driver, By.xpath("//span[text()='张小一']"), 10);
		boolean exists = Utils.isExists(driver, By.xpath("//span[text()='张小一']"));
		if (!exists) {
			Assert.fail("登陆文档云不成功");
			System.out.println("登陆文档云失败");
		}
		System.out.println("登陆文档云成功");
	}

	public void newFolder() {
		Navigate.toMyFile(driver);
		driver.findElement(By.id("new_build")).click();
		WebElement element = driver.findElement(By.name("folderName"));
		String folderName = "folder" + System.currentTimeMillis();
		element.sendKeys(folderName);
		WebElement confirm = driver.findElement(By.className("confirmNewFolder"));
		confirm.click();
		Boolean success = Utils.isExists(driver, By.xpath("//img[contains(@src,'floder_defult.png')]/parent::li[@data-name='" + folderName + "']"));
		if (!success) {
			Assert.fail("新建文件夹失败");
			System.out.println("新建文件夹失败");
		}
		System.out.println("新建文件夹成功");
	}

	/**
	 * 新建同名文件夹，获取到所有文件夹的名称后，获取第一个文件夹名称
	 */
	public void newFolderSame() {
		Navigate.toMyFile(driver);
		List<WebElement> lis = driver.findElements(By.xpath("//img[contains(@src,'floder_defult.png')]/parent::li"));
		String existsFolderName = lis.get(0).getAttribute("data-name");

		driver.findElement(By.id("new_build")).click();
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
		Navigate.toMyFile(driver);
		List<WebElement> lis = driver.findElements(By.xpath("//img[contains(@src,'floder_defult.png')]/parent::li"));
		String existsFolderName = lis.get(0).getAttribute("data-name");
		driver.findElement(By.xpath("//ul[@data-name='" + existsFolderName + "']/child::li[1]/input")).click();
		driver.findElement(By.id("delete")).click();
		driver.findElement(By.className("btn_primary_large")).click();
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
		Navigate.toMyFile(driver);
		File file = new File("lib/jacob-1.18-x64.dll");// 新建文件指向字符串指向的路径
		System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());// 注册此文件
		File file1 = new File("D:\\upload\\" + fileName);

		driver.findElement(By.id("upload")).click();
		driver.findElement(By.xpath(".//*[@id='uploader_browse']/span")).click();
		AutoItX x = new AutoItX();
		String uploadWin = "文件上传";
		x.winActivate(uploadWin);
		try {
			Thread.sleep(1000);
			x.controlFocus(uploadWin, "", "Edit1");// 定位到文件名输入框
			Thread.sleep(1000);
			x.ControlSetText(uploadWin, "", "Edit1", file1.getAbsolutePath());// 在输入框中输入文件名称（包含路径）
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
		x.controlClick(uploadWin, "", "Button1");// 点击“打开”

		driver.findElement(By.xpath("//a[@id='uploader_start']/child::span")).click();
		Utils.waitFor(3000);
		driver.findElement(By.xpath("//button[@title='关闭']")).click();
		Boolean uploadedfile = Utils.isExists(driver, By.xpath("//a[@data-name='" + fileName + "']"));
		if (uploadedfile) {
			System.out.println("文件上传成功");
		} else {
			System.out.println("文件上传失败");
			Assert.fail("文件上传失败");
		}

	}

	/**
	 * 删除文件1.txt，需要依赖uploadCommon（）
	 */
	public void deleteFile() {
		Navigate.toMyFile(driver);
		driver.findElement(By.xpath("//ul[@data-name='1.txt']//input")).click();
		driver.findElement(By.id("delete")).click();
		driver.findElement(By.className("btn_primary_large")).click();
		Utils.waitFor(3000);
		Boolean folderName = Utils.isExists(driver, By.xpath("//a[@data-name='1.txt' and @title='1.txt']"));
		if (!folderName) {
			System.out.println("删除文件成功");
		} else {
			System.out.println("删除文件失败");
			Assert.fail("删除文件失败");
		}
	}

	public void cloudShare() {
		Navigate.toMyFile(driver);
		uploadCommon("2.txt");
		driver.findElement(By.xpath("//ul[@data-name='2.txt']/li[1]/input")).click();
		WebElement element = driver.findElement(By.id("share"));
		element.click();
		driver.findElement(By.id("cloudShare")).click();
		driver.findElement(By.id("s2id_autogen2")).sendKeys("jenny");
		Utils.waitElementShow(driver, By.xpath("//div[@title='张小一(jenny)']"), 5);
		driver.findElement(By.xpath("//div[@title='张小一(jenny)']")).sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("//button[text()='确定分享']")).click();
		Navigate.toMyShares(driver);
		driver.findElement(By.xpath("//span[text()='已发分享']")).click();

		Boolean sendShare = Utils.isExists(driver, By.xpath("//a[@data-name='2.txt']"));
		if (sendShare) {
			System.out.println("云盘分享文件成功");
		} else {
			System.out.println("云盘分享文件失败");
			Assert.fail("云盘分享文件失败");
		}
	}

	public void linkShare() {
		Navigate.toMyFile(driver);
		uploadCommon("3.txt");
		driver.findElement(By.xpath("//ul[@data-name='3.txt']/li[1]/input")).click();
		WebElement element = driver.findElement(By.id("share"));
		element.click();
		driver.findElement(By.id("linkShare")).click();
		driver.findElement(By.xpath("//button[text()='创建分享链接']")).click();
		driver.findElement(By.xpath("//button[text()='关闭']")).click();
		Navigate.toMyShares(driver);
		Boolean sentShare = Utils.isExists(driver, By.xpath("//a[@data-name='3.txt']"));
		if (sentShare) {
			System.out.println("链接分享成功");
		} else {
			System.out.println("链接分享失败");
			Assert.fail("链接分享失败");
		}
	}

	public void openLinkShared() {
		Navigate.toMyShares(driver);
		driver.findElement(By.xpath("//span[contains(text(),'已发分享')]")).click();
		Utils.waitFor(5000);
		driver.findElement(By.xpath("//a[@title='3.txt' and @data-name='3.txt']/ancestor::li[@class='filename_noico']/following-sibling::li[2]/span/div/embed")).click();
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
		boolean exist = Utils.isExists(driver, By.xpath("//a[text()='3.txt' and @code]"));
		if (!exist) {
			 Assert.fail("打开分享链接不成功"); 
		}
	}
	
	public void createTeam() {
		Navigate.toTeamFile(driver);
		Utils.waitElementShow(driver, By.id("btn_newTeam"), 10);
		driver.findElement(By.id("btn_newTeam")).click();
		driver.findElement(By.name("new_team_name")).sendKeys("myTeam");
		driver.findElement(By.xpath("//i[@class='ico_location']")).click();
		driver.findElement(By.id("userGroupTree_1_span")).click();
		driver.findElement(By.xpath("//button[text()='下一步']")).click();
		String path = "//input[contains(@id,'s2id_autogen')]";
		Utils.waitElementShow(driver, By.xpath(path), 10);
		driver.findElement(By.xpath(path)).click();
		Utils.waitElementShow(driver, By.xpath("//input[contains(@id,'s2id_autogen')]"), 2);
		driver.findElement(By.xpath(path)).click();
		driver.findElement(By.xpath(path)).sendKeys("jenny01");
		Utils.waitFor(3000);
		driver.findElement(By.xpath("//div[@title='张小二(jenny01)']")).click();
		driver.findElement(By.xpath("//button[text()='创建团队']")).click();
		Boolean myteam = Utils.isExists(driver, By.xpath("//span[text()='myTeam']"));
		if(myteam){
			System.out.println("创建团队成功");
		}else{
			System.out.println("创建团队失败");
			Assert.fail("创建团队失败");			
		}
	}
	
	public void copyToMyFiles() {
		Navigate.toMyFile(driver);
		uploadCommon("4.txt");
		driver.findElement(By.xpath("//ul[@data-name='4.txt']/child::*/input")).click();
		driver.findElement(By.id("copy")).click();
		driver.findElement(By.id("copy-fileTree-holder_1_span")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Utils.waitFor(2000);
		Boolean copyFileExists = Utils.isExists(driver, By.xpath("//ul[@data-name='4(1).txt']/child::*/input"));
		if (!copyFileExists) {
			System.out.println("复制到个人文件失败");
			Assert.fail("复制到个人文件失败");
		}else{
			System.out.println("复制到个人文件成功");
		}
	}
	
	
	public void copyToTeamFile() {
		Navigate.toMyFile(driver);
		uploadCommon("5.txt");
		driver.findElement(By.xpath("//ul[@data-name='5.txt']/child::*/input")).click();
		driver.findElement(By.id("copy")).click();
		driver.findElement(By.id("copy-teamTree-holder_1_switch")).click(); // +号
		driver.findElement(By.xpath("//span[text()='myTeam']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Navigate.toTeamFile(driver);		
		Boolean file = Utils.isExists(driver, By.xpath("//a[@data-name='5.txt']"));
		if (!file) {
			Assert.fail("复制到团队文件失败");
			System.out.println("复制到团队文件失败");
		}else{
			System.out.println("复制到团队文件成功");
		}
	}
	
	public void copyToCompanyFile() {
		Navigate.toMyFile(driver);
		uploadCommon("6.docx");
		driver.findElement(By.xpath("//ul[@data-name='6.docx']/child::*/input")).click();
		driver.findElement(By.id("copy")).click();
		driver.findElement(By.id("copy-compTree-holder_1_switch")).click();
		driver.findElement(By.xpath("//span[text()='companyTeam']")).click();
		driver.findElement(By.xpath("//span[text()='确定']")).click();
		Navigate.toCompanyFile(driver);
		Boolean file = Utils.isExists(driver, By.xpath("//a[@data-name='6.docx']"));
		if (!file) {
			Assert.fail("复制到公司文件失败");
			System.out.println("复制到公司文件失败");
		}else{
			System.out.println("复制到公司文件成功");
		}			
	}
	
	@Parameters("favoritesUploadFileName")
	public void favorites(String filename) {
		Navigate.toMyFile(driver);
		uploadCommon("7.pdf");
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
	
}
