package src.main.gov.va.vha09.grecc.raptat.gg.sql;

import src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.impl.data_structures.RapTATItemToProcess;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.AnnotationGroup;

public class RaptatAnnotationResult extends RaptatResult {

  private AnnotationGroup annotationGroup;

  public RaptatAnnotationResult(AnnotationGroup annotationGroup, RapTATItemToProcess item) {
    super(item);
    this.annotationGroup = annotationGroup;
  }


  public AnnotationGroup getAnnotationGroup() {
    return annotationGroup;
  }
}
