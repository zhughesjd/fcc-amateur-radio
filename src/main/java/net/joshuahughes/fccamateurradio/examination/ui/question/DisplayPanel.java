package net.joshuahughes.fccamateurradio.examination.ui.question;

import net.joshuahughes.fccamateurradio.examination.Question;

public class DisplayPanel extends QuestionPanel{
	private static final long serialVersionUID = 2491924100626019042L;
	public void setQuestion(Question qstn,int choice)
	{
		super.setQuestion(qstn);
		if(0<=choice && choice<question.size())
			chcs.get(choice).setBackground(wrongBG);
		if(0<=question.getAnswer() && question.getAnswer()<question.size())
			chcs.get(question.getAnswer()).setBackground(rightBG);
	}
}
