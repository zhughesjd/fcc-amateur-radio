package net.joshuahughes.fccamateurradio.examination.exam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.Question.State;
import net.joshuahughes.fccamateurradio.examination.Utility;

public class ExamList extends ArrayList<Question> implements Exam
{
	private static final long serialVersionUID = -5592756316715145573L;
	private ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();
	boolean fixMistakes = true;
	int ndx = 0;
	public void set(ArrayList<Question> questions,ArrayList<BufferedImage> images)
	{
		clear();
		addAll(questions);
		imageList.clear();
		imageList.addAll(images);
	}
	public List<BufferedImage> getImages()
	{
		return imageList;
	}
	public boolean hasNext()
	{
		if(!fixMistakes) return ndx<size();
		return stream().filter(q->!q.getState().equals(State.right)).findAny().isPresent();
	}
	public Question next()
	{
		if(!hasNext()) return null;
		if(!fixMistakes) return get(ndx++);
		Question q = get((ndx++)%size());
		while(q.getState().equals(State.right))
			q = get((ndx++)%size());
		return q;
	}
	public String toString()
	{
		return Utility.runningStats(this);
	}
}