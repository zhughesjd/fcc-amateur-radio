package net.joshuahughes.fccamateurradio.examination;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.stat.Frequency;

import net.joshuahughes.fccamateurradio.examination.Question.State;
import net.joshuahughes.fccamateurradio.examination.exam.Exam;

public class Utility
{
	public static final Random rnd = new Random();
	public enum Ordering{forward,reverse,random}
	public enum Class
	{
		technician(412,35,26),general(454,35,26),extra(622,50,37);
	
		private int poolCount;
		private int questionCount;
		private int passingCount;
	
		Class(int plCnt,int qstnCnt, int pssCnt)
		{
			poolCount = plCnt;
			questionCount = qstnCnt;
			passingCount = pssCnt;
		}
	
		public int getPoolCount()
		{
			return poolCount;
		}
		public int getQuestionCount()
		{
			return questionCount;
		}
	
		public int getPassingCount()
		{
			return passingCount;
		}
	}
	public static String answerStats(List<Question> list)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		Frequency singular = new Frequency(); 
		Frequency inclusive = new Frequency(); 
		ps.println("---------------------------------------------------------------");
		list.stream().forEach(q->
		{
			Frequency f =q.get(3).toLowerCase().contains("are correct")?inclusive:singular;
			f.addValue((char)(q.getAnswer()+'A'));
		
		});
		ps.println("------- singular ---------");
		ps.println(toString(singular));
		ps.println("------- inclusive --------");
		ps.println(toString(inclusive));
		ps.println("--------------------------");
		ps.close();
		return new String(baos.toByteArray());
	}
	public static String runningStats(Exam exam)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		long right = exam.stream().filter(q->q.getState().equals(State.right)).count();
		long wrong = exam.stream().filter(q->q.getState().equals(State.wrong)).count();
		long remaining = exam.stream().filter(q->q.getState().equals(State.remaining)).count();
		ps.println("*********** running totals *****************");
		ps.println("remaining: "+ remaining);
		ps.println("right: "+right);
		ps.println("wrong: "+wrong);
		ps.printf("running score: %2.2f\n",100d*((double)right)/(double)(right+wrong));

		
		ps.close();
		return new String(baos.toByteArray());
	}
	private static String toString(Frequency f)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		Iterable<Map.Entry<Comparable<?>,Long>> iterable = () -> f.entrySetIterator();
		StreamSupport.stream(iterable.spliterator(), false).forEach(e->ps.println(e.getKey()+" -> "+f.getPct(e.getKey())));
		return new String(baos.toByteArray());
	}

}
