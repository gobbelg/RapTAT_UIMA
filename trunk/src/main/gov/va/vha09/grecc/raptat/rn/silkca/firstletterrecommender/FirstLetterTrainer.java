package src.main.gov.va.vha09.grecc.raptat.rn.silkca.firstletterrecommender;

import java.util.List;

import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.SimpleAnnotatedPhrase;

/**
 * A trainer for the toy model for the FirstLetterRecommender
 */

public class FirstLetterTrainer {

	/**
	 * Trains a new FirstLetterModel. creates a new model, or HashMap, that for each
	 * annotation passed, it maps the first letter of the covered text to the
	 * annotation's label, overwriting any previous label if it existed.
	 * 
	 * @param aAnnotations
	 * @return
	 * @throws Exception 
	 */
	public void train(FirstLetterModel model, List<SimpleAnnotatedPhrase> aAnnotations) throws IllegalArgumentException {
		if (model == null) {
			throw new IllegalArgumentException("Training requires a non-null model");
		}
		for (SimpleAnnotatedPhrase ann : aAnnotations) {
			if (!ann.label().isEmpty()) {
				model.addEntryToMap(ann.annotatedText().substring(0, 1).toLowerCase(), ann.label());
			}
		}
	}
}
