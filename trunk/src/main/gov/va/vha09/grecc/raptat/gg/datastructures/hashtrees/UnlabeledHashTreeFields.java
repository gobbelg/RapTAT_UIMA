package src.main.gov.va.vha09.grecc.raptat.gg.datastructures.hashtrees;

import java.util.List;

public class UnlabeledHashTreeFields implements HashTreeFields {

  public List<String> stringSequence = null;
  public long unlabeled = 0;

  // public LabeledHashTreeResultTest()
  // {}

  public UnlabeledHashTreeFields(List<String> stringSequence, long unlabeled) {
    this.stringSequence = stringSequence;
    this.unlabeled = unlabeled;
  }


  @Override
  public String toString() {
    return "Sequence:  " + this.stringSequence + "\n\t\tUnlabeled: " + this.unlabeled + "\n\n";
  }

}
