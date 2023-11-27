use [<database>];

IF EXISTS(SELECT 'view exists' FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME = N'Annotation_View'AND TABLE_SCHEMA = 'RapTAT')
BEGIN
    DROP VIEW RapTAT.Annotation_View
END

GO 

create view RapTAT.Annotation_View as 
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

IF EXISTS(SELECT 'view exists' FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME = N'Relation_View'AND TABLE_SCHEMA = 'RapTAT')
BEGIN
    DROP VIEW RapTAT.Relation_View
END

GO 

create view RapTAT.Relation_View as
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

IF EXISTS(SELECT 'view exists' FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME = N'Attribute_View'AND TABLE_SCHEMA = 'RapTAT')
BEGIN
    DROP VIEW RapTAT.Relation_View
END

GO

create view RapTAT.Attribute_View as
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