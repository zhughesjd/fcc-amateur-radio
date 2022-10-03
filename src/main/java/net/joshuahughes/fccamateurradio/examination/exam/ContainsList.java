package net.joshuahughes.fccamateurradio.examination.exam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.joshuahughes.fccamateurradio.examination.Question;

public class ContainsList extends ExamList
{
	private static final long serialVersionUID = -5592756316715145573L;
	public ContainsList(List<Question> questionPool,ArrayList<BufferedImage> images,String contains)
	{
		super(questionPool.stream().filter(q->
		{
			if(q.getQuestion().toLowerCase().contains(contains.toLowerCase())) return true;
			return q.stream().anyMatch(c->c.toLowerCase().contains(contains.toLowerCase()));
		}).collect(Collectors.toList()),images);
	}
}