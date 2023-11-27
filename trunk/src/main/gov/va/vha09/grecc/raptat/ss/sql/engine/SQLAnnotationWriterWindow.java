package src.main.gov.va.vha09.grecc.raptat.ss.sql.engine;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.XMLExporterRevised;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.DbConnectionObject;
import src.main.gov.va.vha09.grecc.raptat.gg.sql.SQLAnnotationWriter;
import src.main.gov.va.vha09.grecc.raptat.ss.sql.drivers.SQLDriverException;

public class SQLAnnotationWriterWindow {
  public static SQLAnnotationWriterWindow windowInstance = null;

  private SQLAnnotationWriterWindow(DbConnectionObject dbConnectionObject) {
    JFrame mainFrame = new JFrame();

    JLabel lablDestinationPath = new JLabel("Enter path to the folder to write XML files: ");
    JLabel lablSourceXMLPath = new JLabel("Enter path to the folder containing source XML files: ");
    JLabel selectedDestinationPath = new JLabel("");
    JLabel selectedSourceXMLPath = new JLabel("");
    JButton buttonLogin = new JButton("Retrieve");

    buttonLogin.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        String pathForWritingXML = selectedDestinationPath.getText() + "/";
        String pathToSourceXMLFilesFolder = selectedSourceXMLPath.getText() + "/";
        if (pathForWritingXML.equals("") || pathToSourceXMLFilesFolder.equals("")) {
          JOptionPane.showMessageDialog(null, "Enter the input paraemters.");
        } else {
          // String pathForWritingXML = "D:/KarunaWorkspace/Temp/";
          // String pathToSourceXMLFilesFolder =
          // "D:\\KarunaWorkspace\\RapTAT\\ExamplesForKaruna\\ExamplesForKaruna\\XmlFiles\\";
          File directoryForWritingXML = new File(pathForWritingXML);

          boolean overwriteExisting = true;
          XMLExporterRevised exporter = new XMLExporterRevised(AnnotationApp.EHOST,
              directoryForWritingXML, overwriteExisting);

          SQLAnnotationWriter writer = new SQLAnnotationWriter(pathForWritingXML,
              pathToSourceXMLFilesFolder, exporter, dbConnectionObject);

          try {
            writer.retrieveXMl();
            JOptionPane.showMessageDialog(null, "Documents Retrieved!");
          } catch (SQLDriverException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }

        }
      }
    });

    JButton browseDestinationfolder = new JButton("Browse");
    browseDestinationfolder.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser("File Dialog");

        if (OptionsManager.getInstance().getLastSelectedDir() != null) {
          fileChooser.setCurrentDirectory(OptionsManager.getInstance().getLastSelectedDir());
        }

        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          selectedDestinationPath.setText(fileChooser.getSelectedFile().getPath());
        }

      }
    });

    JButton browseSourceXMLFolder = new JButton("Browse");
    browseSourceXMLFolder.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser("File Dialog");

        if (OptionsManager.getInstance().getLastSelectedDir() != null) {
          fileChooser.setCurrentDirectory(OptionsManager.getInstance().getLastSelectedDir());
        }

        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          selectedSourceXMLPath.setText(fileChooser.getSelectedFile().getPath());
        }

      }
    });

    JPanel newPanel = new JPanel(new GridBagLayout());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.anchor = GridBagConstraints.WEST;
    constraints.insets = new Insets(10, 10, 10, 10);

    // add components to the panel
    constraints.gridx = 0;
    constraints.gridy = 0;
    newPanel.add(lablDestinationPath, constraints);

    constraints.gridx = 1;
    newPanel.add(selectedDestinationPath, constraints);

    constraints.gridx = 2;
    newPanel.add(browseDestinationfolder, constraints);

    constraints.gridx = 0;
    constraints.gridy = 1;
    newPanel.add(lablSourceXMLPath, constraints);

    constraints.gridx = 1;
    newPanel.add(selectedSourceXMLPath, constraints);

    constraints.gridx = 2;
    newPanel.add(browseSourceXMLFolder, constraints);

    constraints.gridx = 0;
    constraints.gridy = 3;
    constraints.gridwidth = 2;
    constraints.anchor = GridBagConstraints.CENTER;
    newPanel.add(buttonLogin, constraints);

    // set border for the panel
    newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
        "Retireve XML from Database"));

    // add the panel to this frame
    mainFrame.add(newPanel);
    mainFrame.setSize(900, 200);
    // mainFrame.pack();
    mainFrame.setLocationRelativeTo(null);
    mainFrame.setVisible(true);
  }


  public static synchronized SQLAnnotationWriterWindow getInstance() {
    if (SQLAnnotationWriterWindow.windowInstance == null) {
      DbConnectionObject dbConnectionObject = null;
      SQLAnnotationWriterWindow.windowInstance = new SQLAnnotationWriterWindow(dbConnectionObject);
    }

    return SQLAnnotationWriterWindow.windowInstance;
  }


  public static void main(String[] args) {
    // SQLAnnotationWriterWindow.getInstance();
    DbConnectionObject dbConnectionObject = null;
    SQLAnnotationWriterWindow v = new SQLAnnotationWriterWindow(dbConnectionObject);

  }

}
