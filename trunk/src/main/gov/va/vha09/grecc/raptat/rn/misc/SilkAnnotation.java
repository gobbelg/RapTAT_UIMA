package src.main.gov.va.vha09.grecc.raptat.rn.misc;

public class SilkAnnotation {
	
	public final String label;
	public final int begin;
	public final int end;
	public final String documentSource;

	public SilkAnnotation(String aLabel, int aBegin, int aEnd, String aDocumentSource)
	{
		label = aLabel;
		begin = aBegin;
		end = aEnd;
		documentSource = aDocumentSource;
	}

}
