package online.draughts.rus.server.selenium;

import online.draughts.rus.server.util.NotationPanel;
import online.draughts.rus.server.util.Square;
import online.draughts.rus.server.util.Stroke;
import online.draughts.rus.server.util.XPathUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class PlayTest {

  private static List<String> alph = new ArrayList<String>() {{
    add("a");
    add("b");
    add("c");
    add("d");
    add("e");
    add("f");
    add("g");
    add("h");
  }};
  private static final String OPPONENT = "WebDriver2";
  private static final long WAIT = 120;
  private static final String siteUrl = "http://localhost:8080/rus";
  private static final String gwtDebug = "gwt-debug-";
  private static final int DESK_CELLS = 8;
  private static final int CANVAS_OFFSET_X = 70;
  private static final int CANVAS_OFFSET_Y = 90;
  private static final String NOTATION1 = "<div id='288'><span>1. </span><a><span id='0' data-order='0' data-number='1' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>g3-h4</span></a><span> </span><a><span id='1' data-order='1' data-number='1' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>d6-e5</span></a><br/><span>2. </span><a><span id='2' data-order='2' data-number='2' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>f2-g3</span></a><span> </span><a><span id='3' data-order='3' data-number='2' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>f6-g5</span></a><br/><span>3. </span><a><span id='4' data-order='4' data-number='3' data-simple='false' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='true' data-stopbeat='false'>h4:f6</span></a><span>:</span><a><span id='5' data-order='5' data-number='3' data-simple='false' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='true'>d4</span></a><span> </span><span> </span><a><span id='6' data-order='6' data-number='3' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>e7-d6</span></a><br/><span>4. </span><a><span id='7' data-order='7' data-number='4' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>d4-c5</span></a><span> </span><a><span id='8' data-order='8' data-number='4' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='true' data-stopbeat='false'>b6:d4</span></a><span>:</span><a><span id='9' data-order='9' data-number='4' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='true' data-stopbeat='false'>f2</span></a><span>:</span><a><span id='10' data-order='10' data-number='4' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='true'>h4</span></a><span><br/></span><span>5. </span><a><span id='11' data-order='11' data-number='5' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>h2-g3</span></a><span> </span><a><span id='12' data-order='12' data-number='5' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='false' data-stopbeat='true'>h4:f2</span></a><br/><span>6. </span><a><span id='13' data-order='13' data-number='6' data-simple='false' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='false' data-stopbeat='true'>e1:g3</span></a><span> </span><a><span id='14' data-order='14' data-number='6' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>d6-e5</span></a><br/><span>7. </span><a><span id='15' data-order='15' data-number='7' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>c3-b4</span></a><span> </span><a><span id='16' data-order='16' data-number='6' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>a7-b6</span></a><br/><span>8. </span><a><span id='17' data-order='17' data-number='8' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>g3-f4</span></a><span> </span><a><span id='18' data-order='18' data-number='7' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='false' data-stopbeat='true'>e5:g3</span></a><br/><span>9. </span><a><span id='19' data-order='19' data-number='9' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>g1-f2</span></a><span> </span><a><span id='20' data-order='20' data-number='8' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='true' data-stopbeat='false'>g3:e1</span></a><span>:</span><a><span id='21' data-order='21' data-number='8' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='true' data-stopbeat='false'>c3</span></a><span>:</span><a><span id='22' data-order='22' data-number='8' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='true'>a5</span></a><span><br/></span><span>10. </span><a><span id='23' data-order='23' data-number='10' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>c1-d2</span></a><span> </span><a><span id='24' data-order='24' data-number='9' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='false' data-stopbeat='true'>a5:e1</span></a><br/><span>11. </span><a><span id='25' data-order='25' data-number='11' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>a3-b4</span></a><span> </span><a><span id='26' data-order='26' data-number='10' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='false' data-stopbeat='true'>e1:a5</span></a><br/><span>12. </span><a><span id='27' data-order='27' data-number='12' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>b2-a3</span></a><span> </span><a><span id='28' data-order='28' data-number='10' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>b6-c5</span></a><br/><span>13. </span><a><span id='29' data-order='29' data-number='13' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>a1-b2</span></a><span> </span><a><span id='30' data-order='30' data-number='11' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>c7-d6</span></a><br/><span>14. </span><a><span id='31' data-order='31' data-number='14' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>a3-b4</span></a><span> </span><a><span id='32' data-order='32' data-number='12' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='true' data-stopbeat='false'>c5:a3</span></a><span>:</span><a><span id='33' data-order='33' data-number='12' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='true'>c1</span></a><span><br/></span></div>";
  private static final String NOTATION_BUG1 = "<div id='307'><span>1. </span><a><span id='0' data-order='0' data-number='1' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>g3-h4</span></a><span> </span><a><span id='1' data-order='1' data-number='1' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>d6-e5</span></a><br/><span>2. </span><a><span id='2' data-order='2' data-number='2' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>f2-g3</span></a><span> </span><a><span id='3' data-order='3' data-number='2' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>f6-g5</span></a><br/><span>3. </span><a><span id='4' data-order='4' data-number='3' data-simple='false' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='true' data-stopbeat='false'>h4:f6</span></a><span>:</span><a><span id='5' data-order='5' data-number='3' data-simple='false' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='true'>d4</span></a><span> </span><span> </span><a><span id='6' data-order='6' data-number='3' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>c7-d6</span></a><br/><span>4. </span><a><span id='7' data-order='7' data-number='4' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>d4-c5</span></a><span> </span><a><span id='8' data-order='8' data-number='4' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='true' data-stopbeat='false'>b6:d4</span></a><span>:</span><a><span id='9' data-order='9' data-number='4' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='true' data-stopbeat='false'>f2</span></a><span>:</span><a><span id='10' data-order='10' data-number='4' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='true'>h4</span></a><br/><span>5. </span><a><span id='11' data-order='11' data-number='5' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>h2-g3</span></a><span> </span><a><span id='12' data-order='12' data-number='5' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='false' data-stopbeat='true'>h4:f2</span></a><br/><span>6. </span><a><span id='13' data-order='13' data-number='6' data-simple='false' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='false' data-stopbeat='true'>e1:g3</span></a><span> </span><a><span id='14' data-order='14' data-number='6' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>d6-c5</span></a><br/><span>7. </span><a><span id='15' data-order='15' data-number='7' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>a3-b4</span></a><span> </span><a><span id='16' data-order='16' data-number='7' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='false' data-stopbeat='true'>c5:a3</span></a><br/><span>8. </span><a><span id='17' data-order='17' data-number='8' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>d2-e3</span></a><span> </span><a><span id='18' data-order='18' data-number='8' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>g7-f6</span></a><br/><span>9. </span><a><span id='19' data-order='19' data-number='9' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>e3-f4</span></a><span> </span><a><span id='20' data-order='20' data-number='9' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>e7-d6</span></a><br/><span>10. </span><a><span id='21' data-order='21' data-number='10' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>c1-d2</span></a><span> </span><a><span id='22' data-order='22' data-number='8' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='true' data-stopbeat='false'>a3:c1</span></a><span>:</span><a><span id='23' data-order='23' data-number='8' data-simple='false' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='true' data-stopbeat='false'>f4</span></a></div>";
  private static final String TEST_NOTATION = NOTATION_BUG1;

  private WebDriver webDriver1;
  private WebDriver webDriver2;

  @Before
  public void setUp() throws ParserConfigurationException, IOException, SAXException {
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
        play1();
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

  private void play1() {
    ((JavascriptExecutor) webDriver1).executeScript("scroll(0,0)");
    ((JavascriptExecutor) webDriver2).executeScript("scroll(0,0)");
    final By canvasId = By.xpath(String.format("//div[@id='%s']/div", gwtDebug + "draughtsDesk"));
    new WebDriverWait(webDriver1, WAIT).until(ExpectedConditions.elementToBeClickable(canvasId));
    List<Stroke> steps = new ArrayList<>();
    String prevStroke = "";

    List<String> strokes = XPathUtils.parseNotationForStrokes(TEST_NOTATION);
    for (String stroke : strokes) {
      Stroke newStroke = createStrokeFromNotation(stroke, "", false);
      if (newStroke == null) {
        newStroke = createStrokeFromNotation(stroke, prevStroke, false);
      }
      steps.add(newStroke);
      prevStroke = stroke;
    }
    WebElement canvas1 = webDriver1.findElement(canvasId);
    WebElement canvas2 = webDriver2.findElement(canvasId);
    Dimension size = canvas1.getSize();
    for (Stroke step : steps) {
      if (step.isFirst()) {
        moveDraughtInWebDriver(webDriver1, canvas1, size, step);
      } else {
        moveDraughtInWebDriver(webDriver2, canvas2, size, step.flip());
      }
    }
  }

  private void moveDraughtInWebDriver(WebDriver driver, WebElement canvas, Dimension size, Stroke stroke) {
    System.out.println(stroke);
    Square start = stroke.getStartSquare();
    Square end = stroke.getEndSquare();
    Actions drawing = new Actions(driver)
        .moveToElement(canvas, CANVAS_OFFSET_X, size.getHeight() - CANVAS_OFFSET_Y)
        .moveByOffset(posX(size, start.getCol()), posY(size, start.getRow()))
        .click()
        .moveToElement(canvas, CANVAS_OFFSET_X, size.getHeight() - CANVAS_OFFSET_Y)
        .moveByOffset(posX(size, end.getCol()), posY(size, end.getRow()))
        .click();
    drawing.perform();
  }

  private int posX(Dimension size, int col) {
    int deltaX = (size.getWidth() - CANVAS_OFFSET_X) / DESK_CELLS;
    return deltaX * col - 10;
  }

  private int posY(Dimension size, int row) {
    int deltaY = (size.getHeight() - CANVAS_OFFSET_Y) / DESK_CELLS;
    System.out.println("ROW " + row);
    System.out.println("DELTA Y " + deltaY);
    System.out.println("POS Y " + (5 - deltaY * (DESK_CELLS - row - 1)));
    return 5 - deltaY * (DESK_CELLS - row - 1);
  }

  private void acceptInvite(WebDriver driver) {
    System.out.println("ACCEPT INVITE");
    final String yesExp = "//button[contains(text(), 'Да')]";
    new WebDriverWait(driver, WAIT).until(ExpectedConditions.elementToBeClickable(By.xpath(yesExp)));
    System.out.println("READY");
    By yesXPath = By.xpath(yesExp);
    WebElement yesButton = driver.findElement(yesXPath);
    System.out.println(yesButton);
    assertNotNull(yesButton);
    yesButton.sendKeys(Keys.RETURN);
  }

  private void inviteToPlay(WebDriver driver) {
    final String imgOpponentOnlinePath = String.format("//img[@title = '%s']", OPPONENT + " в сети");
    new WebDriverWait(driver, WAIT).until(new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver webDriver) {
        By imgOpponent = By.xpath(imgOpponentOnlinePath);
        return webDriver.findElement(imgOpponent).isDisplayed();
      }
    });

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

    By playButtonId = By.id(gwtDebug + "connectToServer");
    WebElement playButton = driver.findElement(playButtonId);
    assertNotNull(playButton);
    playButton.click();

    final String nextExp = "//button[contains(text(), 'Далее')]";
    new WebDriverWait(driver, WAIT).until(ExpectedConditions.elementToBeClickable(By.xpath(nextExp)));
    By nextXPath = By.xpath(nextExp);
    WebElement nextButton = driver.findElement(nextXPath);
    assertNotNull(nextButton);
    nextButton.sendKeys(Keys.RETURN);
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

  private String extractXPath(String notation, String path) {
    try {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new ByteArrayInputStream(notation.getBytes())));
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xPath = xPathFactory.newXPath();
      XPathExpression xPathExpression = xPath.compile(path);
      return (String) xPathExpression.evaluate(doc, XPathConstants.STRING);
    } catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException e) {
      e.printStackTrace();
    }
    return null;
  }

  private Square fromNotation(String pos) {
    if (StringUtils.isEmpty(pos)) {
      return null;
    }

    int col = alph.indexOf(String.valueOf(pos.charAt(0)));
    int row = 8 - Integer.valueOf(String.valueOf(pos.charAt(1)));

    return new Square(row, col);
  }

  private Stroke createStrokeFromNotation(String notation, String step, boolean back) {
    System.out.println(notation);
    String stepNotation = extractXPath(notation, "//span");
    if (StringUtils.isNotEmpty(step)) {
      step = extractXPath(step, "//span");
    }
    Stroke stroke = new Stroke();
    final Boolean first = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_FIRST_ATTR)));
    final Integer number = Integer.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_NUMBER_ATTR)));
    final Integer order = Integer.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_ORDER_ATTR)));
    final Boolean simpleMove = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_SIMPLE_ATTR)));
    final Boolean startBeat = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_START_BEAT_ATTR)));
    final Boolean continueBeat = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_CONTINUE_BEAT_ATTR)));
    final Boolean stopBeat = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_STOP_BEAT_ATTR)));
    final String title = extractXPath(notation, getNotationAttribute(NotationPanel.DATA_TITLE_ATTR));
    final String comment = extractXPath(notation, getNotationAttribute(NotationPanel.DATA_COMMENT_ATTR));
    stroke.setOrder(order);
    stroke.setTitle(title);
    stroke.setComment(comment);
    if (simpleMove) {
      stroke.setOnSimpleMove();
    }
    if (startBeat) {
      stroke.setOnStartBeat();
    }
    if (continueBeat) {
      stroke.setOnContinueBeat();
    }
    if (stopBeat) {
      stroke.setOnStopBeat();
    }
    System.out.println(order + " :: " + stepNotation + " " + step);

    final String innerHTML = stepNotation;
    String[] pos = new String[2];
    if (simpleMove || startBeat) {
      pos = innerHTML.split(simpleMove ? Stroke.SIMPLE_MOVE_SEP : Stroke.BEAT_MOVE_SEP);
    } else if (StringUtils.isNotEmpty(step)) {
      pos[0] = step.substring(step.indexOf(Stroke.BEAT_MOVE_SEP) + 1);
      pos[1] = innerHTML;
    } else {
      return null;
    }
    String startPos = pos[0];
    String endPos = pos[1];
    final Square startSquare = fromNotation(startPos);
    final Square endSquare = fromNotation(endPos);
//    Square captured = null;
//    if (!stroke.isSimple()) {
//      captured = findTaken(startSquare, endSquare, back);
//    }
    stroke.setFirst(first)
        .setNumber(number)
        .setStartSquare(startSquare)
        .setEndSquare(endSquare);
//        .setTakenSquare(captured);
    return stroke;
  }

  private String getNotationAttribute(String attrName) {
    return "//span/@" + attrName;
  }

  static class StrokeNum {
    int row;
    int col;

    public StrokeNum(int row, int col) {
      this.row = row;
      this.col = col;
    }

    @Override
    public String toString() {
      return "StrokeNum{" +
          "row=" + row +
          ", col=" + col +
          '}';
    }
  }
}
