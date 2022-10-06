package net.joshuahughes.fccamateurradio.examination.pool.function;

import net.joshuahughes.fccamateurradio.examination.Question;
import net.joshuahughes.fccamateurradio.examination.pool.Exam;
import net.joshuahughes.fccamateurradio.examination.pool.Pool;

public abstract class RemoveIf implements Creator
{
	String string;
	public RemoveIf(String string) {this.string = string;}
	@Override
	public Exam apply(Pool pool)
	{
		Exam exam = new Exam(pool);
		exam.removeIf(q->removeIf(q,string));
		return exam;
	}
	public abstract boolean removeIf(Question q, String s);
}
