package src.main.gov.va.vha09.grecc.raptat.rn.uima.analysisengines;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.ConceptMapTrainer;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.probabilistic.conceptmapping.naivebayes.NBTrainer;
import src.main.gov.va.vha09.grecc.raptat.rn.misc.BridgeClass;

public class UIMAConceptMapCreator extends JCasAnnotator_ImplBase{
	
	ConceptMapTrainer conceptMapTrainer;

	public UIMAConceptMapCreator() {
	}

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		
		if (BridgeClass.conceptMapSolution == null)
			conceptMapTrainer = new NBTrainer(false, false, false, false, false, null);
		else
			conceptMapTrainer = new NBTrainer(BridgeClass.conceptMapSolution, false);
		
	}

	@Override
	public void process( JCas inputJCas ) throws AnalysisEngineProcessException {
		this.conceptMapTrainer.updateTraining(BridgeClass.curRefAnnotations);
		BridgeClass.conceptMapSolution = this.conceptMapTrainer.getCurrentSolution();
	}
	
}	

