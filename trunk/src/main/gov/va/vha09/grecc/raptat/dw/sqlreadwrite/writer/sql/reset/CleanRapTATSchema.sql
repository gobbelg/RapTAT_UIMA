truncate table raptat.link;
truncate table raptat.link_type_concept1;
truncate table raptat.link_type_concept2;
truncate table raptat.rel_ann_attr;
truncate table raptat.rel_con_attr;
truncate table raptat.span;

DELETE FROM SuicideIdeation.raptat.annotation DBCC CHECKIDENT ('SuicideIdeation.raptat.annotation',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.annotator DBCC CHECKIDENT ('SuicideIdeation.raptat.annotator',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.attr_value DBCC CHECKIDENT ('SuicideIdeation.raptat.attr_value',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.attribute DBCC CHECKIDENT ('SuicideIdeation.raptat.attribute',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.concept DBCC CHECKIDENT ('SuicideIdeation.raptat.concept',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.sentence DBCC CHECKIDENT ('SuicideIdeation.raptat.sentence',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.schemas DBCC CHECKIDENT ('SuicideIdeation.raptat.schemas',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.rel_attr_attrval DBCC CHECKIDENT ('SuicideIdeation.raptat.rel_attr_attrval',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.link_type DBCC CHECKIDENT ('SuicideIdeation.raptat.link_type',RESEED, 0)
DELETE FROM SuicideIdeation.raptat.document DBCC CHECKIDENT ('SuicideIdeation.raptat.document',RESEED, 0)
