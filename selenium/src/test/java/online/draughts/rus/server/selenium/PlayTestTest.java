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
public class PlayTestTest {

  private static String siteUrl = "http://localhost:8080/rus";
  private final String gwtDebug = "gwt-debug-";
  private WebDriver vkDriver;
  private WebDriver fbDriver;

  @Test
  public void testPlay() {
    Runnable vkRunnable = new Runnable() {
      @Override
      public void run() {
        vkDriver = connectToServerVk();
      }
    };
    Runnable fbRunnable = new Runnable() {
      @Override
      public void run() {
        fbDriver = connectToServerFb();
      }
    };

    new Thread(vkRunnable).start();
    Thread fbThread = new Thread(fbRunnable);
    fbThread.start();
    try {
      fbThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private WebDriver connectToServerVk() {
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

    new WebDriverWait(driver, 5).until(new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        return webDriver.findElement(By.id(gwtDebug + "connectToServer")).isDisplayed();
      }
    });

    WebElement connectToServer = driver.findElement(By.id(gwtDebug + "connectToServer"));
    connectToServer.click();

    return driver;
  }

  private WebDriver connectToServerFb() {
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

    new WebDriverWait(driver, 5).until(new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        return webDriver.findElement(By.id(gwtDebug + "connectToServer")).isDisplayed();
      }
    });

    WebElement connectToServer = driver.findElement(By.id(gwtDebug + "connectToServer"));
    connectToServer.click();

    return driver;
  }
}
