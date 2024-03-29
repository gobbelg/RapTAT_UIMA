/****** Object:  Table [RapTAT].[schemas]    Script Date: 06/12/2015 16:45:12 ******/


CREATE TABLE [RapTAT].[schemas](
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


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'id_schema'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Schema file (Blob).' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'schema_file'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Schema type (eHost/Knowtator).' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'schema_type'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Source file path.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'file_path'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'512 bit hash of the file data. Used to check duplicate of a schema file.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'schemas', @level2type=N'COLUMN',@level2name=N'file_hash'


/****** Object:  Table [RapTAT].[concept]    Script Date: 06/12/2015 16:47:11 ******/


CREATE TABLE [RapTAT].[concept](
	[id_concept] [bigint] IDENTITY(1,1) NOT NULL,
	[concept_name] [varchar](50) NOT NULL,
	[id_schema] [bigint] NOT NULL,
 CONSTRAINT [PK_concept] PRIMARY KEY CLUSTERED 
(
	[id_concept] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'concept', @level2type=N'COLUMN',@level2name=N'id_concept'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Concepts from Schema file' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'concept', @level2type=N'COLUMN',@level2name=N'concept_name'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to schema table' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'concept', @level2type=N'COLUMN',@level2name=N'id_schema'


ALTER TABLE [RapTAT].[concept]  WITH CHECK ADD  CONSTRAINT [FK_concept_schema] FOREIGN KEY([id_schema])
REFERENCES [RapTAT].[schemas] ([id_schema])


ALTER TABLE [RapTAT].[concept] CHECK CONSTRAINT [FK_concept_schema]


/****** Object:  Table [RapTAT].[attribute]    Script Date: 06/12/2015 16:47:23 ******/


CREATE TABLE [RapTAT].[attribute](
	[id_attribute] [bigint] IDENTITY(1,1) NOT NULL,
	[attribute_name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_attribute] PRIMARY KEY CLUSTERED 
(
	[id_attribute] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'attribute', @level2type=N'COLUMN',@level2name=N'id_attribute'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Attribute Name' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'attribute', @level2type=N'COLUMN',@level2name=N'attribute_name'


/****** Object:  Table [RapTAT].[attr_value]    Script Date: 06/12/2015 16:47:33 ******/
CREATE TABLE [RapTAT].[attr_value](
	[id_value] [bigint] IDENTITY(1,1) NOT NULL,
	[attribute_value] [varchar](50) NOT NULL,
 CONSTRAINT [PK_attr_value] PRIMARY KEY CLUSTERED 
(
	[id_value] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'attr_value', @level2type=N'COLUMN',@level2name=N'id_value'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Value of Attribute' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'attr_value', @level2type=N'COLUMN',@level2name=N'attribute_value'


/****** Object:  Table [RapTAT].[link_type]    Script Date: 06/12/2015 16:46:41 ******/

CREATE TABLE [RapTAT].[link_type](
	[id_link_type] [bigint] IDENTITY(1,1) NOT NULL,
	[link_type] [varchar](50) NOT NULL,
	[id_schema] [bigint] NOT NULL,
 CONSTRAINT [PK_link_type] PRIMARY KEY CLUSTERED 
(
	[id_link_type] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type', @level2type=N'COLUMN',@level2name=N'id_link_type'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Link/Relationship name.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type', @level2type=N'COLUMN',@level2name=N'link_type'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to schemas table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type', @level2type=N'COLUMN',@level2name=N'id_schema'


ALTER TABLE [RapTAT].[link_type]  WITH CHECK ADD  CONSTRAINT [FK_link_type_schema] FOREIGN KEY([id_schema])
REFERENCES [RapTAT].[schemas] ([id_schema])


ALTER TABLE [RapTAT].[link_type] CHECK CONSTRAINT [FK_link_type_schema]


/****** Object:  Table [RapTAT].[link_type_concept1]    Script Date: 06/12/2015 16:46:22 ******/


CREATE TABLE [RapTAT].[link_type_concept1](
	[id_link_type] [bigint] NOT NULL,
	[id_concept] [bigint] NOT NULL
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type_concept1', @level2type=N'COLUMN',@level2name=N'id_link_type'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to concept table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type_concept1', @level2type=N'COLUMN',@level2name=N'id_concept'


ALTER TABLE [RapTAT].[link_type_concept1]  WITH CHECK ADD  CONSTRAINT [FK_link_type_concept1_concept] FOREIGN KEY([id_concept])
REFERENCES [RapTAT].[concept] ([id_concept])


ALTER TABLE [RapTAT].[link_type_concept1] CHECK CONSTRAINT [FK_link_type_concept1_concept]


ALTER TABLE [RapTAT].[link_type_concept1]  WITH CHECK ADD  CONSTRAINT [FK_link_type_concept1_link_type] FOREIGN KEY([id_link_type])
REFERENCES [RapTAT].[link_type] ([id_link_type])


ALTER TABLE [RapTAT].[link_type_concept1] CHECK CONSTRAINT [FK_link_type_concept1_link_type]


/****** Object:  Table [RapTAT].[link_type_concept2]    Script Date: 06/12/2015 16:46:11 ******/



CREATE TABLE [RapTAT].[link_type_concept2](
	[id_link_type] [bigint] NOT NULL,
	[id_concept] [bigint] NOT NULL
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type_concept2', @level2type=N'COLUMN',@level2name=N'id_link_type'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to concept table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link_type_concept2', @level2type=N'COLUMN',@level2name=N'id_concept'


ALTER TABLE [RapTAT].[link_type_concept2]  WITH CHECK ADD  CONSTRAINT [FK_link_type_concept2_concept] FOREIGN KEY([id_concept])
REFERENCES [RapTAT].[concept] ([id_concept])


ALTER TABLE [RapTAT].[link_type_concept2] CHECK CONSTRAINT [FK_link_type_concept2_concept]


ALTER TABLE [RapTAT].[link_type_concept2]  WITH CHECK ADD  CONSTRAINT [FK_link_type_concept2_link_type] FOREIGN KEY([id_link_type])
REFERENCES [RapTAT].[link_type] ([id_link_type])


ALTER TABLE [RapTAT].[link_type_concept2] CHECK CONSTRAINT [FK_link_type_concept2_link_type]


/****** Object:  Table [RapTAT].[rel_con_attr]    Script Date: 06/12/2015 16:45:29 ******/



CREATE TABLE [RapTAT].[rel_con_attr](
	[id_attribute] [bigint] NOT NULL,
	[id_concept] [bigint] NOT NULL
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to attribute table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_con_attr', @level2type=N'COLUMN',@level2name=N'id_attribute'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to concept table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_con_attr', @level2type=N'COLUMN',@level2name=N'id_concept'


ALTER TABLE [RapTAT].[rel_con_attr]  WITH CHECK ADD  CONSTRAINT [FK_rel_con_attr_attribute] FOREIGN KEY([id_attribute])
REFERENCES [RapTAT].[attribute] ([id_attribute])


ALTER TABLE [RapTAT].[rel_con_attr] CHECK CONSTRAINT [FK_rel_con_attr_attribute]


ALTER TABLE [RapTAT].[rel_con_attr]  WITH CHECK ADD  CONSTRAINT [FK_rel_con_attr_concept] FOREIGN KEY([id_concept])
REFERENCES [RapTAT].[concept] ([id_concept])


ALTER TABLE [RapTAT].[rel_con_attr] CHECK CONSTRAINT [FK_rel_con_attr_concept]


/****** Object:  Table [RapTAT].[rel_attr_attrval]    Script Date: 06/12/2015 16:45:39 ******/


CREATE TABLE [RapTAT].[rel_attr_attrval](
	[id_attr_attrval] [bigint] IDENTITY(1,1) NOT NULL,
	[id_attribute] [bigint] NOT NULL,
	[id_value] [bigint] NOT NULL,
 CONSTRAINT [PK_rel_attr_attrval] PRIMARY KEY CLUSTERED 
(
	[id_attr_attrval] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]



EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_attr_attrval', @level2type=N'COLUMN',@level2name=N'id_attr_attrval'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to attribute table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_attr_attrval', @level2type=N'COLUMN',@level2name=N'id_attribute'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to value table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_attr_attrval', @level2type=N'COLUMN',@level2name=N'id_value'


ALTER TABLE [RapTAT].[rel_attr_attrval]  WITH CHECK ADD  CONSTRAINT [FK_rel_attr_attrval_attr_value] FOREIGN KEY([id_value])
REFERENCES [RapTAT].[attr_value] ([id_value])


ALTER TABLE [RapTAT].[rel_attr_attrval] CHECK CONSTRAINT [FK_rel_attr_attrval_attr_value]


ALTER TABLE [RapTAT].[rel_attr_attrval]  WITH CHECK ADD  CONSTRAINT [FK_rel_attr_attrval_attribute] FOREIGN KEY([id_attribute])
REFERENCES [RapTAT].[attribute] ([id_attribute])


ALTER TABLE [RapTAT].[rel_attr_attrval] CHECK CONSTRAINT [FK_rel_attr_attrval_attribute]


/****** Object:  Table [RapTAT].[document]    Script Date: 06/12/2015 16:47:01 ******/


CREATE TABLE [RapTAT].[document](
	[id_document] [bigint] IDENTITY(1,1) NOT NULL,
	[document_blob] [varbinary](max) NULL,
	[document_source] [varchar](500) NULL,
	[doc_source_id] [bigint] NOT NULL,
 CONSTRAINT [PK_document] PRIMARY KEY CLUSTERED 
(
	[id_document] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'document', @level2type=N'COLUMN',@level2name=N'id_document'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Document data' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'document', @level2type=N'COLUMN',@level2name=N'document_blob'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Source path of the document' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'document', @level2type=N'COLUMN',@level2name=N'document_source'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'TIU id of the document' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'document', @level2type=N'COLUMN',@level2name=N'doc_source_id'


/****** Object:  Table [RapTAT].[sentence]    Script Date: 06/12/2015 16:45:02 ******/


CREATE TABLE [RapTAT].[sentence](
	[id_sentence] [bigint] IDENTITY(1,1) NOT NULL,
	[id_document] [bigint] NOT NULL,
	[sentence_text] [varchar](max) NULL,
 CONSTRAINT [PK_sentence] PRIMARY KEY CLUSTERED 
(
	[id_sentence] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'sentence', @level2type=N'COLUMN',@level2name=N'id_sentence'

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to document table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'sentence', @level2type=N'COLUMN',@level2name=N'id_document'

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Sentence text.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'sentence', @level2type=N'COLUMN',@level2name=N'sentence_text'

ALTER TABLE [RapTAT].[sentence]  WITH CHECK ADD  CONSTRAINT [FK_sentence_document] FOREIGN KEY([id_document])
REFERENCES [RapTAT].[document] ([id_document])

ALTER TABLE [RapTAT].[sentence] CHECK CONSTRAINT [FK_sentence_document]


/****** Object:  Table [RapTAT].[annotator]    Script Date: 06/12/2015 16:47:46 ******/


CREATE TABLE [RapTAT].[annotator](
	[id_annotator] [bigint] IDENTITY(1,1) NOT NULL,
	[annotator_name] [varchar](50) NOT NULL,
 CONSTRAINT [PK_annotator] PRIMARY KEY CLUSTERED 
(
	[id_annotator] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotator', @level2type=N'COLUMN',@level2name=N'id_annotator'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Annotator name/ Program version' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotator', @level2type=N'COLUMN',@level2name=N'annotator_name'


/****** Object:  Table [RapTAT].[annotation]    Script Date: 06/16/2015 16:43:57 ******/

CREATE TABLE [RapTAT].[annotation](
	[id_annotation] [bigint] IDENTITY(1,1) NOT NULL,
	[id_annotator] [bigint] NOT NULL,
	[id_concept] [bigint] NOT NULL,
	[xml_annotation_id] [varchar](100) NULL,
	[annotation_creation_date] [varchar](100),
 CONSTRAINT [PK_annotation] PRIMARY KEY CLUSTERED 
(
	[id_annotation] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary Key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotation', @level2type=N'COLUMN',@level2name=N'id_annotation'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotator table' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotation', @level2type=N'COLUMN',@level2name=N'id_annotator'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to concept table' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotation', @level2type=N'COLUMN',@level2name=N'id_concept'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Annotation ID from XML file' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'annotation', @level2type=N'COLUMN',@level2name=N'xml_annotation_id'


ALTER TABLE [RapTAT].[annotation]  WITH CHECK ADD  CONSTRAINT [FK_annotation_annotator] FOREIGN KEY([id_annotator])
REFERENCES [RapTAT].[annotator] ([id_annotator])


ALTER TABLE [RapTAT].[annotation] CHECK CONSTRAINT [FK_annotation_annotator]


ALTER TABLE [RapTAT].[annotation]  WITH CHECK ADD  CONSTRAINT [FK_annotation_concept] FOREIGN KEY([id_concept])
REFERENCES [RapTAT].[concept] ([id_concept])


ALTER TABLE [RapTAT].[annotation] CHECK CONSTRAINT [FK_annotation_concept]


/****** Object:  Table [RapTAT].[link]    Script Date: 06/12/2015 16:46:51 ******/


CREATE TABLE [RapTAT].[link](
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


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'id_link'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotation table. Stores the id of the source annotation.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'id_source_annotation'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotation table. Stores the id of the target annotation.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'id_targer_annotation'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to link type table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'id_link_type'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Relation ID from XML file.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'link', @level2type=N'COLUMN',@level2name=N'xml_relation_id'


ALTER TABLE [RapTAT].[link]  WITH CHECK ADD  CONSTRAINT [FK_link_annotation] FOREIGN KEY([id_source_annotation])
REFERENCES [RapTAT].[annotation] ([id_annotation])


ALTER TABLE [RapTAT].[link] CHECK CONSTRAINT [FK_link_annotation]


ALTER TABLE [RapTAT].[link]  WITH CHECK ADD  CONSTRAINT [FK_link_annotation1] FOREIGN KEY([id_targer_annotation])
REFERENCES [RapTAT].[annotation] ([id_annotation])


ALTER TABLE [RapTAT].[link] CHECK CONSTRAINT [FK_link_annotation1]


ALTER TABLE [RapTAT].[link]  WITH CHECK ADD  CONSTRAINT [FK_link_link_type] FOREIGN KEY([id_link_type])
REFERENCES [RapTAT].[link_type] ([id_link_type])


ALTER TABLE [RapTAT].[link] CHECK CONSTRAINT [FK_link_link_type]


/****** Object:  Table [RapTAT].[span]    Script Date: 06/16/2015 16:44:51 ******/


CREATE TABLE [RapTAT].[span](
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


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'id_span'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotaion table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'id_annotation'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Start offset of the span.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'start_offset'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'End offset of the Span.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'end_offset'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Span text.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'span_text'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Span sequence for multi-spanned annotation.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'span', @level2type=N'COLUMN',@level2name=N'span_sequence'


ALTER TABLE [RapTAT].[span]  WITH CHECK ADD  CONSTRAINT [FK_span_annotation] FOREIGN KEY([id_annotation])
REFERENCES [RapTAT].[annotation] ([id_annotation])


ALTER TABLE [RapTAT].[span] CHECK CONSTRAINT [FK_span_annotation]


ALTER TABLE [RapTAT].[span]  WITH CHECK ADD  CONSTRAINT [FK_span_sentence] FOREIGN KEY([id_sentence])
REFERENCES [RapTAT].[sentence] ([id_sentence])


ALTER TABLE [RapTAT].[span] CHECK CONSTRAINT [FK_span_sentence]


/****** Object:  Table [RapTAT].[rel_ann_attr]    Script Date: 06/12/2015 16:45:59 ******/


CREATE TABLE [RapTAT].[rel_ann_attr](
	[id_ann_attrid] [bigint] IDENTITY(1,1) NOT NULL,
	[id_annotation] [bigint] NOT NULL,
	[id_attr_attrval] [bigint] NOT NULL,
	[xml_attribute_id] [varchar](100) NULL,
 CONSTRAINT [PK_rel_ann_attr] PRIMARY KEY CLUSTERED 
(
	[id_ann_attrid] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Primary key.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_ann_attr', @level2type=N'COLUMN',@level2name=N'id_ann_attrid'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to annotaion table.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_ann_attr', @level2type=N'COLUMN',@level2name=N'id_annotation'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Foreign key to rel_attr_attrval table. It indicates the attribute value for the annotation.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_ann_attr', @level2type=N'COLUMN',@level2name=N'id_attr_attrval'


EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Annotation attribute ID from XML.' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'rel_ann_attr', @level2type=N'COLUMN',@level2name=N'xml_attribute_id'


ALTER TABLE [RapTAT].[rel_ann_attr]  WITH CHECK ADD  CONSTRAINT [FK_rel_ann_attr_annotator] FOREIGN KEY([id_annotation])
REFERENCES [RapTAT].[annotation] ([id_annotation])


ALTER TABLE [RapTAT].[rel_ann_attr] CHECK CONSTRAINT [FK_rel_ann_attr_annotator]


ALTER TABLE [RapTAT].[rel_ann_attr]  WITH CHECK ADD  CONSTRAINT [FK_rel_ann_attr_rel_attr_attrval] FOREIGN KEY([id_attr_attrval])
REFERENCES [RapTAT].[rel_attr_attrval] ([id_attr_attrval])


ALTER TABLE [RapTAT].[rel_ann_attr] CHECK CONSTRAINT [FK_rel_ann_attr_rel_attr_attrval]

