package src.main.gov.va.vha09.grecc.raptat.rn.silkca.core;

/**
 * Types of DocumentSelector, which includes a Base type, one that selects random n documents, and
 * one that selects n documents based on differences in their vectors
 */

public enum DocumentSelectorType {
  BASE, RANDOM, DOCUMENT_VECTOR_BASED;
}
