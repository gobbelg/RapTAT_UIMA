package src.main.gov.va.vha09.grecc.raptat.rn.silkca.preannotation;

import java.util.ArrayList;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.SimpleAnnotatedPhrase;

/**
 * Base implementation of Preannotator
 */

public class BasePreannotator extends Preannotator {

  public BasePreannotator() {
    super();
  }

  @Override
  public ArrayList<SimpleAnnotatedPhrase> getPreannotations(String document) {
	  ArrayList<SimpleAnnotatedPhrase> preannotations = new ArrayList<SimpleAnnotatedPhrase>();
	  SimpleAnnotatedPhrase preannotation = new SimpleAnnotatedPhrase(document, "test", "adj", 0, 4);
	  preannotations.add(preannotation);
	  return preannotations;
  }

}
