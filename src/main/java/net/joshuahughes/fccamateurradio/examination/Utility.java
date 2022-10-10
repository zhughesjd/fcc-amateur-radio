package net.joshuahughes.fccamateurradio.examination;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.commons.math3.stat.Frequency;

import net.joshuahughes.fccamateurradio.examination.Question.State;
import net.joshuahughes.fccamateurradio.examination.pool.Pool;

public class Utility
{
	public static final Random rnd = new Random();
	public enum Ordering{forward,reverse,random}
	public static String answerStats(List<Question> list)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		Frequency singular = new Frequency(); 
		Frequency inclusive = new Frequency(); 
//		ps.println(answerStringLength(list));
		
//		ps.println(answerString(list));
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
	private static String answerStringLength(List<Question> list)
	{
		TreeMap<Integer, Integer> placeMap = new TreeMap<>();
		IntStream.range(0, 4).forEach(i->placeMap.put(i,0));
		list.stream().forEach(q->
		{
			ArrayList<Integer> placeLst = new ArrayList<>(q.stream().map(s->s.length()).distinct().collect(Collectors.toList())); 
			Collections.sort(placeLst);
			IntStream.range(0, q.size()).forEach(i->
			{
				int place = placeLst.indexOf(q.get(i).length());
				if(q.getAnswer() == q.indexOf(q.get(i)));
				placeMap.put(place, placeMap.get(place) + 1);
			});
		});

		placeMap.entrySet().stream().forEach(e->
		{
			System.out.println(e);
		});
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		return "";
	}
	public static String answer(List<Question> list) 
	{
		LinkedHashMap<String, Integer> rightMap = new LinkedHashMap<>();
		LinkedHashMap<String, Integer> wrongMap = new LinkedHashMap<>();
		LinkedHashMap<String, Double> percentMap = new LinkedHashMap<>();
		list.stream().forEach(q->
		{
			int a = q.getAnswer();
			IntStream.range(0, q.size()).forEach(i->
			{
				String answer = q.get(i).substring(2).trim();
				if(!rightMap.containsKey(answer))
				{
					rightMap.put(answer, 0);
					wrongMap.put(answer, 0);
				}
				LinkedHashMap<String, Integer> resultMap = i==a?rightMap:wrongMap;
				resultMap.put(answer, resultMap.get(answer)+1);
			});
		});

		rightMap.keySet().stream().forEach(k->
		{
			double r = rightMap.get(k);
			double w = wrongMap.get(k);
			percentMap.put(k,r/(r+w));
		});
		
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
		percentMap.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		  .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		sortedMap.entrySet().forEach(e->
		{
			String k = e.getKey();
			if(rightMap.get(k)>1)
				System.out.println(e.getValue()+"\t"+rightMap.get(k)+"\t"+wrongMap.get(k)+"\t"+k);
		});
		System.out.println("*******************************************************************");
		return "";
	}
	public static String runningStats(Pool exam)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		long right = exam.stream().filter(q->q.getState().equals(State.right)).count();
		long wrong = exam.stream().filter(q->q.getState().equals(State.wrong)).count();
		long remaining = exam.stream().filter(q->q.getState().equals(State.pending)).count();
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
