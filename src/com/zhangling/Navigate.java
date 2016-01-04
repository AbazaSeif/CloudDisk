package com.zhangling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Navigate {
	
	public static void toMyFile(WebDriver driver){
		By xpath = By.xpath("//i[@class='headermenu_ico_myfile']");
		common(driver,xpath);
	}
	
	public static void toTeamFile(WebDriver driver){		
		By xpath = By.xpath("//i[@class='headermenu_ico_team']");
		common(driver,xpath);
	}
	
	public static  void toCompanyFile(WebDriver driver){		
		By xpath = By.xpath("//i[@class='headermenu_ico_companyfile']");
		common(driver,xpath);
	}
	
	public static  void toMyShares(WebDriver driver){		
		By xpath = By.xpath("//a[@lang='My_Share' and @title='我的分享']");
		common(driver,xpath);
	}
	
	public static  void clickTeam(WebDriver driver,String teamName){
		By xpath = By.xpath("//span[text()='"+teamName+"']");
		common(driver,xpath);
	}
	
	public static void clickCompanyTeam(WebDriver driver,String teamName){
		By xpath = By.xpath("//span[text()='"+teamName+"']");
		common(driver,xpath);
	}
	
	private static void common(WebDriver driver ,By xpath){
		Utils.waitElementShow(driver, xpath, 5000);
		Utils.waitFor(2000);
		driver.findElement(xpath).click();
		Utils.waitFor(3000);
	}

}
