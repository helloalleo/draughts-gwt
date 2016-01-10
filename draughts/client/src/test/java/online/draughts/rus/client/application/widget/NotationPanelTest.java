package online.draughts.rus.client.application.widget;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwtmockito.GwtMockitoTestRunner;
import online.draughts.rus.draughts.Stroke;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 13.11.15
 * Time: 16:11
 */
@RunWith(GwtMockitoTestRunner.class)
public class NotationPanelTest {

  private NotationPanel notationPanel;

  private final String g3_h4 = "<span id=\"0\" data-order=\"0\" data-number=\"1\" data-simple=\"true\" data-first=\"true\" data-title=\"\" data-comment=\"\" data-startbeat=\"false\" data-continuebeat=\"false\" data-stopbeat=\"false\">g3-h4</span>";
  private final String d6_e5 = "<span id=\"1\" data-order=\"1\" data-number=\"1\" data-simple=\"true\" data-first=\"false\" data-title=\"\" data-comment=\"\" data-startbeat=\"false\" data-continuebeat=\"false\" data-stopbeat=\"false\">d6-e5</span>";
  private final String f2_g3 = "<span id=\"2\" data-order=\"2\" data-number=\"2\" data-simple=\"true\" data-first=\"true\" data-title=\"\" data-comment=\"\" data-startbeat=\"false\" data-continuebeat=\"false\" data-stopbeat=\"false\">f2-g3</span>";
  private final String f6_g5 = "<span id=\"3\" data-order=\"3\" data-number=\"2\" data-simple=\"true\" data-first=\"false\" data-title=\"\" data-comment=\"\" data-startbeat=\"false\" data-continuebeat=\"false\" data-stopbeat=\"false\">f6-g5</span>";
  private final String h4xf6 = "<span id=\"4\" data-order=\"4\" data-number=\"3\" data-simple=\"false\" data-first=\"true\" data-title=\"\" data-comment=\"\" data-startbeat=\"true\" data-continuebeat=\"true\" data-stopbeat=\"false\">h4:f6</span>";
  private final String xd4 = "<span id=\"5\" data-order=\"5\" data-number=\"3\" data-simple=\"false\" data-first=\"true\" data-title=\"\" data-comment=\"\" data-startbeat=\"false\" data-continuebeat=\"false\" data-stopbeat=\"true\">d4</span>";

  private List<String> strokes = new ArrayList<String>() {{
    add(g3_h4);
    add(d6_e5);
    add(f2_g3);
    add(f6_g5);
    add(h4xf6);
    add(xd4);
  }};

  private String g3_d4Notation = "<span>1. </span><a><span id='0' data-order='0' data-number='1' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>g3-h4</span></a><span> </span><a><span id='1' data-order='1' data-number='1' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>d6-e5</span></a><br><span>2. </span><a><span id='2' data-order='2' data-number='2' data-simple='true' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>f2-g3</span></a><span> </span><a><span id='3' data-order='3' data-number='2' data-simple='true' data-first='false' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='false'>f6-g5</span></a><br><span>3. </span><a><span id='4' data-order='4' data-number='3' data-simple='false' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='true' data-continuebeat='true' data-stopbeat='false'>h4:f6</span></a><span>:</span><a><span id='5' data-order='5' data-number='3' data-simple='false' data-first='true' data-title=\"\" data-comment=\"\" data-startbeat='false' data-continuebeat='false' data-stopbeat='true'>d4</span></a><span> </span>";

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
  private static final String TO_SEND_SEP = ",";

  @Before
  public void setUp() {
    SimpleEventBus eventBus = new SimpleEventBus();
    notationPanel = new NotationPanel(eventBus, 1L);
  }

  @Test
  public void testAppendMove() throws Exception {
    assertNotNull(notationPanel);
    notationPanel.cleanNotationPanel();
    Stroke newStroke;
    for (int i = 0; i < strokes.size(); i++) {
      String stroke = strokes.get(i);
      if (i == 5) {
//        newStroke = createStrokeFromNotation(stroke, strokes.get(i - 1), false);
//        notationPanel.appendMove(newStroke);
        continue;
      }

//      newStroke = createStrokeFromNotation(stroke, "", false);
//      notationPanel.appendMove(newStroke);
    }
    assertEquals(g3_d4Notation, NotationPanel.notation.toString());
  }

