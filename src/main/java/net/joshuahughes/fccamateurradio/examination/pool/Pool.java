package net.joshuahughes.fccamateurradio.examination.pool;

import java.awt.image.BufferedImage;
import java.util.List;

import net.joshuahughes.fccamateurradio.examination.Question;

public interface Pool extends List<Question>
{
	abstract public List<BufferedImage> getImages();
}
