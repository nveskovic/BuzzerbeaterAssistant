package com.buzzerbeater.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Login extends Page{

	public Login(WebDriver driver) {
		super(driver);
	}

	@FindBy(id = "ctl00_pnlLoginBox")
	WebElement loginPanel;
	
	@FindBy(id = "txtLoginName")
	WebElement usernameField;
	
	@FindBy(id = "txtPassword")
	WebElement passwordField;
	
	@FindBy(id = "btnLogin")
	WebElement loginButton;

	public Page login(String user, String pwd){
		return this.login(user, pwd, Page.class);
	}
	
	public <T extends Page> T login(String user, String pwd, Class<T> p) {
		
		driver.get(baseUrl);
		
		this.usernameField.clear();
		this.usernameField.sendKeys(user);
		
		this.passwordField.clear();
		this.passwordField.sendKeys(pwd);
		
		this.loginButton.click();
		
		username=user;
		password=pwd;
		
		return PageFactory.initElements(driver, p);
		
	}
}
