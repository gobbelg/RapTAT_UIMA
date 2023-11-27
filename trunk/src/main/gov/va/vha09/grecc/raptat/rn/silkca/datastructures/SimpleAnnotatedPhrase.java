package src.main.gov.va.vha09.grecc.raptat.rn.silkca.datastructures;

/**
 * Representation of Raptat SimpleAnnotatedPhrase. This class contains the essential information
 * contained within annotations, represented as simple Java Strings and Integers
 */

public record SimpleAnnotatedPhrase(String documentSource, String annotatedText, String label,
    int beginOffset, int endOffset) {

}
