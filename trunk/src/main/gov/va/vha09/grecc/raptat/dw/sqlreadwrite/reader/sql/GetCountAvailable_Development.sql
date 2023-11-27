SELECT count(1)
FROM
	SuicideIdeation.dbo.vw_TIUDocument_PCTEBP docs WITH (nolock)
LEFT JOIN SuicideIdeation.dbo.AuditLog_Development al WITH (nolock) ON
	docs.TIUDocumentSID = al.DocumentId
where
	AuditLogId is null
	AND ProcessingState is null
	AND EndTimestamp is null