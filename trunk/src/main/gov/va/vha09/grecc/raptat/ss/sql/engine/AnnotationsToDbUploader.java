package src.main.gov.va.vha09.grecc.raptat.ss.sql.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.ConceptRelation;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatToken;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.AnnotationDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.AnnotatorDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.AttrValueDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.AttributeDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ConceptDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.DocumentDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ISQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.LinkDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.LinkTypeDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SentenceDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SpanDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoDataException;

/**
 * The Class AnnotationsToDbUploader.
 */
public class AnnotationsToDbUploader {

  /** The attribute driver. */
  private AttributeDriver attributeDriver;

  /** The attribute value driver. */
  private AttrValueDriver attributeValueDriver;

  /** The annotation driver. */
  private AnnotationDriver annotationDriver;

  /** The document driver. */
  private DocumentDriver documentDriver;

  /** The link type driver. */
  private LinkTypeDriver linkTypeDriver;

  /** The link driver. */
  private LinkDriver linkDriver;

  /** The sentence driver. */
  private SentenceDriver sentenceDriver;

  /** The span driver. */
  private SpanDriver spanDriver;

  /** The annotator driver. */
  private AnnotatorDriver annotatorDriver;

  /** The concept driver. */
  private ConceptDriver conceptDriver;

  /**
   * Instantiates a new annotations to db uploader.
   *
   * @param sqlDriver the sql driver
   */
  public AnnotationsToDbUploader(ISQLDriver sqlDriver) {
    attributeDriver = new AttributeDriver(sqlDriver);
    attributeValueDriver = new AttrValueDriver(sqlDriver);
    annotationDriver = new AnnotationDriver(sqlDriver);
    documentDriver = new DocumentDriver(sqlDriver);
    linkTypeDriver = new LinkTypeDriver(sqlDriver);
    linkDriver = new LinkDriver(sqlDriver);
    sentenceDriver = new SentenceDriver(sqlDriver);
    spanDriver = new SpanDriver(sqlDriver);
    annotatorDriver = new AnnotatorDriver(sqlDriver);
    conceptDriver = new ConceptDriver(sqlDriver);
  }


  /**
   * Adds the attribute to annotation.
   *
   * @param annotation the annotation
   * @param annID the ann ID
   * @param schemaID the schema ID
   * @param schemaName the schema name
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public void addAttributeToAnnotation(AnnotatedPhrase annotation, long annID, long schemaID,
      String schemaName) throws NoConnectionException, NoDataException {
    long attrID;
    long valueID;
    long attrAttrValID;

    for (RaptatAttribute attr : annotation.getPhraseAttributes()) {
      attrID = attributeDriver.attributeExists(attr.getName(), schemaName);

      for (String value : attr.getValues()) {
        valueID = attributeValueDriver.valueExists(value, schemaName);
        attrAttrValID = attributeDriver.attrAttrValueExists(attrID, valueID, schemaName);

        annotationDriver.addAnnotationAttribute(annID, attrAttrValID, attr.getID(), schemaName);
      }
    }
  }



  /**
   * Adds the document.
   *
   * @param textSourceId Path to the file if it exists, otherwise it is an identifier like a TIU
   *        DocumentSID
   * @param documentText the source ID
   * @param dbSchemaName the schema name
   * @return the long
   * @throws NoConnectionException the no connection exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws SAXException the SAX exception
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws XPathExpressionException the x path expression exception
   */
  public long addDocument(String textSourceId, String documentText, String dbSchemaName)
      throws NoConnectionException, ParserConfigurationException, SAXException, IOException,
      XPathExpressionException {

    InputStream is = new ByteArrayInputStream(documentText.getBytes(Charset.forName("UTF-8")));
    File textSourceFile = new File(textSourceId);
    String textSourceName = textSourceFile.exists() ? textSourceFile.getName() : textSourceId;
    long docID = documentDriver.getDocumentID(is, textSourceId, textSourceName, dbSchemaName);

    return docID;
  }


