package src.main.gov.va.vha09.grecc.raptat.rn.silkca.preannotation;

import java.util.ArrayList;
import org.apache.commons.lang3.NotImplementedException;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.SimpleAnnotatedPhrase;

/**
 * Abstract class to preannotate documents
 */

public abstract class Preannotator {

  public Preannotator() {}

  /**
   * Preannotate documents based on Preannotator implementation
   *
   * @param document
   * @return
   * @throws NotImplementedException
   */
  public abstract ArrayList<SimpleAnnotatedPhrase> getPreannotations(String document)
      throws NotImplementedException;

}
