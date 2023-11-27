/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers;

import java.util.ArrayList;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

/**
 * The Class SpanDriver.
 *
 * @author VHATVHSAHAS1
 */
public class SpanDriver extends DriverBase {

  /**
   * Instantiates a new span driver.
   *
   * @param driver the driver
   */
  public SpanDriver(ISQLDriver driver) {
    super(driver);
  }


  /**
   * Adds the span.
   *
   * @param annotationID the annotation ID
   * @param start the start
   * @param end the end
   * @param spanText the span text
   * @param sequence the sequence
   * @param sentenceID the sentence ID
   * @param schema the schema
   * @throws NoConnectionException the no connection exception
   */
  public void addSpan(long annotationID, int start, int end, String spanText, int sequence,
      long sentenceID, String schema) throws NoConnectionException {
    String cols = "id_annotation, start_offset, end_offset, span_text, span_sequence, id_sentence";

    List<String> line = new ArrayList<String>();
    spanText = spanText.replaceAll("\'", "\'\'");
    line.add(annotationID + "," + start + "," + end + ",'" + spanText + "'," + sequence + ","
        + sentenceID);

    getDriver().executeInsert(cols, line, schema, "span");
  }
}
