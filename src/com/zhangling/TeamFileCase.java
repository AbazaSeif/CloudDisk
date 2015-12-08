package com.zhangling;

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
	
	
	
	
	
}
