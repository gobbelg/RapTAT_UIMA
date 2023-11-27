use [SuicideIdeation]
go

ALTER TABLE [raptat].[span] DROP CONSTRAINT [FK_span_sentence]
GO
ALTER TABLE [raptat].[span] DROP CONSTRAINT [FK_span_annotation]
GO
ALTER TABLE [raptat].[sentence] DROP CONSTRAINT [FK_sentence_document]
GO
ALTER TABLE [raptat].[rel_con_attr] DROP CONSTRAINT [FK_rel_con_attr_concept]
GO
ALTER TABLE [raptat].[rel_con_attr] DROP CONSTRAINT [FK_rel_con_attr_attribute]
GO
ALTER TABLE [raptat].[rel_attr_attrval] DROP CONSTRAINT [FK_rel_attr_attrval_attribute]
GO
ALTER TABLE [raptat].[rel_attr_attrval] DROP CONSTRAINT [FK_rel_attr_attrval_attr_value]
GO
ALTER TABLE [raptat].[rel_ann_attr] DROP CONSTRAINT [FK_rel_ann_attr_rel_attr_attrval]
GO
ALTER TABLE [raptat].[rel_ann_attr] DROP CONSTRAINT [FK_rel_ann_attr_annotator]
GO
ALTER TABLE [raptat].[link_type_concept2] DROP CONSTRAINT [FK_link_type_concept2_link_type]
GO
ALTER TABLE [raptat].[link_type_concept2] DROP CONSTRAINT [FK_link_type_concept2_concept]
GO
ALTER TABLE [raptat].[link_type_concept1] DROP CONSTRAINT [FK_link_type_concept1_link_type]
GO
ALTER TABLE [raptat].[link_type_concept1] DROP CONSTRAINT [FK_link_type_concept1_concept]
GO
ALTER TABLE [raptat].[link_type] DROP CONSTRAINT [FK_link_type_schema]
GO
ALTER TABLE [raptat].[link] DROP CONSTRAINT [FK_link_link_type]
GO
ALTER TABLE [raptat].[link] DROP CONSTRAINT [FK_link_annotation1]
GO
ALTER TABLE [raptat].[link] DROP CONSTRAINT [FK_link_annotation]
GO
ALTER TABLE [raptat].[concept] DROP CONSTRAINT [FK_concept_schema]
GO
ALTER TABLE [raptat].[annotation] DROP CONSTRAINT [FK_annotation_solution]
GO
ALTER TABLE [raptat].[annotation] DROP CONSTRAINT [FK_annotation_concept]
GO
ALTER TABLE [raptat].[annotation] DROP CONSTRAINT [FK_annotation_annotator]
GO
/****** Object:  Table [raptat].[span]    Script Date: 12/17/2018 3:36:37 PM ******/
DROP TABLE [raptat].[span]
GO
/****** Object:  Table [raptat].[solution]    Manual Entry by Glenn: 10/09/2020 ******/
DROP TABLE [raptat].[solution]
GO
/****** Object:  Table [raptat].[sentence]    Script Date: 12/17/2018 3:36:37 PM ******/
DROP TABLE [raptat].[sentence]
GO
/****** Object:  Table [raptat].[schemas]    Script Date: 12/17/2018 3:36:37 PM ******/
DROP TABLE [raptat].[schemas]
GO
/****** Object:  Table [raptat].[rel_con_attr]    Script Date: 12/17/2018 3:36:37 PM ******/
DROP TABLE [raptat].[rel_con_attr]
GO
/****** Object:  Table [raptat].[rel_attr_attrval]    Script Date: 12/17/2018 3:36:37 PM ******/
DROP TABLE [raptat].[rel_attr_attrval]
GO
/****** Object:  Table [raptat].[rel_ann_attr]    Script Date: 12/17/2018 3:36:37 PM ******/
DROP TABLE [raptat].[rel_ann_attr]
GO
/****** Object:  Table [raptat].[link_type_concept2]    Script Date: 12/17/2018 3:36:37 PM ******/
DROP TABLE [raptat].[link_type_concept2]
GO
/****** Object:  Table [raptat].[link_type_concept1]    Script Date: 12/17/2018 3:36:37 PM ******/
DROP TABLE [raptat].[link_type_concept1]
GO
/****** Object:  Table [raptat].[link_type]    Script Date: 12/17/2018 3:36:38 PM ******/
DROP TABLE [raptat].[link_type]
GO
/****** Object:  Table [raptat].[link]    Script Date: 12/17/2018 3:36:38 PM ******/
DROP TABLE [raptat].[link]
GO
/****** Object:  Table [raptat].[document]    Script Date: 12/17/2018 3:36:38 PM ******/
DROP TABLE [raptat].[document]
GO
/****** Object:  Table [raptat].[concept]    Script Date: 12/17/2018 3:36:38 PM ******/
DROP TABLE [raptat].[concept]
GO
/****** Object:  Table [raptat].[attribute]    Script Date: 12/17/2018 3:36:38 PM ******/
DROP TABLE [raptat].[attribute]
GO
/****** Object:  Table [raptat].[attr_value]    Script Date: 12/17/2018 3:36:38 PM ******/
DROP TABLE [raptat].[attr_value]
GO
/****** Object:  Table [raptat].[annotator]    Script Date: 12/17/2018 3:36:38 PM ******/
DROP TABLE [raptat].[annotator]
GO
/****** Object:  Table [raptat].[annotation]    Script Date: 12/17/2018 3:36:38 PM ******/
DROP TABLE [raptat].[annotation]
GO
