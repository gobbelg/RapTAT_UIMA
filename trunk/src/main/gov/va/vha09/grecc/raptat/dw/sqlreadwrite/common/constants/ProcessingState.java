package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.common.constants;

/**
 * The Enum ProcessingState.
 */
public enum ProcessingState {

  /** A failure occured which is not accounted for in the code */
  UnknownFailure,
  /** The document is loaded from the input source into the system */
  Loaded,
  /** The document has been sent into RapTAT for processing */
  SentToRapTAT,
  /** The document has been processed by RapTAT */
  ProcessedByRapTAT,
  /** An error occured while processing within RapTAT */
  RatTATError,
  /** Writing to the output has begun */
  WritingStarted,
  /** The output from RapTAT has been written */
  ResultWritten,
  /** The document has been read, processed, and written out */
  Completed,
  /**
   * The document was discovered and an initial entry is created in the audit log
   */
  Loading

}
