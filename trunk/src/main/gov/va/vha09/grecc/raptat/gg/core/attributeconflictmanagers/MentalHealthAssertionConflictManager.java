package src.main.gov.va.vha09.grecc.raptat.gg.core.attributeconflictmanagers;

import java.util.HashMap;
import java.util.Set;
import src.main.gov.va.vha09.grecc.raptat.gg.algorithms.ContextType;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.RaptatAttribute;

public class MentalHealthAssertionConflictManager extends AssertionConflictManager {

  /**
   *
   */
  private static final long serialVersionUID = -5315088464139045659L;


  /*
   * Overrides method of AssertionConflictManager. This method ignores any probabilistically
   * assigned assertion values for mental health notes because "negative" is often used for mental
   * health symptoms. If either the CUE_SCOPE_CRF assignment method or CONTEXTUAL assignment method
   * is "negative", we return "negative" as the assigned value.
   *
   * (non-Javadoc)
   *
   * @see
   * src.main.gov.va.vha09.grecc.raptat.gg.core.attributeconflictmanagers.AssertionConflictManager#
   * getAssertionValue(java.util.Set)
   */
  @Override
  String getAssertionValue(Set<RaptatAttribute> attributeSet) {
    HashMap<AttributeAssignmentMethod, RaptatAttribute> assignmentMethodMap = new HashMap<>();
    try {
      assignmentMethodMap = getMappedAssignmentMethods(attributeSet);
    } catch (IllegalArgumentException e) {
      System.err.println(
          "Illegal call to AssertionConflictManager manageConflicts() method\n" + e.getMessage());
      System.exit(-1);
    }

    RaptatAttribute attribute;

    /*
     * Note that "commonly assigned value" for ContextType.ASSERTIONSTATUS is "negative"
     */
    if ((attribute = assignmentMethodMap.get(AttributeAssignmentMethod.CUE_SCOPE_CRF)) != null) {
      if (attribute.getValues().get(0)
          .equalsIgnoreCase(ContextType.ASSERTIONSTATUS.getCommonlyAssignedValue())) {
        return "negative";
      }
    }

    if ((attribute = assignmentMethodMap.get(AttributeAssignmentMethod.CONTEXTUAL)) != null) {
      if (attribute.getValues().get(0)
          .equalsIgnoreCase(ContextType.ASSERTIONSTATUS.getCommonlyAssignedValue())) {
        return "negative";
      }
    }

    return "positive";
  }


  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
