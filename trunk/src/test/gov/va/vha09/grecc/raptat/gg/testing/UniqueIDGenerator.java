package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class UniqueIDGenerator {
  public static void main(String[] args) {
    UniqueIDGenerator theGenerator = new UniqueIDGenerator();
    for (int i = 0; i < 10; i++) {
      String testValue = theGenerator.getUnique();
      System.out.println(i + ":" + testValue);
    }
  }

  private HashSet<String> assignedValues = new HashSet<>();


  public UniqueIDGenerator() {}


  public String getUnique() {
    String candidateValue;
    do {
      candidateValue = UUID.randomUUID().toString();
      candidateValue = candidateValue.substring(candidateValue.lastIndexOf("-") + 1);
    } while (this.assignedValues.contains(candidateValue));
    this.assignedValues.add(candidateValue);
    return candidateValue;
  }


  public void populateWithExisting(List<String> existingValues) {
    this.assignedValues.addAll(existingValues);
  }
}
