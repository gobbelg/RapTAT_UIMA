/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package src.main.gov.va.vha09.grecc.raptat.ss.sql.engine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotatedPhrase;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.WindowHelper;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.schema.SchemaImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.importer.xml.AnnotationImporter;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.PhraseTokenIntegrator;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.RaptatDocument;
import src.main.gov.va.vha09.grecc.raptat.gg.textanalysis.TextAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.ISQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriver;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriver.ConnectionResult;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriverException;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.exception.NoConnectionException;

/**
 *
 * @author VHATVHSAHAS1
 */
@SuppressWarnings("rawtypes")
public class AnnotationDBWindow extends JDialog {

  private class LoginListener implements ActionListener {
    Point position;
    Dimension dimension;

    public LoginListener(Point p, Dimension d) {
      position = p;
      dimension = d;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

      String userName = userField.getText();
      String password = new String(passField.getPassword());
      String dbName = dbNameField.getText();
      String serverName = serverNameField.getText();

      if (serverName.equals("")) {
        JOptionPane.showMessageDialog(null, "Server Name field can not be empty.", "Error",
            JOptionPane.OK_OPTION);
        return;
      }

      if (dbName.equals("")) {
        JOptionPane.showMessageDialog(null, "Database Name field can not be empty.", "Error",
            JOptionPane.OK_OPTION);
        return;
      }

      boolean has_connection = false;

      /*
       * DAX - 4/30/20 - Updated to resolve closing warning and test for connection result.
       */
      try (SQLDriver driver = new SQLDriver(serverName, dbName, userName, password)) {
        has_connection = driver.test_connection() == ConnectionResult.Success;
      } catch (Exception e1) {
        has_connection = false;
        e1.printStackTrace();
      }

      if (has_connection) {
        JOptionPane.showMessageDialog(null, "Error login. Check all info.", "Error",
            JOptionPane.OK_OPTION);
      } else {
        AnnotationDBWindow.this.remove(loginPanel);
        // initializeUpload( this.position, this.dimension );
        initializeSchemaName(position, dimension, dbName);
        validate();
        AnnotationDBWindow.this.repaint();
      }
    }
  }

  private class SelectListener implements ActionListener {
    Point position;
    Dimension dimension;
    String dbName;

    public SelectListener(Point p, Dimension d, String dbName) {
      position = p;
      dimension = d;
      this.dbName = dbName;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
      String schemaName = schemaNameField.getText();

      /*
       * DAX - 4/30/20 - Updated to convert how SQLDriver is used.
       */
      String userName = userField.getText();
      String password = new String(passField.getPassword());
      String dbName = dbNameField.getText();
      String serverName = serverNameField.getText();
      SQLDriver driver = null;
      Connection connect = null;
      try {
        driver = new SQLDriver(serverName, dbName, userName, password);
        connect = driver.connect();
      } catch (SQLDriverException e2) {
        connect = null;
        e2.printStackTrace();
      }

      if (connect == null && driver != null) {
        JOptionPane.showMessageDialog(null, "Error login. Check all info.", "Error",
            JOptionPane.OK_OPTION);
      } else {
        try {
          if (driver.tablesExists(schemaName)) {
            SQLDriver.closeConnection(connect);
            AnnotationDBWindow.this.remove(prefixPanel);
            initializeUpload(position, dimension, schemaName);
            validate();
            AnnotationDBWindow.this.repaint();
          } else {
            JOptionPane.showMessageDialog(null, "This schema does not exist.", "Error",
                JOptionPane.OK_OPTION);
          }
        } catch (NoConnectionException e1) {
          JOptionPane.showMessageDialog(null, "Username and password does not match.", "Error",
              JOptionPane.OK_OPTION);
          e1.printStackTrace();
        }
      }
    }
  }

  private class UploadAnnotationListener implements ActionListener {
    String _schemaName;

