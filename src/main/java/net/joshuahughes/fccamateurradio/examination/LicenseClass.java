package net.joshuahughes.fccamateurradio.examination;

import java.io.File;
import java.util.stream.Stream;

import net.joshuahughes.fccamateurradio.examination.pool.DocxPool;
import net.joshuahughes.fccamateurradio.examination.ui.ExamFrame;

public class LicenseClass
{
	
	private int questionCount;
	private int passingCount;
	private File file;
	private DocxPool pool;
	private String name;
	public LicenseClass(String nm,LicenseClass... classes)
	{
		questionCount = Stream.of(classes).mapToInt(c->c.getQuestionCount()).sum();
		passingCount = Stream.of(classes).mapToInt(c->c.getPassingCount()).sum();
		file = new File(ExamFrame.class.getClassLoader().getResource("docx/").getFile());
		name = nm;
		pool = new DocxPool(getFile());
		pool.clear();
		pool.getImages().clear();
		Stream.of(classes).forEach(lc->
		{
			pool.addAll(lc.getPool());
			pool.getImages().addAll(pool.getImages());
		});
		
	}
	public String toString(){ return name();}
	public LicenseClass(int qstnCnt,int pssCnt,String nm)
	{
		questionCount = qstnCnt;
		passingCount = pssCnt;
		name = nm;
		File dir = new File(ExamFrame.class.getClassLoader().getResource("docx/").getFile());
		file = Stream.of(dir.listFiles()).filter(f->f.getName().toLowerCase().contains(name())).findAny().get();
		pool = new DocxPool(file);
	}

	public int getPassingCount(){return passingCount;}
	public int getQuestionCount(){return questionCount;}
	public File getFile(){return file;}
	public DocxPool getPool(){return pool;}
	public String name() {return name;}


	
	public static final LicenseClass technician = new LicenseClass(35,26,"technician");
	public static final LicenseClass general = new LicenseClass(35,26,"general");
	public static final LicenseClass extra = new LicenseClass(50,37,"extra");
	public static final LicenseClass all = new LicenseClass("all",technician,general,extra);
	public static LicenseClass[] values() 
	{
		return new LicenseClass[] {technician,general,extra,all};
	} 
}
