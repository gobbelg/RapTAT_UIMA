

   
/* Apache UIMA v3 - First created by JCasGen Mon Sep 13 15:33:38 CDT 2021 */

package src.main.gov.va.vha09.grecc.raptat.gg.uima.annotation_types;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.tcas.Annotation;


/** Stores detailed information about the original source document from which the current CAS was initialized. All information (like size) refers to the source document and not to the document in the CAS which may be converted and filtered by a CAS Initializer. For example this information will be written to the Semantic Search index so that the original document contents can be retrieved by queries.
 * Updated by JCasGen Tue Sep 28 10:02:58 CDT 2021
 * XML source: C:/Projects/SVN_Projects/RAPTAT_SVN/trunk/src/main/resources/desc/SourceDocumentInformation.xml
 * @generated */
public class SourceDocumentInformation extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "src.main.gov.va.vha09.grecc.raptat.gg.uima.annotation_types.SourceDocumentInformation";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SourceDocumentInformation.class);
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
   
  public final static String _FeatName_uri = "uri";
  public final static String _FeatName_offsetInSource = "offsetInSource";
  public final static String _FeatName_documentSize = "documentSize";
  public final static String _FeatName_lastSegment = "lastSegment";
  public final static String _FeatName_SourcePath = "SourcePath";
  public final static String _FeatName_SourceName = "SourceName";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_uri = TypeSystemImpl.createCallSite(SourceDocumentInformation.class, "uri");
  private final static MethodHandle _FH_uri = _FC_uri.dynamicInvoker();
  private final static CallSite _FC_offsetInSource = TypeSystemImpl.createCallSite(SourceDocumentInformation.class, "offsetInSource");
  private final static MethodHandle _FH_offsetInSource = _FC_offsetInSource.dynamicInvoker();
  private final static CallSite _FC_documentSize = TypeSystemImpl.createCallSite(SourceDocumentInformation.class, "documentSize");
  private final static MethodHandle _FH_documentSize = _FC_documentSize.dynamicInvoker();
  private final static CallSite _FC_lastSegment = TypeSystemImpl.createCallSite(SourceDocumentInformation.class, "lastSegment");
  private final static MethodHandle _FH_lastSegment = _FC_lastSegment.dynamicInvoker();
  private final static CallSite _FC_SourcePath = TypeSystemImpl.createCallSite(SourceDocumentInformation.class, "SourcePath");
  private final static MethodHandle _FH_SourcePath = _FC_SourcePath.dynamicInvoker();
  private final static CallSite _FC_SourceName = TypeSystemImpl.createCallSite(SourceDocumentInformation.class, "SourceName");
  private final static MethodHandle _FH_SourceName = _FC_SourceName.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected SourceDocumentInformation() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public SourceDocumentInformation(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public SourceDocumentInformation(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public SourceDocumentInformation(JCas jcas, int begin, int end) {
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
  //* Feature: uri

  /** getter for uri - gets URI of document. (For example, file:///MyDirectory/myFile.txt for a simple file or http://incubator.apache.org/uima/index.html for content from a web source.)
   * @generated
   * @return value of the feature 
   */
  public String getUri() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_uri));
  }
    
  /** setter for uri - sets URI of document. (For example, file:///MyDirectory/myFile.txt for a simple file or http://incubator.apache.org/uima/index.html for content from a web source.) 
   * @generated
   * @param v value to set into the feature 
   */
  public void setUri(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_uri), v);
  }    
    
   
    
  //*--------------*
  //* Feature: offsetInSource

  /** getter for offsetInSource - gets Byte offset of the start of document content within original source file or other input source. Only used if the CAS document was retrieved from an source where one physical source file contained several conceptual documents. Zero otherwise.
   * @generated
   * @return value of the feature 
   */
  public int getOffsetInSource() { 
    return _getIntValueNc(wrapGetIntCatchException(_FH_offsetInSource));
  }
    
  /** setter for offsetInSource - sets Byte offset of the start of document content within original source file or other input source. Only used if the CAS document was retrieved from an source where one physical source file contained several conceptual documents. Zero otherwise. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setOffsetInSource(int v) {
    _setIntValueNfc(wrapGetIntCatchException(_FH_offsetInSource), v);
  }    
    
   
    
  //*--------------*
  //* Feature: documentSize

  /** getter for documentSize - gets Size of original document in bytes before processing by CAS Initializer. Either absolute file size of size within file or other source.
   * @generated
   * @return value of the feature 
   */
  public int getDocumentSize() { 
    return _getIntValueNc(wrapGetIntCatchException(_FH_documentSize));
  }
    
  /** setter for documentSize - sets Size of original document in bytes before processing by CAS Initializer. Either absolute file size of size within file or other source. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setDocumentSize(int v) {
    _setIntValueNfc(wrapGetIntCatchException(_FH_documentSize), v);
  }    
    
   
    
  //*--------------*
  //* Feature: lastSegment

  /** getter for lastSegment - gets For a CAS that represents a segment of a larger source document, this flag indicates whether this CAS is the final segment of the source document.  This is useful for downstream components that want to take some action after having seen all of the segments of a particular source document.
   * @generated
   * @return value of the feature 
   */
  public boolean getLastSegment() { 
    return _getBooleanValueNc(wrapGetIntCatchException(_FH_lastSegment));
  }
    
  /** setter for lastSegment - sets For a CAS that represents a segment of a larger source document, this flag indicates whether this CAS is the final segment of the source document.  This is useful for downstream components that want to take some action after having seen all of the segments of a particular source document. 
   * @generated
   * @param v value to set into the feature 
   */
  public void setLastSegment(boolean v) {
    _setBooleanValueNfc(wrapGetIntCatchException(_FH_lastSegment), v);
  }    
    
   
    
  //*--------------*
  //* Feature: SourcePath

  /** getter for SourcePath - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSourcePath() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_SourcePath));
  }
    
  /** setter for SourcePath - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSourcePath(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_SourcePath), v);
  }    
    
   
    
  //*--------------*
  //* Feature: SourceName

  /** getter for SourceName - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSourceName() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_SourceName));
  }
    
  /** setter for SourceName - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSourceName(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_SourceName), v);
  }    
    
  }

    