  @Test
  public void testCancelMove() {
    notationPanel.cleanNotationPanel();
    NotationPanel.notation.append(g3_d4Notation);
    Stroke newStroke;
    for (int i = strokes.size() - 1; i >= 0; i--) {
      String stroke = strokes.get(i);
      if (i == 5) {
//        newStroke = createStrokeFromNotation(stroke, strokes.get(i - 1), false);
//        notationPanel.cancelMove(newStroke);
        continue;
      }
//      newStroke = createStrokeFromNotation(stroke, "", false);
//      notationPanel.cancelMove(newStroke);
    }
    assertEquals("", NotationPanel.notation.toString());
  }

//  private Stroke createStrokeFromNotation(String notation, String step, boolean back) {
//    String stepNotation = extractXPath(notation, "//span");
//    if (StringUtils.isNotEmpty(step)) {
//      step = extractXPath(step, "//span");
//    }
//    Stroke stroke = new Stroke();
////    final Boolean first = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_FIRST_ATTR)));
////    final Integer number = Integer.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_NUMBER_ATTR)));
////    final Integer order = Integer.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_ORDER_ATTR)));
////    final Boolean simpleMove = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_SIMPLE_ATTR)));
////    final Boolean startBeat = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_START_BEAT_ATTR)));
////    final Boolean continueBeat = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_CONTINUE_BEAT_ATTR)));
////    final Boolean stopBeat = Boolean.valueOf(extractXPath(notation, getNotationAttribute(NotationPanel.DATA_STOP_BEAT_ATTR)));
////    final String title = extractXPath(notation, getNotationAttribute(NotationPanel.DATA_TITLE_ATTR));
////    final String comment = extractXPath(notation, getNotationAttribute(NotationPanel.DATA_COMMENT_ATTR));
//    stroke.setOrder(order);
////    stroke.setTitle(title);
////    stroke.setComment(comment);
//    if (simpleMove) {
//      stroke.setOnSimpleMove();
//    }
//    if (startBeat) {
//      stroke.setOnStartBeat();
//    }
//    if (continueBeat) {
//      stroke.setOnContinueBeat();
//    }
//    if (stopBeat) {
//      stroke.setOnStopBeat();
//    }
//    System.out.println(order + " :: " + stepNotation + " " + step);
//
//    final String innerHTML = stepNotation;
//    String[] pos = new String[2];
//    if (simpleMove || startBeat) {
//      pos = innerHTML.split(simpleMove ? Stroke.SIMPLE_MOVE_SEP : Stroke.BEAT_MOVE_SEP);
//    } else if (StringUtils.isNotEmpty(step)) {
//      pos[0] = step.substring(step.indexOf(Stroke.BEAT_MOVE_SEP) + 1);
//      pos[1] = innerHTML;
//    } else {
//      return null;
//    }
//    String startPos = pos[0];
//    String endPos = pos[1];
////    final Square startSquare = fromNotation(startPos);
////    final Square endSquare = fromNotation(endPos);
////    Square captured = null;
////    if (!stroke.isSimple()) {
////      captured = findTaken(startSquare, endSquare, back);
////    }
////    stroke.setFirst(first)
////        .setNumber(number)
////        .setStartSquare(startSquare)
////        .setEndSquare(endSquare);
////        .setTakenSquare(captured);
//    return stroke;
//  }

//  private Square fromNotation(String pos) {
//    if (StringUtils.isEmpty(pos)) {
//      return null;
//    }
//
//    int col = alph.indexOf(String.valueOf(pos.charAt(0)));
//    int row = 8 - Integer.valueOf(String.valueOf(pos.charAt(1)));
//
//    return new Square(row, col);
//  }

//  private String extractXPath(String notation, String path) {
//    try {
//      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
//      DocumentBuilder builder = builderFactory.newDocumentBuilder();
//      Document doc = builder.parse(new InputSource(new ByteArrayInputStream(notation.getBytes())));
//      XPathFactory xPathFactory = XPathFactory.newInstance();
//      XPath xPath = xPathFactory.newXPath();
//      XPathExpression xPathExpression = xPath.compile(path);
//      return (String) xPathExpression.evaluate(doc, XPathConstants.STRING);
//    } catch (SAXException | IOException | XPathExpressionException | ParserConfigurationException e) {
//      e.printStackTrace();
//    }
//    return null;
//  }

  private String getNotationAttribute(String attrName) {
    return "//span/@" + attrName;
  }
}