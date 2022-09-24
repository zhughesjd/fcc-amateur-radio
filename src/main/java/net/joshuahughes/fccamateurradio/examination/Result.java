package net.joshuahughes.fccamateurradio.exam;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;

public class Result extends LinkedHashMap<Question,Boolean>
{
	private static final long serialVersionUID = 1619957960347129901L;
	private Exam exam;
	public Result(Exam exam)
	{
		this.exam = exam;
	}
	public String toString()
	{
		long correct = values().stream().filter(v->Boolean.TRUE.equals(v)).count();
		long missed = values().stream().filter(v->Boolean.FALSE.equals(v)).count();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		ps.println("*****************************************");
		ps.println("name: "+exam.toString());
		ps.println("*********** running totals *****************");
		ps.println("remaining: "+ exam.size());
		ps.println("right: "+correct);
		ps.println("wrong: "+missed);
		ps.printf("running score: %2.2f\n",100d*((double)correct)/(double)(correct+missed));
		return new String(baos.toByteArray());
	}
}
