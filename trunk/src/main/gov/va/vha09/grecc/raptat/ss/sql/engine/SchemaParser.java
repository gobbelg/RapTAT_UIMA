/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package src.main.gov.va.vha09.grecc.raptat.ss.sql.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.schema.SchemaImporter;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.AttrValueDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.AttributeDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ConceptDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ISQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.LinkTypeDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriverException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SchemasDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoDataException;

/**
 * The Class SchemaParser.
 *
 * @author VHATVHSAHAS1
 */
public class SchemaParser {

  /** The attribute driver. */
  private AttributeDriver __attribute_driver;

  /** The attribute value driver. */
  private AttrValueDriver __attribute_value_driver;

  /** The concept driver. */
  private ConceptDriver __concept_driver;

  /** The link type driver. */
  private LinkTypeDriver __link_type_driver;

  /** The schemas driver. */
  private SchemasDriver schemaDriver;

  /** The schema concepts. */
  List<SchemaConcept> annotationSchemaConcepts;

  /** The schema name. */
  String dbSchemaName;

  /**
   * Instantiates a new schema parser.
   *
   * @param sql_driver the sql driver
   */
  public SchemaParser(ISQLDriver sql_driver) {
    __attribute_driver = new AttributeDriver(sql_driver);
    __concept_driver = new ConceptDriver(sql_driver);
    __attribute_value_driver = new AttrValueDriver(sql_driver);
    __link_type_driver = new LinkTypeDriver(sql_driver);
    schemaDriver = new SchemasDriver(sql_driver);
  }


  /**
   * Instantiates a new schema parser.
   *
   * @param dbSchemaName the schema name
   * @param sql_driver the sql driver
   */
  public SchemaParser(String dbSchemaName, ISQLDriver sql_driver) {
    this(sql_driver);
    this.dbSchemaName = dbSchemaName;
  }


  /**
   * Adds the attributes.
   *
   * @param attributeValueMap the attribute value map
   * @param conceptID the concept ID
   * @param schemaID the schema ID
   * @throws NoConnectionException the no connection exception
   * @throws NoDataException the no data exception
   */
  public void addAttributes(HashMap<String, List<String>> attributeValueMap, long conceptID,
      long schemaID) throws NoConnectionException, NoDataException {
    long attrID;
    long attrValueID;

    for (String attr : attributeValueMap.keySet()) {
      attrID = __attribute_driver.getAttributeID(attr, dbSchemaName);
      __concept_driver.addConceptAttribute(attrID, conceptID, dbSchemaName);

      for (String value : attributeValueMap.get(attr)) {
        attrValueID = __attribute_value_driver.getAttrValueID(value, dbSchemaName);
        __attribute_driver.addAttributeValues(attrID, attrValueID, dbSchemaName);
      }
    }
  }


