/* First created by JCasGen Fri Aug 23 16:55:57 CDT 2013 */
package src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/** 
 * Updated by JCasGen Tue Jun 06 09:06:34 EDT 2023
 * XML source: /home/noriegrt/Documents/silkca_inception/UIMARaptat - Copy/trunk/src/main/resources/desc/UIMATokenDescriptor.xml
 * @generated */
public class UIMAToken extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types.UIMAToken";
  
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int typeIndexID = JCasRegistry.register(UIMAToken.class);
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int type = typeIndexID;


  /** @generated */
  public UIMAToken(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated */
  public UIMAToken(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /**
   * Never called. Disable default constructor
   *
   * @generated
   */
  protected UIMAToken() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public UIMAToken(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /**
   * getter for POS - gets
   *
   * @generated
   */
  public String getPOS() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_POS));
  }
    
  /**
   * getter for Stem - gets
   *
   * @generated
   */
  public String getStem() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_stem));
  }
    
  // *--------------*
  // * Feature: POS

  /**
   * getter for tokenStringAugmented - gets
   *
   * @generated
   */
  public String getTokenStringAugmented() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_tokenStringAugmented));
  }
    
  /** @generated */
  @Override
  public int getTypeIndexID() {return typeIndexID;}
 
 
  /* *******************
   *   Feature Offsets *
   * *******************/ 
   
  public final static String _FeatName_POS = "POS";
  public final static String _FeatName_stem = "stem";
  public final static String _FeatName_tokenStringAugmented = "tokenStringAugmented";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_POS = TypeSystemImpl.createCallSite(UIMAToken.class, "POS");
  private final static MethodHandle _FH_POS = _FC_POS.dynamicInvoker();
  private final static CallSite _FC_stem = TypeSystemImpl.createCallSite(UIMAToken.class, "stem");
  private final static MethodHandle _FH_stem = _FC_stem.dynamicInvoker();
  private final static CallSite _FC_tokenStringAugmented = TypeSystemImpl.createCallSite(UIMAToken.class, "tokenStringAugmented");
  private final static MethodHandle _FH_tokenStringAugmented = _FC_tokenStringAugmented.dynamicInvoker();

   
  // *--------------*
  // * Feature: Stem

  /**
   * setter for POS - sets
   *
   * @generated
   */
  public void setPOS(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_POS), v);
  }    
    
   
    
  /**
   * setter for Stem - sets
   *
   * @generated
   */
  public void setStem(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_stem), v);
  }    
    
   
    
  // *--------------*
  // * Feature: tokenStringAugmented

  /**
   * setter for tokenStringAugmented - sets
   *
   * @generated
   */
  public void setTokenStringAugmented(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_tokenStringAugmented), v);
  }    
    
    /** 
   * <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/* default - does nothing empty block */
  }
}
