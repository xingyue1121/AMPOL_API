package dataProvider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {
	private Properties properties;
	private static ConfigFileReader configReader;

    private ConfigFileReader() {
		BufferedReader reader;
	    	String propertyFilePath = "/Users/PPL/eclipse-workspace/AMPOL/API/configs/configuration.properties";
	        try {
	            reader = new BufferedReader(new FileReader(propertyFilePath));
	            properties = new Properties();
	            try {
	                properties.load(reader);
	                reader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	            throw new RuntimeException("configuration.properties not found at " + propertyFilePath);
	        }		
	}

    public static ConfigFileReader getInstance( ) {
    	if(configReader == null) {
    		configReader = new ConfigFileReader();
    	}
        return configReader;
    }

    public String getBaseUrl() {
        String baseUrl = properties.getProperty("baseUrl");
        if(baseUrl != null) return baseUrl;
        else throw new RuntimeException("baseUrl not specified in the configuration.properties file.");
    }

    public String getXmlFilePath() {
        String xmlFilePath = properties.getProperty("xmlFilePath");
        if(xmlFilePath != null) return xmlFilePath;
        else throw new RuntimeException("xmlFilePath not specified in the configuration.properties file.");
    }
    
    public String getKeyValue() {
        String key_value = properties.getProperty("key_value");
        if(key_value != null) return key_value;
        else throw new RuntimeException("key_value not specified in the configuration.properties file.");
    }
    
    public String getQ_Value() {
        String q_value = properties.getProperty("q_value");
        if(q_value != null) return q_value;
        else throw new RuntimeException("q_value not specified in the configuration.properties file.");
    }
    
    public String getAqi_Value() {
        String aqi_value = properties.getProperty("aqi_value");
        if(aqi_value != null) return aqi_value;
        else throw new RuntimeException("aqi_value not specified in the configuration.properties file.");
    }
}
