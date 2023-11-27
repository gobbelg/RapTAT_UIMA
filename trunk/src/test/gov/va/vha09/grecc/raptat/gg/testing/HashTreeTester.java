package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import java.util.Hashtable;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

public class HashTreeTester extends Hashtable<String, HashTreeTester> {
  private static final long serialVersionUID = -2095970947293176934L;


  public static void main(String[] args) {
    String[] phrase1 = {"a", "b"};
    String[] phrase2 = {"d", "e"};
    String[] phrase3 = {"d", "g"};

    HashTreeTester myHash = new HashTreeTester();
    myHash.add(phrase1);
    myHash.add(phrase2);
    myHash.add(phrase3);

    // myHash.put(phrase1[0], new HashTree());
    // myHash.put(phrase2[0], new HashTree());
    // myHash.add(phrase3);

  }


  public HashTreeTester() {
    super(4);
  }


  public HashTreeTester(String[] phrase) {
    super(1);
    if (phrase != null && phrase.length > 0) {
      String[] subPhrase = GeneralHelper.getSubArray(phrase, 1);
      this.put(phrase[0], new HashTreeTester(subPhrase));
    }
  }


  public void add(String[] phrase) {
    if (phrase.length == 1) {
      if (!this.containsKey(phrase[0])) {
        this.put(phrase[0], new HashTreeTester());
      }
      return;
    }
    String[] subPhrase = GeneralHelper.getSubArray(phrase, 1);
    if (this.containsKey(phrase[0])) {
      HashTreeTester tempHash = this.get(phrase[0]);
      tempHash.add(subPhrase);
      return;
    }
    this.put(phrase[0], new HashTreeTester(subPhrase));
    // put(phrase[0], new HashTree());
  }

}
