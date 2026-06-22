package hooks;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import base.Base;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;

public class ScenerioHook extends Base {

    RemoteWebDriver remoteDriver;

    @Before
    public void setup() {
        try {
            String browser = this.getBrowser();

            if (browser.equals("chrome")) {
                ChromeOptions options = new ChromeOptions();
                options.setCapability("platformName", "Windows");
                remoteDriver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            } else if (browser.equals("edge")) {
                EdgeOptions options = new EdgeOptions();
                options.setCapability("platformName", "Windows");
                remoteDriver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
            }

            setDriver(remoteDriver);
            getdriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            getdriver().manage().window().maximize();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        // Screenshot on FAILURE for debugging (bug fix: was incorrectly on pass)
        if (scenario.isFailed()) {
            byte[] screenshot =
                ((TakesScreenshot) getdriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(
                "Failure Screenshot - " + scenario.getName(),
                new ByteArrayInputStream(screenshot)
            );
        }
        getdriver().quit();
    }
}
