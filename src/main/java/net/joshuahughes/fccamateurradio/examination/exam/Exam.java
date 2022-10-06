package net.joshuahughes.fccamateurradio.examination.exam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.Utility;

public class Exam extends ArrayList<Question>implements Pool
{
	private static final long serialVersionUID = 7016846161780289405L;
	private String prefix = "";
	List<BufferedImage> imageList = new ArrayList<BufferedImage>();
	public Exam(Pool pool)
	{
		prefix = pool.toString();
		addAll(pool);
		imageList.addAll(pool.getImages());
	}
	@Override
	public List<BufferedImage> getImages()
	{
		return imageList;
	}
	public String toString()
	{
		return prefix+Utility.runningStats(this);
	}
}
