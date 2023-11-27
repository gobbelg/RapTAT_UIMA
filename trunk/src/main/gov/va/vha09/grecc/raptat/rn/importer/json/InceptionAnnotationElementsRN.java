package src.main.gov.va.vha09.grecc.raptat.rn.importer.json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.ConceptRelation;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.AnnotatedPhraseFields;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;

public record InceptionAnnotationElementsRN(int begin, int end, String normalizedConcept, String documentSource) { 

  public static List<AnnotatedPhrase> toAnnotatedPhraseList(
      final List<InceptionAnnotationElementsRN> inceptionAnnotations, final RaptatDocument raptatDocument) {

    var resultList = new ArrayList<AnnotatedPhrase>();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
    String dateTime = dtf.format(LocalDateTime.now());

    for (InceptionAnnotationElementsRN inceptionAnnotation : inceptionAnnotations) {

      List<RaptatToken> rawTokens =
          raptatDocument.getRawTokensWithinSpan(inceptionAnnotation.begin, inceptionAnnotation.end);
      String rawString =
          raptatDocument.getRawTextInRange(inceptionAnnotation.begin, inceptionAnnotation.end);
      

      AnnotatedPhrase raptatAnnotation = inceptionAnnotation.toAnnotatedPhrase(rawTokens,
          rawString, dateTime);
      resultList.add(raptatAnnotation);
    }
    return resultList;
  }

  private AnnotatedPhrase toAnnotatedPhrase(final List<RaptatToken> annotationTokens,
      final String annotatedString,
      final String dateTime) {

    AnnotatedPhraseFields phraseFields =
        new AnnotatedPhraseFields("", "", this.normalizedConcept, "",
            Integer.toString(this.begin), Integer.toString(this.end), annotatedString,
            new ArrayList<ConceptRelation>(), new ArrayList<RaptatAttribute>(), dateTime, annotationTokens);

    return new AnnotatedPhrase(phraseFields);
  }

}
