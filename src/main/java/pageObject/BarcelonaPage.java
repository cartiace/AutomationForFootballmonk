package pageObject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class BarcelonaPage {

    WebDriver driver;

    public static By footballJerseysMenu = By.xpath("//nav//a[contains(text(),'Football Jerseys') or contains(@href,'buy-football-jersey-india')]");
    public static By barcelonaMenuItem = By.xpath("//a[contains(text(),'Barcelona Jersey') or contains(@href,'barcelona-jerseys')]");
    public static By sortDropdown = By.cssSelector("select.orderby");
    public static By productTitles = By.cssSelector(
        "li.product .woocommerce-loop-product__title, " +
        "li.product .product-title, " +
        "li.product h2, " +
        "li.product h3, " +
        ".elementor-widget-heading h2, " +
        ".products .product h3 a"
    );


    public static By productPrices = By.cssSelector(".product .price");
    public BarcelonaPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement getFootballJerseysMenu() {
        return driver.findElement(footballJerseysMenu);
    }

    public WebElement getBarcelonaMenuItem() {
        return driver.findElement(barcelonaMenuItem);
    }

    public WebElement getSortDropdown() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(sortDropdown));
        return driver.findElement(sortDropdown);
    }

    public List<WebElement> getProductTitles() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productTitles));
        return driver.findElements(productTitles);
    }

    public List<WebElement> getProductPrices() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productPrices));
        return driver.findElements(productPrices);
    }
}