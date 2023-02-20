package com.buzzerbeater.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;

public class Login extends Page{

	public Login(WebDriver driver) {
		super(driver);
	}

	@FindBy(className = "button-wrap-login")
	WebElement signInLandingPage;

	@FindBy(id = "ctl00_pnlLoginBox")
	WebElement loginPanel;
	
	@FindBy(id = "txtLoginName")
	WebElement usernameField;
	@FindBy(id = "txtLoginUserName")
	WebElement txtLoginUserName;
	@FindBy(id = "cphContent_txtUserName")
	WebElement cphContent_txtUserName;
	
	@FindBy(id = "txtPassword")
	WebElement passwordField;
	@FindBy(id = "txtLoginPassword")
	WebElement txtLoginPassword;
	@FindBy(id = "cphContent_txtPassword")
	WebElement cphContent_txtPassword;
	
	@FindBy(id = "btnLogin")
	WebElement loginButton;
	@FindBy(id = "cphContent_btnLoginUser")
	WebElement cphContent_btnLoginUser;

	public Page login(String user, String pwd) throws IOException {
		return this.login(user, pwd, Page.class);
	}
	
	public <T extends Page> T login(String user, String pwd, Class<T> p) throws IOException {
		
		driver.get(baseUrl);

		/*
		First decide which login form is displayed
		and assign web elements to the proper ones
		 */

		WebElement usernameElement = null, passwordElement = null, signInButtonElement = null;
		if(signInLandingPage.isDisplayed()) {
			// landing page displayed, we need to click on SignIn button for modal to show up
			signInLandingPage.click();
			usernameElement = this.txtLoginUserName;
			passwordElement = this.txtLoginPassword;
			signInButtonElement = this.loginButton;
		}
		else if(usernameField.isDisplayed()) {
			usernameElement = this.usernameField;
			passwordElement = this.passwordField;
			signInButtonElement = this.loginButton;
		} else if(cphContent_txtUserName.isDisplayed()) {
			usernameElement = this.cphContent_txtUserName;
			passwordElement = this.cphContent_txtPassword;
			signInButtonElement = this.cphContent_btnLoginUser;
		} else {
			throw new IOException("Login page elements can't be recognized using any of known elements");
		}

		usernameElement.clear();
		usernameElement.sendKeys(user);

		passwordElement.clear();
		passwordElement.sendKeys(pwd);

		signInButtonElement.click();
		
		username=user;
		password=pwd;
		
		return PageFactory.initElements(driver, p);
		
	}
}
