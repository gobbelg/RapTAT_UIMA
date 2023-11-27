/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.sql;

import java.util.ArrayList;
import java.util.List;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.ConceptRelation;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;

/**
 * @author Glenn Gobbel
 *
 */
public class AnnotatedPhraseFields {
  public final String annotatorName;
  public final String annotatorID;
  public final String conceptName;
  public final String mentionID;
  public final String startOffset;
  public final String endOffset;
  public final String phraseText;
  public final List<ConceptRelation> conceptRelations;
  public final List<RaptatAttribute> attributes;
  public final String creationDate;
  public final List<RaptatToken> rawTokens;


  /**
   * Constructor created for Virani Statin project to allow creation of annotated phrase without all
   * the fields present
   */
  public AnnotatedPhraseFields(final String conceptName, final String startOffset,
      final String endOffset, final String phraseText) {
    this("", "", conceptName, "", startOffset, endOffset, phraseText,
        new ArrayList<ConceptRelation>(), new ArrayList<RaptatAttribute>(), "");

  }

  /**
   * @param mentionID
   * @param startOffset
   * @param endOffset
   * @param phraseText
   * @param conceptRelations
   * @param attributes
   */
  public AnnotatedPhraseFields(final String annotatorName, final String annotatorID,
      final String conceptName, final String mentionID, final String startOffset,
      final String endOffset, final String phraseText, final List<ConceptRelation> conceptRelations,
      final List<RaptatAttribute> attributes, final String creationDate) {
    this(annotatorName, annotatorID, conceptName, mentionID, startOffset, endOffset, phraseText,
        conceptRelations, attributes, creationDate, new ArrayList<RaptatToken>());
  }

  public AnnotatedPhraseFields(final String annotatorName, final String annotatorID,
      final String conceptName, final String mentionID, final String startOffset,
      final String endOffset, final String phraseText, final List<ConceptRelation> conceptRelations,
      final List<RaptatAttribute> attributes, final String creationDate,
      final List<RaptatToken> phraseTokens) {
    super();
    this.annotatorName = annotatorName;
    this.annotatorID = annotatorID;
    this.conceptName = conceptName;
    this.mentionID = mentionID;
    this.startOffset = startOffset;
    this.endOffset = endOffset;
    this.phraseText = phraseText;
    this.conceptRelations = conceptRelations;
    this.attributes = attributes;
    this.creationDate = creationDate;
    this.rawTokens = phraseTokens;
  }
}
