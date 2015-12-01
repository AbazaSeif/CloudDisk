package com.zhangling;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Navigate {
	
	public static void toMyFile(WebDriver driver){		
		driver.findElement(By.xpath("//i[@class='headermenu_ico_myfile']")).click();		
	}
	
	public static void toTeamFile(WebDriver driver){		
		driver.findElement(By.xpath("//i[@class='headermenu_ico_team']")).click();		
	}
	
	public static void toCompanyFile(WebDriver driver){		
		driver.findElement(By.xpath("//i[@class='headermenu_ico_companyfile']")).click();		
	}
	
	public static void toMyShares(WebDriver driver){		
		driver.findElement(By.xpath("//i[@class='headermenu_ico_share']")).click();		
	}
	
	public static void clickTeam(WebDriver driver,String teamName){
		driver.findElement(By.xpath("//span[text()='"+teamName+"']")).click();
	}

}
