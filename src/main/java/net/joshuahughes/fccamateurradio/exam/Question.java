package net.joshuahughes.fccamateurradio.exam;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Question extends ArrayList<String>
{
	public static Question empty = new Question("empty", -1,-1);
	static {IntStream.range(0, 4).forEach(i->empty.add(""));}
	private static final long serialVersionUID = -6805931509053989701L;
	String question;
	int answer;
	int index;
	public Question(String question, int answer, int index)
	{
		this.question = question;
		this.answer = answer;
		this.index = index;
	}
	public int getAnswer() {return answer;}
	public int getIndex() {return index;}
	public String toString() {return question +" ["+index+"]";}
}
