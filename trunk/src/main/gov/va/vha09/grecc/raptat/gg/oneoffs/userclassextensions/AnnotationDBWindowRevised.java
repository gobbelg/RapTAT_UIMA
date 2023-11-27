package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.userclassextensions;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.schema.SchemaImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.PhraseTokenIntegrator;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ISQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriverException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.engine.SchemaParser;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

public class AnnotationDBWindowRevised {

  /**
   *
   */
  private static final long serialVersionUID = 2339660499795296783L;

  public static void main(String[] args) {
    UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {

        String dbName = "vhatvhsqlres1.v09.med.va.gov";
        String serverName = "ORD_Matheny_201312053D";
        String userName = "RapTAT_user";
        String password = "raptat_USER";
        String schemaName = "RapTAT";
        String relativepathToSchemaFile = "/src/main/resources/sqlReadWriteDebug/SchemaFile.xml";
        String pathToSchemaFile = "";
        try {
          pathToSchemaFile = this.getClass().getResource(pathToSchemaFile).toURI().getPath();
        } catch (URISyntaxException e) {
          e.printStackTrace();
          System.exit(-1);
        }

        File schemaFile = new File(pathToSchemaFile);

        /*
         * DAX - 4/30/20
         */
        SQLDriver driver = null;
        Connection connect = null;
        try {
          driver = new SQLDriver(serverName, dbName, userName, password);
          connect = driver.connect();
        } catch (SQLDriverException e) {
          connect = null;
          e.printStackTrace();
        }

        if (connect == null) {
          JOptionPane.showMessageDialog(null, "Error login. Check all info.", "Error",
              JOptionPane.OK_OPTION);
          System.exit(-1);
        }

        SQLDriver.closeConnection(connect);

        try {
          /*
           * DAX - 4/30/20
           */
          if (!driver.tablesExists(schemaName)) {
            JOptionPane.showMessageDialog(null, "This schema does not exist.", "Error",
                JOptionPane.OK_OPTION);
            System.exit(-1);
          }
        } catch (NoConnectionException e1) {
          JOptionPane.showMessageDialog(null, "Username and password does not match.", "Error",
              JOptionPane.OK_OPTION);
          e1.printStackTrace();
          System.exit(-1);
        }

        /*
         * DAX - 4/30/20
         */
        ISQLDriver sqlDriver = new ISQLDriver() {

          @Override
          public void executeInsert(String cols, List<String> line, String schema, String string)
              throws NoConnectionException {
            // TODO Auto-generated method stub

          }


          @Override
          public ResultSet executeSelect(String query) throws NoConnectionException {
            // TODO Auto-generated method stub
            return null;
          }


          @Override
          public Connection getConnection() {
            return null;
          }


          @Override
          public String getLastRowAttributeID(String schema, String string)
              throws NoConnectionException {
            // TODO Auto-generated method stub
            return null;
          }


          @Override
          public long getLastRowID(String schema, String string) throws NoConnectionException {
            // TODO Auto-generated method stub
            return 0;
          }


          @Override
          public PreparedStatement prepareStatement(String query) throws SQLException {
            // TODO Auto-generated method stub
            return null;
          }
        };
        SchemaParser sParser = new SchemaParser(sqlDriver);
        String schemaSource = AnnotationApp.EHOST.getCommonName();
        sParser.processAnnotationSchemaReturnId(schemaFile, schemaSource);

        List<String> xmlFilesPathList = getXmlFilePaths();
        List<String> textFilesPathList = getTextFilePaths();
        List<List<AnnotatedPhrase>> docPhrases =
            checkTextFilesWithXML(schemaFile, xmlFilesPathList, textFilesPathList);

        System.exit(0);
      }


      private List<List<AnnotatedPhrase>> checkTextFilesWithXML(File schemaFile,
          List<String> xmlFilesPathList, List<String> textFilesPathList) {
        RaptatDocument doc;
        TextAnalyzer ta = new TextAnalyzer();
        PhraseTokenIntegrator integrator = new PhraseTokenIntegrator();

        List<AnnotatedPhrase> phrases;
        List<SchemaConcept> schemaConcepts = SchemaImporter.importSchemaConcepts(schemaFile);
        AnnotationImporter annotationImporter =
            new AnnotationImporter(SchemaImporter.getSchemaApp());

        List<List<AnnotatedPhrase>> phraseList = new ArrayList<List<AnnotatedPhrase>>();

        for (int i = 0; i < xmlFilesPathList.size(); i++) {
          phrases = annotationImporter.importAnnotations(xmlFilesPathList.get(i),
              textFilesPathList.get(i), null, schemaConcepts);

          doc = ta.processDocument(textFilesPathList.get(i));
          integrator.addProcessedTokensToAnnotations(phrases, doc, false, 0);

          phraseList.add(phrases);
        }

        return phraseList;
      }


      private List<String> getTextFilePaths() {
        // TODO Auto-generated method stub
        return null;
      }


      private List<String> getXmlFilePaths() {
        // TODO Auto-generated method stub
        return null;
      }
    });
  }

}
