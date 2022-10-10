package net.joshuahughes.fccamateurradio.examination.pool.function;

import net.joshuahughes.fccamateurradio.examination.pool.Exam;
import net.joshuahughes.fccamateurradio.examination.pool.Pool;

public class Reverse implements Creator
{
	@Override
	public Exam apply(Pool pool)
	{
		return new Exam(pool);
	}
	public String toString() {return getClass().getSimpleName();}
}
