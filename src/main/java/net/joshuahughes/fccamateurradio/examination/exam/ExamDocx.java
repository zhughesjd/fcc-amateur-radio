package net.joshuahughes.fccamateurradio.examination.exam;

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

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.Utility;

public class ExamDocx extends ArrayList<Question> implements Exam
{
	private static final long serialVersionUID = -5592756316715145573L;
	private File file = new File("");
	private ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	int ndx = 0;
	String prefix = "";
	public void set(File f,Utility.Ordering ordering, int count, Predicate<Question> filter)
	{
		List<Question> qList = new ArrayList<>();
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
			prefix =
					"---------------------------\n" +
					"---------------------------\n" +
					Utility.answerStats(qList);
			List<Question> fList = qList.stream().filter(q->filter.test(q)).collect(Collectors.toList());
			if(ordering.equals(Utility.Ordering.reverse))
				Collections.reverse(fList);
			if(ordering.equals(Utility.Ordering.random))
			{
				ArrayList<Question> temp = new ArrayList<>(fList);
				fList.clear();
				IntStream.range(0, temp.size()).forEach(i->fList.add(temp.remove(Utility.rnd.nextInt(temp.size()))));
			}
			IntStream.range(0, Math.min(count, fList.size())).forEach(i->add(fList.get(i)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public List<BufferedImage> getImages()
	{
		return imageList;
	}
	public String toString()
	{
		return prefix+Utility.runningStats(this);
	}
}
