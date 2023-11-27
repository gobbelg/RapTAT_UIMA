/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.testing.inheritanceimpact;

/********************************************************
 *
 *
 * @author Glenn Gobbel - May 17, 2012
 *******************************************************/
public class Cat extends Animal {

  public static void main(String[] args) {
    Cage myCage = new Cage();

    System.out.println(myCage.haveAnimalSpeak());

  }


  /**********************************************
   *
   *
   * @author Glenn Gobbel - May 17, 2012
   **********************************************/
  public Cat() {
    // TODO Auto-generated constructor stub
  }


  /*************************************************************
   * OVERRIDES PARENT METHOD
   *
   * @return
   *
   * @author Glenn Gobbel - May 17, 2012
   *************************************************************/
  @Override
  public String speak() {
    return "Cat meow";
  }

}
