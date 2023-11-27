/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package src.main.gov.va.vha09.grecc.raptat.ss.sql.engine;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ISQLDriver;

/**
 * Parse the Annotation XML files and upload the annotation information to DB. Date May 27, 2015
 *
 * @author VHATVHSAHAS1
 */
public class AnnotationXMLParser {

  /** The creation date. */
  static String creationDate = "";

  /** The sql driver. */
  private ISQLDriver __sql_driver;

  /**
   * Instantiates a new annotation XML parser.
   *
   * @param sql_driver the sql driver
   */
  public AnnotationXMLParser(ISQLDriver sql_driver) {
    __sql_driver = sql_driver;
  }



  /**
   * Find creation date.
   *
   * @param node the node
   */
  public static void findCreationDate(Node node) {
    // do something with the current node instead of System.out
    System.out.println(node.getNodeName());
    if (node.getNodeName().equals("creationDate")) {
      AnnotationXMLParser.creationDate = node.getNodeValue();
      return;
    }
    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node currentNode = nodeList.item(i);
      if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
        findCreationDate(currentNode);
      }
    }
  }


}
