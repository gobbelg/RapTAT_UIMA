package src.main.gov.va.vha09.grecc.raptat.rn.uima.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
//import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.ConfigurationParameterSettings;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;

import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.sequenceidentification.probabilistic.ProbabilisticTSFinderSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.RaptatAnnotationSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.TokenSequenceFinderSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.core.options.OptionsManager;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.cpe.FileSystemCollectionReader;
import src.main.gov.va.vha09.grecc.raptat.rn.misc.BridgeClass;

public class UIMARaptatTraining {
	private static final String TEXT_READER_DESC = "/TextCollectionReaderDescriptor.xml";
	private static final String AGGREGATE_ENGINE_DESC = "/UIMAAggregateAnnotatorTraining.xml";
	private static Logger logger = Logger.getLogger(UIMARaptatTraining.class);
	
	public UIMARaptatTraining() {
		int lvgSetting = UserPreferences.INSTANCE.initializeLVGLocation();
		if (lvgSetting == 0) {
			GeneralHelper.errorWriter("Please install the 2012 Lexical Variant"
					+ " Generator (LVG) from the U.S. National Library of Medicine before running RapTAT");
			System.exit(-1);
		}
	}

	private boolean setTextDirectory(String[] inputArgs, CollectionReaderDescription cpeDesc) {
		ConfigurationParameterSettings cpeSettings =
				cpeDesc.getCollectionReaderMetaData().getConfigurationParameterSettings();
		if(inputArgs == null)
			return true;
		if (inputArgs.length > 0 && inputArgs[0].length() > 0) {
			if (cpeSettings.isModifiable()) {
				cpeSettings.setParameterValue(FileSystemCollectionReader.PARAM_INPUTDIR, inputArgs[0]);
				return true;
			}
			return false;
		}

		cpeSettings.setParameterValue(FileSystemCollectionReader.PARAM_INPUTDIR, " ");
		String inputDir = (String) cpeSettings.getParameterValue(FileSystemCollectionReader.PARAM_INPUTDIR);
		if (inputDir == null || inputDir.length() == 0) {
			if (cpeSettings.isModifiable()) {
				if (inputDir != null & inputDir.length() > 0) {
					cpeSettings.setParameterValue(FileSystemCollectionReader.PARAM_INPUTDIR, inputDir);
					cpeSettings.setParameterValue(FileSystemCollectionReader.PARAM_SUBDIR, true);
					return true;
				}
			}
			return false;
		}
		return true;
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				runRaptat(args);
			}
		});
	}

	public static void runRaptat(final String[] args) {
		
		UIMARaptatTraining raptatInstance = new UIMARaptatTraining();
		
		
		
		String relativePathToDescriptors = System.getProperty("user.home") + "/uimaRaptatInception/" + "/descrTemp/";
		//String relativePathToDescriptors = "C:\\Projects\\SVN_Projects\\UIMARaptat\\trunk\\src\\main\\resources\\desc\\";
		writeDescriptorFileFromJar();

		try {
			URL readerURL = new File(relativePathToDescriptors + TEXT_READER_DESC).toURI().toURL();
			CollectionReaderDescription cpeDesc = UIMAFramework.getXMLParser().parseCollectionReaderDescription(new XMLInputSource(readerURL));

			if (raptatInstance.setTextDirectory(args, cpeDesc)) {
				URL aeURL = new File(relativePathToDescriptors + AGGREGATE_ENGINE_DESC).toURI().toURL();
				ResourceSpecifier aeSpecifier = UIMAFramework.getXMLParser().parseResourceSpecifier(new XMLInputSource(aeURL));
				AnalysisEngine aggregateTrainer = UIMAFramework.produceAnalysisEngine(aeSpecifier);
				CAS aCas = aggregateTrainer.newCAS();
				for (String annotatedDocumentFilePath: BridgeClass.annotatedDocumentFilePaths) {
					BridgeClass.nextTrainingDocumentFilePath = annotatedDocumentFilePath;
					File annotatedDocument = new File(annotatedDocumentFilePath);
					String annotatedDocumentText = FileUtils.readFileToString(annotatedDocument, StandardCharsets.UTF_8);
					aCas.setDocumentText(annotatedDocumentText);
					
					try {
						aggregateTrainer.process(aCas);
						aCas.reset();
					}
					catch (AnalysisEngineProcessException e) {
						e.printStackTrace();
					}
				}
				TokenSequenceFinderSolution solution = new ProbabilisticTSFinderSolution(System.getProperty("user.home") + "\\tree.txt",
				        BridgeClass.probabilisticSequenceIDTrainer.getSequenceIDStructure(), BridgeClass.conceptMapSolution,
				        null, 7,
				        BridgeClass.probabilisticSequenceIDTrainer.getPhraseIDSmoothingMethod(), BridgeClass.schemaConcepts);
				
				RaptatAnnotationSolution.packageAndSaveSolution(null, solution, new File(BridgeClass.solutionFilePath));
				aggregateTrainer.destroy();
			}
		}

		catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (InvalidXMLException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (ResourceInitializationException e) {
			System.out.println(e);
			e.printStackTrace();}
	}

	public static void writeDescriptorFileFromJar() {
		File descrFileDirectoryTemp = new File(System.getProperty("user.home") + "/uimaRaptatInception/" + "/descrTemp/");
		if (!descrFileDirectoryTemp.exists())
			descrFileDirectoryTemp.mkdir();
		String[] descrFileNames = {"ConceptMapperDescriptor.xml", "PhraseFinderDescriptor.xml", "ResultWriterDescriptor.xml","ResultWriterDescriptor.xml"
				,"SourceDocumentInformation.xml", "TextAnalysisEngineDescriptor.xml", "TextCollectionReaderDescriptor.xml"
				,"UIMAAggregateAnnotator.xml", "UIMAAnnotatedPhraseDescriptor.xml", "UIMASentenceDescriptor.xml", "UIMATokenDescriptor.xml"
				, "UIMAAggregateAnnotatorTraining.xml", "ProbabilisticSequenceIDTrainerDescriptor.xml", "ConceptMapCreatorDescriptor.xml"};
		for (String descrFileName: descrFileNames) {
			InputStream descrFileStream = UIMARaptatTraining.class.getClassLoader().getResourceAsStream("src/main/resources/desc/" + descrFileName);
			byte[] descrFileByteArray;
			try {
				descrFileByteArray = descrFileStream.readAllBytes();
				File descrFileTemp = new File(descrFileDirectoryTemp.getAbsolutePath() + "/" + descrFileName);
				if (!descrFileTemp.exists())
					descrFileTemp.delete();
				descrFileTemp.createNewFile();
				FileOutputStream fileOutputStreamDescriptor = new FileOutputStream(descrFileTemp.getAbsolutePath());
				fileOutputStreamDescriptor.write(descrFileByteArray);
				fileOutputStreamDescriptor.flush();
				fileOutputStreamDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}


