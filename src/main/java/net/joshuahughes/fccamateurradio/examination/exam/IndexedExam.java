package net.joshuahughes.fccamateurradio.examination.exam;

import java.io.File;

public class IndexedExam extends DocxExam
{
	private static final long serialVersionUID = -2085521442066356828L;

	public IndexedExam(File file,int min,int max)
	{
		super(file,q->min<=q.getIndex() && q.getIndex()<=max);
;	}
}
