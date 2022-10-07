package net.joshuahughes.fccamateurradio.examination.pool.function;

import net.joshuahughes.fccamateurradio.examination.Question;

public class Subelement extends RemoveIf
{

	public Subelement(String... strings) {super(strings);}
	@Override
	public boolean removeIf(Question q, String s)
	{
		return q.getSubelement().equals(s);
	}
}
