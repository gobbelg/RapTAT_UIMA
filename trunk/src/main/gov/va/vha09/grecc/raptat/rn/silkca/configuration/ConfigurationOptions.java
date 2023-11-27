package src.main.gov.va.vha09.grecc.raptat.rn.silkca.configuration;

import java.util.Properties;

import src.main.gov.va.vha09.grecc.raptat.rn.silkca.core.DocumentSetGeneratorType;
import src.main.gov.va.vha09.grecc.raptat.rn.silkca.documentprocessing.DocumentPreprocessorType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Configuration options that are connected to a properties file. Information
 * here will fill in details to the rest of the program, such as which
 * implementations of DocumentSetGenerator and DocumentPreprocessor should be
 * used.
 */

public class ConfigurationOptions implements Serializable {

	private static final long serialVersionUID = 2289765317053825125L;

	private static final String PATH_TO_PROPERTIES_FILE = "/home/noriegrt/LinuxInception/silk.properties";

	private Properties properties;

	public ConfigurationOptions() {
		try {
			this.properties = new Properties();
			this.properties.load(new FileInputStream(PATH_TO_PROPERTIES_FILE));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
			System.exit(-1);
		}
	}

	public int getAnnotationBatchNumberSize() {
		return Integer.parseInt(this.properties.getProperty("BatchNumberForAnnotation"));
	}

	public boolean isOnlineLearningActive() {
		return Boolean.parseBoolean(this.properties.getProperty("OnlineLearning"));
	}

	public boolean isActiveLearningActive() {
		return Boolean.parseBoolean(this.properties.getProperty("ActiveLearning"));
	}
	
	public DocumentSetGeneratorType getDocumentSetGeneratorType() {
		return Enum.valueOf(DocumentSetGeneratorType.class, this.properties.getProperty("DocumentSetGeneratorType"));
	}
	
	public DocumentPreprocessorType getDocumentPreprocessorType() {
		return Enum.valueOf(DocumentPreprocessorType.class, this.properties.getProperty("DocumentPreprocessorType"));
	}

}
