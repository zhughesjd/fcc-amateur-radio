package net.joshuahughes.fccamateurradio.examination.exam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.joshuahughes.fccamateurradio.examination.Question;

public class PoolImpl extends ArrayList<Question> implements Pool
{
	private static final long serialVersionUID = 1846398106912563692L;
	ArrayList<BufferedImage> imageList = new ArrayList<BufferedImage>();

	@Override
	public List<BufferedImage> getImages() {return imageList;}

}