    public UploadAnnotationListener(String prefix) {
      _schemaName = prefix;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

      /*
       * See comment at end of this method
       */
      GeneralHelper.errorWriter("testProg() method in AnnotationDBWindow is not functional");
      System.exit(-1);

      if (schemaFile == null) {
        JOptionPane.showMessageDialog(null, "Schema File is not Selected.", "File selection error",
            JOptionPane.OK_OPTION);
        return;
      }

      if (fileMapPath == null) {
        JOptionPane.showMessageDialog(null, "File-Map File is not Selected.",
            "File selection error", JOptionPane.OK_OPTION);
        return;
      }

      /*
       * DAX - 4/30/20 - added sqlDriver in order to path update to SchemerParser and
       * AnnotationsToDbUploader
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
          // TODO Auto-generated method stub
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
      String schemaSource = comboBoxScoringMode.getSelectedItem().toString();

      long schemaID = sParser.processAnnotationSchemaReturnId(schemaFile, schemaSource);
      List<List<AnnotatedPhrase>> phraseList = checkTextFilesWithXML();
      HashMap<String, String> fileRefMap = XLSXReader.getFileReferenceNoMap(fileMapPath.getText());
      AnnotationsToDbUploader uploadAnnotationsToDB = new AnnotationsToDbUploader(sqlDriver);
      String[] docPath;
      String docName;

      for (int i = 0; i < xmlFilesPathList.size(); i++) {

        /*
         * Glenn comment - 10/1/18 - Use File.separator to make sure that file separator character
         * works on all systems - also consider using Patter.quote() method here, which should make
         * it easier to understand what you are doing.
         */
        // docPath =
        // AnnotationDBWindow.this.xmlFilesPathList.get(i).split("\\\\");
        // docName = docPath[docPath.length - 1].split("\\.")[0];

        String separatorPattern = Pattern.quote(File.separator);
        docPath = xmlFilesPathList.get(i).split(separatorPattern);
        String periodPattern = Pattern.quote(".");
        docName = docPath[docPath.length - 1].split(periodPattern)[0];

        /*
         * Commented out due to changes in code that lead to errors. This needs to be fixed to
         * continue using this code.
         */
        // uploadAnnotationsToDB.uploadAnnotations(schemaID, phraseList.get(i),
        // Optional.of(textFilesPathList.get(i)), Optional.empty(), fileRefMap.get(docName),
        // _schemaName);
        // System.out.println("uploaded file: " + i + 1);
      }

