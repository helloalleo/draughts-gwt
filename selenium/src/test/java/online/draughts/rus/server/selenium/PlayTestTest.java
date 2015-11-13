package online.draughts.rus.server.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.safari.SafariDriver;
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
  private WebDriver vkFFDriver;
  private WebDriver fbSFDriver;

  @Before
  public void setUp() {
    ProfilesIni profileObj = new ProfilesIni();
    FirefoxProfile defaultFFProfile = profileObj.getProfile("0qo4ubi7");
    vkFFDriver = new FirefoxDriver(defaultFFProfile);
    fbSFDriver = new SafariDriver();
  }

  @After
  public void tearDown() {
//    vkFFDriver.quit();
//    fbSFDriver.quit();
  }

  @Test
  public void testPlay() {
    Runnable vkRunnable = new Runnable() {
      @Override
      public void run() {
        logginInVk();
        connectToServer(vkFFDriver);
      }
    };
    Runnable fbRunnable = new Runnable() {
      @Override
      public void run() {
        loginInFb();
        connectToServer(fbSFDriver);
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

  private void connectToServer(WebDriver driver) {
    new WebDriverWait(driver, 5).until(new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        return webDriver.findElement(By.id(gwtDebug + "connectToServer")).isDisplayed();
      }
    });

    WebElement connectToServer = driver.findElement(By.id(gwtDebug + "connectToServer"));
    connectToServer.click();
  }

  private void logginInVk() {
    // Create a new instance of the Firefox driver
    // Notice that the remainder of the code relies on the interface,
    // not the implementation.
    // And now use this to visit Google
    vkFFDriver.get(siteUrl + "/vkOAuth");

    // Check the title of the page
    assertEquals("ВКонтакте | Вход", vkFFDriver.getTitle());

    WebElement emailField = vkFFDriver.findElement(By.name("email"));
    emailField.sendKeys("aleksey.p10n@gmail.com");
    WebElement passField = vkFFDriver.findElement(By.name("pass"));
    passField.sendKeys("krAj8oT9Ez0binK");
    WebElement submitButton = vkFFDriver.findElement(By.id("install_allow"));
    submitButton.click();
    // Google's search is rendered dynamically with JavaScript.
    // Wait for the page to load, timeout after 10 seconds
    (new WebDriverWait(vkFFDriver, 10)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getCurrentUrl().endsWith("#!home");
      }
    });

    // Should see: "cheese! - Google Search"
    assertEquals("Шашки онлайн", vkFFDriver.getTitle());
  }

  private void loginInFb() {
    // Create a new instance of the Firefox driver
    // Notice that the remainder of the code relies on the interface,
    // not the implementation.
    // And now use this to visit Google
    fbSFDriver.get(siteUrl + "/fbOAuth");

    // Check the title of the page
//    assertEquals("Войдите на Facebook | Facebook", fbSFDriver.getTitle());
//
//    WebElement emailField = fbSFDriver.findElement(By.id("email"));
//    emailField.sendKeys("lynx.p9@gmail.com");
//    WebElement passField = fbSFDriver.findElement(By.id("pass"));
//    passField.sendKeys("123#@!ewq");
//    WebElement submitButton = fbSFDriver.findElement(By.id("u_0_2"));
//    submitButton.click();
    // Google's search is rendered dynamically with JavaScript.
    // Wait for the page to load, timeout after 10 seconds
    (new WebDriverWait(fbSFDriver, 10)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getCurrentUrl().endsWith("#!home");
      }
    });

    // Should see: "cheese! - Google Search"
    assertEquals("Шашки онлайн", fbSFDriver.getTitle());
  }
}
