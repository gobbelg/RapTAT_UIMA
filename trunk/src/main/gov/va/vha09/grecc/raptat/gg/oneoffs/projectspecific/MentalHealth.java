package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.projectspecific;

import javax.swing.SwingUtilities;
import src.main.gov.va.vha09.grecc.raptat.gg.analysis.crossvalidate.PhraseIDCrossValidationAnalyzer;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.core.annotation.Annotator;
import src.main.gov.va.vha09.grecc.raptat.gg.core.training.TokenSequenceSolutionTrainer;
import src.main.gov.va.vha09.grecc.raptat.gg.exporters.XMLExporterRevised;

public class MentalHealth {

  private enum RunType {
    DEFAULT_RUN, CROSS_VALIDATION, WRITE_TO_TAB_DELIMITED, TRAIN, ANNOTATE_DATA_SET;
  }

  public static void main(String[] args) {
    UserPreferences.INSTANCE.initializeLVGLocation();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        RunType runType = RunType.CROSS_VALIDATION;

        switch (runType) {
          case DEFAULT_RUN: {

          }
            break;
          case CROSS_VALIDATION: {
            String[] crossValidationArguments = new String[] {"-p",
                "C:\\Users\\VHATVHGOBBEG\\Documents\\SubversionMatheny"
                    + "\\branches\\GlennWorkspace\\Projects\\RaptatBaseProject"
                    + "\\trunk\\src\\main\\resources\\PropertyFilesForCommandLine\\"
                    + "MentalHealthProject\\CrossValidation\\"
                    + "CV_MentalHealth_NoGroupNotes_NegTagger_CombinedProvAndNonSI_201030_v02.prop"};
            PhraseIDCrossValidationAnalyzer.crossValidate(crossValidationArguments);
          }
            break;

          case TRAIN: {
            String[] trainingArguments = new String[] {"-p",
                "C:\\Users\\VHATVHGOBBEG\\Documents\\SubversionMatheny\\"
                    + "branches\\GlennWorkspace\\Projects\\RaptatBaseProject\\trunk"
                    + "\\src\\main\\resources\\PropertyFilesForCommandLine\\"
                    + "MentalHealthProject\\Training\\"
                    + "Training_MentalHealth_NoGroupNotes_NegTag_CombinedProviderAndNon_200630_v01.prop"};
            TokenSequenceSolutionTrainer.parseAndTrain(trainingArguments);
          }
            break;

          case ANNOTATE_DATA_SET: {
            String[] annotationArguments = new String[] {"-p",
                "C:\\Users\\VHATVHGOBBEG\\Documents\\SubversionMatheny\\branches\\GlennWorkspace\\"
                    + "Projects\\RaptatBaseProject\\trunk\\src\\main\\resources\\PropertyFilesForCommandLine\\"
                    + "MentalHealthProject\\Annotate\\Annotation_MentalHealth__200630_v01.prop"};
            Annotator.annotate(annotationArguments);
          }
            break;

          case WRITE_TO_TAB_DELIMITED: {
            String txtFolderPath =
                "D:\\GlennWorkspace\\SuicidalIdeationProject\\McCarthyProject_200617\\txtDocs";
            String xmlFolderPath =
                "D:\\GlennWorkspace\\SuicidalIdeationProject\\McCarthyProject_200617\\xmlRaptatGenerated_200630\\txtDocs_XMLOutput";
            XMLExporterRevised.xmlFilesToTabDelimited(xmlFolderPath, txtFolderPath);
          }
            break;
          default:
            break;
        }

        System.exit(0);
      }

    });

  }

}
