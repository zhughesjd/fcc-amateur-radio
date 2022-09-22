package net.joshuahughes.fccamateurradio.exam;

import java.util.ArrayList;

public class Question extends ArrayList<String>
{
	private static final long serialVersionUID = -6805931509053989701L;
	String question;
	int answer; 
	public Question(String question, int answer)
	{
		this.question = question;
		this.answer = answer;
	}
	public int getAnswer() {return answer;}
	public String toString() {return question;}
}