  /**
   * Adds the relations.
   *
   * @param annotationMap the annotation map
   * @param relationMap the relation map
   * @param schemaID the schema ID
   * @param schemaName the schema name
   */
  public void addRelations(HashMap<String, Long> annotationMap,
      HashMap<Long, ConceptRelation> relationMap, long schemaID, String schemaName) {
    ConceptRelation relation;
    long targetID;
    long linkTypeID;

    for (Long sourceID : relationMap.keySet()) {
      try {
        relation = relationMap.get(sourceID);
        targetID = annotationMap.get(relation.getRelatedAnnotationID());
        linkTypeID =
            linkTypeDriver.getLinkTypeID(relation.getConceptRelationName(), schemaID, schemaName);

        linkDriver.addLink(sourceID, targetID, linkTypeID, relation.getID(), schemaName);
      } catch (NoConnectionException ex) {
        Logger.getLogger(AnnotationXMLParser.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }


  /**
   * Adds the spans.
   *
   * @param annotation the annotation
   * @param annotationID the annotation ID
   * @param docID the doc ID
   * @param schemaName the schema name
   * @throws NoConnectionException the no connection exception
   */
  public void addSpans(AnnotatedPhrase annotation, long annotationID, long docID, String schemaName)
      throws NoConnectionException {
    int sequence = 1;
    int start;
    int end;
    long sentenceID;

    while (annotation != null) {
      String annotationText = "";
      List<RaptatToken> tokens = annotation.getProcessedTokens();
      if (tokens.size() > 0) {
        AnnotatedPhrase tokenSentence = tokens.get(0).getSentenceOfOrigin();
        if (tokenSentence != null) {
          annotationText = tokenSentence.getPhraseStringUnprocessed();
        }
      }
      sentenceID = sentenceDriver.getsentenceID(annotationText, docID, schemaName);

      /*
       * Modified code (Glenn 06/12/18) so that we use unprocessed rather than processed offsets
       */
      start = Integer.parseInt(annotation.getProcessedTokensStartOffset());
      end = Integer.parseInt(annotation.getProcessedTokensEndOffset());

      start = Integer.parseInt(annotation.getRawTokensStartOff());
      end = Integer.parseInt(annotation.getRawTokensEndOff());

      spanDriver.addSpan(annotationID, start, end, annotation.getPhraseStringUnprocessed(),
          sequence, sentenceID, schemaName);
      sequence++;
      annotation = annotation.getNextPhrase();
    }
  }



  /**
   * Upload annotations.
   *
   * @param schemaID the schema ID
   * @param annotations the annotations
   * @param optionalTextFilePath the text file path
   * @param dbSchemaName the database schema name
   */
  public void uploadAnnotations(long schemaID, List<AnnotatedPhrase> annotations,
      Optional<String> optionalDocumentText, String textSourceId, String dbSchemaName) {

    String annotatorID;
    Long conceptID;
    long annotationID;

    HashMap<String, Long> annotationMap = new HashMap<String, Long>();
    HashMap<Long, ConceptRelation> relationMap = new HashMap<Long, ConceptRelation>();

    HashMap<String, String> annotatorMap = new HashMap<String, String>();
    HashMap<String, Long> conceptMap = new HashMap<String, Long>();

    try {

      String documentText =
          optionalDocumentText.orElse("****NO****TEXT***SUPPLIED****FOR****UPLOAD***");

      long docID = this.addDocument(textSourceId, documentText, dbSchemaName);

      for (AnnotatedPhrase ann : annotations) {
        if (ann.getPrevPhrase() != null) {
          continue;
        }

        if ((annotatorID = annotatorMap.get(ann.getAnnotatorID())) == null) {
          annotatorID = annotatorDriver.getAnnotatorID(ann.getAnnotatorID(), dbSchemaName);
          annotatorMap.put(ann.getAnnotatorID(), annotatorID);
        }

        if ((conceptID = conceptMap.get(ann.getConceptName())) == null) {
          conceptID = conceptDriver.getConceptID(ann.getConceptName(), schemaID, dbSchemaName);
          conceptMap.put(ann.getConceptName(), conceptID);
        }

        annotationID = annotationDriver.addAnnotation(annotatorID, conceptID, ann.getMentionId(),
            dbSchemaName, ann.getCreationDate());
        addSpans(ann, annotationID, docID, dbSchemaName);
        annotationMap.put(ann.getMentionId(), annotationID);
        addAttributeToAnnotation(ann, annotationID, schemaID, dbSchemaName);

        for (ConceptRelation relation : ann.getConceptRelations()) {
          relationMap.put(annotationID, relation);
        }

      }

      addRelations(annotationMap, relationMap, schemaID, dbSchemaName);
    } catch (NoConnectionException ex) {
      System.err.println("Connection Error while processing " + textSourceId);
      Logger.getLogger(AnnotationXMLParser.class.getName()).log(Level.SEVERE,
          "Connection Error while processing " + textSourceId, ex);
      System.exit(-1);
    } catch (NoDataException ex) {
      System.err.println("Missing Data while processing " + textSourceId);
      Logger.getLogger(AnnotationXMLParser.class.getName()).log(Level.SEVERE,
          "Missing Data while processing " + textSourceId, ex);
      System.exit(-1);
    } catch (Exception ex) {
      System.err.println("Error occured while processing " + textSourceId);
      Logger.getLogger(AnnotationXMLParser.class.getName()).log(Level.SEVERE,
          "Error occured while processing " + textSourceId, ex);
      System.exit(-1);
    }
  }
}
