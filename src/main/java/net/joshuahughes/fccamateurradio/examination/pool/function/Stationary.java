package net.joshuahughes.fccamateurradio.examination.pool.function;

import java.util.Collections;

import net.joshuahughes.fccamateurradio.examination.pool.Exam;
import net.joshuahughes.fccamateurradio.examination.pool.Pool;

public class Stationary implements Creator
{
	@Override
	public Exam apply(Pool pool)
	{
		Exam exam = new Exam(pool);
		Collections.reverse(exam);
		return exam;
	}
	public String toString() {return getClass().getSimpleName();}
}
