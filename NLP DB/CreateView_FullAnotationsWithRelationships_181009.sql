use [ORD_Matheny_201312053D];

go

CREATE VIEW RapTAT.full_annnotation_with_relationships AS

with 

-- Get span associated with each annotation (ignore multi-span for now)
ann_spn as
(
select 
	ann.id_annotation, ann.id_concept, ann.xml_annotation_id, spn.start_offset, spn.end_offset, spn.span_text, spn.id_sentence 
from 
	RapTAT.annotation as ann 
	left join 
	(
		select id_annotation, start_offset, end_offset, span_text, id_sentence 
		from RapTAT.span
	) 
	as spn
	on  ann.id_annotation=spn.id_annotation
),

-- Get sentence text and document id associated with each annotation and span
ann_snt as
(
select 
	ann_spn.id_annotation, ann_spn.id_concept, ann_spn.xml_annotation_id, ann_spn.start_offset, ann_spn.end_offset, ann_spn.span_text, snt.sentence_text, snt.id_document
from 
	ann_spn 
	left join 
	(
		select sentence_text, id_document, id_sentence
		from RapTAT.sentence
	) 
	as snt
	on  ann_spn.id_sentence = snt.id_sentence
),

-- Lookup and replace document id with source_id (VA document SID) and path where the annotated document was found
ann_doc as
(
	select
		id_annotation, id_concept, xml_annotation_id, start_offset, end_offset, span_text, sentence_text, doc.doc_source_id, doc.document_source
	from 
		ann_snt
	left join
	(
		select doc_source_id, id_document, document_source
		from Raptat.document 
	)
	as doc
	on ann_snt.id_document = doc.id_document
),

-- Get relationship id connecting annotation to its attribute and its value
as_attr_rel as
(
select 
	ann_doc.id_annotation,id_concept, rs.id_attr_attrval, xml_annotation_id, start_offset, end_offset, span_text, sentence_text, doc_source_id, document_source
from 
	ann_doc
	left join
	(
		select id_attr_attrval, id_annotation from Raptat.rel_ann_attr
	)
	as rs
	on ann_doc.id_annotation = rs.id_annotation
),

-- Get ids of attributes and attribute values connected to each annotation
id_attr_and_val as
(
select 
	id_annotation, id_concept, xml_annotation_id, start_offset, end_offset, span_text, sentence_text, doc_source_id, document_source, att.id_attr_attrval, att.id_attribute, att.id_value 
from	
	as_attr_rel as aar
	left join
	(
		select * from Raptat.rel_attr_attrval
	)
	as att
	on aar.id_attr_attrval = att.id_attr_attrval
),

-- Lookup name of attribute and use it to replace id_attribute
id_av
as
(
select 
	id_attr_and_val.id_annotation id_ann
	,id_attr_and_val.id_concept id_cpt
	,id_attr_and_val.xml_annotation_id id_xml
	,id_attr_and_val.start_offset start_off
	,id_attr_and_val.end_offset end_off
	,id_attr_and_val.span_text ann_txt
	,id_attr_and_val.id_value id_val
	,att.attribute_name att_name
	,sentence_text snt_txt
	,doc_source_id doc_src_id
	,document_source doc_src
from 
	id_attr_and_val
	left join
	Raptat.attribute as att
	on att.id_attribute = id_attr_and_val.id_attribute
),
ann_and_att_and_val

-- Lookup attribute value id and replce it with name of the value
as
(
select 
	id_ann
	,id_cpt
	,id_xml
	,start_off
	,end_off
	,ann_txt
	,att_name
	,av.attribute_value att_val
	,snt_txt
	,doc_src_id
	,doc_src
from 
	id_av
	left join
	Raptat.attr_value as av
	on id_av.id_val = av.id_value
),

-- Lookup concept id and replace it with concept name
ann_and_cpt
as
(
select
	id_ann
	,cpt.concept_name cpt_name
	,id_xml
	,start_off
	,end_off
	,ann_txt
	,att_name
	,att_val
	,snt_txt
	,doc_src_id
	,doc_src
from
	ann_and_att_and_val
	left join 
	Raptat.concept as cpt
	on ann_and_att_and_val.id_cpt=cpt.id_concept
),

-- Lookup id of link type by annotation id and add it and the id to which it is linked (the 'target' annotation)
ann_and_lnktype_id
as
(
select
	id_ann
	,lnk.id_link lnk_typ_id
	,lnk.id_targer_annotation trg_id
	,cpt_name
	,id_xml
	,start_off
	,end_off
	,ann_txt
	,att_name
	,att_val
	,snt_txt
	,doc_src_id
	,doc_src
from
	ann_and_cpt
	left join 
	Raptat.link lnk
	on id_ann=lnk.id_source_annotation
),

-- Replace link type with link type name
ann_and_lnktype_name
as
(
select
	id_ann
	,cpt_name
	,id_xml
	,start_off
	,end_off
	,ann_txt
	,att_name
	,att_val
	,Raptat.link_type.link_type lnk_nam
	,trg_id
	,snt_txt
	,doc_src_id
	,doc_src
from
	ann_and_lnktype_id
	left join 
	Raptat.link_type
	on Raptat.link_type.id_link_type = lnk_typ_id
),

-- Get concept ID of linked annotation
ann_and_lnk_cpt_id
as
(
select
	id_ann
	,cpt_name
	,id_xml
	,start_off
	,end_off
	,ann_txt
	,att_name
	,att_val
	,lnk_nam
	,trg_id
	,ann.id_concept lnk_cpt_id
	,snt_txt
	,doc_src_id
	,doc_src
from
	ann_and_lnktype_name aln
	left join 
	Raptat.annotation ann
	on aln.trg_id = ann.id_annotation
)

-- Lookup linked concept id and replace it with linked concept name
select
	id_ann SQLAnnotationID
	,cpt_name Concept
	,id_xml XmlMentionID
	,start_off StartOffset
	,end_off EndOffset
	,ann_txt AnnotatedText
	,att_name AttributeName
	,att_val AttributeValue
	,lnk_nam RelationshipName
	,trg_id SQLAnnotationIdOfRelatedAnnoation
	,cpt.concept_name ConceptOfRelatedAnnotation
	,snt_txt SentenceText
	,doc_src_id VADocumentSID
	,doc_src TextSourcePath
from 
	ann_and_lnk_cpt_id
	left join
	Raptat.Concept cpt
	on cpt.id_concept=lnk_cpt_id