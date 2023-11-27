package src.main.gov.va.vha09.grecc.raptat.gg.importer.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import src.main.gov.va.vha09.grecc.raptat.gg.core.Constants.AnnotationApp;
import src.main.gov.va.vha09.grecc.raptat.gg.datastructures.annotationcomponents.SchemaConcept;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

public abstract class SchemaImporter {
  protected static AnnotationApp schemaApp;

  public File getSchemaFile() {
    String startDirectory = System.getProperty("user.dir");
    String[] fileTypes = {".pont", ".xml"};
    File schemaFile = GeneralHelper.getFileByType("Select schema file", startDirectory, fileTypes);
    return schemaFile;
  }

  /**
   * Remove reference in schema to concepts that are not in the set of accepted concepts and add any
   * concepts in accepted concepts even if they are *not* in list of SchemaConcept objects provided
   * as a parameter. Return these in a list of SchemaConcept instances.
   *
   * @param schemaConcepts
   * @param conceptNames
   * @return
   */
  public static List<SchemaConcept> filterSchemaConcepts(List<SchemaConcept> schemaConcepts,
      HashSet<String> conceptNames) {
    List<SchemaConcept> resultConcepts = new ArrayList<>(conceptNames.size());
    Set<String> tempConceptNames = new HashSet<>(conceptNames);

    for (SchemaConcept schemaConcept : schemaConcepts) {
      String conceptName = schemaConcept.getConceptName();
      if (tempConceptNames.contains(conceptName)) {
        if (schemaConcept.isRelationship()) {
          /*
           * Remove from the list of concepts that the relationship indicates as linking 'from' or
           * 'to' any that are not in allowable concept names. If, after doing this, the list of
           * concepts the relationship indicates as linking *to* AND the list of concepts the
           * relationship indicates as linking *from* is *NOT* empty, then add this relationship
           * concepts to the list of result concepts.
           */
          removeNonAllowableConcepts(tempConceptNames, schemaConcept.getLinkedFromConcepts());
          removeNonAllowableConcepts(tempConceptNames, schemaConcept.getLinkedToConcepts());

          /*
           * Add the relationship concept only if there are still remaining concepts it links from
           * AND to after the previous filtering.
           */
          if (!schemaConcept.getLinkedFromConcepts().isEmpty()
              && !schemaConcept.getLinkedToConcepts().isEmpty()) {
            resultConcepts.add(schemaConcept);
          }
        } else {
          resultConcepts.add(schemaConcept);
        }
        /*
         * Remove the conceptName instances that have been handled in this 'if' block
         */
        tempConceptNames.remove(conceptName);
      }
    }

    /* Add any concepts that are allowable but not in original schema */
    for (String conceptToAdd : tempConceptNames) {
      resultConcepts.add(new SchemaConcept(conceptToAdd, false));
    }

    return resultConcepts;
  }

  public static AnnotationApp getSchemaApp() {
    return SchemaImporter.schemaApp;
  }

  public static List<SchemaConcept> importSchemaConcepts(File schemaFile) {
    return importSchemaConcepts(schemaFile.getAbsolutePath());
  }

  public static List<SchemaConcept> importSchemaConcepts(String schemaFilePath) {
//    if (schemaFilePath.endsWith(".pont")) {
//      SchemaImporter.schemaApp = AnnotationApp.KNOWTATOR;
//      return KnowtatorSchemaImporter.importSchemaConcepts(schemaFilePath);
//    } else {
      SchemaImporter.schemaApp = AnnotationApp.EHOST;
      return EhostSchemaImporter.importSchemaConcepts(schemaFilePath);
//    }

  }

  /**
   * Remove any concepts from the conceptList parameter that are not in the set of the
   * allowableConcepts parameter
   *
   * @param allowableConcepts
   * @param conceptList
   */
  private static void removeNonAllowableConcepts(Set<String> allowableConcepts,
      Set<String> conceptList) {
    Iterator<String> iterator = conceptList.iterator();
    while (iterator.hasNext()) {
      String listConcept = iterator.next();
      if (!allowableConcepts.contains(listConcept)) {
        iterator.remove();
      }
    }
  }

}
