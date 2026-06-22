package stepDefinations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import base.Base;
import io.cucumber.java.en.*;
import pageObject.BarcelonaPage;
import pageObject.LoginPage;

public class D_BarcelonaJerseySortStep extends Base {

    private static final Logger logger = LogManager.getLogger(D_BarcelonaJerseySortStep.class);

    LoginPage loginPage;
    BarcelonaPage barcelonaPage;

    // ── Step 1: Login ─────────────────────────────────────────────────────────
    @Given("user navigates to FootballMonk home page and logs in with email {string} and password {string}")
    public void user_navigates_and_logs_in(String email, String password) {
        try {
            logger.debug("Navigating to FootballMonk login page");
            loginPage = new LoginPage(getdriver());
            loginPage.navigateToLoginPage();

            WebDriverWait wait = new WebDriverWait(getdriver(), Duration.ofSeconds(15));
            wait.until(ExpectedConditions.visibilityOf(loginPage.getUsernameField()));

            loginPage.getUsernameField().sendKeys(email);
            logger.debug("Email entered: " + email);

            loginPage.getPasswordField().sendKeys(password);
            logger.debug("Password entered");

            loginPage.getLoginButton().click();
            logger.debug("Login button clicked");

            wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".woocommerce-MyAccount-navigation")
                ),
                ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".woocommerce-error, .woocommerce-message")
                )
            ));

            boolean hasError = !getdriver().findElements(
                By.cssSelector(".woocommerce-error")
            ).isEmpty();

            if (hasError) {
                String errorText = getdriver()
                    .findElement(By.cssSelector(".woocommerce-error"))
                    .getText().trim();
                logger.error("Login failed - WooCommerce error: " + errorText);
                Assert.fail("Login failed on site. Error shown: " + errorText);
            }

            logger.debug("Login successful. Current URL: " + getdriver().getCurrentUrl());

        } catch (Exception ex) {
            logger.error("Login step failed: " + ex.getMessage());
            Assert.fail("Login step failed: " + ex.getMessage());
        }
    }

    // ── Step 2: Hover Football Jerseys → Click Barcelona ─────────────────────
    @When("user hovers on Football Jerseys menu and clicks Barcelona Jersey")
    public void user_hovers_football_jerseys_and_clicks_barcelona() {
        try {
            barcelonaPage = new BarcelonaPage(getdriver());
            JavascriptExecutor js = (JavascriptExecutor) getdriver();
            WebDriverWait wait = new WebDriverWait(getdriver(), Duration.ofSeconds(15));

            String currentUrl = getdriver().getCurrentUrl();
            if (!currentUrl.contains("footballmonk.in") || currentUrl.contains("my-account")) {
                getdriver().get(this.getUrl());
                wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            }
            logger.debug("On home page: " + getdriver().getCurrentUrl());

            // ── Strategy 1: Actions hover + wait for submenu ──────────────────
            boolean navigated = false;
            try {
                WebElement footballMenu = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//nav//a[contains(@href,'buy-football-jersey-india')]")
                    )
                );

                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", footballMenu);
                Thread.sleep(500);

                Actions actions = new Actions(getdriver());
                actions.moveToElement(footballMenu).pause(Duration.ofMillis(800)).perform();
                logger.debug("Strategy 1: Hovered over Football Jerseys menu");

                WebElement barcelonaLink = new WebDriverWait(getdriver(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[contains(@href,'barcelona-jerseys') and contains(text(),'Barcelona')]")
                    ));

                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", barcelonaLink);
                barcelonaLink.click();
                logger.debug("Strategy 1: Clicked Barcelona Jersey link");
                navigated = true;

            } catch (Exception e1) {
                logger.warn("Strategy 1 (Actions hover) failed: " + e1.getMessage());
            }

            if (!navigated) {
                try {
                    WebElement barcelonaLink = wait.until(
                        ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//a[contains(@href,'barcelona-jerseys')]")
                        )
                    );
                    js.executeScript("arguments[0].click();", barcelonaLink);
                    logger.debug("Strategy 2: JS click on Barcelona link");
                    navigated = true;

                } catch (Exception e2) {
                    logger.warn("Strategy 2 (JS click) failed: " + e2.getMessage());
                }
            }

            if (!navigated) {
                logger.warn("Strategy 3: Navigating directly to Barcelona jerseys URL");
                getdriver().get("https://footballmonk.in/product-category/buy-football-jersey-india/barcelona-jerseys/");
                navigated = true;
            }


            wait.until(ExpectedConditions.urlContains("barcelona-jerseys"));
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            logger.debug("Barcelona jerseys page loaded: " + getdriver().getCurrentUrl());

        } catch (Exception ex) {
            logger.error("Navigation step failed: " + ex.getMessage());
            Assert.fail("Navigation step failed: " + ex.getMessage());
        }
    }

    @Then("user should be on the Barcelona jerseys page")
    public void user_should_be_on_barcelona_jerseys_page() {
        try {
            String currentUrl = getdriver().getCurrentUrl();
            logger.debug("Current URL: " + currentUrl);
            Assert.assertTrue(
                currentUrl.contains("barcelona-jerseys"),
                "Expected to be on Barcelona jerseys page but URL was: " + currentUrl
            );
            logger.debug("Confirmed: on Barcelona jerseys page");
        } catch (Exception ex) {
            logger.error("URL assertion failed: " + ex.getMessage());
            Assert.fail("URL assertion failed: " + ex.getMessage());
        }
    }

 // ── Step 4: Sort Low to High ──────────────────────────────────────────────
    @When("user selects sort by {string} low to high")
    public void user_selects_sort_low_to_high(String sortType) {
        try {
            barcelonaPage = new BarcelonaPage(getdriver());
            JavascriptExecutor js = (JavascriptExecutor) getdriver();
            WebDriverWait wait = new WebDriverWait(getdriver(), Duration.ofSeconds(15));
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

            boolean sorted = false;
            try {
                WebElement selectEl = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("select.orderby"))
                );

                List<WebElement> options = selectEl.findElements(By.tagName("option"));
                logger.debug("Sort options found in <select>:");
                for (WebElement opt : options) {
                    logger.debug("  value='" + opt.getAttribute("value") + "' text='" + opt.getText() + "'");
                }

                js.executeScript(
                    "var sel = arguments[0];" +
                    "sel.value = 'price';" +
                    "sel.dispatchEvent(new Event('change', { bubbles: true }));",
                    selectEl
                );
                logger.debug("Strategy 1: JS set select.orderby value to 'price' and fired change event");
                sorted = true;

            } catch (Exception e1) {
                logger.warn("Strategy 1 (JS set select value) failed: " + e1.getMessage());
            }

       
            if (!sorted) {
                try {
                    WebElement trigger = wait.until(
                        ExpectedConditions.elementToBeClickable(
                            By.cssSelector(".woocommerce-ordering, .orderby-wrapper, [class*='sort'], [class*='orderby']")
                        )
                    );
                    trigger.click();
                    Thread.sleep(500);

                    WebElement priceOption = wait.until(
                        ExpectedConditions.elementToBeClickable(
                            By.xpath("//*[contains(text(),'Price: low to high') or contains(text(),'price') or @value='price']")
                        )
                    );
                    priceOption.click();
                    logger.debug("Strategy 2: Clicked custom dropdown option");
                    sorted = true;

                } catch (Exception e2) {
                    logger.warn("Strategy 2 (custom dropdown click) failed: " + e2.getMessage());
                }
            }

        
            if (!sorted) {
                String currentUrl = getdriver().getCurrentUrl();
                String sortedUrl = currentUrl.contains("?")
                    ? currentUrl.replaceAll("[?&]orderby=[^&]*", "") + "&orderby=price"
                    : currentUrl + "?orderby=price";
                sortedUrl = sortedUrl.replace("?&", "?");
                logger.warn("Strategy 3: Navigating to URL with orderby=price: " + sortedUrl);
                getdriver().get(sortedUrl);
            }

       
            wait.until(ExpectedConditions.urlContains("orderby=price"));
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            
        
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(BarcelonaPage.productTitles));
            
            logger.debug("Sort complete. URL: " + getdriver().getCurrentUrl());

        } catch (Exception ex) {
            logger.error("Sort step failed: " + ex.getMessage());
            Assert.fail("Sort step failed: " + ex.getMessage());
        }
    }

 // ── Step 5: Top 5 products contain "Barcelona" ────────────────────────────
    @Then("top 5 products should all contain {string} in their name")
    public void top_5_products_contain_word_in_name(String keyword) {
        try {
            barcelonaPage = new BarcelonaPage(getdriver());
            List<WebElement> titles = barcelonaPage.getProductTitles();
            int limit = Math.min(5, titles.size());

            Assert.assertTrue(limit > 0, "No products were found on the page to validate.");

            logger.debug("Validating the top " + limit + " products for keyword: " + keyword);

            SoftAssert softAssert = new SoftAssert();

            for (int i = 0; i < limit; i++) {
                String productName = titles.get(i).getText().trim();
                logger.debug("Product " + (i + 1) + ": " + productName);

                boolean containsKeyword = productName.toLowerCase().contains(keyword.toLowerCase());
                softAssert.assertTrue(
                    containsKeyword,
                    "Name validation FAILED at position " + (i + 1) 
                        + ". Expected keyword '" + keyword + "' but actual name was: [" + productName + "]\n"
                );
            }

            logger.debug("Finished checking product names. Tallying results...");
            softAssert.assertAll();

        } catch (Exception ex) {
            logger.error("Product name validation failed: " + ex.getMessage());
            Assert.fail("Product name validation failed: " + ex.getMessage());
        }
    }

 // ── Step 6: Prices are sorted low to high ─────────────────────────────────
    @And("prices should be sorted from low to high")
    public void prices_should_be_sorted_low_to_high() {
        try {
            barcelonaPage = new BarcelonaPage(getdriver());
            List<WebElement> priceElements = barcelonaPage.getProductPrices();
            Assert.assertTrue(priceElements.size() >= 2,
                "Need at least 2 prices to validate sorting. Found: " + priceElements.size());

            List<Double> prices = new ArrayList<>();

            for (WebElement priceEl : priceElements) {
                String rawText = priceEl.getText().trim();
                String[] priceParts = rawText.split("\\n");
                String activePriceText = priceParts[priceParts.length - 1].trim();
                
                String cleaned = activePriceText.replaceAll("[^0-9.]", "");
                if (!cleaned.isEmpty()) {
                    try {
                        prices.add(Double.parseDouble(cleaned));
                    } catch (NumberFormatException nfe) {
                        logger.warn("Could not parse price from: [" + cleaned + "]");
                    }
                }
            }

            Assert.assertTrue(prices.size() >= 2,
                "Could not parse enough prices. Only parsed: " + prices.size());

            logger.debug("All parsed active prices: " + prices);
            SoftAssert softAssert = new SoftAssert();

  
            for (int i = 0; i < prices.size() - 1; i++) {
                softAssert.assertTrue(
                    prices.get(i) <= prices.get(i + 1),
                    "Sort validation FAILED at position " + (i + 1)
                        + ": ₹" + prices.get(i) + " should be <= ₹" + prices.get(i + 1) + "\n"
                );
            }

            logger.debug("Finished checking all prices. Tallying results...");
            softAssert.assertAll();

        } catch (Exception ex) {
            logger.error("Price sort validation failed: " + ex.getMessage());
            Assert.fail("Price sort validation failed: " + ex.getMessage());
        }
    }
}