package net.joshuahughes.fccamateurradio.examination.pool.function;

import net.joshuahughes.fccamateurradio.examination.Question;

public class Group extends RemoveIf
{
	public Group(String... strings) {super(strings);}
	@Override
	public boolean removeIf(Question q, String s)
	{
		return q.getGroup().equals(s);
	}
}
