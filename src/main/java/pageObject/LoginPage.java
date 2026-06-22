package pageObject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class LoginPage {

    WebDriver driver;

    // WooCommerce login page at /my-account/
    By myAccountLink     = By.cssSelector("a[href*='my-account']");
    By usernameField     = By.id("username");
    By passwordField     = By.id("password");
    By loginButton       = By.name("login");
    // After login, WooCommerce shows "Hello, <name>" in the My Account nav
    By loggedInGreeting  = By.cssSelector(".woocommerce-MyAccount-navigation, .wc-item-meta, .logged-in-as, a[href*='my-account'][class*='account']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getMyAccountLink() {
        return driver.findElement(myAccountLink);
    }

    public WebElement getUsernameField() {
        return driver.findElement(usernameField);
    }

    public WebElement getPasswordField() {
        return driver.findElement(passwordField);
    }

    public WebElement getLoginButton() {
        return driver.findElement(loginButton);
    }

    // Navigate directly to my-account login page
    public void navigateToLoginPage() {
        driver.get("https://footballmonk.in/my-account/");
    }

    // Confirms login succeeded: WooCommerce shows account navigation only when logged in
    public boolean isLoggedIn() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".woocommerce-MyAccount-navigation")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}