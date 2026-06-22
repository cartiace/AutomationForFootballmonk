package stepDefinations;


import org.apache.logging.log4j.LogManager;
import org.testng.Assert;

import base.Base;
import io.cucumber.java.en.*;
import pageObject.*;

public class A_LoginStep extends Base{
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(A_LoginStep.class);
	LoginPage loginPage;
	PriceFilterPage pricefilterpage;
	
	
	
	@Given("user is home page")
	public void user_is_home_page() {
	    // Write code here that turns the phrase above into concrete actions
	    try {
	    	logger.debug("Reached Method");
	    	getdriver().get(this.getUrl());
	    	logger.debug("URL Launched");
	    	getdriver().manage().window().maximize();
	    	logger.debug("Window Maximized");
	    }catch(Exception ex) {
	    	
	    }
	}
	@When("clicks login")
	public void clicks_login() {
	    // Write code here that turns the phrase above into concrete actions
	   try {
		   
		   loginPage = new LoginPage(getdriver());
		   logger.debug("Login Page");
		   loginPage.getSignUpandInBtn().click();
		   logger.debug("Sign UP");
		   loginPage.getSignInBtn().click();
		   logger.debug("Sign IN");
	   }catch(Exception ex) {
		   logger.error("Exception occured : "+ex.getMessage());
	   }
	}
	@When("user enters email {string} and password {string}")
	public void user_enters_email_and_password(String email, String password) {
	    // Write code here that turns the phrase above into concrete actions
	    try {
	  
				loginPage.getEmail().sendKeys(email);
				logger.debug("Email Entered");
	    			loginPage.getPassword().sendKeys(password);
	    			logger.debug("Password Entered");
	    			Thread.sleep(2000);
	    }catch(Exception ex) {
	    	
	    }
	}
	@Then("user is successfully logged in")
	public void user_is_successfully_logged_in() {
	    // Write code here that turns the phrase above into concrete actions
	    try {
	    		loginPage.getSubmitBtn().click();
	    		logger.debug("Logged IN");
	    		Thread.sleep(2000);
	    		Assert.assertTrue(loginPage.getLoginText().getText().contains("Hi"), "Login Failure");
	    }catch(Exception ex) {
	    	
	    }
	}

}
