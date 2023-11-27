package src.main.gov.va.vha09.grecc.raptat.dw.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The Class CoreUrl.
 *
 * @author westerd
 */
public class CoreUrl {

  /** The url. */
  URL url;

  /** The new document builder. */
  private DocumentBuilder newDocumentBuilder;

  /**
   * Instantiates a new core url.
   *
   * @param url the url
   */
  public CoreUrl(URL url) {
    this.url = url;
  }


  /**
   * As text.
   *
   * @return the string
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public String as_text() throws IOException {
    String line;
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.url.openStream()))) {
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
    }
    return sb.toString();
  }


  /**
   * As xml document.
   *
   * @return the document
   * @throws ParserConfigurationException the parser configuration exception
   * @throws SAXException the SAX exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Document as_xml_document() throws ParserConfigurationException, SAXException, IOException {
    File file = new File(this.url.getFile());
    DocumentBuilderFactory documentfactory = DocumentBuilderFactory.newInstance();
    documentfactory.setIgnoringElementContentWhitespace(true);
    if (this.newDocumentBuilder == null) {
      this.newDocumentBuilder = documentfactory.newDocumentBuilder();
    }
    return this.newDocumentBuilder.parse(file);
  }
}
