package net.joshuahughes.fccamateurradio.examination.exam.function;

import net.joshuahughes.fccamateurradio.examination.Question;

public class Contains extends RemoveIf
{
	public Contains(String string) {super(string);}

	@Override
	public boolean removeIf(Question q, String s)
	{
		if(q.getQuestion().toLowerCase().contains(s)) return false;
		if(q.stream().filter(c->c.toLowerCase().contains(s)).findAny().isPresent()) return false;
		return true;
	}
}
