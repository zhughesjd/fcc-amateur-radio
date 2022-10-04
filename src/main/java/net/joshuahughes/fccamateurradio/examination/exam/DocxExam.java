package net.joshuahughes.fccamateurradio.examination.exam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.poi.extractor.POITextExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.Utility;

public class DocxExam extends ArrayList<Question> implements Exam
{
	private static final long serialVersionUID = -5592756316715145573L;
	private File file = new File("");
	private ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	String prefix = "";
	public DocxExam(File f,Predicate<Question> predicate)
	{
		List<Question> qList = new ArrayList<>();
		file = f;
		String subelement = "invalidSubelement";
		String group = "invalidGroup";
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
				if(strings[ndx].toLowerCase().contains("subelement"))
					subelement = strings[ndx];
				if(validGroup(strings[ndx]))
					group = strings[ndx];
				String previous = strings[ndx];
				String test = strings[ndx].replaceAll("\\s+","");
				if(test.length()<8) continue;
				test = test.substring(0,8);
				if(test.matches("^[A-Z0-9]{5}\\([A-Z]\\)"))
				{
					int answer = test.split("\\(")[1].charAt(0)-'A';
					String questionString = strings[++ndx];
					Question question = new Question(subelement,group,previous,questionString, answer, qstnNdx.getAndIncrement());
					AtomicInteger n = new AtomicInteger(ndx++);
					IntStream.range(0, 4).mapToObj(i->strings[n.incrementAndGet()]).forEach(s->question.add(s));
					qList.add(question);
					ndx=n.get();
				}
			}
			prefix =
					"---------------------------\n" +
					f.getName()+"\n" +
					"---------------------------\n" +
					Utility.answerStats(qList);
			qList.stream().filter(q->predicate.test(q)).forEach(q->add(q));
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
		return 
				"-----------------\n"+
				file.getName()+"\n"+
				"-----------------\n"+
				Utility.answerStats(this);
	}
	private boolean validGroup(String string)
	{
		if(string.length()<4) return false;
		if(!Character.isAlphabetic(string.charAt(0))) return false;
		if(!Character.isDigit(string.charAt(1))) return false;
		if(!Character.isAlphabetic(string.charAt(2))) return false;
		if(!Character.isWhitespace(string.charAt(3))) return false;
		return true;
	}
	public Utility.Class getUtilityClass()
	{
		return Stream.of(Utility.Class.values()).filter(c->file.getName().toLowerCase().contains(c.name().toLowerCase())).findAny().get();
	}
}
