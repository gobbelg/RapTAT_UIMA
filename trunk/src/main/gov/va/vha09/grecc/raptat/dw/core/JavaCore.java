package src.main.gov.va.vha09.grecc.raptat.dw.core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 * The Class JavaCore.
 */
public final class JavaCore {

  /**
   * At class level.
   *
   * @param cls the cls
   * @return the class
   */
  public static Class at_class_level(Class cls) {
    return cls;
  }


  /**
   * Coalesce.
   *
   * @param <T> the generic type
   * @param items the items
   * @return the t
   */
  @SafeVarargs
  public static <T> T coalesce(T... items) {
    T rv = null;
    for (T item : items) {
      if (item != null) {
        rv = item;
        break;
      }
    }
    return rv;
  }


  /**
   * Gets the resource.
   *
   * @param cls the cls
   * @param resource_file_name the resource file name
   * @return the resource
   */
  public static CoreUrl get_resource(Class cls, String resource_file_name) {
    URL url = cls.getResource(resource_file_name);
    return new CoreUrl(url);
  }


  /**
   * Tranform xml doc to string.
   *
   * @param dom_xml the dom xml
   * @return the string
   * @throws TransformerException the transformer exception
   */
  public static String tranform_xml_doc_to_string(Document dom_xml) throws TransformerException {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer transformer = tf.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    StringWriter writer = new StringWriter();
    transformer.transform(new DOMSource(dom_xml), new StreamResult(writer));
    return writer.getBuffer().toString().replaceAll("\n|\r", "");
  }


  /**
   * Write file.
   *
   * @param file_path the file path
   * @param value the value
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void write_file(String file_path, String value) throws IOException {
    try (FileOutputStream outputStream = new FileOutputStream(file_path)) {
      byte[] strToBytes = coalesce(value, "").getBytes();
      outputStream.write(strToBytes);
    }
  }
}
