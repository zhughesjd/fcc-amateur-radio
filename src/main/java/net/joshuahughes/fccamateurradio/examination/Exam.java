package net.joshuahughes.fccamateurradio.examination;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import net.joshuahughes.fccamateurradio.examination.Question.State;

public class Exam extends ArrayList<Question>
{
	private static final long serialVersionUID = 7387093180348957357L;
	public enum Ordering{forward,reverse,random}
	private File file = new File("");
	private ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	boolean correctMistakes = true;
	int ndx = 0;
	String prefix = "";
	public boolean set(File f, boolean crtMstks, Ordering ordering,int count,Predicate<Question> filter)
	{
		List<Question> qList = new ArrayList<>();
		clear();
		imageList.clear();
		correctMistakes = crtMstks;
		file = f;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument doc = new XWPFDocument(fis);
			POITextExtractor extractor = new XWPFWordExtractor(doc);
			String questionsString = extractor.getText();
			doc.getAllPictures().stream().forEach(p->
			{
				try
				{
					imageList.add(ImageIO.read(new ByteArrayInputStream(p.getData())));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			});
			extractor.close();
			String[] strings = questionsString.split("\n");
			AtomicInteger qstnNdx = new AtomicInteger();
			for(int ndx=0;ndx<strings.length;ndx++)
			{
				if(strings[ndx].toLowerCase().contains("errata"))
				{
					clear();
					continue;
				}
				String previous = strings[ndx];
				String test = strings[ndx].replaceAll("\\s+","");
				if(test.length()<8) continue;
				test = test.substring(0,8);
				if(test.matches("^[A-Z0-9]{5}\\([A-Z]\\)"))
				{
					int answer = test.split("\\(")[1].charAt(0)-'A';
					String questionString = strings[++ndx];
					Question question = new Question(previous,questionString, answer, qstnNdx.getAndIncrement());
					AtomicInteger n = new AtomicInteger(ndx++);
					IntStream.range(0, 4).mapToObj(i->strings[n.incrementAndGet()]).forEach(s->question.add(s));
					qList.add(question);
					ndx=n.get();
				}
			}
			prefix = Utility.answerStats(qList);
			List<Question> fList = qList.stream().filter(q->filter.test(q)).collect(Collectors.toList());
			if(ordering.equals(Ordering.reverse))
				Collections.reverse(fList);
			if(ordering.equals(Ordering.random))
			{
				ArrayList<Question> temp = new ArrayList<>(fList);
				fList.clear();
				IntStream.range(0, temp.size()).forEach(i->fList.add(temp.remove(Utility.rnd.nextInt(temp.size()))));
			}
			IntStream.range(0, Math.min(count, fList.size())).forEach(i->add(fList.get(i)));
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	public String toString()
	{
		return file.getAbsolutePath();
	}
	public List<BufferedImage> getImages()
	{
		return imageList;
	}
	public boolean hasNext()
	{
		if(!correctMistakes) return ndx<size();
		return stream().filter(q->!q.getState().equals(State.right)).findAny().isPresent();
	}
	public Question next()
	{
		if(!hasNext()) return null;
		if(!correctMistakes) return get(ndx++);
		Question q = get((ndx++)%size());
		while(q.getState().equals(State.right))
			q = get((ndx++)%size());
		return q;
	}
	public String getStats()
	{
		return prefix+Utility.runningStats(this);
	}
}
