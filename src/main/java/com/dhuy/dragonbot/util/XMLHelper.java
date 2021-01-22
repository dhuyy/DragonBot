package com.dhuy.dragonbot.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.dhuy.dragonbot.global.Store;
import com.dhuy.dragonbot.model.Item;

public class XMLHelper {
  private String ITEMS_LIST_XML_PATH;

  public XMLHelper() {
    ITEMS_LIST_XML_PATH = Store.getInstance().getChosenSellItemsXmlFileName();
  }

  public List<Item> getItemsList() {
    List<Item> items = new ArrayList<Item>();

    File xmlFile = new File(ITEMS_LIST_XML_PATH);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;

    try {
      dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();

      NodeList nodeList = doc.getElementsByTagName("Item");

      for (int i = 0; i < nodeList.getLength(); i++) {
        items.add(getItem(nodeList.item(i)));
      }
    } catch (SAXException | ParserConfigurationException | IOException e) {
      e.printStackTrace();
    }

    return items;
  }

  public void updateBuyAmount(String buyAmount, String itemName) {
    File xmlFile = new File(ITEMS_LIST_XML_PATH);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;

    try {
      dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();

      NodeList items = doc.getElementsByTagName("Item");
      Element item = null;

      for (int i = 0; i < items.getLength(); i++) {
        item = (Element) items.item(i);

        String name = item.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

        if (name.equals(itemName)) {
          Node id = item.getElementsByTagName("buy").item(0).getFirstChild();
          id.setNodeValue(buyAmount);
        }
      }

      doc.getDocumentElement().normalize();
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(ITEMS_LIST_XML_PATH));
      transformer.setOutputProperty(OutputKeys.INDENT, "no");
      transformer.transform(source, result);

    } catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
      e.printStackTrace();
    }
  }

  public void updateItemId(String itemId, String itemName) {
    File xmlFile = new File(ITEMS_LIST_XML_PATH);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;

    try {
      dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();

      NodeList items = doc.getElementsByTagName("Item");
      Element item = null;

      for (int i = 0; i < items.getLength(); i++) {
        item = (Element) items.item(i);

        String name = item.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

        if (name.equals(itemName)) {
          Node id = item.getElementsByTagName("id").item(0).getFirstChild();
          id.setNodeValue(itemId);
        }
      }

      doc.getDocumentElement().normalize();
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(ITEMS_LIST_XML_PATH));
      transformer.setOutputProperty(OutputKeys.INDENT, "no");
      transformer.transform(source, result);

    } catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
      e.printStackTrace();
    }
  }

  public void deleteItem(String itemName) {
    File xmlFile = new File(ITEMS_LIST_XML_PATH);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;

    try {
      dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();

      NodeList items = doc.getElementsByTagName("Item");
      Element item = null;

      for (int i = 0; i < items.getLength(); i++) {
        item = (Element) items.item(i);

        String name = item.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

        if (name.equals(itemName)) {
          item.getParentNode().removeChild(item);
        }
      }

      doc.getDocumentElement().normalize();
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(ITEMS_LIST_XML_PATH));
      transformer.setOutputProperty(OutputKeys.INDENT, "no");
      transformer.transform(source, result);

    } catch (SAXException | ParserConfigurationException | IOException | TransformerException e) {
      e.printStackTrace();
    }
  }

  public int getLength() {
    File xmlFile = new File(ITEMS_LIST_XML_PATH);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;

    try {
      dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(xmlFile);
      doc.getDocumentElement().normalize();

      NodeList items = doc.getElementsByTagName("Item");

      int length = 0;
      for (int i = 0; i < items.getLength(); i++) {
        length++;
      }

      return length;
    } catch (SAXException | ParserConfigurationException | IOException e) {
      return 0;
    }
  }

  private Item getItem(Node node) {
    Item item = new Item();

    if (node.getNodeType() == Node.ELEMENT_NODE) {
      Element element = (Element) node;

      item.setId(getTagValue("id", element));
      item.setName(getTagValue("name", element));
      item.setPrice(Integer.parseInt(getTagValue("price", element)));
      item.setBuy(Integer.parseInt(getTagValue("buy", element)));
    }

    return item;
  }

  private String getTagValue(String tag, Element element) {
    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
    Node node = nodeList.item(0);

    return node.getNodeValue();
  }
}
