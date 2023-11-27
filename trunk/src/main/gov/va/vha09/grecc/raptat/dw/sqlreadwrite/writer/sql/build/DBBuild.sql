/* CREATE SCHEMA */
USE [SIProjectDevDB]
GO
CREATE SCHEMA [siproject]
GO

/* CREATE AND ASSOCIATE USER */
USE [master]
GO
CREATE LOGIN [siprojectuser] WITH PASSWORD=N'siprojectuser#123', DEFAULT_DATABASE=[SIProjectDevDB], CHECK_EXPIRATION=ON, CHECK_POLICY=ON
GO
USE [SIProjectDevDB]
GO
CREATE USER [siprojectuser] FOR LOGIN [siprojectuser]
GO
USE [SIProjectDevDB]
GO
ALTER ROLE [db_datareader] ADD MEMBER [siprojectuser]
GO
USE [SIProjectDevDB]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [siprojectuser]
GO

/* To prevent any potential data loss issues, you should review this script in detail before running it outside the context of the database designer.*/
BEGIN TRANSACTION
SET QUOTED_IDENTIFIER ON
SET ARITHABORT ON
SET NUMERIC_ROUNDABORT OFF
SET CONCAT_NULL_YIELDS_NULL ON
SET ANSI_NULLS ON
SET ANSI_PADDING ON
SET ANSI_WARNINGS ON
COMMIT
BEGIN TRANSACTION
GO
CREATE TABLE dbo.ProcessingQueue
	(
	id bigint NOT NULL IDENTITY (1, 1),
	sourceId varchar(50) NOT NULL,
	[text] varchar(MAX) NOT NULL,
	processingState varchar(10) NOT NULL,
	startTimeStamp datetime2(7) NULL,
	endTimeStamp datetime2(7) NULL
	)  ON [PRIMARY]
	 TEXTIMAGE_ON [PRIMARY]
GO
ALTER TABLE dbo.ProcessingQueue ADD CONSTRAINT
	PK_ProcessingQueue PRIMARY KEY CLUSTERED 
	(
	sourceId
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO
ALTER TABLE dbo.ProcessingQueue SET (LOCK_ESCALATION = TABLE)
GO
COMMIT

/* To prevent any potential data loss issues, you should review this script in detail before running it outside the context of the database designer.*/
BEGIN TRANSACTION
SET QUOTED_IDENTIFIER ON
SET ARITHABORT ON
SET NUMERIC_ROUNDABORT OFF
SET CONCAT_NULL_YIELDS_NULL ON
SET ANSI_NULLS ON
SET ANSI_PADDING ON
SET ANSI_WARNINGS ON
COMMIT
BEGIN TRANSACTION
GO
CREATE TABLE dbo.AuditLog
	(
	id bigint NOT NULL,
	message varchar(MAX) NOT NULL,
	timestamp datetime2(7) NOT NULL
	)  ON [PRIMARY]
	 TEXTIMAGE_ON [PRIMARY]
GO
ALTER TABLE dbo.AuditLog SET (LOCK_ESCALATION = TABLE)
GO
COMMIT





