package net.joshuahughes.fccamateurradio.exam.ui.question;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.JRadioButton;

import net.joshuahughes.fccamateurradio.exam.Question;

public class ChoicePanel extends QuestionPanel
{
	private static final long serialVersionUID = -1098765530107793570L;
	Color tglBG = defltBG;
	public ChoicePanel()
	{
		setFocusable(true);
		chcs.stream().forEach(r->chcGrp.add(r));
		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				if(e.getKeyChar() == 'a')
				{
					tglBG = tglBG.equals(defltBG)?rightBG:defltBG;
					getRadioAnswer().setBackground(tglBG);
				}
			}
			@Override
			public void keyReleased(KeyEvent e)
			{
				if(e.getKeyChar() == 'h' && tglBG.equals(defltBG))
					getRadioAnswer().setBackground(defltBG);
			}
			
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyChar() == 'h')
				{
					getRadioAnswer().setBackground(rightBG);
				}
			}
		});
		chcs.stream().forEach(r->
		{
			r.addActionListener(l->
			{
				if(question == null) return;
				Optional<JRadioButton> op = chcs.stream().filter(b->b.isSelected()).findAny();
				int choice = op.isPresent()?chcs.indexOf(op.get()):-1;
				ChoicePanel.this.firePropertyChange(ChoicePanel.class.getCanonicalName(), null,new Integer(choice));
			});
		});
	}
	public void setQuestion(Question qstn)
	{
		super.setQuestion(qstn);
		if(qstn == Question.empty) return;
		JRadioButton ra = getRadioAnswer();
		ra.setBackground(tglBG);
		requestFocus();
	}
}
