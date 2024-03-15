package src.main.gov.va.vha09.grecc.raptat.rn.silkca.firstletterrecommender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.uima.cas.text.AnnotationFS;

import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.SimpleAnnotatedPhrase;

/**
 * A predictor for the toy model for the FirstLetterRecommender
 */

public class FirstLetterPredictor {
	
	/**
	 * Create predictions.
	 * Taking candidate annotations and a model, for each candidate, creates a SimpleAnnotatedPhrase.
	 * The label of the SimpleAnnotatedPhrase is determined by the model.
	 */

	public List<SimpleAnnotatedPhrase> predict(Collection<AnnotationFS> candidates, FirstLetterModel aModel) {
        List<SimpleAnnotatedPhrase> result = new ArrayList<>();
        for (AnnotationFS token : candidates) {
            String tokenText = token.getCoveredText();
            int begin = token.getBegin();
            int end = token.getEnd();
            String label = aModel.getFirstLetterLabel(tokenText);
            if (label != null) {
                SimpleAnnotatedPhrase annotation = new SimpleAnnotatedPhrase("", tokenText,
                        label, begin, end);
                result.add(annotation);
            }
        }
        return result;
	}
	
}
