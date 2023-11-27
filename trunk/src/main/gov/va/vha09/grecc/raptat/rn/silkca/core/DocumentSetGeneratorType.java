package src.main.gov.va.vha09.grecc.raptat.rn.silkca.core;

import src.main.gov.va.vha09.grecc.raptat.rn.silkca.preannotation.PreannotatorType;

/**
 * Type of DocumentSetGenerator, which in turn determines the implementation types for
 * DocumentSelector and Preannotator
 */

public enum DocumentSetGeneratorType {
  BASE;

  DocumentSelectorType getDocumentSelectorType() throws Exception {
    switch (this) {
      case BASE: {
        return DocumentSelectorType.BASE;
      }
      default: {
        throw new Exception("DocumentSetGeneratorType class improperly constructed");
      }
    }
  }

  PreannotatorType getPreannotatorType() throws Exception {
    switch (this) {
      case BASE: {
        return PreannotatorType.BASE;
      }
      default: {
        throw new Exception("DocumentSetGeneratorType class improperly constructed");
      }
    }
  }
}
