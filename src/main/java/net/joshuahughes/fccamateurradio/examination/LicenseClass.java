package net.joshuahughes.fccamateurradio.examination;

import java.io.File;
import java.util.stream.Stream;

import net.joshuahughes.fccamateurradio.examination.pool.DocxPool;
import net.joshuahughes.fccamateurradio.examination.ui.ExamDialog;

public enum LicenseClass
{
	technician(35,26),general(35,26),extra(50,37);
	
	private int questionCount;
	private int passingCount;
	private File file;
	private DocxPool pool;
	
	LicenseClass(int qstnCnt, int pssCnt)
	{
		questionCount = qstnCnt;
		passingCount = pssCnt;
		File dir = new File(ExamDialog.class.getClassLoader().getResource("docx/").getFile());
		file = Stream.of(dir.listFiles()).filter(f->f.getName().toLowerCase().contains(name())).findAny().get();
		pool = new DocxPool(file);
	}
	public int getPassingCount(){return passingCount;}
	public int getQuestionCount(){return questionCount;}
	public File getFile(){return file;}
	public DocxPool getPool(){return pool;}
}
