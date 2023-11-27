/****** Object:  Table [raptat].[schemas]    Script Date: 06/12/2015 16:45:12 ******/

use SuicideIdeation
go


CREATE TABLE [raptat].[schemas](
	[id_schema] [bigint] IDENTITY(1,1) NOT NULL,
	[schema_file] [varbinary](max) NULL,
	[schema_type] [varchar](50) NOT NULL,
	[file_path] [varchar](500) NULL,
	[file_hash] [varchar](200) NOT NULL,
 CONSTRAINT [PK_schema] PRIMARY KEY CLUSTERED 
(
	[id_schema] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


CREATE TABLE [raptat].[solution](
	[id_solution] [bigint] IDENTITY(1,1) NOT NULL,
	[solution] [varbinary](max) NULL,
	[creation_date_time] [datetime] NULL,
 CONSTRAINT [PK_solution] PRIMARY KEY CLUSTERED 
(
	[id_solution] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO



-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'id_schema'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Schema file (Blob).' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'schema_file'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Schema type (eHost/Knowtator).' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'schema_type'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Source file path.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'file_path'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'512 bit hash of the file data. Used to check duplicate of a schema file.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'file_hash'


/****** Object:  Table [raptat].[concept]    Script Date: 06/12/2015 16:47:11 ******/


CREATE TABLE [raptat].[concept](
	[id_concept] [bigint] IDENTITY(1,1) NOT NULL,
	[concept_name] [varchar](50) NOT NULL,
	[id_schema] [bigint] NOT NULL,
 CONSTRAINT [PK_concept] PRIMARY KEY CLUSTERED 
(
	[id_concept] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'concept', @level2type=N'COLUMN',@level2name=N'id_concept'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Concepts from Schema file' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'concept', @level2type=N'COLUMN',@level2name=N'concept_name'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to schema table' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'concept', @level2type=N'COLUMN',@level2name=N'id_schema'


ALTER TABLE [raptat].[concept]  WITH CHECK ADD  CONSTRAINT [FK_concept_schema] FOREIGN KEY([id_schema])
REFERENCES [raptat].[schemas] ([id_schema])


ALTER TABLE [raptat].[concept] CHECK CONSTRAINT [FK_concept_schema]


/****** Object:  Table [raptat].[attribute]    Script Date: 06/12/2015 16:47:23 ******/


CREATE TABLE [raptat].[attribute](
	[id_attribute] [bigint] IDENTITY(1,1) NOT NULL,
	[attribute_name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_attribute] PRIMARY KEY CLUSTERED 
(
	[id_attribute] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'attribute', @level2type=N'COLUMN',@level2name=N'id_attribute'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Attribute Name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'attribute', @level2type=N'COLUMN',@level2name=N'attribute_name'


/****** Object:  Table [raptat].[attr_value]    Script Date: 06/12/2015 16:47:33 ******/
CREATE TABLE [raptat].[attr_value](
	[id_value] [bigint] IDENTITY(1,1) NOT NULL,
	[attribute_value] [varchar](50) NOT NULL,
 CONSTRAINT [PK_attr_value] PRIMARY KEY CLUSTERED 
(
	[id_value] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'attr_value', @level2type=N'COLUMN',@level2name=N'id_value'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Value of Attribute' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'attr_value', @level2type=N'COLUMN',@level2name=N'attribute_value'


/****** Object:  Table [raptat].[link_type]    Script Date: 06/12/2015 16:46:41 ******/

CREATE TABLE [raptat].[link_type](
	[id_link_type] [bigint] IDENTITY(1,1) NOT NULL,
	[link_type] [varchar](50) NOT NULL,
	[id_schema] [bigint] NOT NULL,
 CONSTRAINT [PK_link_type] PRIMARY KEY CLUSTERED 
(
	[id_link_type] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type', @level2type=N'COLUMN',@level2name=N'id_link_type'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Link/Relationship name.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type', @level2type=N'COLUMN',@level2name=N'link_type'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to schemas table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type', @level2type=N'COLUMN',@level2name=N'id_schema'


ALTER TABLE [raptat].[link_type]  WITH CHECK ADD  CONSTRAINT [FK_link_type_schema] FOREIGN KEY([id_schema])
REFERENCES [raptat].[schemas] ([id_schema])


ALTER TABLE [raptat].[link_type] CHECK CONSTRAINT [FK_link_type_schema]


/****** Object:  Table [raptat].[link_type_concept1]    Script Date: 06/12/2015 16:46:22 ******/


CREATE TABLE [raptat].[link_type_concept1](
	[id_link_type] [bigint] NOT NULL,
	[id_concept] [bigint] NOT NULL
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type_concept1', @level2type=N'COLUMN',@level2name=N'id_link_type'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to concept table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type_concept1', @level2type=N'COLUMN',@level2name=N'id_concept'


ALTER TABLE [raptat].[link_type_concept1]  WITH CHECK ADD  CONSTRAINT [FK_link_type_concept1_concept] FOREIGN KEY([id_concept])
REFERENCES [raptat].[concept] ([id_concept])


ALTER TABLE [raptat].[link_type_concept1] CHECK CONSTRAINT [FK_link_type_concept1_concept]


ALTER TABLE [raptat].[link_type_concept1]  WITH CHECK ADD  CONSTRAINT [FK_link_type_concept1_link_type] FOREIGN KEY([id_link_type])
REFERENCES [raptat].[link_type] ([id_link_type])


ALTER TABLE [raptat].[link_type_concept1] CHECK CONSTRAINT [FK_link_type_concept1_link_type]


/****** Object:  Table [raptat].[link_type_concept2]    Script Date: 06/12/2015 16:46:11 ******/



CREATE TABLE [raptat].[link_type_concept2](
	[id_link_type] [bigint] NOT NULL,
	[id_concept] [bigint] NOT NULL
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type_concept2', @level2type=N'COLUMN',@level2name=N'id_link_type'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to concept table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type_concept2', @level2type=N'COLUMN',@level2name=N'id_concept'


ALTER TABLE [raptat].[link_type_concept2]  WITH CHECK ADD  CONSTRAINT [FK_link_type_concept2_concept] FOREIGN KEY([id_concept])
REFERENCES [raptat].[concept] ([id_concept])


ALTER TABLE [raptat].[link_type_concept2] CHECK CONSTRAINT [FK_link_type_concept2_concept]


ALTER TABLE [raptat].[link_type_concept2]  WITH CHECK ADD  CONSTRAINT [FK_link_type_concept2_link_type] FOREIGN KEY([id_link_type])
REFERENCES [raptat].[link_type] ([id_link_type])


ALTER TABLE [raptat].[link_type_concept2] CHECK CONSTRAINT [FK_link_type_concept2_link_type]


/****** Object:  Table [raptat].[rel_con_attr]    Script Date: 06/12/2015 16:45:29 ******/



CREATE TABLE [raptat].[rel_con_attr](
	[id_attribute] [bigint] NOT NULL,
	[id_concept] [bigint] NOT NULL
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to attribute table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_con_attr', @level2type=N'COLUMN',@level2name=N'id_attribute'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to concept table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_con_attr', @level2type=N'COLUMN',@level2name=N'id_concept'


ALTER TABLE [raptat].[rel_con_attr]  WITH CHECK ADD  CONSTRAINT [FK_rel_con_attr_attribute] FOREIGN KEY([id_attribute])
REFERENCES [raptat].[attribute] ([id_attribute])


ALTER TABLE [raptat].[rel_con_attr] CHECK CONSTRAINT [FK_rel_con_attr_attribute]


ALTER TABLE [raptat].[rel_con_attr]  WITH CHECK ADD  CONSTRAINT [FK_rel_con_attr_concept] FOREIGN KEY([id_concept])
REFERENCES [raptat].[concept] ([id_concept])


ALTER TABLE [raptat].[rel_con_attr] CHECK CONSTRAINT [FK_rel_con_attr_concept]


/****** Object:  Table [raptat].[rel_attr_attrval]    Script Date: 06/12/2015 16:45:39 ******/


CREATE TABLE [raptat].[rel_attr_attrval](
	[id_attr_attrval] [bigint] IDENTITY(1,1) NOT NULL,
	[id_attribute] [bigint] NOT NULL,
	[id_value] [bigint] NOT NULL,
 CONSTRAINT [PK_rel_attr_attrval] PRIMARY KEY CLUSTERED 
(
	[id_attr_attrval] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]



-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_attr_attrval', @level2type=N'COLUMN',@level2name=N'id_attr_attrval'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to attribute table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_attr_attrval', @level2type=N'COLUMN',@level2name=N'id_attribute'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to value table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_attr_attrval', @level2type=N'COLUMN',@level2name=N'id_value'


ALTER TABLE [raptat].[rel_attr_attrval]  WITH CHECK ADD  CONSTRAINT [FK_rel_attr_attrval_attr_value] FOREIGN KEY([id_value])
REFERENCES [raptat].[attr_value] ([id_value])


ALTER TABLE [raptat].[rel_attr_attrval] CHECK CONSTRAINT [FK_rel_attr_attrval_attr_value]


ALTER TABLE [raptat].[rel_attr_attrval]  WITH CHECK ADD  CONSTRAINT [FK_rel_attr_attrval_attribute] FOREIGN KEY([id_attribute])
REFERENCES [raptat].[attribute] ([id_attribute])


ALTER TABLE [raptat].[rel_attr_attrval] CHECK CONSTRAINT [FK_rel_attr_attrval_attribute]


/****** Object:  Table [raptat].[document]    Script Date: 06/12/2015 16:47:01 ******/


CREATE TABLE [raptat].[document](
	[id_document] [bigint] IDENTITY(1,1) NOT NULL,
	[document_blob] [varbinary](max) NULL,
	[document_source] [varchar](500) NULL,
	[doc_source_id] [bigint] NOT NULL,
	[doc_creation_date] [datetime] NULL
 CONSTRAINT [PK_document] PRIMARY KEY CLUSTERED 
(
	[id_document] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'document', @level2type=N'COLUMN',@level2name=N'id_document'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Document data' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'document', @level2type=N'COLUMN',@level2name=N'document_blob'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Source path of the document' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'document', @level2type=N'COLUMN',@level2name=N'document_source'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'TIU id of the document' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'document', @level2type=N'COLUMN',@level2name=N'doc_source_id'


/****** Object:  Table [raptat].[sentence]    Script Date: 06/12/2015 16:45:02 ******/


CREATE TABLE [raptat].[sentence](
	[id_sentence] [bigint] IDENTITY(1,1) NOT NULL,
	[id_document] [bigint] NOT NULL,
	[sentence_text] [varchar](max) NULL,
 CONSTRAINT [PK_sentence] PRIMARY KEY CLUSTERED 
(
	[id_sentence] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'sentence', @level2type=N'COLUMN',@level2name=N'id_sentence'

-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to document table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'sentence', @level2type=N'COLUMN',@level2name=N'id_document'

-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Sentence text.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'sentence', @level2type=N'COLUMN',@level2name=N'sentence_text'

ALTER TABLE [raptat].[sentence]  WITH CHECK ADD  CONSTRAINT [FK_sentence_document] FOREIGN KEY([id_document])
REFERENCES [raptat].[document] ([id_document])

ALTER TABLE [raptat].[sentence] CHECK CONSTRAINT [FK_sentence_document]


/****** Object:  Table [raptat].[annotator]    Script Date: 06/12/2015 16:47:46 ******/


CREATE TABLE [raptat].[annotator](
	[id_annotator] [bigint] IDENTITY(1,1) NOT NULL,
	[annotator_name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_annotator] PRIMARY KEY CLUSTERED 
(
	[id_annotator] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotator', @level2type=N'COLUMN',@level2name=N'id_annotator'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Annotator name/ Program version' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotator', @level2type=N'COLUMN',@level2name=N'annotator_name'


/****** Object:  Table [raptat].[annotation]    Script Date: 06/16/2015 16:43:57 ******/

CREATE TABLE [raptat].[annotation](
	[id_annotation] [bigint] IDENTITY(1,1) NOT NULL,
	[id_annotator] [bigint] NOT NULL,
	[id_concept] [bigint] NOT NULL,
	[id_solution] [bigint] NOT NULL,
	[xml_annotation_id] [varchar](100) NULL,
	[annotation_creation_date] [varchar](50) NULL,
 CONSTRAINT [PK_annotation] PRIMARY KEY CLUSTERED 
(
	[id_annotation] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary Key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotation', @level2type=N'COLUMN',@level2name=N'id_annotation'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotator table' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotation', @level2type=N'COLUMN',@level2name=N'id_annotator'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to concept table' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotation', @level2type=N'COLUMN',@level2name=N'id_concept'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Annotation ID from XML file' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotation', @level2type=N'COLUMN',@level2name=N'xml_annotation_id'


ALTER TABLE [raptat].[annotation]  WITH CHECK ADD  CONSTRAINT [FK_annotation_annotator] FOREIGN KEY([id_annotator])
REFERENCES [raptat].[annotator] ([id_annotator])
GO

ALTER TABLE [raptat].[annotation] CHECK CONSTRAINT [FK_annotation_annotator]
GO

ALTER TABLE [raptat].[annotation]  WITH CHECK ADD  CONSTRAINT [FK_annotation_concept] FOREIGN KEY([id_concept])
REFERENCES [raptat].[concept] ([id_concept])
GO

ALTER TABLE [raptat].[annotation] CHECK CONSTRAINT [FK_annotation_concept]
GO

ALTER TABLE [raptat].[annotation]  WITH CHECK ADD  CONSTRAINT [FK_annotation_solution] FOREIGN KEY([id_solution])
REFERENCES [raptat].[solution] ([id_solution])
GO

ALTER TABLE [raptat].[annotation] CHECK CONSTRAINT [FK_annotation_solution]
GO


/****** Object:  Table [raptat].[link]    Script Date: 06/12/2015 16:46:51 ******/


CREATE TABLE [raptat].[link](
	[id_link] [bigint] IDENTITY(1,1) NOT NULL,
	[id_source_annotation] [bigint] NOT NULL,
	[id_targer_annotation] [bigint] NOT NULL,
	[id_link_type] [bigint] NOT NULL,
	[xml_relation_id] [varchar](100) NULL,
 CONSTRAINT [PK_link] PRIMARY KEY CLUSTERED 
(
	[id_link] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'id_link'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotation table. Stores the id of the source annotation.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'id_source_annotation'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotation table. Stores the id of the target annotation.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'id_targer_annotation'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to link type table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'id_link_type'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Relation ID from XML file.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'xml_relation_id'


ALTER TABLE [raptat].[link]  WITH CHECK ADD  CONSTRAINT [FK_link_annotation] FOREIGN KEY([id_source_annotation])
REFERENCES [raptat].[annotation] ([id_annotation])


ALTER TABLE [raptat].[link] CHECK CONSTRAINT [FK_link_annotation]


ALTER TABLE [raptat].[link]  WITH CHECK ADD  CONSTRAINT [FK_link_annotation1] FOREIGN KEY([id_targer_annotation])
REFERENCES [raptat].[annotation] ([id_annotation])


ALTER TABLE [raptat].[link] CHECK CONSTRAINT [FK_link_annotation1]


ALTER TABLE [raptat].[link]  WITH CHECK ADD  CONSTRAINT [FK_link_link_type] FOREIGN KEY([id_link_type])
REFERENCES [raptat].[link_type] ([id_link_type])


ALTER TABLE [raptat].[link] CHECK CONSTRAINT [FK_link_link_type]


/****** Object:  Table [raptat].[span]    Script Date: 06/16/2015 16:44:51 ******/


CREATE TABLE [raptat].[span](
	[id_span] [bigint] IDENTITY(1,1) NOT NULL,
	[id_annotation] [bigint] NOT NULL,
	[start_offset] [int] NOT NULL,
	[end_offset] [int] NOT NULL,
	[span_text] [varchar](200) NOT NULL,
	[span_sequence] [int] NOT NULL,
	[id_sentence] [bigint] NOT NULL,
 CONSTRAINT [PK_span] PRIMARY KEY CLUSTERED 
(
	[id_span] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'id_span'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotaion table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'id_annotation'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Start offset of the span.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'start_offset'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'End offset of the Span.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'end_offset'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Span text.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'span_text'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Span sequence for multi-spanned annotation.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'span_sequence'


ALTER TABLE [raptat].[span]  WITH CHECK ADD  CONSTRAINT [FK_span_annotation] FOREIGN KEY([id_annotation])
REFERENCES [raptat].[annotation] ([id_annotation])


ALTER TABLE [raptat].[span] CHECK CONSTRAINT [FK_span_annotation]


ALTER TABLE [raptat].[span]  WITH CHECK ADD  CONSTRAINT [FK_span_sentence] FOREIGN KEY([id_sentence])
REFERENCES [raptat].[sentence] ([id_sentence])


ALTER TABLE [raptat].[span] CHECK CONSTRAINT [FK_span_sentence]


/****** Object:  Table [raptat].[rel_ann_attr]    Script Date: 06/12/2015 16:45:59 ******/


CREATE TABLE [raptat].[rel_ann_attr](
	[id_ann_attrid] [bigint] IDENTITY(1,1) NOT NULL,
	[id_annotation] [bigint] NOT NULL,
	[id_attr_attrval] [bigint] NOT NULL,
	[xml_attribute_id] [varchar](100) NULL,
 CONSTRAINT [PK_rel_ann_attr] PRIMARY KEY CLUSTERED 
(
	[id_ann_attrid] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_ann_attr', @level2type=N'COLUMN',@level2name=N'id_ann_attrid'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotaion table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_ann_attr', @level2type=N'COLUMN',@level2name=N'id_annotation'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to rel_attr_attrval table. It indicates the attribute value for the annotation.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_ann_attr', @level2type=N'COLUMN',@level2name=N'id_attr_attrval'


-- EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Annotation attribute ID from XML.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_ann_attr', @level2type=N'COLUMN',@level2name=N'xml_attribute_id'


ALTER TABLE [raptat].[rel_ann_attr]  WITH CHECK ADD  CONSTRAINT [FK_rel_ann_attr_annotator] FOREIGN KEY([id_annotation])
REFERENCES [raptat].[annotation] ([id_annotation])


ALTER TABLE [raptat].[rel_ann_attr] CHECK CONSTRAINT [FK_rel_ann_attr_annotator]


ALTER TABLE [raptat].[rel_ann_attr]  WITH CHECK ADD  CONSTRAINT [FK_rel_ann_attr_rel_attr_attrval] FOREIGN KEY([id_attr_attrval])
REFERENCES [raptat].[rel_attr_attrval] ([id_attr_attrval])


ALTER TABLE [raptat].[rel_ann_attr] CHECK CONSTRAINT [FK_rel_ann_attr_rel_attr_attrval]

GO

USE [SuicideIdeation]
GO

EXEC sys.sp_dropextendedproperty @name=N'MS_DiagramPaneCount' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'vAnnotationDetail'
GO

EXEC sys.sp_dropextendedproperty @name=N'MS_DiagramPane2' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'vAnnotationDetail'
GO

EXEC sys.sp_dropextendedproperty @name=N'MS_DiagramPane1' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'vAnnotationDetail'
GO

/****** Object:  View [dbo].[vAnnotationDetail]    Script Date: 6/4/2019 7:47:02 AM ******/
DROP VIEW [dbo].[vAnnotationDetail]
GO

/****** Object:  View [dbo].[vAnnotationDetail]    Script Date: 6/4/2019 7:47:02 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [dbo].[vAnnotationDetail]
AS
SELECT        raptat.[document].id_document, raptat.span.start_offset, raptat.span.end_offset, raptat.span.span_text, raptat.concept.concept_name, raptat.attribute.attribute_name, raptat.attr_value.attribute_value
FROM            raptat.sentence INNER JOIN
                         raptat.[document] ON raptat.sentence.id_document = raptat.[document].id_document INNER JOIN
                         raptat.span ON raptat.sentence.id_sentence = raptat.span.id_sentence INNER JOIN
                         raptat.annotation ON raptat.span.id_annotation = raptat.annotation.id_annotation INNER JOIN
                         raptat.concept ON raptat.annotation.id_concept = raptat.concept.id_concept INNER JOIN
                         raptat.rel_ann_attr ON raptat.annotation.id_annotation = raptat.rel_ann_attr.id_annotation INNER JOIN
                         raptat.rel_attr_attrval ON raptat.rel_ann_attr.id_attr_attrval = raptat.rel_attr_attrval.id_attr_attrval INNER JOIN
                         raptat.attr_value ON raptat.rel_attr_attrval.id_value = raptat.attr_value.id_value INNER JOIN
                         raptat.attribute ON raptat.rel_attr_attrval.id_attribute = raptat.attribute.id_attribute
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "annotation (raptat)"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 305
               Right = 278
            End
            DisplayFlags = 344
            TopColumn = 0
         End
         Begin Table = "document (raptat)"
            Begin Extent = 
               Top = 5
               Left = 784
               Bottom = 135
               Right = 968
            End
            DisplayFlags = 344
            TopColumn = 0
         End
         Begin Table = "sentence (raptat)"
            Begin Extent = 
               Top = 7
               Left = 570
               Bottom = 123
               Right = 740
            End
            DisplayFlags = 344
            TopColumn = 0
         End
         Begin Table = "span (raptat)"
            Begin Extent = 
               Top = 0
               Left = 295
               Bottom = 203
               Right = 500
            End
            DisplayFlags = 344
            TopColumn = 0
         End
         Begin Table = "concept (raptat)"
            Begin Extent = 
               Top = 114
               Left = 91
               Bottom = 236
               Right = 275
            End
            DisplayFlags = 344
            TopColumn = 0
         End
         Begin Table = "rel_ann_attr (raptat)"
            Begin Extent = 
               Top = 61
               Left = 316
               Bottom = 191
               Right = 489
            End
            DisplayFlags = 344
            TopColumn = 0
         End
         Begin Table = "rel_attr_attrval (raptat)"
            Begin Extent = 
               Top = 69
               Left = 568
               Bottom = 182
  ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'vAnnotationDetail'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'             Right = 738
            End
            DisplayFlags = 344
            TopColumn = 0
         End
         Begin Table = "attr_value (raptat)"
            Begin Extent = 
               Top = 70
               Left = 780
               Bottom = 166
               Right = 950
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "attribute (raptat)"
            Begin Extent = 
               Top = 240
               Left = 316
               Bottom = 336
               Right = 486
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 3345
         Width = 3780
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'vAnnotationDetail'
GO

EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'vAnnotationDetail'
GO



/****** Object:  View [raptat].[Annotation_View]    Script Date: 6/4/2019 2:36:28 PM ******/
DROP VIEW [raptat].[Annotation_View]
GO

/****** Object:  View [raptat].[Annotation_View]    Script Date: 6/4/2019 2:36:28 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


create view [raptat].[Annotation_View] as 
select D.id_document
	, SN.id_sentence
	, SP.id_span
	, SP.id_annotation
	, A.id_concept
	, AN.annotator_name
	, SP.start_offset
	, SP.end_offset
	, SP.span_text
	, A.xml_annotation_id 
	, D.doc_creation_date
from Raptat.document as D
	join Raptat.sentence as Sn
		on D.id_document = SN.id_document
	join Raptat.span as SP
		on SN.id_sentence = SP.id_sentence
	join Raptat.annotation as A
		on SP.id_annotation = A.id_annotation
	join RapTAT.annotator as AN
		on A.id_annotator = AN.id_annotator


GO



/****** Object:  View [raptat].[Attribute_View]    Script Date: 6/4/2019 2:36:35 PM ******/
DROP VIEW [raptat].[Attribute_View]
GO

/****** Object:  View [raptat].[Attribute_View]    Script Date: 6/4/2019 2:36:35 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


create view [raptat].[Attribute_View] as
select AV.id_document
	, AV.id_annotation
	, RAAV.id_attribute
	, A.attribute_name
	, AVAL.attribute_value
	, RAA.xml_attribute_id
from Annotation_View as AV
	join Raptat.rel_ann_attr as RAA
		on AV.id_annotation = RAA.id_annotation
	join Raptat.rel_attr_attrval as RAAV
		on RAA.id_attr_attrval = RAAV.id_attr_attrval
	join Raptat.attribute as A
		on RAAV.id_attribute = A.id_attribute
	join Raptat.attr_value as AVAL
		on RAAV.id_value = AVAL.id_value
GO

/****** Object:  View [raptat].[Relation_View]    Script Date: 6/4/2019 2:36:43 PM ******/
DROP VIEW [raptat].[Relation_View]
GO

/****** Object:  View [raptat].[Relation_View]    Script Date: 6/4/2019 2:36:43 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


create view [raptat].[Relation_View] as
select AV.id_document
	, AV.id_annotation
	, L.id_targer_annotation
	, LT.link_type
	, L.xml_relation_id
from Raptat.Link as L
	join Annotation_view as AV
		on L.id_source_annotation = AV.id_annotation
	join Raptat.link_type as LT
		on L.id_link_type = LT.id_link_type

GO

