package src.main.gov.va.vha09.grecc.raptat.rn.silkca.core;

import java.util.ArrayList;
import java.util.List;

import src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures.DocumentCorpus;

public class BaseDocumentSelector implements DocumentSelector {

	@Override
	public List<String> getDocuments(String user, int n) {
		List<String> nextDocumentBatch = new ArrayList<String>();
		List<String> unannotatedDocuments = DocumentCorpus.INSTANCE.getUnannotatedDocuments(user);
		for (int i=0; i<n && i<unannotatedDocuments.size(); i++) {
			nextDocumentBatch.add(unannotatedDocuments.get(i));
		}
		return nextDocumentBatch;
	}

}
