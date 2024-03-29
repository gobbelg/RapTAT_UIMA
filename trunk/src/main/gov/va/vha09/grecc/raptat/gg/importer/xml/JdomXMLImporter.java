package src.main.gov.va.vha09.grecc.raptat.gg.importer.xml;

import java.io.File;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class JdomXMLImporter {
  public JdomXMLImporter() {}


  public static String getTextSource(File theFile) {
    SAXBuilder builder = new SAXBuilder();
    String srcFileName = null;
    try {
      Document doc = builder.build(theFile);
      Element root = doc.getRootElement();
      srcFileName = root.getAttributeValue("textSource");
    } catch (JDOMException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return srcFileName;
  }


  public static String getTextSource(String FilePath) {
    return getTextSource(new File(FilePath));
  }

}