  /**
   * Adds the links.
   *
   * @param schemaID the schema ID
   * @param schemaConcept the schema concept
   */
  public void addLinks(long schemaID, SchemaConcept schemaConcept) {
    long linkTypeID;
    long conceptID;

    try {
      linkTypeID =
          __link_type_driver.getLinkTypeID(schemaConcept.getConceptName(), schemaID, dbSchemaName);

      for (String left : schemaConcept.getLinkedFromConcepts()) {
        conceptID = __concept_driver.getConceptID(left, schemaID, dbSchemaName);
        __link_type_driver.addLinkTypeConcept(linkTypeID, conceptID, 1, dbSchemaName);
      }

      for (String right : schemaConcept.getLinkedToConcepts()) {
        conceptID = __concept_driver.getConceptID(right, schemaID, dbSchemaName);
        __link_type_driver.addLinkTypeConcept(linkTypeID, conceptID, 2, dbSchemaName);
      }

      addAttributes(schemaConcept.getAttributeValueMap(), linkTypeID, schemaID);
    } catch (NoConnectionException ex) {
      Logger.getLogger(SchemaParser.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoDataException ex) {
      Logger.getLogger(SchemaParser.class.getName()).log(Level.SEVERE, null, ex);
    }
  }


  /**
   * Adds the schema.
   *
   * @param annotationSchemaFile the file
   * @param annotationAppName the source
   * @param annotationSchemaInDb the schema exists
   * @return the long
   */
  public long addSchemaToDBReturnId(File annotationSchemaFile, String annotationAppName,
      AtomicBoolean annotationSchemaInDb) {
    String fileHash;
    String pathToAnnotationSchemaFile = annotationSchemaFile.getAbsolutePath();

    long schemaID = -1;

    try (FileInputStream fis = new FileInputStream(annotationSchemaFile)) {
      fileHash = getFileHash(annotationSchemaFile);
      schemaID = schemaDriver.getAnnotationSchemaID(fis, annotationAppName, pathToAnnotationSchemaFile,
          fileHash, annotationSchemaInDb, dbSchemaName);
    } catch (NoConnectionException | IOException ex) {
      Logger.getLogger(SchemaParser.class.getName()).log(Level.SEVERE, null, ex);
    }

    return schemaID;
  }


  /**
   * Adds the schema concepts.
   *
   * @param schemaID the schema ID
   * @param schemaConcept the schema concept
   */
  public void addSchemaConcepts(long schemaID, SchemaConcept schemaConcept) {
    long conceptID;
    try {
      conceptID =
          __concept_driver.getConceptID(schemaConcept.getConceptName(), schemaID, dbSchemaName);
      addAttributes(schemaConcept.getAttributeValueMap(), conceptID, schemaID);
    } catch (NoConnectionException ex) {
      Logger.getLogger(SchemaParser.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoDataException ex) {
      Logger.getLogger(SchemaParser.class.getName()).log(Level.SEVERE, null, ex);
    }
  }


  /**
   * Gets the file hash.
   *
   * @param file the file
   * @return the file hash
   */
  public String getFileHash(File file) {

    String rv = "";

    try (FileInputStream fis = new FileInputStream(file)) {
      MessageDigest digest = MessageDigest.getInstance("SHA-512");

      byte[] byteBuffer = new byte[1024];
      int bytesRead;

      while ((bytesRead = fis.read(byteBuffer)) != -1) {
        digest.update(byteBuffer, 0, bytesRead);
      }

      byte[] hashedBytes = digest.digest();

      rv = new String(hashedBytes, StandardCharsets.UTF_8);
    } catch (NoSuchAlgorithmException | IOException ex) {
      Logger.getLogger(SchemaParser.class.getName()).log(Level.SEVERE, null, ex);
      rv = "";
    }

    return rv;
  }


  /**
   * Gets the schema concepts.
   *
   * @return the schema concepts
   */
  public List<SchemaConcept> getSchemaConcepts() {
    return annotationSchemaConcepts;
  }


  /**
   * Parses the schema.
   *
   * @param annotationSchemaFile the schema file
   * @param annotationAppName the source
   * @return the long
   */
  public long processAnnotationSchemaReturnId(File annotationSchemaFile, String annotationAppName) {
    AtomicBoolean annotationSchemaInDb = new AtomicBoolean(false);
    long schemaID = addSchemaToDBReturnId(annotationSchemaFile, annotationAppName, annotationSchemaInDb);

    if (annotationSchemaInDb.get()) {
      return schemaID;
    }

    annotationSchemaConcepts = SchemaImporter.importSchemaConcepts(annotationSchemaFile);

    for (SchemaConcept schemaConcept : annotationSchemaConcepts) {
      if (schemaConcept.isRelationship()) {
        addLinks(schemaID, schemaConcept);
      } else {
        addSchemaConcepts(schemaID, schemaConcept);
      }
    }

    return schemaID;
  }


  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    try {
      SQLDriver driver = new SQLDriver("localhost", "akinlp", "test", "test1234");
      SchemaParser parser = new SchemaParser("test.", driver);
      File schemaFile = new File("P:\\workspace\\LEO_Raptat\\data\\New folder\\projectschema.xml");
      long id = parser.processAnnotationSchemaReturnId(schemaFile, "eHost");
      System.out.println(id);
    } catch (SQLDriverException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
