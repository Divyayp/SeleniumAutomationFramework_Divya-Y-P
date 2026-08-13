package com.automation.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ElementUtil {

    private WebDriver driver;

    public ElementUtil(WebDriver driver) {
        this.driver = driver;
    }


    public void enterText(String locator, String value) {

        driver.findElement(getBy(locator))
              .sendKeys(value);

    }


    public void click(String locator) {

        driver.findElement(getBy(locator))
              .click();

    }


    private By getBy(String locator) {

        if(locator.startsWith("//")) {

            return By.xpath(locator);

        } 
        else {

            return By.id(locator);

        }

    }

}