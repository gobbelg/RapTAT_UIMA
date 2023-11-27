SELECT
	top 1 docs.TIUDocumentSID
	, docs.ReportText
	, al.AuditLogId
	, al.Message
	, al.ProcessingState
	, al.StartTimestamp
	, al.EndTimestamp
FROM
	SuicideIdeation.dbo.vw_TIUDocument_PCTEBP docs WITH (nolock)
LEFT JOIN SuicideIdeation.dbo.AuditLog al WITH (nolock) ON
	docs.TIUDocumentSID = al.DocumentId
where
	AuditLogId is null
	AND ProcessingState is null
	AND EndTimestamp is null
