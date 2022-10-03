package net.joshuahughes.fccamateurradio.examination.exam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.joshuahughes.fccamateurradio.examination.Question;

public class ExamList extends ArrayList<Question> implements Exam
{
	private static final long serialVersionUID = -5592756316715145573L;
	private ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public ExamList(List<Question> questionPool,ArrayList<BufferedImage> images)
	{
		addAll(questionPool);
		imageList.addAll(images);
	}
	public List<BufferedImage> getImages()
	{
		return imageList;
	}
}