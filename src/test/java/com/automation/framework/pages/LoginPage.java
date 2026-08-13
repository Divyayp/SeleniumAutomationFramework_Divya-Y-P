package com.automation.framework.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class LoginPage {

    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    @FindBy(xpath = "//input[@id='username']")
    WebElement username;


    @FindBy(xpath = "//input[@id='password']")
    WebElement password;


    @FindBy(xpath = "//button[@id='submit']")
    WebElement loginButton;



    public void enterUsername(String user) {
        username.sendKeys(user);
    }


    public void enterPassword(String pass) {
        password.sendKeys(pass);
    }


    public void clickLogin() {
        loginButton.click();
    }

}