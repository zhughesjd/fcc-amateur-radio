package net.joshuahughes.fccamateurradio.examination;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.stream.IntStream;

import javax.swing.JPanel;

public class Question extends ArrayList<String>
{
	public enum State{right,wrong,remaining}
	public static final String propertyName = Question.class.getCanonicalName();
	public static final Question empty = new Question("empty", -1,-1);
	static {IntStream.range(0, 4).forEach(i->empty.add(""));}
	private static final long serialVersionUID = -6805931509053989701L;
	JPanel handler = new JPanel();
	int selection = -1;
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
	public int getSelection() {return selection;}
	public int setSelection(int selection) 
	{
		handler.firePropertyChange(propertyName, selection != answer, selection == answer);
		if(this.selection==selection) return selection;
		int oldValue = this.selection;
		this.selection = selection;
		return oldValue;
	}
	
	public String toString() {return question +" ["+index+"]";}
	public State getState()
	{
		if(selection == answer) return State.right;
		if(selection == -1) return State.remaining;
		return State.wrong;
	}
	public void addPropertyListener(PropertyChangeListener listener)
	{
		handler.addPropertyChangeListener(getClass().getCanonicalName(), listener);
	}
}
