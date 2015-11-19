package online.draughts.rus.server.selenium;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 8:13
 */
@RunWith(JUnit4.class)
public class LoginTest {

  private static String siteUrl = "http://localhost:8080/";

  @Test
  public void oauthGoogle() {
    // Create a new instance of the Firefox driver
    // Notice that the remainder of the code relies on the interface,
    // not the implementation.
    WebDriver driver = new FirefoxDriver();

    // And now use this to visit Google
    driver.get(siteUrl + "/gOAuth");

    // Check the title of the page
    assertEquals("Вход – Google Аккаунты", driver.getTitle());

    // Google's search is rendered dynamically with JavaScript.
    // Wait for the page to load, timeout after 10 seconds

    WebElement emailField = driver.findElement(By.id("Email"));
    emailField.sendKeys("aleks.po5r@gmail.com");
    WebElement nextField = driver.findElement(By.id("next"));
    nextField.click();
    new WebDriverWait(driver, 3);
    WebElement passwdField = driver.findElement(By.id("Passwd"));
    passwdField.sendKeys("qwertygoogle321");
    WebElement signInField = driver.findElement(By.id("signIn"));
    signInField.click();
    final WebElement submitAccessField = driver.findElement(By.id("submit_approve_access"));
    new WebDriverWait(driver, 10).until(new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        return submitAccessField.isEnabled();
      }
    });
    submitAccessField.click();
    // Google's search is rendered dynamically with JavaScript.
    // Wait for the page to load, timeout after 10 seconds
    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getCurrentUrl().endsWith("#!home");
      }
    });

    // Should see: "cheese! - Google Search"
    assertEquals("Шашки онлайн", driver.getTitle());

    //Close the browser
    driver.quit();
  }

  @Test
  public void oauthFb() {
    // Create a new instance of the Firefox driver
    // Notice that the remainder of the code relies on the interface,
    // not the implementation.
    WebDriver driver = new FirefoxDriver();

    // And now use this to visit Google
    driver.get(siteUrl + "/fbOAuth");

    // Check the title of the page
    assertEquals("Войдите на Facebook | Facebook", driver.getTitle());

    WebElement emailField = driver.findElement(By.id("email"));
    emailField.sendKeys("lynx.p9@gmail.com");
    WebElement passField = driver.findElement(By.id("pass"));
    passField.sendKeys("123#@!ewq");
    WebElement submitButton = driver.findElement(By.id("u_0_2"));
    submitButton.click();
    // Google's search is rendered dynamically with JavaScript.
    // Wait for the page to load, timeout after 10 seconds
    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getCurrentUrl().endsWith("#!home");
      }
    });

    // Should see: "cheese! - Google Search"
    assertEquals("Шашки онлайн", driver.getTitle());

    //Close the browser
    driver.quit();
  }

  @Test
  public void oauthVk() {
    // Create a new instance of the Firefox driver
    // Notice that the remainder of the code relies on the interface,
    // not the implementation.
    WebDriver driver = new FirefoxDriver();

    // And now use this to visit Google
    driver.get(siteUrl + "/vkOAuth");

    // Check the title of the page
    assertEquals("ВКонтакте | Вход", driver.getTitle());

    WebElement emailField = driver.findElement(By.name("email"));
    emailField.sendKeys("aleksey.p10n@gmail.com");
    WebElement passField = driver.findElement(By.name("pass"));
    passField.sendKeys("krAj8oT9Ez0binK");
    WebElement submitButton = driver.findElement(By.id("install_allow"));
    submitButton.click();
    // Google's search is rendered dynamically with JavaScript.
    // Wait for the page to load, timeout after 10 seconds
    (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getCurrentUrl().endsWith("#!home");
      }
    });

    // Should see: "cheese! - Google Search"
    assertEquals("Шашки онлайн", driver.getTitle());

    //Close the browser
    driver.quit();
  }
}
