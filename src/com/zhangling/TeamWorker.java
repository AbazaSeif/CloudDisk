package com.zhangling;

import org.openqa.selenium.WebDriver;

public class TeamWorker {
	
	public static void teamFileToMyFile(WebDriver driver,String teamName,String file){
		Navigate.toTeamFile(driver);
		Navigate.clickTeam(driver, teamName);
		Worker.uploadCommon(file);
	}
	
}
