package net.joshuahughes.fccamateurradio.examination;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import javax.imageio.ImageIO;

import org.apache.commons.math3.stat.Frequency;
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
	public boolean set(File f, int count, boolean crtMstks, Ordering ordering, String contains)
	{
		ArrayList<Question> qList = new ArrayList<>();
		try
		{
			clear();
			imageList.clear();
			correctMistakes = crtMstks;
			file = f;
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
			prefix = answerStats(qList);
			Iterator<Question> it = qList.iterator();
			String lo = contains.toLowerCase();
			while(it.hasNext())
			{
				Question q = it.next();
				if
					(
					!q.getQuestion().toLowerCase().contains(lo) &&
					!q.getPrevious().toLowerCase().contains(lo) &&
					!q.stream().anyMatch(s->s.toLowerCase().contains(lo))
				)
					it.remove();
			}
			if(ordering.equals(Ordering.reverse))
				Collections.reverse(qList);
			if(ordering.equals(Ordering.random))
			{
				ArrayList<Question> temp = new ArrayList<>(qList);
				qList.clear();
				IntStream.range(0, Math.min(count, temp.size())).forEach(i->qList.add(temp.remove(Utility.rnd.nextInt(temp.size()))));
			}
			IntStream.range(0, Math.min(count, qList.size())).forEach(i->add(qList.get(i)));
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
		return prefix+runningStats(this);
	}
	private static String answerStats(ArrayList<Question> list)
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
		ps.println("------- singular --------");
		ps.println(toString(singular));
		ps.println("------- inclusive --------");
		ps.println(toString(inclusive));
		ps.close();
		return new String(baos.toByteArray());
	}
	private static String runningStats(Exam exam)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
		PrintStream ps = new PrintStream(baos);
		long right = exam.stream().filter(q->q.getState().equals(State.right)).count();
		long wrong = exam.stream().filter(q->q.getState().equals(State.wrong)).count();
		long remaining = exam.stream().filter(q->q.getState().equals(State.remaining)).count();
		ps.println("*****************************************");
		ps.println("name: "+exam.toString());
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
