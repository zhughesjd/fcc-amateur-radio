package net.joshuahughes.fccamateurradio.examination.pool.function;

import net.joshuahughes.fccamateurradio.examination.Question;

public class Subelement extends RemoveIf
{

	public Subelement(String string) {super(string);}

	@Override
	public boolean removeIf(Question q, String s) {
		return q.getSubelement().equals(s);
	}
}
