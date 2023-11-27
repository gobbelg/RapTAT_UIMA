/**
 *
 */
package src.test.gov.va.vha09.grecc.raptat.gg.testing.inheritanceimpact;

/********************************************************
 *
 *
 * @author Glenn Gobbel - May 17, 2012
 *******************************************************/
public class Kennel {
  protected Animal theAnimal;


  public Kennel() {
    this.theAnimal = new Cat();
  }


  public String haveAnimalSpeak() {
    return this.theAnimal.speak();
  }
}
