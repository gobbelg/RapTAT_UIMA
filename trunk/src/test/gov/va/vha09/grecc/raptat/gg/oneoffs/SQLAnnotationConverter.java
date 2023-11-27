package src.test.gov.va.vha09.grecc.raptat.gg.oneoffs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.XMLExporter;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.UniqueIDGenerator;

/**************************************************************
 * Takes annotations stored in a MS-SQL database and converts them to Knowtator compliant XML
 * annotaitons
 *
 * @author Glenn Gobbel @date Aug 19, 2012
 **************************************************************/
public class SQLAnnotationConverter {
  private static final int DOC_ID = 1, SPAN_BEGIN = 2, SPAN_END = 3, ANNO_TEXT = 4, SCUI = 5,
      SCUI_COUNT = 6;


  public static void main(String[] args) {
    String storageDirectory = GeneralHelper
        .getDirectory("Select directory" + " for storing XML files and concept mappings")
        .getAbsolutePath();
    String connectionUrl = "jdbc:sqlserver://VHATVHRES6:1433;"
        + "databaseName=Glenn;integratedsecurity=true;user=VHA09\\vhatvhgobbeg;password=fota$*_@!hsev20;";
    String dbTitle = "YTEX_SNOMED_LongestPhrase_NoOverlap";
    String queryStart = "Select documentID, span_begin, span_end,"
        + "annoTextLower, SCUI, scuiCount FROM " + dbTitle;
    Connection con = null;
    ResultSet rs;
    UniqueIDGenerator idGenerator = UniqueIDGenerator.INSTANCE;
    HashMap<String, List<String>> conceptToListMap = new HashMap<>();
    HashMap<List<String>, String> conceptListToConceptMap = new HashMap<>();
    HashMap<String, List<AnnotatedPhrase>> docAnnotations = new HashMap<>();
    String[] phraseData = new String[6];
    phraseData[4] = "";
    phraseData[1] = null;
    try {
      String conceptID = "";
      List<String> conceptList = new ArrayList<>();
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      con = DriverManager.getConnection(connectionUrl);
      Statement stmt = con.createStatement();

      GeneralHelper.tic("Starting SQL queries");
      rs = stmt.executeQuery(queryStart);
      String doc = "";
      while (rs.next()) {
        // Only the first, identical annotation is added to the list
        // of annotations. We create a proxy or surrogate conceptID
        // to correspond to all the various concept ID mappings of that
        // one annotation.
        if (rs.getString(SQLAnnotationConverter.SCUI_COUNT).equals("1")) {
          if (phraseData[1] != null) {
            if (conceptListToConceptMap.containsKey(conceptList)) {
              conceptID = conceptListToConceptMap.get(conceptList);
            } else {
              conceptID = idGenerator.getUnique();
              conceptListToConceptMap.put(conceptList, conceptID);
              conceptToListMap.put(conceptID, conceptList);
            }
            phraseData[5] = conceptID;
            if (!docAnnotations.containsKey(doc)) {
              docAnnotations.put(doc, new ArrayList<AnnotatedPhrase>());
            }
            docAnnotations.get(doc).add(new AnnotatedPhrase(phraseData));
            conceptList = new ArrayList<>();
          }

          doc = rs.getString(SQLAnnotationConverter.DOC_ID).replace("-", "_");
          phraseData[0] = idGenerator.getUnique();
          phraseData[1] = rs.getString(SQLAnnotationConverter.SPAN_BEGIN);
          phraseData[2] = rs.getString(SQLAnnotationConverter.SPAN_END);
          phraseData[3] = rs.getString(SQLAnnotationConverter.ANNO_TEXT);
        }
        conceptList.add(rs.getString(SQLAnnotationConverter.SCUI));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();

    } finally {

      try {
        con.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    writeConceptMap(conceptToListMap, dbTitle, storageDirectory);
    writeAnnotationsToXML(docAnnotations, storageDirectory);
  }


  private static void writeAnnotationsToXML(HashMap<String, List<AnnotatedPhrase>> docAnnotations,
      String storageDirectory) {
    Set<String> theDocs = docAnnotations.keySet();

    for (String curDoc : theDocs) {
      String textSourcePath = storageDirectory + File.separator + curDoc + ".txt";
      XMLExporter theExporter = new XMLExporter(textSourcePath, true);
      theExporter.writeAllDataObj(docAnnotations.get(curDoc), true);
      theExporter.writeXMLFile(storageDirectory, "");
    }
  }


  private static void writeConceptMap(HashMap<String, List<String>> conceptMap, String dbTitle,
      String storageDirectory) {
    PrintWriter resultWriter;
    try {
      resultWriter = new PrintWriter(
          storageDirectory + File.separator + dbTitle + "_" + System.currentTimeMillis() + ".txt");
      Set<String> conceptMapSet = conceptMap.keySet();
      for (String mappedConcept : conceptMapSet) {
        resultWriter.print(mappedConcept);
        List<String> mappings = conceptMap.get(mappedConcept);
        for (String curMapping : mappings) {
          resultWriter.print("," + curMapping);
        }
        resultWriter.println();
      }
      resultWriter.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

  }
}
