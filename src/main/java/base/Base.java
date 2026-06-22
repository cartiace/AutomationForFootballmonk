package base;

import java.util.Properties;
import java.io.*;
import org.openqa.selenium.*;

public class Base {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getdriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driver1) {
        driver.set(driver1);
    }

    private Properties loadProperties() throws Exception {
        Properties prop = new Properties();
        InputStream input = Base.class.getClassLoader().getResourceAsStream("data.properties");
        if (input == null) {
            throw new FileNotFoundException("data.properties not found in classpath");
        }
        prop.load(input);
        return prop;
    }

    public String getUrl() throws Exception {
        return loadProperties().getProperty("url");
    }

    public String getbarcelonaUrl() throws Exception {
        return loadProperties().getProperty("barcelonaUrl");
    }

    public String getBrowser() throws Exception {
        return loadProperties().getProperty("browser");
    }
}
