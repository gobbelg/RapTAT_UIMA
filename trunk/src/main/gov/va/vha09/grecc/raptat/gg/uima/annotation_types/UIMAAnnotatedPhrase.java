package src.main.gov.va.vha09.grecc.raptat.gg.uima.annotation_types;

/* First created by JCasGen Fri Sep 06 13:09:01 CDT 2013 */

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

/** 
 * Updated by JCasGen Mon Sep 13 15:47:46 CDT 2021
 * XML source: C:/Projects/SVN_Projects/RAPTAT_SVN/build/classes/resources/desc/UIMASentenceDescriptor.xml
 * @generated */
public class UIMAAnnotatedPhrase extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "src.main.gov.va.vha09.grecc.raptat.gg.uima.annotation_types.UIMAAnnotatedPhrase";
  
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int typeIndexID = JCasRegistry.register(UIMAAnnotatedPhrase.class);
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int type = typeIndexID;


  /** @generated */
  public UIMAAnnotatedPhrase(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated */
  public UIMAAnnotatedPhrase(JCas jcas, int begin, int end) {
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
  protected UIMAAnnotatedPhrase() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public UIMAAnnotatedPhrase(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /**
   * getter for concept - gets
   *
   * @generated
   */
  public String getConcept() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_concept));
  }
    
  /**
   * getter for Tokens - gets
   *
   * @generated
   */
  public FSArray getTokens() { 
    return (FSArray<UIMAToken>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_Tokens)));
  }
    
  // *--------------*
  // * Feature: concept

  /**
   * indexed getter for Tokens - gets an indexed value -
   *
   * @generated
   */
  public UIMAToken getTokens(int i) {
     return (UIMAToken)(((FSArray<UIMAToken>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_Tokens)))).get(i));
  } 

  /** @generated */
  @Override
  public int getTypeIndexID() {return typeIndexID;}
 
 
  /* *******************
   *   Feature Offsets *
   * *******************/ 
   
  public final static String _FeatName_concept = "concept";
  public final static String _FeatName_Tokens = "Tokens";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_concept = TypeSystemImpl.createCallSite(UIMAAnnotatedPhrase.class, "concept");
  private final static MethodHandle _FH_concept = _FC_concept.dynamicInvoker();
  private final static CallSite _FC_Tokens = TypeSystemImpl.createCallSite(UIMAAnnotatedPhrase.class, "Tokens");
  private final static MethodHandle _FH_Tokens = _FC_Tokens.dynamicInvoker();

   
  // *--------------*
  // * Feature: Tokens

  /**
   * setter for concept - sets
   *
   * @generated
   */
  public void setConcept(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_concept), v);
  }    
    
   
    
  /**
   * setter for Tokens - sets
   *
   * @generated
   */
  public void setTokens(FSArray v) {
    _setFeatureValueNcWj(wrapGetIntCatchException(_FH_Tokens), v);
  }    
    
    
  /**
   * indexed setter for Tokens - sets an indexed value -
   *
   * @generated
   */
  public void setTokens(int i, UIMAToken v) {
    ((FSArray<UIMAToken>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_Tokens)))).set(i, v);
  }  
    /** 
   * <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/* default - does nothing empty block */
  }
}
