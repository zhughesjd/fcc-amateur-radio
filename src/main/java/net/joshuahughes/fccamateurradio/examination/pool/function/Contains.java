package net.joshuahughes.fccamateurradio.examination.pool.function;

import net.joshuahughes.fccamateurradio.examination.Question;

public class Contains extends RemoveIf
{
	public Contains(String... strings) {super(strings);}
	@Override
	public boolean removeIf(Question q, String string)
	{
		String s = string.toLowerCase();
		if(q.getQuestion().toLowerCase().contains(s)) {return false;}
		if(q.stream().filter(c->c.toLowerCase().contains(s)).findAny().isPresent()) return false;
		return true;
	}
}
