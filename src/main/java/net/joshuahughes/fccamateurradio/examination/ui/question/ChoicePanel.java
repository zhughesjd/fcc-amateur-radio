package net.joshuahughes.fccamateurradio.examination.ui.question;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;

import javax.swing.JRadioButton;

import net.joshuahughes.fccamateurradio.examination.Question;

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
		});
		chcs.stream().forEach(r->
		{
			r.addActionListener(l->
			{
				if(question == null) return;
				Optional<JRadioButton> op = chcs.stream().filter(b->b.isSelected()).findAny();
				int choice = op.isPresent()?chcs.indexOf(op.get()):-1;
				question.setSelection(choice);
				ChoicePanel.this.firePropertyChange(ChoicePanel.class.getCanonicalName(), null,new Integer(choice));
			});
		});
	}
	public void setQuestion(Question q)
	{
		super.setQuestion(q);
		if(question == Question.empty) return;
		questionLbl.setToolTipText(question.getPrevious());
		JRadioButton ra = getRadioAnswer();
		ra.setBackground(tglBG);
		requestFocus();
	}
}
