package src.main.gov.va.vha09.grecc.raptat.rn.silkca.firstletterrecommender;

import java.util.HashSet;
import java.util.Map;

/**
 * Simple model, or a Map, that takes the first letter of annotations, 
 * and maps them to their most recent label.
 */

public class FirstLetterModel {
    private final Map<String, String> aFirstLetterMap;
    private HashSet<String> previousDocuments = new HashSet<>();

    public FirstLetterModel(Map<String, String> firstLetterMap)
    {
        aFirstLetterMap = firstLetterMap;
    }
    
    /**
     * Gets a label based on the first letter of its text.
     * @param coveredText
     * @return
     */
    public String getFirstLetterLabel(String coveredText) {
    	return this.aFirstLetterMap.get(coveredText.substring(0, 1).toLowerCase());
    }
    
    public void addAnnotatedDocument(String documentTitle) {
    	this.previousDocuments.add(documentTitle);
    }
    
    public boolean hasDocumentBeenAnnotated(String documentTitle) {
    	return this.previousDocuments.contains(documentTitle);
    }
    
    public void addEntryToMap(String firstLetter, String label) {
    	this.aFirstLetterMap.put(firstLetter, label);
    }
 

}
