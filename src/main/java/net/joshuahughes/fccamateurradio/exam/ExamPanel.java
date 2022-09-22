package net.joshuahughes.fccamateurradio.exam;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.JRadioButton;

public class ExamPanel extends BasisPanel
{
	private static final long serialVersionUID = -1771840904347845324L;
	Color tglBG = defltBG;
	Question question;
	public ExamPanel()
	{
		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyTyped(KeyEvent e)
			{
				if(e
						.getKeyChar() == 'a')
				{
					JRadioButton ra = getRadioAnswer();
					ra.setBackground(tglBG = ra.getBackground().equals(defltBG)?rightBG:defltBG);
				}
			}
			@Override
			public void keyReleased(KeyEvent e)
			{
				if(e.getKeyChar() == 'h')
					getRadioAnswer().setBackground(defltBG);
			}
			
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyChar() == 'h')
					getRadioAnswer().setBackground(rightBG);
			}
		});
		chcs.stream().forEach(r->
		{
			r.addActionListener(l->
			{
				if(question == null) return;
				int qNdx = exam.indexOf(question);
				Optional<JRadioButton> op = chcs.stream().filter(b->b.isSelected()).findAny();
				Integer choice = op.isPresent()?chcs.indexOf(op.get()):null;
				ExamPanel.this.firePropertyChange(Result.class.getCanonicalName(), null, new Point(qNdx,choice==null?-1:choice.intValue()));
				submit();
			});
		});
	}
	public boolean setExam(Exam newExam)
	{
		if(!super.setExam(newExam))	return false;
		submit();
		return true;
	}
	private JRadioButton getRadioAnswer()
	{
		return chcs.get(question.getAnswer());
	}
	private void submit()
	{	
		question = exam.nextQuestion(question);
		if(question == null)
		{
			ExamPanel.this.firePropertyChange(ExamPanel.class.getCanonicalName(), null, null);
			return;
		}
		setText(question);
		getRadioAnswer().setBackground(tglBG);
		requestFocus();
	}
}
