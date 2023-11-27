package src.main.gov.va.vha09.grecc.raptat.rn.uima.resourceinterfaces;

import java.io.IOException;
import java.io.ObjectInputStream;
import org.apache.uima.resource.DataResource;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.ConceptMapSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.RaptatAnnotationSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.solutions.TokenSequenceFinderSolution;
import src.main.gov.va.vha09.grecc.raptat.gg.core.training.TSTrainingParameterObject;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.SequenceIDStructure;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.TokenProcessingOptions;
import src.main.gov.va.vha09.grecc.raptat.rn.uima.resourceinterfaces.UIMASolution;
import src.main.gov.va.vha09.grecc.raptat.rn.algorithms.solutions.RaptatAnnotationSolutionExt;

/********************************************************
 * This is just a wrapper that adds functionality to the AlgorithmSolution class so that
 * AlgorithmSolution instances can be used as external resources within UIMA
 *
 * @author Glenn Gobbel - Sep 9, 2013
 *******************************************************/
public class UIMAAlgorithmSolution extends RaptatAnnotationSolution
implements UIMASolution, SharedResourceObject {
	private static final long serialVersionUID = 8997739732917115791L;

	private TokenSequenceFinderSolution tokenSequenceFinderSolution;
	private TSTrainingParameterObject trainingParameters;

	public UIMAAlgorithmSolution() {
		// TODO Auto-generated constructor stub
	}


	/*************************************************************
	 * OVERRIDES PARENT METHOD Necessary for implementing the shared resource object interface for use
	 * with UIMA
	 *
	 * @param inputResource
	 * @throws ResourceInitializationException
	 *
	 * @author Glenn Gobbel - Aug 26, 2013
	 *************************************************************/
	@Override
	public void load(DataResource inputResource) throws ResourceInitializationException {
		ObjectInputStream inObject = null;

		try {
			inObject = new ObjectInputStream(inputResource.getInputStream());
			// inObject = new ObjectInputStream( new FileInputStream( new File(
			// inputResource.getUri() ) ) );
			copyFields((RaptatAnnotationSolution) inObject.readObject());
		} catch (Exception e) {
			System.out.println("Unable to open and read in solution file");
			e.printStackTrace();
		} finally {
			try {
				if (inObject != null) {
					inObject.close();
				}
			} catch (IOException e) {
				System.out.println("Unable to close solution file");
				e.printStackTrace();
			}

		}
	}


	private void copyFields(RaptatAnnotationSolution raptatAnnotationSolution) {
		this.tokenSequenceFinderSolution = raptatAnnotationSolution.getTokenSequenceFinderSolution();
		this.trainingParameters = raptatAnnotationSolution.getTrainingParameters();
	}
	
	@Override
	public TSTrainingParameterObject getTrainingParameters() {
		return this.trainingParameters;
	}
	
	@Override
	public TokenSequenceFinderSolution getTokenSequenceFinderSolution() {
		return this.tokenSequenceFinderSolution;
	}


	@Override
	public ConceptMapSolution getConceptMapSolution() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public SequenceIDStructure getSequenceIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TokenProcessingOptions getTokenTrainingOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
