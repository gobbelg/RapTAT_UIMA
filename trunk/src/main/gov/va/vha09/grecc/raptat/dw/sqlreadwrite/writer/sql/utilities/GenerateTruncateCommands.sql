select
	'DELETE FROM SuicideIdeation.raptat.' + name + ' DBCC CHECKIDENT (''SuicideIdeation.raptat.' + name + ''',RESEED, 0)' as truncate_command
from
	sys.tables
where
	schema_name(schema_id) = 'raptat'
	-- put your schema name here

	order by name;