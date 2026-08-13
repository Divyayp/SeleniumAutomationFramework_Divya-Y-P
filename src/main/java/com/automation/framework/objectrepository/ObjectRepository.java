package com.automation.framework.objectrepository;

import java.util.HashMap;
import org.openqa.selenium.By;

public class ObjectRepository {

    private static HashMap<String, By> locators = new HashMap<>();


    static {

        locators.put("username",
                By.id("username"));

        locators.put("password",
                By.id("password"));

        locators.put("loginButton",
                By.id("submit"));

    }


    public static By getLocator(String elementName) {

        return locators.get(elementName);

    }

}