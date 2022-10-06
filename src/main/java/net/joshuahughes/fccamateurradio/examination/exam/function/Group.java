package net.joshuahughes.fccamateurradio.examination.exam.function;

import net.joshuahughes.fccamateurradio.examination.Question;

public class Group extends RemoveIf
{

	public Group(String string) {super(string);}

	@Override
	public boolean removeIf(Question q, String s) {
		return q.getGroup().equals(s);
	}
}
