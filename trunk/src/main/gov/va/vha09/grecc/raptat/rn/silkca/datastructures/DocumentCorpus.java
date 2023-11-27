package src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.RaptatPair;

/**
 * Stores paths to documents and also contains information about which have been
 * annotated/completed. This class ensures that Raptat has the appropriate
 * information about documents that it needs, while being separate from
 * Inception.
 */

public enum DocumentCorpus {

	INSTANCE;

	private class DocumentAnnotations {
		private List<SimpleAnnotatedPhrase> preAnnotations;
		private List<SimpleAnnotatedPhrase> reviewerAnnotations;
		private boolean isReviewerAnnotated = false;

		private DocumentAnnotations(List<SimpleAnnotatedPhrase> preAnnotations,
				List<SimpleAnnotatedPhrase> reviewerAnnotations, boolean isReviewerAnnotated) {
			this.preAnnotations = preAnnotations;
			this.reviewerAnnotations = reviewerAnnotations;
			this.isReviewerAnnotated = isReviewerAnnotated;
		}
	}

	private Map<String, Map<String, DocumentAnnotations>> documents = new HashMap<>();

	private DocumentCorpus() {
	}

	private void addUsers(List<String> users) {
		for (String user : users) {
			this.documents.put(user, new HashMap<String, DocumentAnnotations>());
		}
	}

	/**
	 * Adds a document to the 'documents' map. This is used to make Raptat aware of
	 * pertinent document information, while still being separate from Inception
	 *
	 * @param document
	 * @return
	 */
	public boolean addDocument(List<String> users, String document) {
		if (this.documents.keySet().isEmpty()) {
			addUsers(users);
		}
		for (String user : this.documents.keySet()) {
			if (!this.documents.get(user).containsKey(document)) {
				this.documents.get(user).put(document, new DocumentAnnotations(new ArrayList<SimpleAnnotatedPhrase>(),
						new ArrayList<SimpleAnnotatedPhrase>(), false));
			}
		}
		System.out.println("Document uploaded to DocumentCorpus successfully!");
		return false;
	}

	public List<String> getUnannotatedDocuments(String user) {
		// TODO: consider keeping unannotated documents as field, and remove as they are
		// ReviewerAnnotated; careful with syncing
		ArrayList<String> unAnnotatedDocuments = new ArrayList<String>();
		for (String document : this.documents.get(user).keySet()) {
			if (!this.documents.get(user).get(document).isReviewerAnnotated) {
				unAnnotatedDocuments.add(document);
			}
		}
		return unAnnotatedDocuments;
	}

	public boolean updateReviewerAnnotations(String user, ArrayList<RaptatPair<String, List<SimpleAnnotatedPhrase>>> listPairs) {
		for (RaptatPair<String, List<SimpleAnnotatedPhrase>> pair : listPairs) {
			if (this.documents.get(user).containsKey(pair.left)) {
				List<SimpleAnnotatedPhrase> newAnnotations = pair.right;
				DocumentAnnotations documentAnnotations = this.documents.get(user).get(pair.left);
				documentAnnotations.reviewerAnnotations = newAnnotations;
				documentAnnotations.isReviewerAnnotated = true;
			} else {
				this.documents.get(user).put(pair.left,
						new DocumentAnnotations(new ArrayList<SimpleAnnotatedPhrase>(), pair.right, true));
			}
		}
		return false;
	}

}
