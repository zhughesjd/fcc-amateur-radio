package net.joshuahughes.fccamateurradio.examination.exam;

import net.joshuahughes.fccamateurradio.examination.Utility;

public class Exam extends PoolImpl
{
	private static final long serialVersionUID = 7016846161780289405L;
	private String prefix = "";
	public Exam(Pool pool)
	{
		addAll(pool);
		imageList.addAll(pool.getImages());
		prefix = pool.toString();
	}
	public String toString()
	{
		return prefix+Utility.runningStats(this);
	}
}
