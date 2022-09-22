package net.joshuahughes.fccamateurradio.exam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Exam extends ArrayList<Question>
{
	private static final long serialVersionUID = -7783047363660109022L;
	private String name="unassigned";
	private List<BufferedImage> imageList;
	public Exam(String name,String string, List<BufferedImage> imageList)
	{
		this.name = name;
		this.imageList = new ArrayList<>(imageList.stream().filter(i->i!=null).collect(Collectors.toList()));
		
		String[] strings = string.split("\n");
		for(int ndx=0;ndx<strings.length;ndx++)
		{
			if(strings[ndx].toLowerCase().contains("errata"))
			{
				clear();
				continue;
			}
			String test = strings[ndx].replaceAll("\\s+","");
			if(test.length()<8) continue;
			test = test.substring(0,8);
			if(test.matches("^[A-Z0-9]{5}\\([A-Z]\\)"))
			{
				int answer = test.split("\\(")[1].charAt(0)-'A';
				String question = strings[++ndx];
				Question choices = new Question(question, answer);
				AtomicInteger n = new AtomicInteger(ndx++);
				IntStream.range(0, 4).mapToObj(i->strings[n.incrementAndGet()]).forEach(s->choices.add(s));
				add(choices);
				ndx=n.get();
			}
		}
	}
	public String toString()
	{
		return name;
	}
	public List<BufferedImage> getImages()
	{
		return imageList;
	}
	public Question nextQuestion(Question question)
	{
		if(question == null && size()>0)
			return get(0);
		int nextIndex = indexOf(question)+1;
		if(nextIndex<size())
			return get(nextIndex);
		return null;
	}
}
