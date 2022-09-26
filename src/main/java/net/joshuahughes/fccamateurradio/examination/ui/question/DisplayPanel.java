package net.joshuahughes.fccamateurradio.examination.ui.question;

import net.joshuahughes.fccamateurradio.examination.Question;

public class DisplayPanel extends QuestionPanel{
	private static final long serialVersionUID = 2491924100626019042L;
	public void setQuestion(Question qstn)
	{
		super.setQuestion(qstn);
		if(qstn.getSelection()>=0) chcs.get(qstn.getSelection()).setBackground(wrongBG);
		if(qstn.getAnswer()>=0) chcs.get(qstn.getAnswer()).setBackground(rightBG);
	}
}