      JOptionPane.showMessageDialog(null, "Annotations uploaded to Database.", "DB message",
          JOptionPane.OK_OPTION);
    }
  }

  protected class AddReferenceXMLFileListener implements ActionListener {

    /**
     * Invokes a File chooser to select the Reference XML file location.
     *
     * @param ev Action Event for the listener
     */
    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent ev) {
      JFileChooser fileChooser = new JFileChooser("File Dialog");

      if (OptionsManager.getInstance().getLastSelectedDir() != null) {
        fileChooser.setCurrentDirectory(OptionsManager.getInstance().getLastSelectedDir());
      }

      fileChooser.setMultiSelectionEnabled(true);
      fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      fileChooser.setFileFilter(new FileNameExtensionFilter("XML files only", "xml"));

      int returnVal = fileChooser.showOpenDialog(null);
      File[] dirFiles;

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File[] selectedFiles = fileChooser.getSelectedFiles();
        OptionsManager.getInstance().setLastSelectedDir(fileChooser.getCurrentDirectory());

        for (File selectedFile : selectedFiles) {
          if (WindowHelper.validFile(selectedFile)
              && !xmlListModel.contains(selectedFile.getName())) {
            xmlListModel.addElement(selectedFile.getName());
            xmlFilesPathList.add(selectedFile.getAbsolutePath());
          } else if (selectedFile.isDirectory()) {
            dirFiles = selectedFile.listFiles();

            for (File dirFile : dirFiles) {
              if (WindowHelper.validFile(dirFile) && !xmlListModel.contains(dirFile.getName())) {
                xmlListModel.addElement(dirFile.getName());
                xmlFilesPathList.add(dirFile.getAbsolutePath());
              }
            }
          }
        }
      }
    }
  }

  protected class AddTextFileListener implements ActionListener {

    /**
     * Invokes a File chooser to select the Text file location.
     *
     * @param ev Action Event for the listener
     */
    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent ev) {
      JFileChooser fileChooser = new JFileChooser("File Dialog");

      if (OptionsManager.getInstance().getLastSelectedDir() != null) {
        fileChooser.setCurrentDirectory(OptionsManager.getInstance().getLastSelectedDir());
      }

      fileChooser.setMultiSelectionEnabled(true);
      fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
      fileChooser.setFileFilter(new FileNameExtensionFilter("Text files only", "txt"));

      int returnVal = fileChooser.showOpenDialog(null);
      File[] dirFiles;

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File[] selectedFiles = fileChooser.getSelectedFiles();
        OptionsManager.getInstance().setLastSelectedDir(fileChooser.getCurrentDirectory());

        for (File selectedFile : selectedFiles) {
          if (WindowHelper.validFile(selectedFile)
              && !textFileListModel.contains(selectedFile.getName())) {
            textFileListModel.addElement(selectedFile.getName());
            textFilesPathList.add(selectedFile.getAbsolutePath());
          } else if (selectedFile.isDirectory()) {
            dirFiles = selectedFile.listFiles();

            for (File dirFile : dirFiles) {
              if (WindowHelper.validFile(dirFile)
                  && !textFileListModel.contains(dirFile.getName())) {
                textFileListModel.addElement(dirFile.getName());
                textFilesPathList.add(dirFile.getAbsolutePath());
              }
            }
          }
        }
      }
    }
  }

  protected class GetFileMapListerner implements ActionListener {

    /**
     * Create the Hash Set for the concepts.
     *
     * @param e Action Event for the listener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser =
          new JFileChooser(OptionsManager.getInstance().getLastSelectedDir());

      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setFileFilter(new FileNameExtensionFilter("Select File Map", "xlsx"));

      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        fileMapFile = fileChooser.getSelectedFile();
        fileMapPath.setText(fileMapFile.getAbsolutePath());

        OptionsManager.getInstance().setLastSelectedDir(fileChooser.getCurrentDirectory());
      }
    }
  }

  protected class GetSchemaFileListerner implements ActionListener {

    /**
     * Create the Hash Set for the concepts.
     *
     * @param e Action Event for the listener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      JFileChooser fileChooser =
          new JFileChooser(OptionsManager.getInstance().getLastSelectedDir());

      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      fileChooser.setFileFilter(new FileNameExtensionFilter("Select Schema File", "xml"));

      if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        schemaFile = fileChooser.getSelectedFile();
        schemaFilePath.setText(schemaFile.getAbsolutePath());

        OptionsManager.getInstance().setLastSelectedDir(fileChooser.getCurrentDirectory());
      }
    }
  }

  protected class RemoveReferenceXMLFileListener implements ActionListener {

    /**
     * Removes the selected Reference XML files.
     *
     * @param e Action Event for the listener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      for (int i = xmlSelections.length - 1; i >= 0; i--) {
        xmlFilesPathList.remove(xmlSelections[i]);
        xmlListModel.removeElementAt(xmlSelections[i]);
      }
    }
  }

  protected class RemoveTextFileListener implements ActionListener {

    /**
     * Removes the selected Text files.
     *
     * @param e Action Event for the listener
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      for (int i = textFileSelections.length - 1; i >= 0; i--) {
        textFilesPathList.remove(textFileSelections[i]);
        textFileListModel.removeElementAt(textFileSelections[i]);
      }
    }
  }

  /**
   *
   */
  private static final long serialVersionUID = -4702261440738050638L;

  public static AnnotationDBWindow windowInstance = null;

  protected DefaultListModel xmlListModel;

  protected DefaultListModel textFileListModel;

  protected JList xmlList;
  protected JList textFileList;
  protected List<String> xmlFilesPathList;

  protected List<String> textFilesPathList;
  protected JTextField schemaFilePath;
  protected JTextField fileMapPath;
  private JComboBox comboBoxScoringMode;
  protected int[] xmlSelections;

  protected int[] textFileSelections;

  protected File schemaFile = null;

  protected File fileMapFile = null;

  private JTextField dbNameField;

  private JTextField serverNameField;

  private JTextField schemaNameField;

  private JTextField userField;

  private JPasswordField passField;

  private JPanel loginPanel;

  private JPanel prefixPanel;

  public AnnotationDBWindow() {

  }


  private AnnotationDBWindow(Point thePoint, Dimension theDimension) {
    xmlFilesPathList = new ArrayList<String>();
    textFilesPathList = new ArrayList<String>();

    xmlListModel = new DefaultListModel();
    textFileListModel = new DefaultListModel();

    // initializeUpload(thePoint, theDimension);
    initializeLogin(thePoint, theDimension);
  }



  public void testProg() {

    /*
     * See comment at end of this method
     */
    GeneralHelper.errorWriter("testProg() method in AnnotationDBWindow is not functional");
    System.exit(-1);;

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
        // TODO Auto-generated method stub
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


      /**
       * DAX - 4/30/20 - Added dummy ISQLDriver, which I don't think will cause an error, as we're
       * not using this path right now. However, this will need to be resolved later.
       */

      @Override
      public PreparedStatement prepareStatement(String query) throws SQLException {
        // TODO Auto-generated method stub
        return null;
      }
    };

    xmlFilesPathList = new ArrayList<String>();
    textFilesPathList = new ArrayList<String>();

    schemaFile = new File("P:\\workspace\\LEO_Raptat\\data\\New folder\\projectschema.xml");


    SchemaParser sParser = new SchemaParser(sqlDriver);
    AnnotationsToDbUploader uploadAnnotationsToDB = new AnnotationsToDbUploader(sqlDriver);

    long schemaID;
    List<List<AnnotatedPhrase>> phraseList;
    HashMap<String, String> fileRefMap = XLSXReader.getFileReferenceNoMap(
        "P:\\workspace\\LEO_Raptat\\data\\New folder\\AnnotationID_TIUdocumentSID Correspondence.xlsx");

    String[] docPath;
    String docName;

    for (int j = 2; j < 14; j++) {
      // docName = path + j + "_Fern\\";
      docName = "D:\\CARTCL-IIR\\ActiveLearning\\Sanjib\\AdjudicatedAKI_Block_01_Copy\\";

      // schemaFile = new File(path + j + "_Fern\\" +
      // "config\\projectschema.xml");
      schemaFile = new File(docName + "config\\projectschema.xml");
      schemaID = sParser.processAnnotationSchemaReturnId(schemaFile, "eHost");

      xmlFilesPathList.clear();
      for (File file : Arrays.asList(new File(docName + "saved\\").listFiles())) {
        xmlFilesPathList.add(file.getAbsolutePath());
      }

      textFilesPathList.clear();
      for (File file : Arrays.asList(new File(docName + "corpus\\").listFiles())) {
        textFilesPathList.add(file.getAbsolutePath());
      }

      phraseList = checkTextFilesWithXML();

      for (int i = 0; i < xmlFilesPathList.size(); i++) {
        docPath = xmlFilesPathList.get(i).split("\\\\");
        docName = docPath[docPath.length - 1].split("\\.")[0];

        /*
         * Commented out due to errors
         */
        // uploadAnnotationsToDB.uploadAnnotations(schemaID, phraseList.get(i), Optional.empty(),
        // Optional.of(xmlFilesPathList.get(i)), fileRefMap.get(docName), "");
        // System.err.println("uploaded file: " + i);
      }
    }
  }


  @SuppressWarnings("unchecked")
  private JPanel buildSchemaPanel() {
    JPanel mainPanel = new JPanel(new BorderLayout());

    JPanel schemaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
    JButton selectSchemaButton = new JButton("<html><center>Select<br>Schema File</center></html>");
    selectSchemaButton.addActionListener(new GetSchemaFileListerner());
    schemaPanel.add(selectSchemaButton);

    schemaFilePath = new JTextField(30);
    schemaFilePath.setEditable(false);
    schemaFilePath.setText("Schema file path.");
    schemaFilePath.setHorizontalAlignment(SwingConstants.LEFT);
    schemaPanel.add(schemaFilePath);

    comboBoxScoringMode = new JComboBox();
    comboBoxScoringMode.setPreferredSize(new Dimension(150, 40));
    Border b2 = BorderFactory.createTitledBorder("Schema Source");
    comboBoxScoringMode.setBorder(b2);
    comboBoxScoringMode.addItem("eHost");
    comboBoxScoringMode.addItem("Knowtator");
    schemaPanel.add(comboBoxScoringMode);
    mainPanel.add(schemaPanel, BorderLayout.CENTER);

    JPanel fileMapPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
    JButton selectFileMapButton =
        new JButton("<html><center>Select<br>File-Map File</center></html>");
    selectFileMapButton.addActionListener(new GetFileMapListerner());
    fileMapPanel.add(selectFileMapButton);

    fileMapPath = new JTextField(30);
    fileMapPath.setEditable(false);
    fileMapPath.setText("File map file path.");
    fileMapPath.setHorizontalAlignment(SwingConstants.LEFT);
    fileMapPanel.add(fileMapPath);
    mainPanel.add(fileMapPanel, BorderLayout.SOUTH);

    Font borderFont =
        new Font(schemaPanel.getFont().getFontName(), Font.ITALIC, schemaPanel.getFont().getSize());
    Border b3 = BorderFactory.createTitledBorder(
        BorderFactory.createBevelBorder(BevelBorder.RAISED), "Select configurations files",
        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, borderFont);
    mainPanel.setBorder(b3);

    return mainPanel;
  }


  private List<List<AnnotatedPhrase>> checkTextFilesWithXML() {
    RaptatDocument doc;
    TextAnalyzer ta = new TextAnalyzer();
    PhraseTokenIntegrator integrator = new PhraseTokenIntegrator();

    List<AnnotatedPhrase> phrases;
    List<SchemaConcept> schemaConcepts = SchemaImporter.importSchemaConcepts(schemaFile);
    AnnotationImporter annotationImporter = new AnnotationImporter(SchemaImporter.getSchemaApp());

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


  private JPanel getOptionsPanelLogin() {
    JPanel dbPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 5));

    Border b1 = BorderFactory.createTitledBorder("Server Name");
    serverNameField = new JTextField(20);
    serverNameField.setBorder(b1);
    serverNameField.setHorizontalAlignment(SwingConstants.LEFT);
    dbPanel.add(serverNameField);

    Border b3 = BorderFactory.createTitledBorder("Database Name");
    dbNameField = new JTextField(20);
    dbNameField.setBorder(b3);
    dbNameField.setHorizontalAlignment(SwingConstants.LEFT);
    dbPanel.add(dbNameField);

    Font borderFont =
        new Font(dbPanel.getFont().getFontName(), Font.ITALIC, dbPanel.getFont().getSize());
    Border b4 =
        BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
            "Database", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, borderFont);
    dbPanel.setBorder(b4);

    return dbPanel;
  }


  private Component getRunPanel(String prefix) {
    Box runPanel = Box.createVerticalBox();
    runPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton runButton = new JButton("Upload Annotations");
    runButton.addActionListener(new UploadAnnotationListener(prefix));

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        try {
          AnnotationDBWindow.windowInstance.dispose();
          AnnotationDBWindow.windowInstance = null;
        } catch (Throwable ex) {
          Logger.getLogger(AnnotationDBWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });

    buttonPanel.add(runButton);
    buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    buttonPanel.add(cancelButton);
    runPanel.add(buttonPanel);

    return runPanel;
  }


  private Component getRunPanelLogin(Point thePosition, Dimension theDimension) {
    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton loginButton = new JButton("Login");
    JButton cancelButton = new JButton("Cancel");

    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        AnnotationDBWindow.windowInstance.dispose();
        AnnotationDBWindow.windowInstance = null;
      }
    });

    buttonPanel.add(loginButton);
    loginButton.addActionListener(new LoginListener(thePosition, theDimension));

    buttonPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    buttonPanel.add(cancelButton);

    return buttonPanel;
  }


  private JPanel getUserPassPanelLogin() {
    JPanel userDetailPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 5));

    Border brdr = BorderFactory.createTitledBorder("User Name");
    userField = new JTextField(20);
    userField.setBorder(brdr);
    userField.setHorizontalAlignment(SwingConstants.LEFT);
    userDetailPanel.add(userField);

    brdr = BorderFactory.createTitledBorder("Password");
    passField = new JPasswordField(20);
    passField.setBorder(brdr);
    passField.setHorizontalAlignment(SwingConstants.LEFT);
    userDetailPanel.add(passField);

    Font borderFont = new Font(userDetailPanel.getFont().getFontName(), Font.ITALIC,
        userDetailPanel.getFont().getSize());
    Border b2 =
        BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
            "Admin Credentials", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, borderFont);
    userDetailPanel.setBorder(b2);

    return userDetailPanel;
  }


  private void initializeLogin(Point thePosition, Dimension theDimension) {
    int windowWidth = (int) (theDimension.width * 1.0);
    int windowHeight = (int) (theDimension.height * 0.8);

    windowWidth = windowWidth < Constants.TRAIN_WINDOW_MIN_WIDTH ? Constants.TRAIN_WINDOW_MIN_WIDTH
        : windowWidth;
    windowHeight =
        windowHeight < Constants.TRAIN_WINDOW_MIN_HEIGHT ? Constants.TRAIN_WINDOW_MIN_HEIGHT
            : windowHeight;

    this.setSize(windowWidth, windowHeight);
    setTitle("Database Login");
    setResizable(false);
    thePosition.translate(10, 10);

    this.setLocation(thePosition);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        AnnotationDBWindow.windowInstance.dispose();
        AnnotationDBWindow.windowInstance = null;
      }
    });

    loginPanel = new JPanel(new BorderLayout());
    loginPanel.add(getOptionsPanelLogin(), BorderLayout.NORTH);
    loginPanel.add(getUserPassPanelLogin(), BorderLayout.CENTER);
    loginPanel.add(getRunPanelLogin(thePosition, theDimension), BorderLayout.SOUTH);

    this.add(loginPanel);
    setVisible(true);
    pack();
  }


  private void initializeSchemaName(Point thePosition, Dimension theDimension, String dbName) {
    int windowWidth = (int) (theDimension.width * 0.7);
    int windowHeight = (int) (theDimension.height * 0.8);

    windowWidth = windowWidth < Constants.TRAIN_WINDOW_MIN_WIDTH ? Constants.TRAIN_WINDOW_MIN_WIDTH
        : windowWidth;
    windowHeight =
        windowHeight < Constants.TRAIN_WINDOW_MIN_HEIGHT ? Constants.TRAIN_WINDOW_MIN_HEIGHT
            : windowHeight;

    thePosition.translate(10, 10);
    this.setSize(windowWidth, windowHeight);
    setTitle("Table Prefix");
    setResizable(false);
    this.setLocation(thePosition);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        AnnotationDBWindow.windowInstance.dispose();
        AnnotationDBWindow.windowInstance = null;
      }
    });

    prefixPanel = new JPanel(new BorderLayout());

    JPanel inputPanel = new JPanel(new FlowLayout());
    Border b1 = BorderFactory.createTitledBorder("Schema Name");
    schemaNameField = new JTextField(20);
    schemaNameField.setBorder(b1);
    schemaNameField.setHorizontalAlignment(SwingConstants.LEFT);
    inputPanel.add(schemaNameField);

    JCheckBox chkbox = new JCheckBox("No Prefix");
    chkbox.setSelected(false);
    chkbox.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean selected = ((AbstractButton) e.getSource()).getModel().isSelected();

        if (selected) {
          schemaNameField.setText("");
          schemaNameField.setEditable(false);
        } else {
          schemaNameField.setEditable(true);
        }
      }
    });
    inputPanel.add(chkbox);
    prefixPanel.add(inputPanel, BorderLayout.NORTH);

    JPanel selectPanel = new JPanel(new FlowLayout());
    JButton selectButton = new JButton("Select");
    JButton cancelButton = new JButton("Cancel");

    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        AnnotationDBWindow.windowInstance.dispose();
        AnnotationDBWindow.windowInstance = null;
      }
    });

    selectPanel.add(selectButton);
    selectButton.addActionListener(new SelectListener(thePosition, theDimension, dbName));

    selectPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    selectPanel.add(cancelButton);
    prefixPanel.add(selectPanel, BorderLayout.SOUTH);

    this.add(prefixPanel);
    setVisible(true);
    pack();
  }


  private void initializeUpload(Point thePosition, Dimension theDimension, String prefix) {
    int windowWidth = (int) (theDimension.width * 0.7);
    int windowHeight = (int) (theDimension.height * 0.8);

    windowWidth = windowWidth < Constants.TRAIN_WINDOW_MIN_WIDTH ? Constants.TRAIN_WINDOW_MIN_WIDTH
        : windowWidth;
    windowHeight =
        windowHeight < Constants.TRAIN_WINDOW_MIN_HEIGHT ? Constants.TRAIN_WINDOW_MIN_HEIGHT
            : windowHeight;

    this.setSize(windowWidth, windowHeight);
    setTitle("Upload Annotations");
    setResizable(false);
    thePosition.translate(10, 10);

    this.setLocation(thePosition);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        try {
          AnnotationDBWindow.windowInstance.dispose();
          AnnotationDBWindow.windowInstance = null;
          finalize();
        } catch (Throwable ex) {
          Logger.getLogger(AnnotationDBWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
      };
    });

    // Creates the Box
    Box listPanel = Box.createHorizontalBox();

    JPanel listButtonPanel = new JPanel(new GridLayout(1, 2));
    JPanel generalButtonPanel = new JPanel(new BorderLayout());
    JPanel optionsRunPanel = new JPanel(new GridLayout(1, 2));

    listPanel.setPreferredSize(new Dimension(windowWidth, (int) (windowHeight * 5.0 / 8)));

    // Adding the box for file lists
    setLayout(new BorderLayout());
    this.add(listPanel, BorderLayout.NORTH);

    Dimension panelSize = new Dimension((int) (0.35 * listPanel.getPreferredSize().width),
        listPanel.getPreferredSize().height);

    listPanel.add(buildXMLListPanel(panelSize));
    listPanel.add(buildTextFileListPanel(panelSize));

    // Adding the add and remove file buttons
    this.add(generalButtonPanel, BorderLayout.SOUTH);
    generalButtonPanel.add(listButtonPanel, BorderLayout.NORTH);

    panelSize = new Dimension((int) (0.30 * listPanel.getPreferredSize().width),
        (int) (0.10 * listPanel.getPreferredSize().height));

    JPanel refXMLButtonsPanel = new JPanel(new FlowLayout());
    refXMLButtonsPanel.add(getAddRemoveButtonPanel(new AddReferenceXMLFileListener(),
        new RemoveReferenceXMLFileListener(), "Reference XML", panelSize));
    listButtonPanel.add(refXMLButtonsPanel);

    JPanel textButtonsPanel = new JPanel(new FlowLayout());
    textButtonsPanel.add(getAddRemoveButtonPanel(new AddTextFileListener(),
        new RemoveTextFileListener(), "Text", panelSize));
    listButtonPanel.add(textButtonsPanel);

    // Adding Upload button
    optionsRunPanel.add(getRunPanel(prefix));
    optionsRunPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createEmptyBorder(2, 2, 2, 2), BorderFactory.createLineBorder(Color.GRAY)));

    generalButtonPanel.add(Box.createRigidArea(new Dimension(0, 40)));
    generalButtonPanel.add(buildSchemaPanel(), BorderLayout.CENTER);
    generalButtonPanel.add(optionsRunPanel, BorderLayout.SOUTH);
    setVisible(true);
    pack();
  }


  @SuppressWarnings("unchecked")
  protected JPanel buildTextFileListPanel(Dimension panelSize) {
    JScrollPane scrollPane = new JScrollPane();

    textFileList = new JList(textFileListModel);
    textFileList.setBorder(
        new TitledBorder(null, "Text Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));

    scrollPane.setViewportView(textFileList);

    ListSelectionListener ListSelLis = new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
          textFileList = (JList) listSelectionEvent.getSource();
          textFileSelections = textFileList.getSelectedIndices();
        }
      }
    };

    textFileList.addListSelectionListener(ListSelLis);

    scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setPreferredSize(panelSize);

    JPanel thePanel = new JPanel();
    thePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    thePanel.add(scrollPane);

    return thePanel;
  }


  @SuppressWarnings("unchecked")
  protected JPanel buildXMLListPanel(Dimension panelSize) {
    JScrollPane scrollPane = new JScrollPane();

    xmlList = new JList(xmlListModel);
    xmlList.setBorder(
        new TitledBorder(null, "XML Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));

    scrollPane.setViewportView(xmlList);

    ListSelectionListener ListSelLis = new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (!listSelectionEvent.getValueIsAdjusting()) {
          xmlList = (JList) listSelectionEvent.getSource();
          xmlSelections = xmlList.getSelectedIndices();
        }
      }
    };

    xmlList.addListSelectionListener(ListSelLis);

    scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
    scrollPane.setPreferredSize(panelSize);

    JPanel thePanel = new JPanel();
    thePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    thePanel.add(scrollPane);

    return thePanel;
  }


  protected JPanel getAddRemoveButtonPanel(ActionListener addFileListener,
      ActionListener removeFileListener, String chooseButtonString, Dimension panelSize) {

    JButton chooseButton = new JButton("Add");
    chooseButton.addActionListener(addFileListener);

    JButton removeButton = new JButton("Remove");
    removeButton.addActionListener(removeFileListener);

    JPanel theButtonPanel = new JPanel(new GridBagLayout());
    theButtonPanel.setPreferredSize(panelSize);

    GridBagConstraints c = new GridBagConstraints();

    c.weightx = 0.5;
    c.gridx = 0;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_START;
    theButtonPanel.add(chooseButton, c);

    c.weightx = 0.5;
    c.gridx = 1;
    c.gridy = 0;
    c.anchor = GridBagConstraints.LINE_END;
    theButtonPanel.add(removeButton, c);

    return theButtonPanel;
  }


  public static synchronized AnnotationDBWindow getInstance(Point thePoint,
      Dimension theDimension) {
    if (AnnotationDBWindow.windowInstance == null) {
      AnnotationDBWindow.windowInstance = new AnnotationDBWindow(thePoint, theDimension);
    }

    return AnnotationDBWindow.windowInstance;
  }


  public static void main(String[] args) {
    AnnotationDBWindow.getInstance(new Point(100, 100), new Dimension(600, 800));

    // AnnotationDBWindow w = new AnnotationDBWindow();
    // w.testProg();
    // w.test2();
  }
}
