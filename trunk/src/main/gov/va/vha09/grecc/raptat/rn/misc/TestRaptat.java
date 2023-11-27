package src.main.gov.va.vha09.grecc.raptat.rn.misc;

import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;
import src.main.gov.va.vha09.grecc.raptat.gg.core.training.TokenSequenceSolutionTrainer;

public class TestRaptat {

	public static void main(String[] args) {
		UserPreferences.INSTANCE.initializeLVGLocation();
		String[] trainingArguments = new String[] {"-p",
		"C:\\Projects\\SVN_Projects\\RAPTAT_SVN\\trunk\\src\\main\\resources\\PropertyFilesForCommandLine\\Testing\\TrainingForCreatingTestSolution_211002_v02.prop"};
		TokenSequenceSolutionTrainer.parseAndTrain(trainingArguments);

	}

}
