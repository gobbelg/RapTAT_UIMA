/* First created by JCasGen Fri Aug 23 17:00:27 CDT 2013 */
package src.main.gov.va.vha09.grecc.raptat.rn.uima.annotation_types;

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
public class UIMASentence extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "src.main.gov.va.vha09.grecc.raptat.gg.uima.annotation_types.UIMASentence";
  
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int typeIndexID = JCasRegistry.register(UIMASentence.class);
  /**
   * @generated
   * @ordered
   */
  @SuppressWarnings("hiding")
  public final static int type = typeIndexID;


  /** @generated */
  public UIMASentence(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated */
  public UIMASentence(JCas jcas, int begin, int end) {
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
  protected UIMASentence() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public UIMASentence(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /**
   * getter for containsAnnotatedPhrase - gets
   *
   * @generated
   */
  public boolean getContainsAnnotatedPhrase() { 
    return _getBooleanValueNc(wrapGetIntCatchException(_FH_containsAnnotatedPhrase));
  }
    
  /**
   * getter for phraseAnnotations - gets
   *
   * @generated
   */
  public FSArray getPhraseAnnotations() { 
    return (FSArray<UIMAAnnotatedPhrase>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_phraseAnnotations)));
  }
    
  // *--------------*
  // * Feature: SentenceTokens

  /**
   * indexed getter for phraseAnnotations - gets an indexed value -
   *
   * @generated
   */
  public UIMAAnnotatedPhrase getPhraseAnnotations(int i) {
     return (UIMAAnnotatedPhrase)(((FSArray<UIMAAnnotatedPhrase>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_phraseAnnotations)))).get(i));
  } 

  /**
   * getter for SentenceTokens - gets
   *
   * @generated
   */
  public FSArray getSentenceTokens() { 
    return (FSArray<UIMAToken>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_SentenceTokens)));
  }
    
  /**
   * indexed getter for SentenceTokens - gets an indexed value -
   *
   * @generated
   */
  public UIMAToken getSentenceTokens(int i) {
     return (UIMAToken)(((FSArray<UIMAToken>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_SentenceTokens)))).get(i));
  } 

  /**
   * getter for sourcePath - gets
   *
   * @generated
   */
  public String getSourcePath() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_sourcePath));
  }
    
  // *--------------*
  // * Feature: containsAnnotatedPhrase

  /** @generated */
  @Override
  public int getTypeIndexID() {return typeIndexID;}
 
 
  /* *******************
   *   Feature Offsets *
   * *******************/ 
   
  public final static String _FeatName_SentenceTokens = "SentenceTokens";
  public final static String _FeatName_containsAnnotatedPhrase = "containsAnnotatedPhrase";
  public final static String _FeatName_phraseAnnotations = "phraseAnnotations";
  public final static String _FeatName_sourcePath = "sourcePath";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_SentenceTokens = TypeSystemImpl.createCallSite(UIMASentence.class, "SentenceTokens");
  private final static MethodHandle _FH_SentenceTokens = _FC_SentenceTokens.dynamicInvoker();
  private final static CallSite _FC_containsAnnotatedPhrase = TypeSystemImpl.createCallSite(UIMASentence.class, "containsAnnotatedPhrase");
  private final static MethodHandle _FH_containsAnnotatedPhrase = _FC_containsAnnotatedPhrase.dynamicInvoker();
  private final static CallSite _FC_phraseAnnotations = TypeSystemImpl.createCallSite(UIMASentence.class, "phraseAnnotations");
  private final static MethodHandle _FH_phraseAnnotations = _FC_phraseAnnotations.dynamicInvoker();
  private final static CallSite _FC_sourcePath = TypeSystemImpl.createCallSite(UIMASentence.class, "sourcePath");
  private final static MethodHandle _FH_sourcePath = _FC_sourcePath.dynamicInvoker();

   
  /**
   * setter for containsAnnotatedPhrase - sets
   *
   * @generated
   */
  public void setContainsAnnotatedPhrase(boolean v) {
    _setBooleanValueNfc(wrapGetIntCatchException(_FH_containsAnnotatedPhrase), v);
  }    
    
   
    
  // *--------------*
  // * Feature: phraseAnnotations

  /**
   * setter for phraseAnnotations - sets
   *
   * @generated
   */
  public void setPhraseAnnotations(FSArray v) {
    _setFeatureValueNcWj(wrapGetIntCatchException(_FH_phraseAnnotations), v);
  }    
    
    
  /**
   * indexed setter for phraseAnnotations - sets an indexed value -
   *
   * @generated
   */
  public void setPhraseAnnotations(int i, UIMAAnnotatedPhrase v) {
    ((FSArray<UIMAAnnotatedPhrase>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_phraseAnnotations)))).set(i, v);
  }  
   
    
  /**
   * setter for SentenceTokens - sets
   *
   * @generated
   */
  public void setSentenceTokens(FSArray v) {
    _setFeatureValueNcWj(wrapGetIntCatchException(_FH_SentenceTokens), v);
  }    
    
    
  /**
   * indexed setter for SentenceTokens - sets an indexed value -
   *
   * @generated
   */
  public void setSentenceTokens(int i, UIMAToken v) {
    ((FSArray<UIMAToken>)(_getFeatureValueNc(wrapGetIntCatchException(_FH_SentenceTokens)))).set(i, v);
  }  
   
    
  // *--------------*
  // * Feature: sourcePath

  /**
   * setter for sourcePath - sets
   *
   * @generated
   */
  public void setSourcePath(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_sourcePath), v);
  }    
    
    /** 
   * <!-- begin-user-doc --> Write your own initialization here <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/* default - does nothing empty block */
  }
}
