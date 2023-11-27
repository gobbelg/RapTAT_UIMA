

   
/* Apache UIMA v3 - First created by JCasGen Mon Sep 13 15:30:51 CDT 2021 */

package src.main.gov.va.vha09.grecc.raptat.uima.annotation_types;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Sep 13 15:30:51 CDT 2021
 * XML source: C:/Projects/SVN_Projects/RAPTAT_SVN/build/classes/resources/desc/UIMATokenDescriptor.xml
 * @generated */
public class UIMAToken extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "src.main.gov.va.vha09.grecc.raptat.uima.annotation_types.UIMAToken";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(UIMAToken.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
 
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

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
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
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public UIMAToken(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public UIMAToken(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: POS

  /** getter for POS - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPOS() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_POS));
  }
    
  /** setter for POS - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPOS(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_POS), v);
  }    
    
   
    
  //*--------------*
  //* Feature: stem

  /** getter for stem - gets 
   * @generated
   * @return value of the feature 
   */
  public String getStem() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_stem));
  }
    
  /** setter for stem - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setStem(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_stem), v);
  }    
    
   
    
  //*--------------*
  //* Feature: tokenStringAugmented

  /** getter for tokenStringAugmented - gets 
   * @generated
   * @return value of the feature 
   */
  public String getTokenStringAugmented() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_tokenStringAugmented));
  }
    
  /** setter for tokenStringAugmented - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTokenStringAugmented(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_tokenStringAugmented), v);
  }    
    
  }

    