package net.joshuahughes.fccamateurradio.exam;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.TreeMap;

public class Result extends TreeMap<Integer,Boolean>
{
	private static final long serialVersionUID = 1619957960347129901L;
	private Exam exam;
	public Result(Exam exam)
	{
		this.exam = exam;
	}
	public void process(int q,int c) 
	{
		if(q<0 || c<0) return;
		Question question = exam.get(q);
		put(q,c<0 ? null : c == question.getAnswer());
	}
	public Exam getExam() {return exam;}
	public String toString()
	{
		long correct = values().stream().filter(v->Boolean.TRUE.equals(v)).count();
		long missed = values().stream().filter(v->Boolean.FALSE.equals(v)).count();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		ps.println("*****************************************");
		ps.println("name: "+exam.toString());
		ps.println("*****************************************");
		ps.println("*********** running totals *****************");
		ps.println("question: "+size()+" of "+exam.size());
		
		ps.println("right: "+correct);
		ps.println("wrong: "+missed);
		ps.printf("running score: %2.2f\n",100d*((double)correct)/size());
		return new String(baos.toByteArray());
	}
}
