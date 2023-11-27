

   
/* Apache UIMA v3 - First created by JCasGen Mon Feb 14 15:51:39 CST 2022 */

package org.apache.uima;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Mon Feb 14 15:51:39 CST 2022
 * XML source: C:/Projects/SVN_Projects/UIMARaptat/trunk/src/main/gov/va/cha09/grecc/raptat/rn/testers/NewFile.xml
 * @generated */
public class MainConcept extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "org.apache.uima.MainConcept";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(MainConcept.class);
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
   
  public final static String _FeatName_building = "building";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_building = TypeSystemImpl.createCallSite(MainConcept.class, "building");
  private final static MethodHandle _FH_building = _FC_building.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected MainConcept() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public MainConcept(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public MainConcept(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public MainConcept(JCas jcas, int begin, int end) {
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
  //* Feature: building

  /** getter for building - gets 
   * @generated
   * @return value of the feature 
   */
  public String getBuilding() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_building));
  }
    
  /** setter for building - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setBuilding(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_building), v);
  }    
    
  }

    