package src.main.gov.va.vha09.grecc.raptat.rn.uima.makers;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public abstract class AnnotationMaker {
  abstract Annotation newAnnotation(JCas jcas, int start, int end);
}
