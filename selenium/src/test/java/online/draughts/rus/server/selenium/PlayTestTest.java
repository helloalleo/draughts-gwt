package online.draughts.rus.server.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 04.11.15
 * Time: 8:13
 */
@RunWith(JUnit4.class)
public class PlayTestTest {

  private static final String OPPONENT = "WebDriver2";
  private static final long WAIT = 60;
  private static final String siteUrl = "http://localhost:8080/rus";
  private static final String gwtDebug = "gwt-debug-";
  private static final int DESK_CELLS = 8;
  private WebDriver webDriver1;
  private WebDriver webDriver2;

  @Before
  public void setUp() {
    File profDir = new File("/Users/alekspo/Library/Application Support/Firefox/Profiles/p7ws5dyp.WebDriver2");
    FirefoxProfile profile = new FirefoxProfile(profDir);
    webDriver2 = new FirefoxDriver(profile);

    profDir = new File("/Users/alekspo/Library/Application Support/Firefox/Profiles/se57dh3q.WebDriver1");
    profile = new FirefoxProfile(profDir);
    webDriver1 = new FirefoxDriver(profile);
  }

  @After
  public void tearDown() {
//    webDriver1.quit();
//    webDriver2.quit();
  }

  @Test
  public void testPlay() {
    Runnable vkRunnable = new Runnable() {
      @Override
      public void run() {
        loginWebDriver1();
        try {
          sleep(1500);
        } catch (InterruptedException e) {
        }
        connectToServer(webDriver1);
        inviteToPlay(webDriver1);
        acceptInvite(webDriver2);
        play();
      }
    };
    Runnable fbRunnable = new Runnable() {
      @Override
      public void run() {
        loginWebDriver2();
        connectToServer(webDriver2);
      }
    };

    new Thread(fbRunnable).start();
    Thread vkThread = new Thread(vkRunnable);
    vkThread.start();
    try {
      vkThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void play() {
    final By canvasId = By.xpath(String.format("//div[@id='%s']/div", gwtDebug + "draughtsDesk"));
    webDriverWait(webDriver1, WAIT, new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        WebElement canvas = webDriver2.findElement(canvasId);
        return canvas.isDisplayed();
      }
    });
    WebElement canvas = webDriver1.findElement(canvasId);
    Dimension size = canvas.getSize();
    System.out.println(size);
    Actions drawing = new Actions(webDriver1)
        .moveToElement(canvas, 400, size.getHeight() - 110)
//        .moveByOffset(posX(size, 6), posY(size, 5))
        .click();
    System.out.println(posX(size, 6) + ", " + posY(size, 5));
    drawing.perform();
  }

  private int posX(Dimension size, int col) {
    int deltaX = size.getWidth() / DESK_CELLS;
    return deltaX * col;
  }

  private int posY(Dimension size, int row) {
    int deltaY = size.getHeight() / DESK_CELLS;
    return -deltaY * (DESK_CELLS - row);
  }

  private void acceptInvite(WebDriver driver) {
    System.out.println("ACCEPT INVITE");
    final String yesExp = "//button[contains(text(), 'Да')]";
    webDriverWait(driver, WAIT, new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        By yesXPath = By.xpath(yesExp);
        return webDriver.findElement(yesXPath).isDisplayed();
      }
    });
    System.out.println("READY");
    By yesXPath = By.xpath(yesExp);
    WebElement yesButton = driver.findElement(yesXPath);
    System.out.println(yesButton);
    assertNotNull(yesButton);
    yesButton.sendKeys(Keys.RETURN);
  }

  private void inviteToPlay(WebDriver driver) {
    final String opponentPath = String.format("//div[contains(text(), '%s')]", OPPONENT);
    new WebDriverWait(driver, WAIT).until(new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        By opponentXPath = By.xpath(opponentPath);
        return webDriver.findElement(opponentXPath).isDisplayed();
      }
    });
    By opponentXPath = By.xpath(opponentPath);
    WebElement opponentDiv = driver.findElement(opponentXPath);
    assertNotNull(opponentDiv);
    opponentDiv.click();

    final String imgOpponentOnlinePath = String.format("//img[@title = '%s']", OPPONENT + " в сети");
    new WebDriverWait(driver, WAIT).until(new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        By imgOpponent = By.xpath(imgOpponentOnlinePath);
        return webDriver.findElement(imgOpponent).isDisplayed();
      }
    });

    By playButtonId = By.id(gwtDebug + "connectToServer");
    WebElement playButton = driver.findElement(playButtonId);
    assertNotNull(playButton);
    playButton.click();

    final String nextExp = "//button[contains(text(), 'Далее')]";
    webDriverWait(driver, WAIT, new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        By nextXPath = By.xpath(nextExp);
        return webDriver.findElement(nextXPath).isDisplayed();
      }
    });
    By nextXPath = By.xpath(nextExp);
    WebElement nextButton = driver.findElement(nextXPath);
    assertNotNull(nextButton);
    nextButton.sendKeys(Keys.RETURN);
  }

  private void webDriverWait(WebDriver driver, long timeout, ExpectedCondition<Boolean> expectedCondition) {
    new WebDriverWait(driver, timeout).until(expectedCondition);
  }

  private void connectToServer(WebDriver driver) {
    new WebDriverWait(driver, WAIT).until(new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        By connectToServerId = By.id(gwtDebug + "connectToServer");
        return webDriver.findElement(connectToServerId).isDisplayed();
      }
    });

    By connectToServerId = By.id(gwtDebug + "connectToServer");
    WebElement connectToServer = driver.findElement(connectToServerId);
    assertNotNull(connectToServer);
    connectToServer.click();
  }

  private void loginWebDriver1() {
    webDriver1.get(siteUrl + "/fbOAuth");

    (new WebDriverWait(webDriver1, WAIT)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getCurrentUrl().endsWith("#!home");
      }
    });

    assertEquals("Шашки онлайн", webDriver1.getTitle());
  }

  private void loginWebDriver2() {
    webDriver2.get(siteUrl + "/gOAuth");

    (new WebDriverWait(webDriver2, WAIT)).until(new ExpectedCondition<Boolean>() {
      public Boolean apply(WebDriver d) {
        return d.getCurrentUrl().endsWith("#!home");
      }
    });

    assertEquals("Шашки онлайн", webDriver2.getTitle());
  }
}
