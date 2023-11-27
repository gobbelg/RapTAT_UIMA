package src.test.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import src.main.gov.va.vha09.grecc.raptat.gg.analysis.bootstrap.ConceptMapBootstrapSampler;

public class BootStrapperTest {
  public HashMap<Integer, List<String[]>> RandomSetMap =
      new HashMap<Integer, List<String[]>>(3000000);


  public void printMapData(HashMap<Integer, ConceptMapBootstrapSampler> mp) {
    Set<Integer> s = mp.keySet();
    Iterator<Integer> itr = s.iterator();
    while (itr.hasNext()) {
      Integer str = itr.next();
      ConceptMapBootstrapSampler temp = mp.get(str);
      temp.printAnnotationSet();
    }

  }


  public void printRandomSet(List<String[]> data) {
    String tmpstr = "";
    Iterator<String[]> it = data.iterator();
    while (it.hasNext()) {
      String[] temp = it.next();

      for (int i = 0; i < temp.length; i++) {
        tmpstr = tmpstr + " " + temp[i];
      }
      System.out.println(tmpstr);
      tmpstr = "";
    }

  }

}
