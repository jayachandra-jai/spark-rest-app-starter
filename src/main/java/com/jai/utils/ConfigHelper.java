package com.jai.utils;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;

public class ConfigHelper {
	private static PropertiesConfiguration config;
	
	static {
        if (config == null) {
            try {
            	config = new PropertiesConfiguration();
            	config.setDelimiterParsingDisabled(true);
            	config.setFileName("config/config.properties");
            	config.load();
            	config.setReloadingStrategy(new FileChangedReloadingStrategy());            	
            	config.setAutoSave(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public static String getSpecificValue(String key) {
        return config.getString(key);
    }
    public static String getSpecificValue(String key, String defaultValue) {
        return StringUtils.defaultIfEmpty(config.getString(key),defaultValue);
    }
}
