package com.automation.framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import com.automation.framework.constants.FrameworkConstants;

public class ConfigReader {

    private static Properties properties = new Properties();

    static {

        try {
           
            
            FileInputStream file =
                    new FileInputStream(FrameworkConstants.CONFIG_PATH);       
            
            properties.load(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getProperty(String key) {

        return properties.getProperty(key);

    }

}