package online.draughts.rus.server.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alekspo
 * Date: 15.11.15
 * Time: 14:00
 */
public class XPathUtils {

  public static List<String> parseNotationForStrokes(String xml) {
    try {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = builderFactory.newDocumentBuilder();
      Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes())));
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xPath = xPathFactory.newXPath();
      XPathExpression xPathExpression = xPath.compile("//span[@data-order]");
      NodeList nodeList = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
      List<String> spans = new ArrayList<>();
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);
        spans.add(getNodeString(node));
      }
      return spans;
    } catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  private static String getNodeString(Node node) {
    try {
      StringWriter writer = new StringWriter();
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.transform(new DOMSource(node), new StreamResult(writer));
      String output = writer.toString();
      return output.substring(output.indexOf("?>") + 2);//remove <?xml version="1.0" encoding="UTF-8"?>
    } catch (TransformerException e) {
      e.printStackTrace();
    }
    return node.getTextContent();
  }
}
