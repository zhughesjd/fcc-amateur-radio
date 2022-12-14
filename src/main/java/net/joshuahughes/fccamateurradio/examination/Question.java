package net.joshuahughes.fccamateurradio.examination;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.stream.IntStream;

import javax.swing.JPanel;

public class Question extends ArrayList<String>
{
	public enum State{right,wrong,pending}
	public static final String propertyName = Question.class.getCanonicalName();
	public static final Question empty = new Question("subelement","group","previous","empty", -1,-1);
	static {IntStream.range(0, 4).forEach(i->empty.add("invalid_"+i));}
	private static final long serialVersionUID = -6805931509053989701L;
	JPanel handler = new JPanel();
	int selection = -1;
	String previous;
	String question;
	String subelement;
	String group;
	int answer;
	int index;
	public Question(String subelement,String group,String previous,String question, int answer, int index)
	{
		this.subelement = subelement;
		this.group = group;
		this.previous = previous;
		this.question = question;
		this.answer = answer;
		this.index = index;
	}
	public String getSubelement() {return subelement;}
	public String getGroup() {return group;}
	public String getPrevious() {return previous;}
	public String getQuestion() {return question;}
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
		if(selection == -1) return State.pending;
		return State.wrong;
	}
	public void addPropertyListener(PropertyChangeListener listener)
	{
		handler.addPropertyChangeListener(getClass().getCanonicalName(), listener);
	}
}
